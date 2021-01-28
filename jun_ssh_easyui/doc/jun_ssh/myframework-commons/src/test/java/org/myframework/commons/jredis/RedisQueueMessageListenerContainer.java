package org.myframework.commons.jredis;

/*
 * Copyright 2011-2014 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.SmartLifecycle;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.ConnectionUtils;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.util.ByteArrayWrapper;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.SchedulingAwareRunnable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ErrorHandler;

/**
 * Container providing asynchronous behaviour for Redis message listeners.
 * Handles the low level details of listening, converting and message
 * dispatching.
 * <p>
 * As oppose to the low level Redis (one connection per subscription), the
 * container uses only one connection that is 'multiplexed' for all registered
 * listeners, the message dispatch being done through the task executor.
 * <p>
 * Note the container uses the connection in a lazy fashion (the connection is
 * used only if at least one listener is configured).
 * <p>
 * Adding and removing listeners at the same time has undefined results. It is
 * strongly recommended to synchronize/order these methods accordingly.
 * 
 * @author Wujun
 * @author Wujun
 * @author Wujun
 * @author Wujun
 */
public class RedisQueueMessageListenerContainer implements InitializingBean,
		DisposableBean, BeanNameAware, SmartLifecycle {

	/** Logger available to subclasses */
	protected final Log logger = LogFactory.getLog(getClass());

	/**
	 * The default recovery interval: 5000 ms = 5 seconds.
	 */
	public static final long DEFAULT_RECOVERY_INTERVAL = 5000;

	/**
	 * The default subscription wait time: 2000 ms = 2 seconds.
	 */
	public static final long DEFAULT_SUBSCRIPTION_REGISTRATION_WAIT_TIME = 2000L;

	private long initWait = TimeUnit.SECONDS.toMillis(5);

	private Executor taskExecutor;

	private RedisConnectionFactory connectionFactory;


	private ErrorHandler errorHandler;

	private final Object monitor = new Object();
	// whether the container is running (or not)
	private volatile boolean running = false;
	// whether the container has been initialized
	private volatile boolean initialized = false;
	// whether the container uses a connection or not
	// (as the container might be running but w/o listeners, it won't use any
	// resources)
	private volatile boolean listening = false;

	private volatile boolean manageExecutor = false;

	// lookup maps
	// to avoid creation of hashes for each message, the maps use raw byte
	// arrays (wrapped to respect the equals/hashcode
	// contract)

	// lookup map between patterns and listeners
	private final Map<ByteArrayWrapper, Collection<MessageListener>> patternMapping = new ConcurrentHashMap<ByteArrayWrapper, Collection<MessageListener>>();
	// lookup map between channels and listeners
	private final Map<ByteArrayWrapper, Collection<MessageListener>> channelMapping = new ConcurrentHashMap<ByteArrayWrapper, Collection<MessageListener>>();
	// lookup map between listeners and channels
	private final Map<MessageListener, Set<Topic>> listenerTopics = new ConcurrentHashMap<MessageListener, Set<Topic>>();

	private final PopQueueMsgTask popMsgQueueTask = new PopQueueMsgTask();

	private volatile RedisSerializer<String> serializer = new StringRedisSerializer();

	private long recoveryInterval = DEFAULT_RECOVERY_INTERVAL;

	private long maxSubscriptionRegistrationWaitingTime = DEFAULT_SUBSCRIPTION_REGISTRATION_WAIT_TIME;

	private String beanName;

	public void afterPropertiesSet() {
		if (taskExecutor == null) {
			manageExecutor = true;
			taskExecutor = createDefaultTaskExecutor();
		}

		initialized = true;
	}

	/**
	 * Creates a default TaskExecutor. Called if no explicit TaskExecutor has
	 * been specified.
	 * <p>
	 * The default implementation builds a
	 * {@link org.springframework.core.task.SimpleAsyncTaskExecutor} with the
	 * specified bean name (or the class name, if no bean name specified) as
	 * thread name prefix.
	 * 
	 * @see org.springframework.core.task.SimpleAsyncTaskExecutor#SimpleAsyncTaskExecutor(String)
	 */
	protected TaskExecutor createDefaultTaskExecutor() {
		return new ThreadPoolTaskExecutor( );
	}

	public void destroy() throws Exception {
		initialized = false;

		stop();

		if (manageExecutor) {
			if (taskExecutor instanceof DisposableBean) {
				((DisposableBean) taskExecutor).destroy();

				if (logger.isDebugEnabled()) {
					logger.debug("Stopped internally-managed task executor");
				}
			}
		}
	}

	public boolean isAutoStartup() {
		return true;
	}

	public void stop(Runnable callback) {
		stop();
		callback.run();
	}

	public int getPhase() {
		// start the latest
		return Integer.MAX_VALUE;
	}

	public boolean isRunning() {
		return running;
	}

	public void start() {
		if (!running) {
			running = true;
			// wait for the subscription to start before returning
			// technically speaking we can only be notified right before the
			// subscription starts
			synchronized (monitor) {
				lazyListen();
				if (listening) {
					try {
						// wait up to 5 seconds for Subscription thread
						monitor.wait(initWait);
					} catch (InterruptedException e) {
						// stop waiting
					}
				}
			}

			if (logger.isDebugEnabled()) {
				logger.debug("Started RedisMessageListenerContainer");
			}
		}
	}

	public void stop() {
		if (isRunning()) {
			running = false;
			popMsgQueueTask.cancel();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Stopped RedisMessageListenerContainer");
		}
	}

	/**
	 * Process a message received from the provider.
	 * 
	 * @param message
	 * @param pattern
	 */
	protected void processMessage(MessageListener listener, Message message,
			byte[] pattern) {
		executeListener(listener, message, pattern);
	}

	/**
	 * Execute the specified listener.
	 * 
	 * @see #handleListenerException
	 */
	protected void executeListener(MessageListener listener, Message message,
			byte[] pattern) {
		try {
			listener.onMessage(message, pattern);
		} catch (Throwable ex) {
			handleListenerException(ex);
		}
	}

	/**
	 * Return whether this container is currently active, that is, whether it
	 * has been set up but not shut down yet.
	 */
	public final boolean isActive() {
		return initialized;
	}

	/**
	 * Handle the given exception that arose during listener execution.
	 * <p>
	 * The default implementation logs the exception at error level. This can be
	 * overridden in subclasses.
	 * 
	 * @param ex
	 *            the exception to handle
	 */
	protected void handleListenerException(Throwable ex) {
		if (isActive()) {
			// Regular case: failed while active.
			// Invoke ErrorHandler if available.
			invokeErrorHandler(ex);
		} else {
			// Rare case: listener thread failed after container shutdown.
			// Log at debug level, to avoid spamming the shutdown logger.
			logger.debug("Listener exception after container shutdown", ex);
		}
	}

	/**
	 * Invoke the registered ErrorHandler, if any. Log at error level otherwise.
	 * 
	 * @param ex
	 *            the uncaught error that arose during message processing.
	 * @see #setErrorHandler
	 */
	protected void invokeErrorHandler(Throwable ex) {
		if (this.errorHandler != null) {
			this.errorHandler.handleError(ex);
		} else if (logger.isWarnEnabled()) {
			logger.warn(
					"Execution of JMS message listener failed, and no ErrorHandler has been set.",
					ex);
		}
	}

	/**
	 * Returns the connectionFactory.
	 * 
	 * @return Returns the connectionFactory
	 */
	public RedisConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}

	/**
	 * @param connectionFactory
	 *            The connectionFactory to set.
	 */
	public void setConnectionFactory(RedisConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public void setBeanName(String name) {
		this.beanName = name;
	}

	/**
	 * Sets the task executor used for running the message listeners when
	 * messages are received. If no task executor is set, an instance of
	 * {@link SimpleAsyncTaskExecutor} will be used by default. The task
	 * executor can be adjusted depending on the work done by the listeners and
	 * the number of messages coming in.
	 * 
	 * @param taskExecutor
	 *            The taskExecutor to set.
	 */
	public void setTaskExecutor(Executor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}


	/**
	 * Sets the serializer for converting the {@link Topic}s into low-level
	 * channels and patterns. By default, {@link StringRedisSerializer} is used.
	 * 
	 * @param serializer
	 *            The serializer to set.
	 */
	public void setTopicSerializer(RedisSerializer<String> serializer) {
		this.serializer = serializer;
	}

	/**
	 * Set an ErrorHandler to be invoked in case of any uncaught exceptions
	 * thrown while processing a Message. By default there will be <b>no</b>
	 * ErrorHandler so that error-level logging is the only result.
	 */
	public void setErrorHandler(ErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

	/**
	 * Attaches the given listeners (and their topics) to the container.
	 * <p>
	 * Note: it's possible to call this method while the container is running
	 * forcing a reinitialization of the container. Note however that this might
	 * cause some messages to be lost (while the container reinitializes) -
	 * hence calling this method at runtime is considered advanced usage.
	 * 
	 * @param listeners
	 *            map of message listeners and their associated topics
	 */
	public void setMessageListeners(
			Map<? extends MessageListener, Collection<? extends Topic>> listeners) {
		initMapping(listeners);
	}

	/**
	 * Adds a message listener to the (potentially running) container. If the
	 * container is running, the listener starts receiving (matching) messages
	 * as soon as possible.
	 * 
	 * @param listener
	 *            message listener
	 * @param topics
	 *            message listener topic
	 */
	public void addMessageListener(MessageListener listener,
			Collection<? extends Topic> topics) {
		addListener(listener, topics);
		lazyListen();
	}

	/**
	 * Adds a message listener to the (potentially running) container. If the
	 * container is running, the listener starts receiving (matching) messages
	 * as soon as possible.
	 * 
	 * @param listener
	 *            message listener
	 * @param topic
	 *            message topic
	 */
	public void addMessageListener(MessageListener listener, Topic topic) {
		addMessageListener(listener, Collections.singleton(topic));
	}

	/**
	 * Removes a message listener from the given topics. If the container is
	 * running, the listener stops receiving (matching) messages as soon as
	 * possible.
	 * <p>
	 * Note that this method obeys the Redis (p)unsubscribe semantics - meaning
	 * an empty/null collection will remove listener from all channels.
	 * Similarly a null listener will unsubscribe all listeners from the given
	 * topic.
	 * 
	 * @param listener
	 *            message listener
	 * @param topics
	 *            message listener topics
	 */
	public void removeMessageListener(MessageListener listener,
			Collection<? extends Topic> topics) {
		removeListener(listener, topics);
	}

	/**
	 * Removes a message listener from the from the given topic. If the
	 * container is running, the listener stops receiving (matching) messages as
	 * soon as possible.
	 * <p>
	 * Note that this method obeys the Redis (p)unsubscribe semantics - meaning
	 * an empty/null collection will remove listener from all channels.
	 * Similarly a null listener will unsubscribe all listeners from the given
	 * topic.
	 * 
	 * @param listener
	 *            message listener
	 * @param topic
	 *            message topic
	 */
	public void removeMessageListener(MessageListener listener, Topic topic) {
		removeMessageListener(listener, Collections.singleton(topic));
	}

	/**
	 * Removes the given message listener completely (from all topics). If the
	 * container is running, the listener stops receiving (matching) messages as
	 * soon as possible. Similarly a null listener will unsubscribe all
	 * listeners from the given topic.
	 * 
	 * @param listener
	 *            message listener
	 */
	public void removeMessageListener(MessageListener listener) {
		removeMessageListener(listener, Collections.<Topic> emptySet());
	}

	private void initMapping(
			Map<? extends MessageListener, Collection<? extends Topic>> listeners) {
		// stop the listener if currently running
		if (isRunning()) {
			popMsgQueueTask.cancel();
		}

		patternMapping.clear();
		channelMapping.clear();
		listenerTopics.clear();

		if (!CollectionUtils.isEmpty(listeners)) {
			for (Map.Entry<? extends MessageListener, Collection<? extends Topic>> entry : listeners
					.entrySet()) {
				addListener(entry.getKey(), entry.getValue());
			}
		}

		// resume activity
		if (initialized) {
			start();
		}
	}

	/**
	 * Method inspecting whether listening for messages (and thus using a
	 * thread) is actually needed and triggering it.
	 */
	private void lazyListen() {
		boolean debug = logger.isDebugEnabled();
		boolean started = false;

		if (isRunning()) {
			if (!listening) {
				synchronized (monitor) {
					if (!listening) {
//						if (channelMapping.size() > 0
//								|| patternMapping.size() > 0) {
							taskExecutor.execute(popMsgQueueTask);
							listening = true;
							started = true;
//						}
					}
				}
				if (debug) {
					if (started) {
						logger.debug("Started listening for Redis messages");
					} else {
						logger.debug(
								"Postpone listening for Redis messages until actual listeners are added");
					}
				}
			}
		}
	}

	private void addListener(MessageListener listener,
			Collection<? extends Topic> topics) {
		Assert.notNull(listener, "a valid listener is required");
		Assert.notEmpty(topics, "at least one topic is required");

		List<byte[]> channels = new ArrayList<byte[]>(topics.size());
		List<byte[]> patterns = new ArrayList<byte[]>(topics.size());

		boolean trace = logger.isTraceEnabled();

		// add listener mapping
		Set<Topic> set = listenerTopics.get(listener);
		if (set == null) {
			set = new CopyOnWriteArraySet<Topic>();
			listenerTopics.put(listener, set);
		}
		set.addAll(topics);

		for (Topic topic : topics) {

			ByteArrayWrapper holder = new ByteArrayWrapper(
					serializer.serialize(topic.getTopic()));

			if (topic instanceof ChannelTopic) {
				Collection<MessageListener> collection = channelMapping
						.get(holder);
				if (collection == null) {
					collection = new CopyOnWriteArraySet<MessageListener>();
					channelMapping.put(holder, collection);
				}
				collection.add(listener);
				channels.add(holder.getArray());

				if (trace)
					logger.trace("Adding listener '" + listener
							+ "' on channel '" + topic.getTopic() + "'");
			}

			else if (topic instanceof PatternTopic) {
				Collection<MessageListener> collection = patternMapping
						.get(holder);
				if (collection == null) {
					collection = new CopyOnWriteArraySet<MessageListener>();
					patternMapping.put(holder, collection);
				}
				collection.add(listener);
				patterns.add(holder.getArray());

				if (trace)
					logger.trace("Adding listener '" + listener
							+ "' for pattern '" + topic.getTopic() + "'");
			}

			else {
				throw new IllegalArgumentException(
						"Unknown topic type '" + topic.getClass() + "'");
			}
		}

		// check the current listening state
	}

	private void removeListener(MessageListener listener,
			Collection<? extends Topic> topics) {
		boolean trace = logger.isTraceEnabled();

		// check stop listening case
		if (listener == null && CollectionUtils.isEmpty(topics)) {
			popMsgQueueTask.cancel();
			return;
		}

		List<byte[]> channelsToRemove = new ArrayList<byte[]>();
		List<byte[]> patternsToRemove = new ArrayList<byte[]>();

		// check unsubscribe all topics case
		if (CollectionUtils.isEmpty(topics)) {
			Set<Topic> set = listenerTopics.remove(listener);
			// listener not found, bail out
			if (set == null) {
				return;
			}
			topics = set;
		}

		for (Topic topic : topics) {
			ByteArrayWrapper holder = new ByteArrayWrapper(
					serializer.serialize(topic.getTopic()));

			if (topic instanceof ChannelTopic) {
				remove(listener, topic, holder, channelMapping,
						channelsToRemove);

				if (trace) {
					String msg = (listener != null
							? "listener '" + listener + "'" : "all listeners");
					logger.trace("Removing " + msg + " from channel '"
							+ topic.getTopic() + "'");
				}
			}

			else if (topic instanceof PatternTopic) {
				remove(listener, topic, holder, patternMapping,
						patternsToRemove);

				if (trace) {
					String msg = (listener != null
							? "listener '" + listener + "'" : "all listeners");
					logger.trace("Removing " + msg + " from pattern '"
							+ topic.getTopic() + "'");
				}
			}
		}

		// double check whether there are still subscriptions available
		// otherwise cancel the connection
		// as most drivers forfeit the connection on unsubscribe
		if (listenerTopics.isEmpty()) {
			popMsgQueueTask.cancel();
		}

	}

	private void remove(MessageListener listener, Topic topic,
			ByteArrayWrapper holder,
			Map<ByteArrayWrapper, Collection<MessageListener>> mapping,
			List<byte[]> topicToRemove) {

		Collection<MessageListener> listeners = mapping.get(holder);
		Collection<MessageListener> listenersToRemove = null;

		if (listeners != null) {
			// remove only one listener
			if (listener != null) {
				listeners.remove(listener);
				listenersToRemove = Collections.singletonList(listener);
			}

			// no listener given - remove all of them
			else {
				listenersToRemove = listeners;
			}

			// start removing listeners
			for (MessageListener messageListener : listenersToRemove) {
				Set<Topic> topics = listenerTopics.get(messageListener);
				if (topics != null) {
					topics.remove(topic);
				}
				if (CollectionUtils.isEmpty(topics)) {
					listenerTopics.remove(messageListener);
				}
			}
			// if we removed everything, remove the empty holder collection
			if (listener == null || listeners.isEmpty()) {
				mapping.remove(holder);
				topicToRemove.add(holder.getArray());
			}
		}
	}

	/**
	 * Handle subscription task exception. Will attempt to restart the
	 * subscription if the Exception is a connection failure (for example, Redis
	 * was restarted).
	 * 
	 * @param ex
	 *            Throwable exception
	 */
	protected void handleSubscriptionException(Throwable ex) {
		listening = false;
		popMsgQueueTask.closeConnection();
		if (ex instanceof RedisConnectionFailureException) {
			if (isRunning()) {
				logger.error(
						"Connection failure occurred. Restarting subscription task after "
								+ recoveryInterval + " ms");
				sleepBeforeRecoveryAttempt();
				lazyListen();
			}
		} else {
			logger.error("SubscriptionTask aborted with exception:", ex);
		}
	}

	/**
	 * Sleep according to the specified recovery interval. Called between
	 * recovery attempts.
	 */
	protected void sleepBeforeRecoveryAttempt() {
		if (this.recoveryInterval > 0) {
			try {
				Thread.sleep(this.recoveryInterval);
			} catch (InterruptedException interEx) {
				logger.debug(
						"Thread interrupted while sleeping the recovery interval");
			}
		}
	}

	/**
	 * Runnable used for Redis subscription. Implemented as a dedicated class to
	 * provide as many hints as possible to the underlying thread pool.
	 * 
	 * @author Wujun
	 */
	private class PopQueueMsgTask implements SchedulingAwareRunnable {
		private volatile RedisConnection connection;
		private boolean taskRunning = false;
		private final Object localMonitor = new Object();
		private long taskWait = TimeUnit.SECONDS.toMillis(5);

		public boolean isLongLived() {
			return true;
		}

		public void run() {
			System.out.println("11111111111111");
			synchronized (localMonitor) {
				taskRunning = true;
			}
			try {
				connection = connectionFactory.getConnection();

				boolean asyncConnection = ConnectionUtils
						.isAsync(connectionFactory);

				// NB: async drivers' Xsubscribe calls block, so we notify the
				// RDMLC before performing the actual subscription.
				if (!asyncConnection) {
					synchronized (monitor) {
						monitor.notify();
					}
				}

				ConnectionPresentCondition connectionPresentCondition = new ConnectionPresentCondition();
//				for (Map.Entry<ByteArrayWrapper,Collection<MessageListener>> entry : channelMapping.entrySet()) {
//					byte[] queueName = entry.getKey().getArray();
//					Collection<MessageListener>  listeners = entry.getValue();
//					byte[] msg = connection.rPop(queueName);
//				}
				
				if (asyncConnection) {
					SpinBarrier.waitFor(connectionPresentCondition,
							getMaxSubscriptionRegistrationWaitingTime());
					synchronized (monitor) {
						monitor.notify();
					}
				}
			} catch (Throwable t) {
				handleSubscriptionException(t);
			} finally {
				// this block is executed once the subscription thread has
				// ended, this may or may not mean
				// the connection has been unsubscribed, depending on driver
				synchronized (localMonitor) {
					taskRunning = false;
					localMonitor.notify();
				}
			}
		}

		/**
		 * Checks whether the current connection has an associated subscription.
		 * 
		 * @author Wujun
		 */
		private class ConnectionPresentCondition implements Condition {

			public boolean passes() {
				return !connection.isClosed();
			}
		}

		void cancel() {
			if (!listening) {
				return;
			}
			listening = false;

			if (logger.isTraceEnabled()) {
				logger.trace("Cancelling Redis subscription...");
			}
			if (connection != null) {
				synchronized (localMonitor) {
					logger.trace("Unsubscribing from all channels");
					if (taskRunning) {
						try {
							localMonitor.wait(taskWait);
						} catch (InterruptedException e) {
							// Stop waiting
						}
					}
					if (!taskRunning) {
						closeConnection();
					} else {
						logger.warn(
								"Unable to close connection. Subscription task still running");
					}
				}
			}
		}

		void closeConnection() {
			if (connection != null) {
				logger.trace("Closing connection");
				try {
					connection.close();
				} catch (Exception e) {
					logger.warn("Error closing subscription connection", e);
				}
				connection = null;
			}
		}

		 

	}

	/**
	 * Actual message dispatcher/multiplexer.
	 * 
	 * @author Wujun
	 */
	private class DispatchMessageListener implements MessageListener {

		public void onMessage(Message message, byte[] pattern) {
			Collection<MessageListener> listeners = null;

			// if it's a pattern, disregard channel
			if (pattern != null && pattern.length > 0) {
				listeners = patternMapping.get(new ByteArrayWrapper(pattern));
			} else {
				pattern = null;
				// do channel matching first
				listeners = channelMapping
						.get(new ByteArrayWrapper(message.getChannel()));
			}

			if (!CollectionUtils.isEmpty(listeners)) {
				dispatchMessage(listeners, message, pattern);
			}
		}
	}

	private void dispatchMessage(Collection<MessageListener> listeners,
			final Message message, final byte[] pattern) {
		final byte[] source = (pattern != null ? pattern.clone()
				: message.getChannel());

		for (final MessageListener messageListener : listeners) {
			taskExecutor.execute(new Runnable() {
				public void run() {
					processMessage(messageListener, message, source);
				}
			});
		}
	}

	/**
	 * Specify the interval between recovery attempts, in <b>milliseconds</b>.
	 * The default is 5000 ms, that is, 5 seconds.
	 * 
	 * @see #handleSubscriptionException
	 */
	public void setRecoveryInterval(long recoveryInterval) {
		this.recoveryInterval = recoveryInterval;
	}

	public long getMaxSubscriptionRegistrationWaitingTime() {
		return maxSubscriptionRegistrationWaitingTime;
	}

	/**
	 * Specify the max time to wait for subscription registrations, in
	 * <b>milliseconds</b>. The default is 2000ms, that is, 2 second.
	 * 
	 * @param maxSubscriptionRegistrationWaitingTime
	 * @see #DEFAULT_SUBSCRIPTION_REGISTRATION_WAIT_TIME
	 */
	public void setMaxSubscriptionRegistrationWaitingTime(
			long maxSubscriptionRegistrationWaitingTime) {
		this.maxSubscriptionRegistrationWaitingTime = maxSubscriptionRegistrationWaitingTime;
	}

	/**
	 * @author Wujun
	 * @author Wujun
	 */
	private static abstract class SpinBarrier {

		/**
		 * Periodically tests, in 100ms intervals, for a condition until it is
		 * met or a timeout occurs.
		 * 
		 * @param condition
		 *            The condition to periodically test
		 * @param timeout
		 *            The timeout
		 * @return true if condition passes, false if condition does not pass
		 *         within timeout
		 */
		static boolean waitFor(Condition condition, long timeout) {

			long startTime = System.currentTimeMillis();

			while (!timedOut(startTime, timeout)) {
				if (condition.passes()) {
					return true;
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
			return false;
		}

		private static boolean timedOut(long startTime, long timeout) {
			return (startTime + timeout) < System.currentTimeMillis();
		}
	}

	/**
	 * A condition to test periodically, used in conjunction with
	 * {@link org.springframework.data.redis.listener.RedisMessageListenerContainer.SpinBarrier}
	 * 
	 * @author Wujun
	 * @author Wujun
	 */
	private static interface Condition {

		/**
		 * @return true if condition passes
		 */
		boolean passes();
	}
}
