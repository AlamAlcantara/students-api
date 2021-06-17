/**
 * 
 */
package com.example.students.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.students.entities.Student;
import com.example.students.services.StudentService;

/**
 * @author alamalcantara
 *
 */

@RestController
@RequestMapping("/")
public class StudentController {

	@Autowired
	private StudentService studentService;
	
	@GetMapping
	public ResponseEntity<List<Student>> getStudents() {
		return new ResponseEntity<>(studentService.getStudents(), HttpStatus.OK);
	}
	
	@GetMapping("/{studentId}")
	public ResponseEntity<Student> getStudentbyId(@PathVariable("studentId") Integer studentId) {
		return new ResponseEntity<>(studentService.getStudentById(studentId), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Student> createStudent(@RequestBody() Student student) {
		
		return new ResponseEntity<>(studentService.createStudent(student), HttpStatus.CREATED);
	}
	
	@PutMapping("/{studentId}")
	public ResponseEntity<Student> updateStudent(@PathVariable("studentId") Integer studentId, @RequestBody() Student student) {
		
		return new ResponseEntity<>(studentService.updateStudent(studentId, student), HttpStatus.OK);
	}
	
	@DeleteMapping("/{studentId}")
	public ResponseEntity<Void> deleteStudent(@PathVariable("studentId") Integer studentId) {
		studentService.deleteStudent(studentId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
