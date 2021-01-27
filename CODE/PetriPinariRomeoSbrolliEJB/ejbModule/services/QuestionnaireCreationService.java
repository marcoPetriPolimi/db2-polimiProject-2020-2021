package services;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import database.PossibleAnswer;
import database.Product;
import database.Question;
import database.Questionnaire;
import database.User;
import utils.forms.FormQuestion;

/**
 * Session Bean implementation class QuestionnaireCreationService
 * @author Cristian Sbrolli
 */
@Stateful
public class QuestionnaireCreationService {

	@PersistenceContext(unitName = "PetriPinariRomeoSbrolliEJB")
	private EntityManager em;
	private Questionnaire questionnaire;
	private List<FormQuestion> newQuestions; //questions defined by the creator of this questionnaire
	private List<Question> storedQuestions; //old questions already in the database from previous questionnaires
	
    /**
     * Default constructor. 
     */
    public QuestionnaireCreationService() {
        newQuestions= new ArrayList<>();
        storedQuestions= new ArrayList<>();
    }
    
    /**
     * Add to the currently creating questionnaire a question not already present in database
     * @param question utility class containing all the new question's info
     * @throws IllegalArgumentException
     */
    public void addQuestion(FormQuestion question) throws IllegalArgumentException {
    	if (question.getQuestionText() == null) {
    		throw new IllegalArgumentException();
    		}
    	newQuestions.add(question);
    }
    
    /**
     * Method for removing a qustion added to the questionnaire being created
     * @param questionId id of the question to remove
     */
    public void removeQuestion(int questionId) {
    	FormQuestion toRemove=null;
    	for (FormQuestion fq: newQuestions) {
    		if (fq.getQuestion()==questionId) toRemove=fq;
    	}
    	if (toRemove!=null) {
    		newQuestions.remove(toRemove);
    	}
    }
    
    /**
     * Add to the currently creating questionnaire a question already present in database
     * @param questionId id of the stored question to add
     */
    public void addStoredQuestion(int questionId) {
    	storedQuestions.add(em.find(Question.class, questionId));
    }
    
    /**
     * Removes a selected question for the currently creating questionnaire among the ones that are already present in database
     * @param questionId id of the stored question to remove
     */
    public void removeStoredQuestion(int questionId){
    	Question toRemove=null;
    	for (Question q: storedQuestions) {
    		if (q.getId()==questionId) toRemove=q;
    	}
    	if (toRemove!=null) {
    		storedQuestions.remove(toRemove);
    	}
    }
    
    /**
     * Create and persist the newly created questionnaire
     * @param userId user creating the questionnaire
     * @param name name of the questionnaire
     * @param presDate date in which the questionnaire has to be presented to users
     * @param productId product related to questionnaire
     */
    public User createQuestionnaire(int userId, String name,Date presDate, int productId) {
    	questionnaire= new Questionnaire(name);
    	Product product= em.find(Product.class,	productId);
    	questionnaire.setProduct(product);
    	User creator= em.find(User.class, userId);
    	creator.addQuestionnaire(questionnaire);
    	questionnaire.setPresDate(presDate);
    	for (FormQuestion fq: newQuestions) {
    		Question newQuestion = new Question(fq.getQuestionText(),fq.getType());
    		questionnaire.addQuestion(newQuestion);;
    		for (String answer: fq.getPossibleAnswers()) {
    			PossibleAnswer possAnswer= new PossibleAnswer(answer);
    			newQuestion.addAnswer(possAnswer);  		
    		}
    	}
    	for (Question question: storedQuestions) {
    		questionnaire.addQuestion(question);
    	}    	
    	return em.merge(creator);
    }
    
    /**
     * Get all the stored questions from other questionnaires
     * @return the list of all the already present questions in the database
     */
    public List<Question> getAllStoredQuestions(){
		List<Question> questions=  em
				.createNamedQuery("Question.getAll",Question.class)
				.getResultList();
		List<Integer> alreadyStored = new ArrayList<>();
		for (Question q: storedQuestions) alreadyStored.add(q.getId());
		questions.removeIf(q -> alreadyStored.contains(q.getId()));
		
		return questions;
		}
    
	public int getNextQuestionId() {
		return newQuestions.size();
	}

	public List<FormQuestion> getFormQuestions() {
		return newQuestions;
	}

	public List<Question> getStoredQuestions() {
		return storedQuestions;
	}

	/*
	 * Get all products present in database
	 */
	public List<Product> getAllProducts() {
		return 	em
				.createNamedQuery("Product.getAll",Product.class)
				.getResultList();
	}
	
	/**
	 * Add a new product
	 * @param name name of new product
	 * @param imgByteArray image of new product
	 * @return true if the product was not present and has been added, false if it was already present
	 */
	public boolean addProduct(String name, byte[] imgByteArray) {

		try {
		em.createNamedQuery("Product.getById",Product.class)
				.setParameter("pName", name)
				.getSingleResult();
		}
		catch (NoResultException e) {
			Product newProd= new Product(imgByteArray, name);
			em.persist(newProd);
			return true;
		}
		return false;

	}
	
    @Remove
	public void remove() {}

}
