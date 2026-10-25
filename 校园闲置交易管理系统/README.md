# 大工校园二手：校园闲置交易管理系统

《网站建设与管理》期末大作业。后端采用 Spring、Spring MVC 和 MyBatis，页面使用 Thymeleaf 服务端渲染。项目不预置登录账号，打开系统后需要先注册。页面采用校园分类信息站的布局，37 条物品记录分别使用对应的实物照片。

## 本地运行

环境要求：JDK 17 或更高版本。项目自带 Maven Wrapper。

```powershell
.\mvnw.cmd spring-boot:run
```

访问 <http://localhost:8080> 后注册账号并登录。本地配置使用 H2 内存数据库，并带有 37 条用于界面展示的初始物品记录。

运行测试：

```powershell
.\mvnw.cmd test
```

## 连接云端 MySQL

项目包含 `cloud` 配置。云数据库可以是任意兼容 MySQL 8 的服务。先在云控制台创建数据库，然后把连接信息放入环境变量，不要把密码写进源码。

PowerShell 示例：

```powershell
$env:SPRING_PROFILES_ACTIVE = "cloud"
$env:DB_URL = "jdbc:mysql://数据库地址:3306/数据库名?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai"
$env:DB_USERNAME = "数据库用户名"
$env:DB_PASSWORD = "数据库密码"
$env:DB_INIT_MODE = "always"
.\mvnw.cmd spring-boot:run
```

`DB_INIT_MODE=always` 会在首次启动时创建数据表。确认表已创建后应改回 `never`，避免每次启动重复执行初始化脚本。

如需导入本地样本数据，可在命令行中执行：

```powershell
mysql -h 数据库地址 -P 3306 -u 数据库用户名 -p 数据库名 < .\src\main\resources\data.sql
```

部署到需要动态端口的平台时，可直接设置 `PORT` 环境变量，Spring 会使用该端口启动。

## 功能

- 用户注册、登录、退出和 BCrypt 密码哈希
- 关键词、分类、交易状态组合筛选
- 闲置信息发布、编辑、删除及状态维护
- 文本、密码、数字、单选、复选、下拉框和日期时间控件
- 服务端字段校验、会话拦截和统一错误页面
- 本地 H2 开发配置与云端 MySQL 部署配置
