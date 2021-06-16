package me.wuwenbin.noteblog.v4.web;

import me.wuwenbin.modules.utils.http.R;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;

import static me.wuwenbin.modules.utils.http.R.custom;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * created by Wuwenbin on 2018/1/28 at 12:26
 */
@Controller
@RequestMapping("/error")
public class ErrorController extends BaseController implements org.springframework.boot.autoconfigure.web.ErrorController {

    private static final String ERROR_PAGE = "error/page";
    private static final String ERROR_ROUTER = "error/router";

    private HttpServletRequest request;
    private ErrorAttributes errorAttributes;
    private ServerProperties serverProperties;


    @Override
    public String getErrorPath() {
        return isRouter(request) ? ERROR_ROUTER : ERROR_PAGE;
    }

    public ErrorController(HttpServletRequest request, ErrorAttributes errorAttributes, ServerProperties serverProperties) {
        Assert.notNull(errorAttributes, "ErrorAttributes must not be null");
        this.errorAttributes = errorAttributes;
        this.serverProperties = serverProperties;
        this.request = request;
    }

    /**
     * 非json的错误请求处理
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(produces = "text/html")
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        HttpStatus status = getStatus(request);
        Map<String, Object> model = Collections.unmodifiableMap(getErrorAttributes(request, isIncludeStackTrace(request)));
        response.setStatus(status.value());
        String template = isRouter(request) ? ERROR_ROUTER : ERROR_PAGE;
        return new ModelAndView(template, model);
    }

    /**
     * json的请求错误处理
     *
     * @param request
     * @return
     */
    @RequestMapping(produces = {APPLICATION_JSON_VALUE, APPLICATION_FORM_URLENCODED_VALUE})
    @ResponseBody
    public R error(HttpServletRequest request) {
        Map<String, Object> body = getErrorAttributes(request, isIncludeStackTrace(request));
        HttpStatus status = getStatus(request);
        String message = body.get("message").toString();
        body.remove("message");
        return custom(status.value(), message, body);
    }


    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            String code = request.getParameter("errorCode");
            statusCode = code == null ? null : Integer.valueOf(code);
        }
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            return HttpStatus.valueOf(statusCode);
        } catch (Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        return this.errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
    }

    private boolean isIncludeStackTrace(HttpServletRequest request) {
        ErrorProperties.IncludeStacktrace include = this.serverProperties.getError().getIncludeStacktrace();
        return include == ErrorProperties.IncludeStacktrace.ALWAYS || include == ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM && getTraceParameter(request);
    }

    private boolean getTraceParameter(HttpServletRequest request) {
        String parameter = request.getParameter("trace");
        return parameter != null && !"false".equals(parameter.toLowerCase());
    }
}
