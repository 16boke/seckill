# seckill
项目概述

秒杀系统采用MyBatis+Spring4+SpringMVC+jsp/bootstrap/jquery等框架来开发的高性能秒杀系统。

一、DAO层

1、mybatis相关的配置

- mybatis-config.xml

配置mybatis的相关设置信息

<?xml version="1.0" encoding="UTF-8" ?>

<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">

<configuration>

	<settings>
		<!-- 使用jdbc的getGeneratedKeys获取数据库自增主键值 -->
		<setting name="useGeneratedKeys" value="true"/>
		<!-- 列标签代替列名 -->
		<setting name="useColumnLabel" value="true"/>
		<!-- 开启自动驼峰命名规则（camel case）映射，即从经典数据库列名 A_COLUMN 到经典 Java 属性名 aColumn 的类似映射 -->
		<setting name="mapUnderscoreToCamelCase" value="true"/>
		<setting name="logImpl" value="LOG4J" />
	</settings>
</configuration>


- spring-dao.xml

配置数据库相关信息，连接池配置、sqlSessionFactory、dao包扫描

二、Service层

- spring-service.xml

配置事务管理器、注解声明式事务、service包扫描注解

三、web层

- spring-web.xml

开启spingmvc注解模式：<mvc:annotation-driven />

配置jsp显式ViewResolver

四、注意

- dao接口：如果有多个参数，最好在参数前加上@Param("属性名")这个注解，以便在mapping文件中可以直接使用
- 常量定义最好使用枚举类：例如SeckillStatEnum定义key-value类型的枚举
- 使用自定义异常并继承RuntimeException，只需要重载(String message,Throwable cause)和(String message)这两个方法
- js文件使用模块化来进行组织，定义成json的形式。seckill.js