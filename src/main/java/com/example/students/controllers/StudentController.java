/**
 * 
 */
package com.example.students.controllers;

import java.io.IOException;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.students.entities.Student;
import com.example.students.services.StudentService;

/**
 * @author alamalcantara
 *
 */
@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class StudentController {

	@Autowired
	private StudentService studentService;
	
	
	private static final Logger log = LoggerFactory.getLogger(StudentController.class);

	
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
	
	@PostMapping("/{studentId}/biography")
	public ResponseEntity<Void> uploadBiographyFile(@PathVariable("studentId")int studentId, @RequestParam("file") MultipartFile file) {
		studentService.uploadStudentBiography(studentId, file);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("/{studentId}/biography")
	public ResponseEntity<Resource> getBiographyFile(@PathVariable("studentId")int studentId) {
		
		try {
			Resource resource = studentService.getStudentBiography(studentId);
			String mimeType = new MimetypesFileTypeMap().getContentType(resource.getFile());
			
			return ResponseEntity.ok()
					.contentType(MediaType.parseMediaType(mimeType))
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ resource.getFilename()+"\"")
					.body(resource);
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while loading the student biography");
		}
	}
	
	@DeleteMapping("/{studentId}/biography")
	public ResponseEntity<Void> uploadBiographyFile(@PathVariable("studentId")int studentId) {
		studentService.deleteStudenBiography(studentId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/file")
	public ResponseEntity<Void> createStudentsFromFile(@RequestParam("file") MultipartFile file) {
		studentService.bulkStudents(file);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
