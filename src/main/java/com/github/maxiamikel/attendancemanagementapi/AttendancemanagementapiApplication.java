package com.github.maxiamikel.attendancemanagementapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AttendancemanagementapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AttendancemanagementapiApplication.class, args);
	}

}
