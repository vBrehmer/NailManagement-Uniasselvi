package com.godev.nailManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableAsync
public class NailManagementApplication {

	public static void main(String[] args) {SpringApplication.run(NailManagementApplication.class, args);}

}
