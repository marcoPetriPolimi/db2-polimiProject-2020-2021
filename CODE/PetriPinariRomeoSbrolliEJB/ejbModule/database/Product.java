package database;


import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;


import utils.Const;

/**
 * This class is the EJB for the product database class.
 * @author Marco Petri
 */
@Entity
@Table(name = "product", schema = "db2_project")
public class Product {
	private static final long serialVersionUID = Const.EJBVersion;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Basic(fetch=FetchType.LAZY)
	@Lob
	private byte[] image;
	
	@Column(unique=true)
	private String name;
		
	public Product() {}
	public Product(byte[] image, String name) {
		this.image		= image;
		this.name		= name;
	}
	
	/* ******************
	 * 		GETTERS		*
	 ********************/
	public int getId() {
		return id;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	/* ******************
	 * 		SETTERS		*
	 ********************/
	public void setId(int id) {
		this.id = id;
	}
	public byte[] getImage() {
		return image;
	}
	public String getName() {
		return name;
	}

}
