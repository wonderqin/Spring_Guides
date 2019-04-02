package com.example.demo;

import com.example.demo.hello.Customer;
import jdk.nashorn.internal.scripts.JD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
/**
 * @Author wonderqin
 * @Description jdbc with Spring
 **/
@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	private static final Logger LOGGER= LoggerFactory.getLogger(DemoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public void run(String... args) throws Exception {

		LOGGER.info("Creating table customer");
		jdbcTemplate.execute("DROP TABLE customer IF EXISTS");
		jdbcTemplate.execute("CREATE TABLE customer (id SERIAL, firstName varchar(255), lastName varchar(255))");

		List<Object[]> splitUpName = Arrays.asList("John Wu", "Smith Luolrn", "Alisda Ite","Smith bro").stream()
				.map(name -> name.split(" "))
				.collect(Collectors.toList());

		splitUpName.forEach(name -> LOGGER.info(String.format("Inserting customer record for %s %s",name[0],name[1])));
		jdbcTemplate.batchUpdate("INSERT INTO customer(firstName, lastName) VALUES (?,?)",splitUpName);

		LOGGER.info("Querying for customer records where first_name = 'Josh':");

		jdbcTemplate.query(
				"select id, firstName, lastName from customer where firstName = ?",new Object[]{"Smith"},
				(rs,rowNum)->new Customer(rs.getLong("id"),rs.getString("firstName"),rs.getString("lastName"))
		).forEach(customer -> LOGGER.info(customer.toString()));

	}
}
