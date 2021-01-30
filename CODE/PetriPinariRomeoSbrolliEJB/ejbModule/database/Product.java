package database;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


import utils.Const;

/**
 * This class is the EJB for the product database class.
 * @author Marco Petri
 */
@Entity
@Table(name = "product", schema = "db2_project")
@NamedQueries(value = {
		@NamedQuery(name="Product.getAll", query="SELECT p FROM Product p"),
		@NamedQuery(name="Product.getByName", query="SELECT p FROM Product p WHERE p.name=:pName")
	})
public class Product {
	private static final long serialVersionUID = Const.EJBVersion;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "review",joinColumns = @JoinColumn(name="productId"))
	@MapKeyJoinColumn(name = "userId")
	@Column(name = "productReview")
	private Map<User, String> reviews;

	
	@Basic(fetch=FetchType.EAGER)
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

	public Map<User, String> getReviews() {
		return reviews;
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
	
	public void setReviews(Map<User, String> reviews) {
		this.reviews = reviews;
	}
	
	public void addReview(User user,String review) {
		this.reviews.put(user, review);
	}
}
