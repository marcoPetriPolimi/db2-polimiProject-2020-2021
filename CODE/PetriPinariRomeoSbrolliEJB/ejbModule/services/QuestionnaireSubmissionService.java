package services;

import java.lang.instrument.IllegalClassFormatException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import database.OffensiveWord;
import database.PersonalAnswer;
import database.ProductAnswer;
import database.Question;
import database.Questionnaire;
import database.Submission;
import database.User;
import exceptions.QuestionnaireException;
import utils.Const;
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
	private boolean offensiveWords;
	

	@EJB(name = "services/OffensiveWordsService")
	private OffensiveWordsService offWords;
	
	public QuestionnaireSubmissionService() {
		offensiveWords = false;
	}
	
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
		
		offensiveWords = false;
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
	
	public void clearResponses() {
		questions.clear();
		productAnswers.clear();
		personalAnswer = null;
	}
	
	public boolean isQuestionnaireSet() {
		return questionnaire != null;
	}
	
	public boolean getOffensiveWords() {
		return offensiveWords;
	}
	
	public boolean areAllQuestionsReplied() {
		for (Question q : questions) {
			boolean replied = false;
			for (ProductAnswer p : productAnswers) {
				if (p.getQuestionId().getId() == q.getId()) {
					replied = true;
				}
			}
			if (!replied) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isSubmissionWellFormed() {
		if (!areAllQuestionsReplied()) {
			return false;
		} else {
			for (ProductAnswer p: productAnswers) {
				if (p.getQuestionId().getType() == 1 || p.getQuestionId().getType() == 2) {
					List<String> answers = questions.stream().filter((e) -> e.getId() == p.getQuestionId().getId()).collect(Collectors.toList()).get(0).getQuestionAnswers().stream().map((e) -> e.getAnswerText()).collect(Collectors.toList());
					if (!answers.contains(p.getWord())) {
						// an answer given is not one of the one predicted
						return false;
					}
				} else {
					if (p.getWord().trim().length() == 0 || p.getWord().length() > Const.answersMaxLength) {
						return false;
					} else {
						for (OffensiveWord ow : offWords.getAllBadWords()) {
							if (p.getWord().contains(ow.getWord())) {
								offensiveWords = true;
								return false;
							}
						}
					}
				}
			}
		}
		return true;
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
				toCreate.setPersonalAnswers(personalAnswer);;
				em.persist(personalAnswer);
			}
		}
	}
	
	@Remove
	public void remove() {}
}
