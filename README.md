# BaseSSM
一个学习的过程中不断充实和完善的J2ee项目, 主要使用Spring SpringMvc Mybatis 和 Hibernate构建,

# 源码结构

## com
* controller 控制器 
  * BaseController 基本抽象类 可继承快捷实现单表配置化增删查改
  * FileController 文件控制 提供文件的上传下载修改和共享文件夹的浏览
  * ClassController 类反射控制 提供package的浏览和实例化调用
  * TableController 抽象化实现表的增删查改 即把表名也参数化 结合ddl级sql语句 实现数据库的远程控制
  * Page 分页参数类
  * Context 结合ThreadLocal实现请求处理上下文
  
* dao 数据存取 分别以hibernate和mybatis实现了通用型List<Map>结构的数据查询和修改

* event 
  * intercept 拦截器 登录拦截 日志拦截 环绕监控
  * listener 系统启动监听OnLoad
  * task 定时任务调度器
* mode 基本数据模型

* service 服务层 封装业务逻辑块 


## util

* Bean 继承HashMap的增强型Map 提供泛型存取 build模式

* cache 抽象缓存模块 有内存Map和Redis两种实现方式 并实现了cache Map的浏览数据结构 用于浏览器的缓存监控
* ClassUitl 反射类工具 即类名类方法的参数化 (注入反转?) 并提供class即jar包->Map结构 用于浏览器远程包浏览以及类方法调用
* FileUtil 文件处理工具 并提供了文件->Map结构 用于浏览器远程文件夹浏览操控

* database 原生jdbc工具类 List<Map> 形式

* socket 长连接模块 抽象出业务处理 和 底层实现 共有Socket SocketIo Netty三种实现 并编写了基本界面GUI测试工具 两端Client-Server
* ThreadUtil 四种线程池的工具
* JsonUtil json结合泛型的快捷工具
