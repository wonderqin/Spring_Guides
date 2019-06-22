# Building a RESTful Web Service

### 前言

​	本文主要阐述如何利用Spring搭建一个REST的web服务。

在该项目中，你可以通过一个HTTP GET请求：

```html
http://[ip]:[port]/greeting
```

并得到一个以json格式的返回：

```json
{"id":1,"content":"Hello, World!"}
```

你也可以在HTTP GET请求中增加可选参数 **name** 来自定义问候语

```
http://[ip]:[port]/greeting？name=Wonder
```

该 **name** 参数会覆盖默认返回中的 "World"

```json
{"id":1,"content":"Hello, Wonder!"}
```

### 本机环境

- **Java**：1.8.0
- **maven**：apache maven 3.6.0
- **IDE** ：IDEA

### 通过maven搭建系统

- pom文件：

```java
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
	<name>demo</name>
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
			<groupId>com.jayway.jsonpath</groupId>
			<artifactId>json-path</artifactId>
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

- 在这里，spring-boot-maven-plugin插件的作用：
  - 收集类路径上的所有jar并构建一个可运行的“über-jar”，这使得执行和传输服务更加方便。
  - 搜索public static void main（）方法以标记为可运行的类。
  - 提供了一个内置的依赖项解析器，设置版本号以匹配Spring Boot依赖项。 您可以覆盖任何您希望的版本，但它将默认为Boot的所选版本集。
- 本项目目录结构

![](http://i66.tinypic.com/6s5rsz.png)

- 创建资源表示类Greeting对问候语建模：

```java
package com.wonderqin.restfulwebservice.hello;

/**
 * @ClassName Greeting
 * @Description pojo
 * @Author wonderQin
 * @Date 2019-03-20 1:41
 **/
public class Greeting {
    private long id;
    private String content;

    public Greeting() {
    }

    public Greeting(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}

```

接下来，Spring将使用Jackson JSON库自动封装Greeting类型为JSON的实例。

- 创建资源控制类

```java
package com.wonderqin.restfulwebservice.hello;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @ClassName GreetingControler
 * @Description greeting controller
 * @Author wonderQin
 * @Date 2019-03-20 1:49
 **/
@RestController
public class GreetingController {
    private static final String TEMPLATE = "hello,%s";
    private final AtomicLong counter = new AtomicLong();
    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name",defaultValue = "World") String name){
        return new Greeting(counter.incrementAndGet(),String.format(TEMPLATE,name));
    }
}

```

**这个控制器简洁而简单，但引擎盖下有很多东西。 让我们一步一步地分解它。**

1. 注解**@RequestMapping‘**确保HTTP请求“/greeting"与方法greeting()是一个映射

   **注意：上面的示例未指定GET与PUT，POST等，因为@RequestMapping默认映射所有HTTP操作。 使用@RequestMapping（method = GET）缩小此映射范围。**

2. **@RequestParam**将查询字符串参数名称的值绑定到greeting（）方法的**name**参数中。 如果请求中不存在name参数，则使用默认值”World"
3. greeting()方法体的实现创建并返回了一个新的Greeting对象，该对象具有基于下一个计数器的id和content属性，并使用greeting模板格式化给定的名称（在这里说的就是name所对应的值）
4. 传统MVC控制器和上面的RESTful Web服务控制器之间的关键区别在于创建HTTP响应主体的方式。RESTful Web服务控制器只是填充并返回一个Greeting对象，将对象数据作为JSON直接写入HTTP响应。而不是依靠视图技术来执行，将问候数据的服务器端呈现为HTML。 
5. 此代码使用Spring 4的新**@RestController**注释，它将类标记为控制器，其中每个方法都返回一个**domain**对象而不是视图。 这是**@Controller**和**@ResponseBody**汇总在一起的简写。
6. Greeting对象转换为JSON。多亏了Spring的HTTP消息转换器支持，我们无需手动执行此转换。 因为Jackson 2在类路径上，所以会自动选择Spring的**MappingJackson2HttpMessageConverter**将Greeting实例转换为JSON。
7. 我们再来看下AtomicLong实例的incrementAndGet方法

```java
/**
     * Atomically increments by one the current value.
     *
     * @return the updated value
     */
    public final long incrementAndGet() {
        return unsafe.getAndAddLong(this, valueOffset, 1L) + 1L;
    }
```

注释上写得很清楚，其实这就是一个计数器得操作，返回得是新的值。

另外我们来看下**Springboot**的启动类

```java
package com.wonderqin.restfulwebservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * @Author wonderqin
 * @Description TODO
**/
@SpringBootApplication
public class DemoApplication {

   public static void main(String[] args) {
      SpringApplication.run(DemoApplication.class, args);
   }

}
```

**`@SpringBootApplication` 看似简单，其实暗藏玄机，它包含了以下所有内容：**

- **@Configuration**将类标记为应用程序上下文的bean定义源。
- **@EnableAutoConfiguration**告诉Spring Boot根据类路径设置，其他bean和各种属性设置开始添加bean。
- 通常我们在使用spring的mvc时，会为Spring MVC应用程序添加**@EnableWebMvc**注解，但Spring Boot会在类路径上看到spring-webmvc时自动添加它。 这会将应用程序标记为Web应用程序并激活关键行为，例如设置DispatcherServlet。

- @ComponentScan告诉Spring在**当前目录下（包括当前目录）的包中**寻找其他组件，配置和服务，允许它找到控制器。

### 运行

你可以选择以下方式运行：

- IDE直接启动

- 打jar包用命令行启动

  ```
  java -jar
  ```

- 打war包到tomcat上启动

  - 不带参数

  ![](http://i65.tinypic.com/2vjwriv.png)

  - 带**name**参数

    ![](http://i64.tinypic.com/e6du80.png)

