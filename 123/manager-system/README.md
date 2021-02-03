# 后台管理系统

## 感谢：

**首先感谢各位对我项目的支持，由于我一般是将项目更新在GitHub上，因此在Gitee上项目可能不是最新的。并且Gitee我并不经常使用主要使用GitHub，如果大家有问题可以在GitHub上留言。因此有一些bug可能在Gitee上没有更改，再次向各位说声抱歉。目前这个项目其实早就完成，只是忘了更新Gitee上。最后再次谢谢大家的支持。**

**最后关于一些问题回复：这个小项目是有两个分支的，一个是基于过滤器完成的权限校验，一个是基于spring security完成的校验。大家可以在tag上对应下载。默认是使用spring security这个分支。对于初学者我建议可以下载第一个分支。**

GitGub地址：https://github.com/ZeroWdd

## 简介:
简单的后台开发模板框架，具备菜单管理,用户管理,角色管理,权限管理,日志管理这5个功能。

运用layui后台管理模板，界面简介大方且美观。

采用mvc架构，使代码易于管理和修改，合理的注解，使代码通俗易懂，具有较强的阅读性。

## 编译环境：

> jdk 1.8 
>
> mysql 5.7
>
> tomcat 7

## 框架：

> springboot2.0 
>
> mybatis
>
> spring security

## jar包管理工具：

> Maven

## 编译器：

> IDEA

## 完成进度：

> manager_system_1.0.0版本完成  从tag中 V1.0.0可获取完整版本
>
> manager_system_2.0.0版本打算结合spring security完成  从tag中 V2.0.0可获取完整版本

## 系统功能：

> 控制面板
>
> 用户管理：
>
> > 用户管理
> >
> > 角色管理
> >
> > 权限管理
>
> 系统日志：
>
> > 日志管理

## 启动设置:
* 修改application.yml中的数据链接

* 执行`mysql -uroot -p 数据库 < manager_system.sql`导入数据库脚本。
* 运行`ManagerSystemApplication`，启动后访问:<http://localhost:8080/manager/login> 
* 管理员:
  * 账号：`superadmin`
  * 密码:   `123`


## 数据表关系：

![](https://gitee.com/ZeroWdd/manager-system/raw/master/%E9%A1%B9%E7%9B%AE%E6%88%AA%E5%9B%BE/10.png)

## 项目截图：

![](https://gitee.com/ZeroWdd/manager-system/raw/master/%E9%A1%B9%E7%9B%AE%E6%88%AA%E5%9B%BE/1.png)

![](https://gitee.com/ZeroWdd/manager-system/raw/master/%E9%A1%B9%E7%9B%AE%E6%88%AA%E5%9B%BE/2.png)

![](https://gitee.com/ZeroWdd/manager-system/raw/master/%E9%A1%B9%E7%9B%AE%E6%88%AA%E5%9B%BE/3.png)

![](https://gitee.com/ZeroWdd/manager-system/raw/master/%E9%A1%B9%E7%9B%AE%E6%88%AA%E5%9B%BE/4.png)

![](https://gitee.com/ZeroWdd/manager-system/raw/master/%E9%A1%B9%E7%9B%AE%E6%88%AA%E5%9B%BE/5.png)

![](https://gitee.com/ZeroWdd/manager-system/raw/master/%E9%A1%B9%E7%9B%AE%E6%88%AA%E5%9B%BE/6.png)

![](https://gitee.com/ZeroWdd/manager-system/raw/master/%E9%A1%B9%E7%9B%AE%E6%88%AA%E5%9B%BE/7.png)

![](https://gitee.com/ZeroWdd/manager-system/raw/master/%E9%A1%B9%E7%9B%AE%E6%88%AA%E5%9B%BE/8.png)

![](https://gitee.com/ZeroWdd/manager-system/raw/master/%E9%A1%B9%E7%9B%AE%E6%88%AA%E5%9B%BE/9.png)

