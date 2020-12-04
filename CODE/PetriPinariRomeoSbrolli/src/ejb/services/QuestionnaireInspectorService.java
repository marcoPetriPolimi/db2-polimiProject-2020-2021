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
public class QuestionnaireInspectorService {
	@PersistenceContext(unitName = "PetriPinariRomeoSbrolliEJB")
	private EntityManager em;

	public QuestionnaireInspectorService() {
	}
	
	public List<Submission> getQuestionnaireSubmissionList(int questionnaireId){
		Questionnaire questionnaire = em.find(Questionnaire.class, questionnaireId);
		List<Submission> submissions = questionnaire.getSubmissions();
		return submissions;
	}
	
	public Map<Integer,String> getQuestionnaireProductAnswers(int questionnaireId){
		Map<Integer,String> answers= em
				.createQuery("SELECT u.Id,u.nickname from User u, Submission s "
						   + "WHERE u.Id = s.userId AND s.questionnaireId = :qId"
						   + "ORDER BY u.Id DESC",Tuple.class)
				.setParameter("qId",questionnaireId).getResultStream().collect(Collectors.toMap(tuple -> ((Number) tuple.get(0)).intValue(), tuple-> ((String) tuple.get(1))));
		return answers;
	}
}
