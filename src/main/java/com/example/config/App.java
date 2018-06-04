package com.example.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Hello world!
 *
 */
@EnableConfigServer
@SpringBootApplication
public class App {
	public static void main(String[] args) {
		System.out.println("Hello World!");
		new SpringApplicationBuilder(App.class).web(true).run(args);
	}
}
