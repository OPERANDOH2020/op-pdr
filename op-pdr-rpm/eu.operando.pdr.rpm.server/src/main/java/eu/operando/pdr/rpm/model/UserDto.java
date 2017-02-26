package eu.operando.pdr.rpm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


public class UserDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private int iduser;

	private String firstname;

	private String lastname;
	
	private String salary;
	
	private List<Metadatavalue> metadatavalues;
	
	public int getIduser() {
		return iduser;
	}

	public void setIduser(int iduser) {
		this.iduser = iduser;
	}



	public String getFirstname() {
		return firstname;
	}



	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}



	public String getLastname() {
		return lastname;
	}



	public void setLastname(String lastname) {
		this.lastname = lastname;
	}



	public String getSalary() {
		return salary;
	}



	public void setSalary(String salary) {
		this.salary = salary;
	}



	public List<Metadatavalue> getMetadatavalues() {
		return metadatavalues;
	}



	public void setMetadatavalues(List<Metadatavalue> metadatavalues) {
		this.metadatavalues = metadatavalues;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	public UserDto() {
	}

}