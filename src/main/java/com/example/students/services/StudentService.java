/**
 * 
 */
package com.example.students.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
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
import com.amazonaws.services.s3.model.S3Object;
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
	private AmazonS3Service amazonS3Service;
	
	
	private static final Logger log = LoggerFactory.getLogger(StudentService.class);

	/** 
	 * Method to get the list of the students
	 * @return List of Student
	 * */
	public List<Student> getStudents() {
		return studentRepository.findAll();
	}
	
	/** 
	 * Method to create a student
	 * @param student -> Student to be created
	 * @return the created Student
	 * */
	public Student createStudent(Student student) {
		return studentRepository.save(student);
	}
	
	/** 
	 * Method to get a student by its id
	 * @param id -> integer of the id to be searched
	 * @return Student
	 * @throws ResponseStatusException if the student doesn't exists
	 * */
	public Student getStudentById(Integer id) {
		return studentRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
	}
	
	/** 
	 * Method to update a student by its id
	 * @param id -> integer of the id to be updated
	 * @param student -> the student's new info
	 * @return Student
	 * @throws ResponseStatusException if the student doesn't exists
	 * */
	public Student updateStudent(Integer studentId, Student student) {
		Student loadedStudent = this.getStudentById(studentId);
		student.setId(studentId);
		
		return this.createStudent(student);
	}
	
	/** 
	 * Method to delete a student by its id
	 * @param id -> integer of the id to be deleted
	 * */
	public void deleteStudent(Integer studentId) {
		this.deleteStudenBiography(studentId);
		this.studentRepository.deleteById(studentId);
	}
	
	/** 
	 * Method to create a students from an XML file
	 * @param file -> XML MultipartFile containing the students info
	 * */
	public void bulkStudents(MultipartFile file) {
		
		String fileExtension = file.getOriginalFilename().split("\\.")[1];
		
		if(!fileExtension.equals("xml")) {
			throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "The file should be an XML");
		}
		
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
	
	/** 
	 * Method to upload a student biography file
	 * @param id -> student id
	 * @param biographyFile -> the file to be uploaded
	 * */
	public void uploadStudentBiography(int studentId, MultipartFile biographyFile) {
		
		//check file extension
		boolean isExtensionValid = this.isFileExtensionValid(biographyFile.getOriginalFilename().split("\\.")[1]);
		
		if(!isExtensionValid) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file extension");
		}
		
		//Search student
		Student student = this.getStudentById(studentId);
		
		//upload the file to s3 and update the student info
		String pathToUpload = String.format("students/%s/biography/%s", studentId, biographyFile.getOriginalFilename());
		
		try {
			String[] fileName = biographyFile.getOriginalFilename().split("\\.");
			
			File fileToUpload = File.createTempFile(fileName[0], fileName[1]);
			FileCopyUtils.copy(biographyFile.getInputStream(), new FileOutputStream(fileToUpload));
			
			//Check if the directory is empty other wise the current file needs to be replaced
			String pathToSearch =  String.format("students/%s/biography", studentId);
			amazonS3Service.deleteObjects(pathToSearch);
			
			//Save file
			amazonS3Service.putObject(pathToUpload, fileToUpload);
			
			//Update student biography URL
			student.setBiographyDocumentUrl(pathToUpload);
			this.updateStudent(studentId, student);
			
		} catch (SdkClientException | IOException e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while saving the student biography");
		}
	}
	
	
	/** 
	 * Method to check if the biography file extension is valid
	 * @param fileExtension -> file extension to be evaluated
	 * @return true if the file extension is valid
	 * */
	public boolean isFileExtensionValid(String fileExtension) {	
		List<String> permittedFileExtensions = new ArrayList<>();
		permittedFileExtensions.add("pdf");
		permittedFileExtensions.add("txt");
		permittedFileExtensions.add("doc");
		permittedFileExtensions.add("docx");
		
		return permittedFileExtensions.contains(fileExtension);	
	}
	
	
	/** 
	 * Method to get a student biography file
	 * @param studentId -> id of the student
	 * @return Resource of the biography file
	 * */
	public Resource getStudentBiography(int studentId) {
		
		Student s = this.getStudentById(studentId);
		
		if(s.getBiographyDocumentUrl().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, "The student has no biography file");
		}
	
		try {
			S3Object biographyFile = amazonS3Service.getS3Object(s.getBiographyDocumentUrl());
				
			String[] directories = biographyFile.getKey().split("\\/");
			String fileName = directories[directories.length - 1];
		
			String tDir = System.getProperty("java.io.tmpdir");
			
			File fileToRetrieve = new File(tDir, fileName);
			FileCopyUtils.copy(biographyFile.getObjectContent(), new FileOutputStream(fileToRetrieve));
			return new FileSystemResource(fileToRetrieve);
			
		} catch (SdkClientException | IOException e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while loading the student biography");
		}
		
	}
	
	/** 
	 * Method to delete a biography file
	 * @param id -> integer of the student id
	 * */
	public void deleteStudenBiography(int studentId) {
		Student s = this.getStudentById(studentId);
		
		try {
			amazonS3Service.deleteObjects(s.getBiographyDocumentUrl());
			s.setBiographyDocumentUrl("");
			this.studentRepository.save(s);
			
		} catch (SdkClientException e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while deleting the student biography");
		}
	}

}
