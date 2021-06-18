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
	@Column(name = "Id")
	private Integer Id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "lastName")
	private String lastName;
	
	@Column(name = "age")
	private int age;
	
	@Column(name = "biographyDocumentUrl")
	private String biographyDocumentUrl;
	
	
	public Student() {
		this.name = "";
		this.lastName = "";
		this.biographyDocumentUrl = "";
	}


	/**
	 * @param name
	 * @param lastName
	 * @param age
	 * @param biographyDocumentUrl
	 */
	public Student(String name, String lastName, int age, String biographyDocumentUrl) {
		super();
		this.name = name;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
