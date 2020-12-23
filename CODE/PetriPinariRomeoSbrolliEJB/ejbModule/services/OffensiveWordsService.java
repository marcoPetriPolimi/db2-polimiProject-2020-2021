package services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import database.OffensiveWord;

/**
 * 
 * @author Marco Petri
 *
 */
@Stateless
public class OffensiveWordsService {
	@PersistenceContext(unitName = "PetriPinariRomeoSbrolliEJB")
	private EntityManager em;
	
	public boolean isPresent(String word) {
		List<OffensiveWord> offensiveWords = em.createNamedQuery("OffensiveWord.findByWord", OffensiveWord.class).setParameter(1, word).getResultList();
		
		return offensiveWords.stream().anyMatch((offWord) -> offWord.getWord().toUpperCase().equals(word.toUpperCase()));
	}
}
