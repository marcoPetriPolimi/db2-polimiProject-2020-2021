package services;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import database.PossibleAnswer;
import database.Product;
import database.Question;
import database.Questionnaire;
import database.User;
import utils.forms.FormQuestion;

/**
 * Session Bean implementation class QuestionnaireCreationService
 */
@Stateful
@LocalBean
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
    
    public void addQuestion(FormQuestion question) throws IllegalArgumentException {
    	if (question.getQuestionText() == null) {
    		throw new IllegalArgumentException();
    		}
    	newQuestions.add(question);
    }
    
    public void removeQuestion(int questionId) {
    	FormQuestion toRemove=null;
    	for (FormQuestion fq: newQuestions) {
    		if (fq.getQuestion()==questionId) toRemove=fq;
    	}
    	if (toRemove!=null) {
    		newQuestions.remove(toRemove);
    	}
    }
    
    
    public void modifyQuestion(FormQuestion question) {
    	if (question.getQuestionText() == null) {
    		throw new IllegalArgumentException();
    		}
    	int toModify=-1;
    	for (FormQuestion fq: newQuestions) if (fq.getQuestion()==question.getQuestion()) toModify=newQuestions.indexOf(fq);
    	if (toModify == -1) throw new IllegalArgumentException();
    	newQuestions.set(toModify, question);
    }
    
    public void addStoredQuestion(int questionId) {
    	storedQuestions.add(em.find(Question.class, questionId));
    }
    
    public void removeStoredQuestion(int questionId){
    	Question toRemove=null;
    	for (Question q: storedQuestions) {
    		if (q.getId()==questionId) toRemove=q;
    	}
    	if (toRemove!=null) {
    		storedQuestions.remove(toRemove);
    	}
    }
    
    public void createQuestionnaire(int userId, String name,Date presDate, int productId) {
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
    	em.merge(creator);
    }
    
    public List<Question> getAllStoredQuestions(){
		List<Question> questions=  em
				.createQuery("SELECT q "
							+ "FROM Question q "
							+ "ORDER BY q.id ASC",Question.class)
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

	public List<Product> getAllProducts() {
		return 	em
				.createQuery("SELECT p "
							+ "FROM Product p "
							+ "ORDER BY p.id ASC",Product.class)
				.getResultList();
	}
	
	public boolean addProduct(String name, byte[] imgByteArray) {
		Product prod= em
				.createQuery("SELECT p "
						+ "FROM Product p "
						+ "WHERE p.name=:pName",Product.class)
				.setParameter("pName", name)
				.getSingleResult();
		if (prod.getName().equals(name)) return false;
		Product newProd= new Product(imgByteArray, name);
		em.persist(newProd);
		return true;

	}
	
    @Remove
	public void remove() {}

}
