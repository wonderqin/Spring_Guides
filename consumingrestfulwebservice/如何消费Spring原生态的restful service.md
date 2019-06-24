# Spring原生态rest服务的消费

[TOC]

### 前言

本文主要阐述利用Spring如何消费其定义的rest服务。

在本文中，你将了解并实际操作如何利用Spring的RestTemplate在https://gturnquist-quoters.cfapps.io/api/random上返回一个Spring Boot的随机引用。

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
	<groupId>com.example</groupId>
	<artifactId>demo</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>consumingrestfulwebservice</name>
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

		<dependency>
			<groupId>com.fasterxml.jackson.core</groupId>
			<artifactId>jackson-databind</artifactId>
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

在这里，spring-boot-maven-plugin插件的作用：

- 收集类路径上的所有jar并构建一个可运行的“über-jar”，这使得执行和传输服务更加方便。
- 搜索public static void main（）方法以标记为可运行的类。
- 提供了一个内置的依赖项解析器，设置版本号以匹配Spring Boot依赖项。 您可以覆盖任何您希望的版本，但它将默认为Boot的所选版本集。

### 拉取一个REST风格的资源

官网已经给我们提供了一个运行在https://gturnquist-quoters.cfapps.io/api/random.上的rest服务，我们用浏览

器访问它，则会返回一个以下格式的随机json:

![](http://i68.tinypic.com/290r4aa.png)

![](http://i65.tinypic.com/2qumdq8.png)

特点是，其访问的返回时随机的value。但是我们所需要的并不仅仅是这个返回。

接下来我们以编程的方式去消费该服务，Spring提供了RestTemplate模板，利用该模板，我们可以很方便地与大

多数的RESTful服务交互，它甚至可以数据绑定到自定义域类型！这对我们的开发无疑是提供了大大的方便。

	- 首先建立domain类封装我们要的数据（也就是引用）

```java
package com.example.demo.hello;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @ClassName Quote
 * @Description TODO
 * @Author wonderQin
 * @Date 2019-04-02 1:28
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
public class Quote {
    private String type;
    private Value value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public Quote() {
    }

    public Quote(String type, Value value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "type='" + type + '\'' +
                ", value=" + value +
                '}';
    }
}
```

**建议使用[lombok插件](https://www.projectlombok.org/download)（IDEA)简化数据封装类的代码**

我们稍微分析下类Quote，如你所见，这知识一个简单的Java类，提供了对象的对外访问与修改方法，但是有一个

注解值得注意：**@JsonIgnoreProperties(ignoreUnknown = true)**

- 该注解的作用是**忽略此类型中未绑定的任何属性**，简单来说就是忽略类中不存在的字段。该注解还可以有另外一种使用方式，就是直接指定忽略的字段：

```java
@JsonIgnoreProperties({ “username”, “password” })
```

- 与其相似的两个注解：**@JsonIgnore和@JsonFormat**，前者的作用与**@JsonIgnoreProperties(ignoreUnknown = true)**相似，但是**@JsonIgnoreProperties**是作用在类上的，而**@JsonIgnore**是作用在属性和方法上的；而**@JsonFormat**则可以对返回做自定义格式化。

为了直接将数据绑定到自定义类型，您需要指定与从API返回的JSON文档中的键完全相同的变量名称。 如果您的

JSON doc中的变量名称和键不匹配，则需要使用**@JsonProperty**注解指定JSON文档的确切键。

我们需要另外一个去嵌入内部的引用：

```Java
package com.example.demo.hello;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @ClassName Value
 * @Description TODO
 * @Author wonderQin
 * @Date 2019-04-02 1:39
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
public class Value {
    private Long id;
    private String quote;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public Value(Long id, String quote) {
        this.id = id;
        this.quote = quote;
    }

    public Value() {
    }

    @Override
    public String toString() {
        return "Value{" +
                "id=" + id +
                ", quote='" + quote + '\'' +
                '}';
    }
}

```

该类的作用其实就是承担返回的数据封装载体，在类中使用了与上一个类相同的注解，但是映射到了另外一个数据字段。

### 在启动类中消费

注：在生产环境中可灵活消费

```java
package com.example.demo;

import com.example.demo.hello.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

/**
 * @Author wonderqin
 * @Description TODO
**/

public class ConsumingRestfulWebserviceApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConsumingRestfulWebserviceApplication.class);

	public static void main(String[] args) {
		RestTemplate restTemplate = new RestTemplate();
		Quote quote = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
		LOGGER.info(quote.toString());
		SpringApplication.run(ConsumingRestfulWebserviceApplication.class, args);
	}

}

```

在这里，我们利用TestTemplate对象将RESTful服务传入的json数据转换为我们自定义的Quote类，完成数据的绑

定，并将Quate的值打印出来。

有细心的小伙伴可能注意到，该类的实际启动是在

```java
SpringApplication.run(ConsumingRestfulWebserviceApplication.class, args);
```

并且这个类是没有**@SpringBootApplication**注解的！这只是一个普通的启动类，其实写在单元测试会更好些，这只是一个demo，则随意些也无妨。

下面我们用**@SpringBootApplication**实现相同的功能：

```java
package com.example.demo;

import com.example.demo.hello.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName Application
 * @Description TODO
 * @Author wonderQin
 * @Date 2019-06-25 0:30
 **/
@SpringBootApplication
public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String args[]) {
        SpringApplication.run(Application.class);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
        return args -> {
            Quote quote = restTemplate.getForObject(
                    "https://gturnquist-quoters.cfapps.io/api/random", Quote.class);
            log.info(quote.toString());
        };
    }
}

```

在该类中，Spring注入了**RestTemplateBuilder**，它是我们可以更方便地使用消息转换器和请求工厂发生的所有

自动配置。

### 运行

同样地

你可以选择以下方式运行：

- IDE直接启动

- 打jar包用命令行启动

  ```
  java -jar
  ```

- 打war包到tomcat上启动

运行结果：

![](http://i63.tinypic.com/i53azk.png)



大家也可以测试下，自己写一个Spring的RESTful服务，然后使用该项目去消费，会发生什么有趣地事情？我在之前地文章中有谈到如何搭建Spring原生态地RESTful服务，欢迎参考阅读!

### 扩展阅读

- [lombok官方文档](https://projectlombok.org/api/lombok/package-summary.html)
- [本项目的官方地址](https://spring.io/guides/gs/consuming-rest/)
- [本项目的个人github地址](https://github.com/wonderqin/Spring_Guides/tree/master/consumingrestfulwebservice)