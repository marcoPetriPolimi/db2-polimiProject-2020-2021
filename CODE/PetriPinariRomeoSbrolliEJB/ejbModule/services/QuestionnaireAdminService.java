package services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;

import org.eclipse.persistence.config.QueryHints;
import org.eclipse.persistence.config.ResultType;

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
		@SuppressWarnings("unchecked")
		List<Object[]> answers= em
				.createQuery("SELECT u.id,u.nickname "
							+ "FROM User u, Submission s "
							+ "WHERE u = s.userSender AND s.submissionQuestionnaire.id = :qId AND s.submitted = :sub "
							+ "ORDER BY u.id DESC")
				.setParameter("qId",questionnaireId)
				.setParameter("sub", submitted? 1 : 0)
				.getResultList();
		return answers.stream().collect(Collectors.toMap(a -> (Integer) a[0], a -> (String) a[1]));
	}
	
	
	
	public Submission getUserSubmission(int questionnaireId, int userId) {
		Submission submission= (Submission) em
				.createQuery("SELECT s "
							+ "FROM Submission s "
							+ "WHERE s.userSender = :uId AND s.submissionQuestionnaire = :qId AND s.submitted = 1 "
							+ "ORDER BY u.id DESC",Submission.class)
				.setParameter("qId",questionnaireId)
				.setParameter("uId",userId);
		return submission;
	}
	
	public void deleteQuestionnaire(int questionnaireId) throws QuestionnaireCancellationException {
		Questionnaire q = em.find(Questionnaire.class, questionnaireId);
		if (q.getDate().compareTo(new Date()) > 0) {
			throw new QuestionnaireCancellationException();
		}
		em.remove(q);		
	}
	
	
}
