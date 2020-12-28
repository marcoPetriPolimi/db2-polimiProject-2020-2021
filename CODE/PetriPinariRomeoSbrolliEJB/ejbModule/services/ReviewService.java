package services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import database.Product;
import database.Review;
import database.User;
import exceptions.ProductException;
import exceptions.ReviewAlreadyPresentException;

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

	public void addProductReview(User user, Product product, String productReview) throws ProductException, ReviewAlreadyPresentException{
		
		if (product == null) throw new ProductException("Could not find the requested product");
		
		try {
			em
			.createQuery("SELECT r.id "
					   	 + "FROM Review r "
						 + "WHERE r.user = :relUser AND r.product = :p ")
			.setParameter("p", product).setParameter("relUser", user).getSingleResult();
		}catch (NoResultException e) {
			Review newReview = new Review(user, product, productReview);
			em.persist(newReview);
			return;
		}
		
		throw new ReviewAlreadyPresentException("The user has already review the product");
		
		
		
		
	}

}
