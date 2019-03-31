# Spring_Guides
The series of Spring Guides from official documentation

Scheduling task

@Component : 把该类纳入Spring容器管理

@Scheduled(fixedRate = 5000) ：指定该方法为定时任务方法，并指定循环调用时间间隔为5000毫秒

以下是该注释的官网解释：

The Scheduled annotation defines when a particular method runs.

NOTE: This example uses fixedRate, which specifies the interval between method invocations measured from the start time of each invocation.

官网也提供了另外一种注释：

@Scheduled(fixedDelay = 1000)

该注释的作用是 ：在该注释下的方法完成后1秒再次调用

其实，还有另外两种注释：

@Scheduled(fixedRateString = "3000")
@Scheduled(fixedDelayString = "4000")

其实这两种注释的作用与前面介绍的两种一样，不同的是，这里用的是String的参数，但是在运行的时候，会将String转化为Long类型

并且如果不能转换的话，程序会报非法参数异常：java.lang.IllegalStateException

Encountered invalid @Scheduled method 'reportCurrentTimeWithFixedRateString': Invalid fixedRateString value "wonder" - cannot parse into long