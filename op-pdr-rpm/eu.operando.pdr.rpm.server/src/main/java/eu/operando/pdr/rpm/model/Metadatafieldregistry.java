package eu.operando.pdr.rpm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the METADATAFIELDREGISTRY database table.
 * 
 */
@Entity
@Table(name="METADATAFIELDREGISTRY")
@NamedQuery(name="Metadatafieldregistry.findAll", query="SELECT m FROM Metadatafieldregistry m")
public class Metadatafieldregistry implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int id;

	@Column(length=255)
	private String description;

	@Column(nullable=false, length=255)
	private String element;

	@Column(length=45)
	private String label;

	//bi-directional many-to-one association to Metadatavalue
	@OneToMany(mappedBy="metadatafieldregistry", cascade={CascadeType.ALL})
	private List<Metadatavalue> metadatavalues;

	public Metadatafieldregistry() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getElement() {
		return this.element;
	}

	public void setElement(String element) {
		this.element = element;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<Metadatavalue> getMetadatavalues() {
		return this.metadatavalues;
	}

	public void setMetadatavalues(List<Metadatavalue> metadatavalues) {
		this.metadatavalues = metadatavalues;
	}

	public Metadatavalue addMetadatavalue(Metadatavalue metadatavalue) {
		getMetadatavalues().add(metadatavalue);
		metadatavalue.setMetadatafieldregistry(this);

		return metadatavalue;
	}

	public Metadatavalue removeMetadatavalue(Metadatavalue metadatavalue) {
		getMetadatavalues().remove(metadatavalue);
		metadatavalue.setMetadatafieldregistry(null);

		return metadatavalue;
	}

}