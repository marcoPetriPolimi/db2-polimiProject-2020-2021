package services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import database.Product;
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
		Product prod= em.find(Product.class, productId);
		Map<User,String> reviews= prod.getReviews();
		return reviews.keySet().stream().collect(Collectors.toMap(u -> (String) u.getNickname(), u -> reviews.get(u)));
	}

	public void addProductReview(User user, int productId, String productReview) throws ProductException, ReviewAlreadyPresentException{
		Product prod= em.find(Product.class, productId);
		if (prod == null) throw new ProductException("Could not find the requested product");
		if (prod.getReviews().keySet().stream().map(u -> u.getId()).collect(Collectors.toList()).contains(user.getId()))
			throw new ReviewAlreadyPresentException("The user has already review the product");
		else prod.addReview(user, productReview);		
	}

}
