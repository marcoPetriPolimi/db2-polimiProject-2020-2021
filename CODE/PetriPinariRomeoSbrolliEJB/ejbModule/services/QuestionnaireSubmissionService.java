package services;

import java.lang.instrument.IllegalClassFormatException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import database.PersonalAnswer;
import database.ProductAnswer;
import database.Question;
import database.Questionnaire;
import database.Submission;
import database.User;
import exceptions.QuestionnaireException;
import utils.forms.FormPersonalAnswer;
import utils.forms.FormProductAnswer;

@Stateful
public class QuestionnaireSubmissionService {
	@PersistenceContext(unitName = "PetriPinariRomeoSbrolliEJB", type = PersistenceContextType.EXTENDED)
	private EntityManager em;
	private Questionnaire questionnaire;
	private List<Question> questions;
	private List<ProductAnswer> productAnswers;
	private PersonalAnswer personalAnswer;
	
	public QuestionnaireSubmissionService() {}
	
	/**
	 * Set the questionnaire value given the questionnaire's id.
	 * @param questionnaireId
	 * @throws QuestionnaireException
	 */
	public void setQuestionnaire(int questionnaireId) throws QuestionnaireException {
		questionnaire = em.find(Questionnaire.class, questionnaireId);
		
		if (questionnaire == null) {
			throw new QuestionnaireException();
		} else {
			questions = questionnaire.getQuestions();
			}
	}
	
	/**
	 * Update the list of product answer given by the user.
	 * @param answers
	 * @param userId
	 * @throws IllegalArgumentException
	 * @throws IllegalClassFormatException
	 */
	public void modifyProductAnswers(List<FormProductAnswer> answers, int userId) throws IllegalArgumentException{
		if (answers == null || answers.size() != questions.size()) {
			throw new IllegalArgumentException();
		}
		
		productAnswers = new ArrayList<>();
		// iterate over all answers and stores them in attributes
		for (int i = 0; i < answers.size(); i++) {
			Question relatedQuestion = em.find(Question.class, answers.get(i).getQuestion());
			
			// when the answer is a checkbox stores every response
			if (answers.get(i).getType() == 1) {
					for (int j = 0; j < answers.get(i).getResponses().size(); j++) {
						productAnswers.add(new ProductAnswer(relatedQuestion,answers.get(i).getResponses().get(j)));
					}
			} else {
					productAnswers.add(new ProductAnswer(relatedQuestion,answers.get(i).getResponse()));
			}
		}
	}

	/**
	 * Update the personal answers given by the user.
	 * @param answers
	 * @param userId
	 * @throws IllegalArgumentException
	 */
	public void modifyPersonalAnswers(FormPersonalAnswer answers, int userId) throws IllegalArgumentException {
		if (answers == null) {
			throw new IllegalArgumentException();
		}
		
		User relatedUser = em.find(User.class, userId);
		personalAnswer = new PersonalAnswer();
		if (answers.getAge() != -1) {
			personalAnswer.setAge(answers.getAge());
		}
		if (answers.getExpertise() != -1) {
			personalAnswer.setExpertise(answers.getExpertise());
		}
		if (answers.getSex() != 'X') {
			personalAnswer.setSex(answers.getSex());
		}
	}
	
	public void submitQuestionnaire(int userId) {
		submitOrCancelQuestionnaire(userId,true);
	}
	
	public void cancelQuestionnaire(int userId) {
		submitOrCancelQuestionnaire(userId,false);
	}
	
	/**
	 * Submits or cancels a questionnaire for a certain user dependently by the parameter submit. This class is used to reduce code replication due to the high similarity of the two operations.
	 * @param userId
	 * @param submit
	 */
	private void submitOrCancelQuestionnaire(int userId, boolean submit) {
		User relatedUser = em.find(User.class, userId);
		Submission toCreate = new Submission(questionnaire,relatedUser,submit? 1 : 0,0,new Date());
		em.persist(toCreate);
		
		if (submit) {
			for (int i = 0; i < productAnswers.size(); i++) {
				toCreate.addProductAnswer(productAnswers.get(i));
				em.persist(productAnswers.get(i));
			}
			if (personalAnswer!= null) {
				toCreate.addPersonalAnswer(personalAnswer);
				em.persist(personalAnswer);
			}
		}
	}
	
	@Remove
	public void remove() {}
}
