package ejb.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;

import ejb.database.*;


@Stateless
public class QuestionnaireOfTheDayService {
	@PersistenceContext(unitName = "QuestionnaireOfTheDayServiceEJB")
	private EntityManager em;

	public QuestionnaireOfTheDayService() {
	}
	
	public void getQuestionnaire(){
		
	}
	
	public void getProduct() {
		
	}
	
	
	/**
	 * Require one question based on its ID
	 */
	public void getQuestion() {
		
	}
	
	/**
	 * get all related questions to one questionnaire
	 */
	public void getQuestions() {
		
	}
}
