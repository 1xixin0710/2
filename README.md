# 大工校园二手

《网站建设与管理》期末大作业：校园闲置物品交易管理系统。

项目使用 Java 17、Spring Boot 3.2.5、Spring MVC、MyBatis、Thymeleaf 和 MySQL/H2 实现，包含注册登录、闲置查询、物品发布与修改、下单、付款登记、确认收货和订单管理功能。

## 在线文件

- [期末大作业报告](./网站建设大作业-学号-姓名.docx)
- 项目源码：[`校园闲置交易管理系统`](./校园闲置交易管理系统)

## 本地运行

```powershell
cd .\校园闲置交易管理系统
.\mvnw.cmd spring-boot:run
```

启动后访问 <http://localhost:8080>。系统没有预置账号，请先在注册页创建账号并登录。

## 数据库

本地开发默认使用 H2，并自动载入 37 条闲置物品数据。需要连接云端 MySQL 时，先执行 [`mysql-schema.sql`](./校园闲置交易管理系统/src/main/resources/db/mysql-schema.sql)，再设置以下环境变量：

```powershell
$env:SPRING_PROFILES_ACTIVE = "cloud"
$env:DB_URL = "jdbc:mysql://主机:3306/campus_market?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai"
$env:DB_USERNAME = "数据库用户名"
$env:DB_PASSWORD = "数据库密码"
$env:DB_INIT_MODE = "always"
.\mvnw.cmd spring-boot:run
```

首次建表时将 `DB_INIT_MODE` 设为 `always`，确认表已创建后改为 `never`。仓库不包含任何公共数据库凭据。

## 付款说明

下单页可选择校内当面付款、微信转账或支付宝转账。微信和支付宝属于买卖双方站外自行转账，系统只登记付款方式和付款状态，不代收、不托管资金，也不会自动扣款。接入真实支付网关需要对应的商户资质。

## 主要技术

- Java 17
- Spring Boot 3.2.5
- Spring MVC
- MyBatis
- Thymeleaf
- H2 / MySQL 8
- JUnit 5 / MockMvc
