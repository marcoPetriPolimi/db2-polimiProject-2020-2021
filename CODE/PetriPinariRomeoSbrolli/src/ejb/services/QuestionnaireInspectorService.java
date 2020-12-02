package ejb.services;

import java.util.List;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import ejb.database.*;


@Stateless
public class QuestionnaireInspectorService {
	@PersistenceContext(unitName = "QuestionnaireOfTheDayEJB")
	private EntityManager em;

	public QuestionnaireInspectorService() {
	}
	
	public List<Submission> getQuestionnaireSubmissionList(int questionnaireId){
		Questionnaire questionnaire = em.find(Questionnaire.class, questionnaireId);
		List<Submission> submissions = questionnaire.getSubmissions();
		return submissions;
	}
	
	public List<ProductAnswer> getQuestionnaireProductAnswers(int questionnaireId){
		Questionnaire questionnaire = em.find(Questionnaire.class, questionnaireId);
	
		return null;
	}
	
	
}
