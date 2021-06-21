/**
 * 
 */
package com.example.students.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
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
	
	@Autowired
	private AmazonS3 s3;
	
	
	private static final Logger log = LoggerFactory.getLogger(StudentService.class);

	
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
	
	
	public void bulkStudents(MultipartFile file) {
		try {
			File xmlFile = File.createTempFile("students", "xml");
			FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(xmlFile));
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
			Document dom = documentBuilder.parse(xmlFile);
			dom.getDocumentElement().normalize();
			
			NodeList nodeList = dom.getElementsByTagName("student");
			
			for(int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				
				if(node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					String name = element.getElementsByTagName("name").item(0).getTextContent();
					String lastName = element.getElementsByTagName("lastname").item(0).getTextContent();
					int age = Integer.valueOf(element.getElementsByTagName("age").item(0).getTextContent());
					
					Student s = new Student(name, lastName, age, "");
					this.createStudent(s);
					
				}
				
			}
			
		} catch (IOException | ParserConfigurationException | SAXException e) {
			// TODO Auto-generated catch block
			log.error("Error parsing xml file", e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while creating the students");
		}
	}
	
	
	public void uploadStudentBiography(int studentId, MultipartFile biographyFile) {
		
		//Search student
		Student student = this.getStudentById(studentId);
		
		//upload the file to s3 and update the student info
		String bucketName = "college-students";
		String pathToUpload = String.format("students/%s/biography/%s", studentId, biographyFile.getOriginalFilename());
		
		try {
			String[] fileName = biographyFile.getOriginalFilename().split("\\.");
			
			File fileToUpload = File.createTempFile(fileName[0], fileName[1]);
			FileCopyUtils.copy(biographyFile.getInputStream(), new FileOutputStream(fileToUpload));
			
			//TODO: check if the directory is empty other wise the current file needs to be replaced
			
			s3.putObject(bucketName, pathToUpload, fileToUpload);
			
		} catch (SdkClientException | IOException e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while saving the student biography");
		}

		
		//Update student biography url
		student.setBiographyDocumentUrl(pathToUpload);
		this.updateStudent(studentId, student);
	}

}
