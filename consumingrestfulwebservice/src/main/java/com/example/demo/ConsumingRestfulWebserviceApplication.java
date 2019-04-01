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
@SpringBootApplication
public class ConsumingRestfulWebserviceApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConsumingRestfulWebserviceApplication.class);

	public static void main(String[] args) {
		RestTemplate restTemplate = new RestTemplate();
		Quote quote = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
		LOGGER.info(quote.toString());
		SpringApplication.run(ConsumingRestfulWebserviceApplication.class, args);
	}

}
