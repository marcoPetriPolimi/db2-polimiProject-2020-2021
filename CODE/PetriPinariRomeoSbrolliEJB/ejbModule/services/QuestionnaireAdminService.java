package services;

import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;

import database.*;
import exceptions.QuestionnaireCancellationException;


@Stateless
public class QuestionnaireAdminService {
	
	@PersistenceContext(unitName = "PetriPinariRomeoSbrolliEJB")
	private EntityManager em;

	public QuestionnaireAdminService() {
	}
	
	/**
	 * Gets the users that submitted or canceled a questionnaire
	*@param questionnaireId Id of the questionnaire 
	*@param submitted True to get Users that submitted, False to get Users that canceled the submission
	*@return A map with key the user Id and with value his nickname
	*/
	public Map<Integer,String> getQuestionnaireUserList(int questionnaireId, boolean submitted){
		Map<Integer,String> answers= em
				.createQuery("SELECT u.Id,u.nickname "
							+ "FROM User u, Submission s "
							+ "WHERE u.Id = s.userId AND s.questionnaireId = :qId AND s.submitted = :sub"
							+ "ORDER BY u.Id DESC",Tuple.class)
				.setParameter("qId",questionnaireId)
				.setParameter("sub", submitted? 1 : 0)
				.getResultStream()
				.collect(Collectors.toMap(tuple -> ((Number) tuple.get(0)).intValue(), tuple-> ((String) tuple.get(1))));
		return answers;
	}
	
	public Submission getUserSubmission(int questionnaireId, int userId) {
		Submission submission= (Submission) em
				.createQuery("SELECT s "
							+ "FROM Submission s"
							+ "WHERE s.userId = :uId AND s.questionnaireId = :qId AND s.submitted = 1"
							+ "ORDER BY u.Id DESC",Submission.class)
				.setParameter("qId",questionnaireId)
				.setParameter("uId",userId);
		return submission;
	}
	
	public void deleteQuestionnaire(int questionnaireId) throws QuestionnaireCancellationException {
		Questionnaire q = em.find(Questionnaire.class, questionnaireId);
		if (q.getDate().compareTo(new Date()) > 0) {
			throw new QuestionnaireCancellationException();
		}
		
	}
	
	
}
