package services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import database.Submission;
import database.User;

@Stateless
public class LeaderboardService {
	
	@PersistenceContext(unitName = "PetriPinariRomeoSbrolliEJB")
	private EntityManager em;
	
	public LeaderboardService() {
	}
	
	//TODO Define in a better way (add functional programming to return a map)
	public List<User> getGeneralLeaderboard(){
		List<User> generalLeaderboard = em
				.createQuery("SELECT u FROM User u ORDER BY u.points DESC", User.class).getResultList();

		return generalLeaderboard;
	}
	
	//TODO Define in a better way (add functional programming to return a map)
		public List<Submission> getQuestionnaireLeaderboard(int questionnaireId){
			List<Submission> questionnaireLeaderboard = em
					.createQuery("SELECT s FROM Submission s WHERE s.questionnaireId = :qId "
							   + "ORDER BY s.points DESC", Submission.class).setParameter("qId", questionnaireId).getResultList();

			return questionnaireLeaderboard;
		}

}
