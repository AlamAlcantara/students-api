package com.example.students;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.students.entities.Student;
import com.example.students.services.StudentService;

@SpringBootApplication
public class StudentsApiApplication implements ApplicationRunner {

	@Autowired
	private StudentService studentService;
	
	
	private static final Logger log = LoggerFactory.getLogger(StudentsApiApplication.class);

	
	public static void main(String[] args) {
		SpringApplication.run(StudentsApiApplication.class, args);
	}

	//TODO: remove when the database is ready
	@Override
	public void run(ApplicationArguments args) throws Exception {
		// TODO Auto-generated method stub
		for(int i = 0; i <= 10; i ++) {
			Student s = new Student("Alam", "Alcantara", 21, "RandomUrl");
			studentService.createStudent(s);
			log.info("Student created: {}", s.getName());
		}
	}

}
