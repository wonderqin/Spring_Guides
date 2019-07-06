## SpringBoot生态中的LDAP认证

### 前言

本文主要讲述如何利用Spring的JdbcTemplate访问关系型数据库。

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
		<version>2.1.6.RELEASE</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	<groupId>com.example</groupId>
	<artifactId>authenticating_ldap</artifactId>
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
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.ldap</groupId>
			<artifactId>spring-ldap-core</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.security</groupId>
			<artifactId>spring-security-ldap</artifactId>
		</dependency>
		<dependency>
			<groupId>com.unboundid</groupId>
			<artifactId>unboundid-ldapsdk</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.security</groupId>
			<artifactId>spring-security-test</artifactId>
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

注意到，Spring的ldap认证是在spring.security包下的，说明这是Security模块下的一个功能。

### 控制器

下面我们来简单地建立一个控制器，以控制HTTP的request和response。

```java
package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @ClassName HomeController
 * @Description TODO
 * @Author wonderQin
 * @Date 2019-07-06 16:27
 **/
@Controller
public class HomeController {
    @GetMapping("/")
    public String index(){
        return "Hello, welcome to home page!";
    }
}
```

以上，我们通过返回一条简单的message来处理"/"的GET请求。

我们来复习下在个类中出现的注解的功能：

- @Controller: 被该注解作用的整个类，Spring MVC可以使用它的内置扫描功能自动检测控制器并自动配置Web路由（通俗地说就是标志该类为控制器类，但这种说法没解释到点子上）。
- @GetMapping：该注解则标明被该注解标记的类为HTTP的处理类，与@RequestMapping类似，它主要有两个功能：一是标记路径，标记浏览器的访问路径；二是说明REST操作。这里的@GetMapping则显式地说明该HTTP请求只能是”GET“请求。



### Springboot入口类

```Java
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}

```

该类的相关知识点在我之前的文章中有所描述，在这里复习下：

- **@Configuration**将类标记为应用程序上下文的bean定义源。
- **@EnableAutoConfiguration**告诉Spring Boot根据类路径设置，其他bean和各种属性设置开始添加bean。
- 通常我们在使用spring的mvc时，会为Spring MVC应用程序添加**@EnableWebMvc**注解，但Spring Boot会在类路径上看到spring-webmvc时自动添加它。 这会将应用程序标记为Web应用程序并激活关键行为，例如设置DispatcherServlet。
- @ComponentScan告诉Spring在**当前目录下（包括当前目录）的包中**寻找其他组件，配置和服务，允许它找到控制器。

### 配置LDAP的安全策略

```java
package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import sun.nio.cs.ext.GBK;

/**
 * @ClassName WebSecurityConfig
 * @Description TODO
 * @Author wonderQin
 * @Date 2019-07-06 16:29
 **/
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authorizeRequests()
                .anyRequest().fullyAuthenticated()
                .and()
                .formLogin();
    }

    @Override
    public void configure(AuthenticationManagerBuilder builder) throws Exception{
        builder
                .ldapAuthentication()
                .userDnPatterns("uid={0},ou=people")
                .groupSearchBase("ou=groups")
                .contextSource()
                .url("ldap://localhost:8080/dc=springframework,dc=org")
                .and()
                .passwordCompare()
                .passwordEncoder(new PasswordEncoder() {
                    @Override
                    public String encode(CharSequence charSequence) {
                        return "gbk";
                    }

                    @Override
                    public boolean matches(CharSequence charSequence, String s) {
                        return false;
                    }
                })
                .passwordAttribute("usePassword");
    }
}

```

这里其实就是重写了WebSecurityConfigurerAdapter中的配置策略：

- configure(HttpSecurity httpSecurity)方法是对LDAP登录模式的配置；

- configure(AuthenticationManagerBuilder builder)：则是对Security LDAP上下文的配置，比如：LDAP服务器、编码方式等。

- 在我们看到：.url("ldap://localhost:8080/dc=springframework,dc=org")的时候，就明白我们这里还需要一个ldap服务器，该服务器可以自己搭建，这里可以参考[LDAP服务器的搭建](https://www.cnblogs.com/lemon-le/p/6266921.html)

- ldapAuthentication（）方法配置登录表单中的用户名插入{0}的内容，以便在LDAP服务器中搜索uid = {0}，ou = people，dc = springframework，dc = org。 此外，passwordCompare（）方法配置编码器和密码属性的名称。

### 使用LDIF交换用户数据

LDAP服务器可以使用LDIF（LDAP数据交换格式）文件来交换用户数据。 application.properties中的spring.ldap.embedded.ldif属性允许Spring Boot拉入LDIF数据文件。 这样可以轻松预加载演示数据。

- 在resources文件夹下新建test-server.ldif文件

```
dn: dc=springframework,dc=org
objectclass: top
objectclass: domain
objectclass: extensibleObject
dc: springframework

dn: ou=groups,dc=springframework,dc=org
objectclass: top
objectclass: organizationalUnit
ou: groups

dn: ou=subgroups,ou=groups,dc=springframework,dc=org
objectclass: top
objectclass: organizationalUnit
ou: subgroups

dn: ou=people,dc=springframework,dc=org
objectclass: top
objectclass: organizationalUnit
ou: people

dn: ou=space cadets,dc=springframework,dc=org
objectclass: top
objectclass: organizationalUnit
ou: space cadets

dn: ou=\"quoted people\",dc=springframework,dc=org
objectclass: top
objectclass: organizationalUnit
ou: "quoted people"

dn: ou=otherpeople,dc=springframework,dc=org
objectclass: top
objectclass: organizationalUnit
ou: otherpeople

dn: uid=ben,ou=people,dc=springframework,dc=org
objectclass: top
objectclass: person
objectclass: organizationalPerson
objectclass: inetOrgPerson
cn: Ben Alex
sn: Alex
uid: ben
userPassword: {SHA}nFCebWjxfaLbHHG1Qk5UU4trbvQ=

dn: uid=bob,ou=people,dc=springframework,dc=org
objectclass: top
objectclass: person
objectclass: organizationalPerson
objectclass: inetOrgPerson
cn: Bob Hamilton
sn: Hamilton
uid: bob
userPassword: bobspassword

dn: uid=joe,ou=otherpeople,dc=springframework,dc=org
objectclass: top
objectclass: person
objectclass: organizationalPerson
objectclass: inetOrgPerson
cn: Joe Smeth
sn: Smeth
uid: joe
userPassword: joespassword

dn: cn=mouse\, jerry,ou=people,dc=springframework,dc=org
objectclass: top
objectclass: person
objectclass: organizationalPerson
objectclass: inetOrgPerson
cn: Mouse, Jerry
sn: Mouse
uid: jerry
userPassword: jerryspassword

dn: cn=slash/guy,ou=people,dc=springframework,dc=org
objectclass: top
objectclass: person
objectclass: organizationalPerson
objectclass: inetOrgPerson
cn: slash/guy
sn: Slash
uid: slashguy
userPassword: slashguyspassword

dn: cn=quote\"guy,ou=\"quoted people\",dc=springframework,dc=org
objectclass: top
objectclass: person
objectclass: organizationalPerson
objectclass: inetOrgPerson
cn: quote\"guy
sn: Quote
uid: quoteguy
userPassword: quoteguyspassword

dn: uid=space cadet,ou=space cadets,dc=springframework,dc=org
objectclass: top
objectclass: person
objectclass: organizationalPerson
objectclass: inetOrgPerson
cn: Space Cadet
sn: Cadet
uid: space cadet
userPassword: spacecadetspassword



dn: cn=developers,ou=groups,dc=springframework,dc=org
objectclass: top
objectclass: groupOfUniqueNames
cn: developers
ou: developer
uniqueMember: uid=ben,ou=people,dc=springframework,dc=org
uniqueMember: uid=bob,ou=people,dc=springframework,dc=org

dn: cn=managers,ou=groups,dc=springframework,dc=org
objectclass: top
objectclass: groupOfUniqueNames
cn: managers
ou: manager
uniqueMember: uid=ben,ou=people,dc=springframework,dc=org
uniqueMember: cn=mouse\, jerry,ou=people,dc=springframework,dc=org

dn: cn=submanagers,ou=subgroups,ou=groups,dc=springframework,dc=org
objectclass: top
objectclass: groupOfUniqueNames
cn: submanagers
ou: submanager
uniqueMember: uid=ben,ou=people,dc=springframework,dc=org
```

- application.properties文件配置

```properties
spring.ldap.embedded.ldif=classpath:test-server.ldif
spring.ldap.embedded.base-dn=dc=springframework,dc=org
spring.ldap.embedded.port=8389
```

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

再次访问：localhost:8080/时则需要通过LDAP登录了：

![](http://i65.tinypic.com/24zlouu.png)

这时候输入用户名：ben	密码：benspassword就可以通过验证