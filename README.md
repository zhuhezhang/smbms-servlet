B站原视频链接：[https://www.bilibili.com/video/BV12J411M7Sj](https://www.bilibili.com/video/BV12J411M7Sj)
源码：[https://github.com/zhz000/smbms-servlet](https://github.com/zhz000/smbms-servlet)      [https://gitee.com/zhz000/smbms-servlet](https://gitee.com/zhz000/smbms-servlet)


**注意：**
- 运行在8080端口，通过登录界面（账号admin，密码1234567，用户表中其他账号也行）进入主页就可以对信息进行基本的增删改查操作
- 看视频比较麻烦，网上有很多关于这个项目的文章，只是要注意版本号和有些传递参数不同，避免报错
- 版本不同可能会出现很多错误，我这里使用的jdk15、mysql数据库5.7.34、tomcat 9.0.50、其他jar包的版本都在maven项目的配置文件中了（这个也不用管打开项目联网就会自动帮你导入了）
- 数据库连接的配置的数据库账号密码等记得改成自己的
- 原视频中只做了用户管理的部分功能，我这里自己把用户管理的增删改查都做了，其他的订单管理原理也都是一样，就不太想做了
- 已经导入maven包但是运行项目之后依然报的是class not found的错误的话要在file->project structure，如下图在WEB-INF目录下新建lib文件夹，然后选中左边的jar右键导过来就可以了（视频里面也有说过我记得）
![在这里插入图片描述](https://img-blog.csdnimg.cn/7f537ccab7394cf2814773c18ce3cc17.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQzNzk0NjMz,size_16,color_FFFFFF,t_70)
- 源码里面的sql.txt是建立数据库是sql语句
- 有任何问题欢迎评论区提问
