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
