package services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;

import database.*;
import exceptions.QuestionnaireCancellationException;

import database.Product;
import database.Question;
import database.Questionnaire;
import exceptions.QuestionnaireException;

//@contributors: Etion

@Stateless
public class QuestionnaireOfTheDayService {
	@PersistenceContext(unitName = "PetriPinariRomeoSbrolliEJB")
	private EntityManager em;

	public QuestionnaireOfTheDayService() {
	}

	public Questionnaire getQuestionnaireByDate(String dateAsString) throws QuestionnaireException, ParseException {

	    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateAsString);
		Query query = em.createQuery("Select q "
									+ "From Questionnaire q "
									+ "Where q.presDate = :date", Questionnaire.class )
									.setParameter("date", date);
									List<Questionnaire> listResult = query.getResultList();
		Questionnaire result = listResult.get(0);
		if(result == null) {
			throw new QuestionnaireException("Could not find questionnarie by publication date");
		} else {
			return result;
		}
	}
	
	public Questionnaire getQuestionnaireByDate(Date date) throws QuestionnaireException {

		Query query = em.createQuery("Select q "
									+ "From Questionnaire q "
									+ "Where q.presDate = :date", Questionnaire.class )
									.setParameter("date", date);
									List<Questionnaire> listResult = query.getResultList();
		Questionnaire result = listResult.get(0);
		if(result == null) {
			throw new QuestionnaireException("Could not find questionnarie by publication date");
		} else {
			return result;
		}
	}

	public Questionnaire getQuestionnaire(int id) throws QuestionnaireException {
		Questionnaire result = em.find(Questionnaire.class, id);
		if(result == null) {
			throw new QuestionnaireException("Could not find questionnarie by id");
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
		Questionnaire result = getQuestionnaire(id);

		return getProduct(result);
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


						/////////////////////////
						//CHECK FOR CORRECTNESS//
						//BECAUSE PRODUCT FK IS//
						//NOT PRESENT IN SCHEMA//
						/////////////////////////
			Query query = em.createQuery("Select p "
						+ "From Product p, Questionnaire q "
						+ "Where q.id = :questionnairePK AND p.questionnaireId = q.id ", Product.class )
			.setParameter("questionnairePK", questionnaire);
			List<Product> listResult = query.getResultList();


			if(listResult == null)
				throw new QuestionnaireException("Product not found, contact admins.");

			Product result = listResult.get(0);
			return result;
		}
	}

	/**
	 * Require one question based on its ID
	 */
	public Question getQuestion(int questionnaireId, int questionId) throws QuestionnaireException{

		Questionnaire questionnaire = new Questionnaire();
		questionnaire.setId(questionnaireId);

		Question question = new Question();
		question.setId(questionId);

		Query query = em.createQuery("Select q "
									+ "From Question q, Inclusion i "
									+ "Where i.questionnaire = :questionnaireId AND"
									+ " i.question = :questionId ",Question.class);

		query.setParameter("questionnaireId", questionnaire);
		query.setParameter("questionId", questionnaire);
		List<Question> questions = query.getResultList();

		if(questions == null) {
			throw new QuestionnaireException();
		}

		Question resultQuestion = questions.get(0);

		return resultQuestion;
	}

	/**
	 * get all related questions to one questionnaire
	 */
	public List<Question> getQuestions(int questionnaireId) throws QuestionnaireException{

		Questionnaire parameter= new Questionnaire();
		parameter.setId(questionnaireId);
		Query query = em.createQuery("Select q From Question q, Inclusion i "
									+ "Where :questionnaireId = i.inclusionQuestionnaire AND"
									+ " i.inclusionQuestion = q ",Question.class)
									.setParameter("questionnaireId",  parameter );
		List<Question> questions = query.getResultList();

		if(questions == null) {
			throw new QuestionnaireException("No questions related to questionnaire");
		}

		return questions;
	}

	/**
	 * get all related questions to one questionnaire
	 */
	public List<Question> getQuestions(Questionnaire questionnaire) throws QuestionnaireException{
		return getQuestions(questionnaire.getId());
	}

	public Questionnaire getQuestionnaireOfTheDay() throws QuestionnaireException, ParseException {
		Date date = new Date();  
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
	    String strDate= formatter.format(date);  
	    
	    return this.getQuestionnaireByDate(strDate);
	}
	
	public void deleteQuestionnaire(int questionnaireId) throws QuestionnaireCancellationException {
		Questionnaire q = em.find(Questionnaire.class, questionnaireId);
		if (q.getDate().compareTo(new Date()) > 0) {
			throw new QuestionnaireCancellationException();
		}
		em.remove(q);
	}
}
