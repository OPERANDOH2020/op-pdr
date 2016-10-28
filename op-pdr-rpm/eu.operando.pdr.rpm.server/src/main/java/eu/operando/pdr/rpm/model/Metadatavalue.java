package eu.operando.pdr.rpm.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the METADATAVALUES database table.
 * 
 */
@Entity
@Table(name="METADATAVALUES")
@NamedQuery(name="Metadatavalue.findAll", query="SELECT m FROM Metadatavalue m")
public class Metadatavalue implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	@Column(unique=true, nullable=true)
	private int id;

	@Column(length=45)
	private String lang;

	@Column(name="text_value", length=255)
	private String textValue;

	//bi-directional many-to-one association to Metadatafieldregistry
	@ManyToOne(cascade={CascadeType.ALL}, fetch=FetchType.LAZY)
	@JoinColumn(name="metadatafield_id")
	private Metadatafieldregistry metadatafieldregistry;

	//bi-directional many-to-one association to User
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;

	public Metadatavalue() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLang() {
		return this.lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getTextValue() {
		return this.textValue;
	}

	public void setTextValue(String textValue) {
		this.textValue = textValue;
	}

	public Metadatafieldregistry getMetadatafieldregistry() {
		return this.metadatafieldregistry;
	}

	public void setMetadatafieldregistry(Metadatafieldregistry metadatafieldregistry) {
		this.metadatafieldregistry = metadatafieldregistry;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}