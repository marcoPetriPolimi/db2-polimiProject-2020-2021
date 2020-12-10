package services;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import database.Inclusion;
import database.PersonalAnswer;
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
    
    public void removeQuestion(FormQuestion question) {
    	newQuestions.remove(question);
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
    	storedQuestions.remove(em.find(Question.class, questionId));
    }
    
    public void createQuestionnaire(int userId, String name, int productId) {
    	questionnaire= new Questionnaire(name);
    	Product product= em.find(Product.class,	productId);
    	product.addQuestionaire(questionnaire);
    	User creator= em.find(User.class, userId);
    	creator.addQuestionnaire(questionnaire);
    	for (FormQuestion fq: newQuestions) {
    		Question newQuestion = new Question(fq.getQuestionText(),fq.getType());
    		Inclusion questInclusion = new Inclusion();
    		questionnaire.addInclusion(questInclusion);
			newQuestion.addInclusion(questInclusion);
    		for (String answer: fq.getPossibleAnswers()) {
    			PossibleAnswer possAnswer= new PossibleAnswer(answer);
    			newQuestion.addAnswer(possAnswer);  		
    		}
    		em.persist(newQuestion);
    	}
    	for (Question question: storedQuestions) {
    		Inclusion questInclusion= new Inclusion();
    		questionnaire.addInclusion(questInclusion);
    		question.addInclusion(questInclusion);
    		em.persist(question);
    	}    	
    	em.persist(creator);
    	em.persist(product);
    	
    }
    
    @Remove
	public void remove() {}

}
