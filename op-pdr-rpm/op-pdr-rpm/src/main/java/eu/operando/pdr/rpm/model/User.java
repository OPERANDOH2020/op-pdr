package eu.operando.pdr.rpm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the USER database table.
 * 
 */
@Entity
@Table(name="USER")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	@Column(unique=true, nullable=true)
	private int iduser;

//	@Column(length=255)
//	private String address;
//
//	@Column(length=255)
//	private String country;
//
//	@Lob
//	private String description;
//
//	@Column(length=255)
//	private String email;

	@Column(nullable=false, length=255)
	private String firstname;

	@Column(nullable=false, length=255)
	private String lastname;

	//bi-directional many-to-one association to Metadatavalue
	@OneToMany(mappedBy="user")
	private List<Metadatavalue> metadatavalues;

	public User() {
	}

	public int getIduser() {
		return this.iduser;
	}

	public void setIduser(int iduser) {
		this.iduser = iduser;
	}

//	public String getAddress() {
//		return this.address;
//	}
//
//	public void setAddress(String address) {
//		this.address = address;
//	}
//
//	public String getCountry() {
//		return this.country;
//	}
//
//	public void setCountry(String country) {
//		this.country = country;
//	}
//
//	public String getDescription() {
//		return this.description;
//	}
//
//	public void setDescription(String description) {
//		this.description = description;
//	}
//
//	public String getEmail() {
//		return this.email;
//	}
//
//	public void setEmail(String email) {
//		this.email = email;
//	}

	public String getFirstname() {
		return this.firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return this.lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public List<Metadatavalue> getMetadatavalues() {
		return this.metadatavalues;
	}

	public void setMetadatavalues(List<Metadatavalue> metadatavalues) {
		this.metadatavalues = metadatavalues;
	}

	public Metadatavalue addMetadatavalue(Metadatavalue metadatavalue) {
		getMetadatavalues().add(metadatavalue);
		metadatavalue.setUser(this);

		return metadatavalue;
	}

	public Metadatavalue removeMetadatavalue(Metadatavalue metadatavalue) {
		getMetadatavalues().remove(metadatavalue);
		metadatavalue.setUser(null);

		return metadatavalue;
	}

}