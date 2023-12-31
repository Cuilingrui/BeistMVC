# BeistMVC

这是一个模仿Spring和SpringMVC的项目，实现了Spring的核心功能，同时计划基于Netty和该简易Spring实现SpringMVC部分的功能，MVC部分并没有完全完成，实现了Cookie和Session的管理，BeistHttpRequest和BeistHttpResponse以及RequestMappingHandlerMapping部分。

在Spring部分实现了以下功能：

* 实现了IOC容器和自动注入，支持XML和注解方式配置Bean，并利用三级缓存解决了循环依赖问题
* 基于策略模式，通过Cglib和JDK两种方式，对有/无参构造函数的Bean对象进行实例化
* 使用观察者模式实现了事件机制，可以监听Context刷新和关闭事件，以及自定义事件
* 基于JDK和Cglib动态代理实现了对AOP的支持  
* 实现了Bean的生命周期，并支持实现BeanPostProcessor接口以对Spring中的Bean自定义处理