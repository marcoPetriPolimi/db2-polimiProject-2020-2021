package services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import database.Submission;

/**
 * 
 * @author Marco Petri
 *
 */
@Stateless
public class SubmissionService {
	@PersistenceContext(unitName = "PetriPinariRomeoSbrolliEJB")
	private EntityManager em;
	
	public Submission findSubmission(int userId, int questionnaireId) {
		List<Submission> submissionList = em.createNamedQuery("Submission.findByNameAndQuestionnaire", Submission.class).setParameter(1, userId).setParameter(2, questionnaireId).getResultList();
		
		if (submissionList.isEmpty()) {
			return null;
		} else {
			return submissionList.get(0);
		}
	}
}
