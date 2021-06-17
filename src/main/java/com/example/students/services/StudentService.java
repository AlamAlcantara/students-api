/**
 * 
 */
package com.example.students.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.students.entities.Student;
import com.example.students.repositories.StudentRepository;

/**
 * @author alamalcantara
 *
 */
@Service
public class StudentService {
	
	@Autowired
	private StudentRepository studentRepository;
	
	public List<Student> getStudents() {
		return studentRepository.findAll();
	}
	
	public Student createStudent(Student student) {
		return studentRepository.save(student);
	}
	
	public Student getStudentById(Integer id) {
		return studentRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
	}
	
	
	public Student updateStudent(Integer studentId, Student student) {
		Student loadedStudent = this.getStudentById(studentId);
		student.setId(studentId);
		
		return this.createStudent(student);
	}
	
	public void deleteStudent(Integer studentId) {
		this.studentRepository.deleteById(studentId);
	}
	
	

}
