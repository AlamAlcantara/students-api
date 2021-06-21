/**
 * 
 */
package com.example.students.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.Id;

/**
 * @author alamalcantara
 *
 */
@Entity
@Table(name = "student")
public class Student implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer Id;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "age")
	private int age;
	
	@Column(name = "biography_url")
	private String biographyDocumentUrl;
	
	
	public Student() {
		this.firstName = "";
		this.lastName = "";
		this.biographyDocumentUrl = "";
	}


	/**
	 * @param name
	 * @param lastName
	 * @param age
	 * @param biographyDocumentUrl
	 */
	public Student(String firstName, String lastName, int age, String biographyDocumentUrl) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.biographyDocumentUrl = biographyDocumentUrl;
	}


	/**
	 * @return the id
	 */
	public Integer getId() {
		return Id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		Id = id;
	}

	

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}


	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * @param age the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * @return the biographyDocumentUrl
	 */
	public String getBiographyDocumentUrl() {
		return biographyDocumentUrl;
	}

	/**
	 * @param biographyDocumentUrl the biographyDocumentUrl to set
	 */
	public void setBiographyDocumentUrl(String biographyDocumentUrl) {
		this.biographyDocumentUrl = biographyDocumentUrl;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Id == null) ? 0 : Id.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Student other = (Student) obj;
		if (Id == null) {
			if (other.Id != null)
				return false;
		} else if (!Id.equals(other.Id))
			return false;
		return true;
	}
	
	
	
	
}
