package services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;

import database.*;

import exceptions.QuestionnaireException;

//@contributors: Etion

@Stateless
public class QuestionnaireOfTheDayService {
	@PersistenceContext(unitName = "PetriPinariRomeoSbrolliEJB")
	private EntityManager em;

	public QuestionnaireOfTheDayService() {
	}
	
	public Questionnaire getQuestionnaire(int id) throws QuestionnaireException {
		Questionnaire result = em.find(Questionnaire.class, id);
		if(result == null) {
			throw new QuestionnaireException();
		} else {
			return result;
		}
	}
	
	/**	get product related to questionnaire with id: "int id"
	 * @param id: primary key of questionnaire
	 * @return the product related to questionnaire with id "id"
	 * @throws QuestionnaireException
	 */
	public Product getProduct(int id) throws QuestionnaireException {
		Questionnaire result = em.find(Questionnaire.class, id);
		if(result == null) {
			throw new QuestionnaireException("Questionnaire not found. Try another primary key");
		} else {
			return getProduct(result);
		}
	}
	
	/**
	 * 
	 * @param questionnaire of interest
	 * @return the related product to questionnaire
	 * @throws QuestionnaireException
	 */
	public Product getProduct(Questionnaire questionnaire) throws QuestionnaireException{
		if(questionnaire == null) {
			throw new QuestionnaireException("Null valued questionnaire");
		} else {
			int questionnairePK = questionnaire.getId();
						
						/////////////////////////
						//CHECK FOR CORRECTNESS//
						//BECAUSE PRODUCT FK IS//
						//NOT PRESENT IN SCHEMA//
						/////////////////////////
			Query query = em.createQuery("Select p "
						+ "From Product p, Questionnaire q "
						+ "Where q.id = :questionnairePK AND p.questionnaireId = q.id ", Product.class )
			.setParameter("questionnairePK", questionnairePK);
			List<Product> listResult = query.getResultList();
			
			Product result = listResult.get(0);
			
			if(result == null)
				throw new QuestionnaireException("Product not found, contact admins.");
			
			return result;
		}
	}
	
	/**
	 * Require one question based on its ID
	 */
	public Question getQuestion(int questionnaireId, int questionId) throws QuestionnaireException{
		Query query = em.createQuery("Select q"
									+ "From Question q, Inclusion i"
									+ "Where :questionnaireId = i.questionnaire AND"
									+ "		 i.question = :questionId ",Question.class);
		
		query.setParameter("questionnaireId", questionnaireId);
		query.setParameter("questionId", questionnaireId);
		List<Question> questions = query.getResultList();
		Question resultQuestion = questions.get(0);
		if(resultQuestion == null) {
		throw new QuestionnaireException();
		}
		
		return resultQuestion;
	}
	
	/**
	 * get all related questions to one questionnaire
	 */
	public List<Question> getQuestions(int questionnaireId) throws QuestionnaireException{

								/////////////////////////
								//CHECK FOR CORRECTNESS//
								// BECAUSE INCLUSION FK//
								//  MIGHT NOT HAVE THE //
								//      RIGHT NAME	   //
								/////////////////////////
		Query query = em.createQuery("Select q"
									+ "From Question q, Inclusion i"
									+ "Where :questionnaireId = i.questionnaire AND"
									+ "		 i.question = q.id ",Question.class)
									.setParameter("questionnaireId", questionnaireId);
		List<Question> questions = query.getResultList();
		
		if(questions.get(0) == null) {
			throw new QuestionnaireException();
		}
		
		return questions;
	}
}
