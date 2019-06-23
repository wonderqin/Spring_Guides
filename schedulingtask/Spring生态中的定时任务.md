# Spring生态中的定时任务

### ——Scheduling Tasks

### 前言

​	本文主要阐述利用Spring如何创建一个定时任务

在本项目中，你将使用Spring的@Scheduled注解创建一个每隔5s打印依次当前时间的应用。

### 本机环境

- **Java**：1.8.0
- **maven**：apache maven 3.6.0
- **IDE** ：IDEA

### 通过maven搭建系统

- pom文件：

```html
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.1.3.RELEASE</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	<groupId>com.wonderqin</groupId>
	<artifactId>schedulingtask</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>schedulingtask</name>
	<description>Demo project for Spring Boot</description>

	<properties>
		<java.version>1.8</java.version>
	</properties>

	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>

</project>

```

### 项目目录

![](http://i65.tinypic.com/2qdam3d.png)



### 创建一个定时任务

- ScheduleTask.java

```java
package com.wonderqin.schedulingtask.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName ScheduleTask
 * @Description TODO
 * @Author wonderQin
 * @Date 2019-03-31 22:50
 **/
@Component
public class ScheduleTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleTask.class);

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTimeWithFixedRate(){
        LOGGER.info("fixedRate——the current time is {}" , FORMAT.format(new Date()));
    }

    @Scheduled(fixedDelay = 1000)
    public void reportCurrentTimeWithFixedDelay(){
        LOGGER.info("fixedDelay——the current time is {}" , FORMAT.format(new Date()));
    }

    @Scheduled(fixedRateString = "3000")
    public void reportCurrentTimeWithFixedRateString(){
        LOGGER.info("fixedRateString——the current time is {}" , FORMAT.format(new Date()));
    }

    @Scheduled(fixedDelayString = "4000")
    public void reportCurrentTimeWith(){
        LOGGER.info("fixedDelayString——the current time is {}" , FORMAT.format(new Date()));
    }
}

```

计划注释定义特定方法何时运行。 

注意：

	- 使用fixedRate，它指定从每次调用的开始时间开始测量的方法调用之间的间隔。 
	- 使用fixedDelay，它指定从完成任务开始测量的调用之间的间隔。 
	- 使用@Scheduled（cron =“...”）表达式进行更复杂的任务调度。



- 在启动类添加注解@EnableScheduling

```java
package com.wonderqin.schedulingtask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
/**
 * @Author wonderqin
 * @Description TODO
**/
@SpringBootApplication
@EnableScheduling
public class SchedulingtaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchedulingtaskApplication.class, args);
	}

}

```

	- 关于注解@SpringBootApplication的作用，请参考：https://www.jianshu.com/p/70cc657bddce
	- 而注解@EnableScheduling则保证后台定时任务的创建

### 运行

你可以选择以下方式运行：

- IDE直接启动

- 打jar包用命令行启动

  ```
  java -jar xxx.jar
  ```

- 打war包到tomcat上启动

本文中是直接在本机的IDE上运行的：

- 方法的运行结果

```java
@Scheduled(fixedRate = 5000)
    public void reportCurrentTimeWithFixedRate(){
        LOGGER.info("fixedRate——the current time is {}" , FORMAT.format(new Date()));
    }
```

![](http://i67.tinypic.com/15zi6mq.png)

- 方法的运行结果

```java
@Scheduled(fixedDelay = 1000)
public void reportCurrentTimeWithFixedDelay(){    
    LOGGER.info("fixedDelay——the current time is {}" , FORMAT.format(new Date()));}
```

![](http://i68.tinypic.com/21b7y9y.png)

**以上注解所带参数的区别是：**

- fixedRate = 5000：表示无论前一个任务是否完成，程序每隔5秒就执行依次
- fixedDelay = 1000：表示程序必须等待前一个任务完成后1秒，才开始执行下一个任务

以下是该以上参数的官网解释：

```php+HTML
The Scheduled annotation defines when a particular method runs.

NOTE: This example uses fixedRate, which specifies the interval between method invocations measured from the start time of each invocation.
```

其实，还有另外两种参数：

@Scheduled(fixedRateString = "3000") @Scheduled(fixedDelayString = "4000")

其实这两种注释的作用与前面介绍的两种一样，不同的是，这里用的是String的参数，但是在运行的时候，会将String转化为Long类型

并且如果不能转换的话，程序会报非法参数异常：

```
java.lang.IllegalStateException

Encountered invalid @Scheduled method 'reportCurrentTimeWithFixedRateString': Invalid fixedRateString value "wonder" - cannot parse into long
```



### 扩展阅读

**本项目github地址：**

https://github.com/wonderqin/Spring_Guides/tree/master/schedulingtask

**本项目官方地址：**

https://spring.io/guides/gs/scheduling-tasks/

**本项目官方git地址：**

https://github.com/spring-guides/gs-scheduling-tasks.git