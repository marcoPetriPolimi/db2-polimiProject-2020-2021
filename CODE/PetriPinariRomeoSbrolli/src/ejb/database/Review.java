package ejb.database;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * This class is the EJB for the review database class.
 * @author Marco Petri
 */
@Entity
@Table(name = "review", schema = "db2_project",
	uniqueConstraints = @UniqueConstraint(columnNames={"userId","productId"}))
public class Review {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name="userId")
	private User user;
	
	@ManyToOne
	@JoinColumn(name="productId")
	private Product product;

	public Review() {}
	public Review(User user, Product product) {
		this.user		= user;
		this.product	= product;
	}

	/* ******************
	 * 		GETTERS		*
	 ********************/
	public int getId() {
		return id;
	}
	public Product getProduct() {
		return product;
	}
	public User getUser() {
		return user;
	}

	/* ******************
	 * 		SETTERS		*
	 ********************/
	public void setId(int id) {
		this.id = id;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
