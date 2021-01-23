package services;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import database.Questionnaire;
import exceptions.QuestionnaireException;

/*
 * @author Giorgio Romeo
 */
@Stateless
public class LeaderboardService {
	
	@PersistenceContext(unitName = "PetriPinariRomeoSbrolliEJB")
	private EntityManager em;
	
	public LeaderboardService() {
	}
	
	/* Get users's nickname and its total points
	 * @return  A map with key the user nickname and with value his total points
	 */
	public Map<String, Integer> getGeneralLeaderboard(){
		List<Object[]> general_users_points = em
				.createNamedQuery("User.getAllTimeLeaderBoard", Object[].class).getResultList();
		Map<String, Integer> generalLeaderboard = new LinkedHashMap<String, Integer>();
		for (int i = 0; i < general_users_points.size(); i++)
			generalLeaderboard.put((String)general_users_points.get(i)[0], (Integer)general_users_points.get(i)[1]);

		return generalLeaderboard;
	}
	
	
	/*	Get users's nickname and their points related to the questionnaire: questionnaireId
	 * @param questionnaireId The id of the questionnaire
	 * @return A map with key the user's nickname and with value his points related to the questionnaire
	 * @throw QuestionnaireException
	 */
	public Map<String, Integer> getQuestionnaireLeaderboard(int questionnaireId) throws QuestionnaireException{
		
		Questionnaire questionnaire = em.find(Questionnaire.class, questionnaireId);
		if(questionnaire == null) throw new QuestionnaireException("Could not find questionnarie");
		
		List<Object[]> questionnaire_users_points = em
				.createQuery("SELECT u.nickname, s.points "
						   + "FROM Submission s, User u "
						   + "WHERE s.submissionQuestionnaire.id = :qId AND u = s.userSender AND u.role=1 "
						   + "ORDER BY s.points DESC", Object[].class).setParameter("qId", questionnaireId).getResultList();
		Map<String, Integer> questionnaireLeaderboard = new LinkedHashMap<String, Integer>();
		for (int i = 0; i < questionnaire_users_points.size(); i++)
			questionnaireLeaderboard.put((String)questionnaire_users_points.get(i)[0], (Integer)questionnaire_users_points.get(i)[1]);

		return questionnaireLeaderboard;
	}

}
