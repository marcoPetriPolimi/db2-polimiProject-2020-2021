package services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import database.Product;
import database.Review;
import database.Submission;
import database.User;
import exceptions.ProductException;
import exceptions.UserException;

/*
 * @author Giorgio Romeo
 */
@Stateless
public class ReviewService {
	
	@PersistenceContext(unitName = "PetriPinariRomeoSbrolliEJB")
	private EntityManager em;
	
	public ReviewService() {
	}
	
	/* Get the reviews of the product with id: productId
	 * @param productId The id of the product
	 * @return A map with key the user nickname and with value his review of the product 
	 */
	public Map<String, String> getProductReviews(int productId){
		List<Object[]> productReviews = em
				.createQuery("SELECT u.nickname, r.productReview "
						   + "FROM Review r, User u "
						   + "WHERE r.user = u AND r.product.id = :pId ",Object[].class)
				.setParameter("pId", productId).getResultList();
		
		return productReviews.stream().collect(Collectors.toMap(a -> (String) a[0], a -> (String) a[1]));
	}

	public void addProductReview(int UserId, int productId, String productReview) throws ProductException, UserException {
		
		Product relatedProduct = em.find(Product.class, productId);
		if (relatedProduct == null) throw new ProductException("Could not find the requested product");
		
		User relatedUser = em.find(User.class, UserId);
		if (relatedUser == null) throw new UserException("The user does not exist");
		
		Review newReview = new Review(relatedUser, relatedProduct, productReview);
		em.persist(newReview);
		
	}

}
