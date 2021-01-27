package services;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import database.*;
import utils.userInfo.UserPersonalInfo;

/**
 * This class is the EJB for the admin services.
 * @author Cristian Sbrolli
 */
@Stateful
public class QuestionnaireAdminService {
	private Integer selectedQuestionnaireId;
	private List<Object[]> userSubmissionList;
	private List<Object[]> userCancelList;


	@PersistenceContext(unitName = "PetriPinariRomeoSbrolliEJB")
	private EntityManager em;

	public QuestionnaireAdminService() {
		selectedQuestionnaireId=null;
		userSubmissionList= new ArrayList<Object[]>();
		userCancelList= new ArrayList<Object[]>();
	}

	/**
	 * Gets the users that submitted or canceled a questionnaire
	*@param questionnaireId Id of the questionnaire
	*@param submitted True to get Users that submitted, False to get Users that canceled the submission
	*@return A map with key the user Id and with value his nickname
	*/
	public void getQuestionnaireUserList(boolean submitted){
		@SuppressWarnings("unchecked")
		List<Object[]> answers= em
				.createQuery("SELECT u.id,u.nickname "
							+ "FROM User u, Submission s "
							+ "WHERE u = s.userSender AND s.submissionQuestionnaire.id = :qId AND s.submitted = :sub "
							+ "ORDER BY u.id DESC")
				.setParameter("qId",selectedQuestionnaireId)
				.setParameter("sub", submitted? 1 : 0)
				.getResultList();
		if (submitted) {
			userSubmissionList = answers;
		}
		else {
			userCancelList = answers;
		}
	}


	/**
	 * Gets the response of a user to the current inspectioning questionnaire
	 * @param userId the id of the user for which the response has to be returned
	 * @return a map containing all the questions and the relative answers of the user
	 */
	public Map<Question, List<String>> getUserSubmission(int userId) {
		Questionnaire quest = em.find(Questionnaire.class, selectedQuestionnaireId.intValue());
		User user = em.find(User.class, userId);
		List<ProductAnswer> productAnswers= em
				.createQuery("SELECT pa "
							+ "FROM ProductAnswer pa,Submission s "
							+ "WHERE s.userSender = :uId AND s.submissionQuestionnaire = :qId AND s.submitted = 1 AND pa.submission.id=s.id "
							+ "ORDER BY pa.question ASC",ProductAnswer.class)
				.setParameter("qId",quest)
				.setParameter("uId",user)
				.getResultList();
		List<Question> questions= quest.getQuestions();

		Map<Question, List<String>> questionAnswers= new HashMap<>();
		for (Question q: questions) {
			List<String> answers= new ArrayList<>();
			for (ProductAnswer pa: productAnswers) {
				if (pa.getQuestionId().getId()==q.getId()) {
					answers.add(pa.getWord());
				}
				questionAnswers.put(q, answers);
			}
		}
		return questionAnswers;

	}

	public Integer getSelectedQuestionnaireId() {
		return selectedQuestionnaireId;
	}

	public void setSelectedQuestionnaireId(Integer selectedQuestionnaireId) {
		this.selectedQuestionnaireId = Integer.valueOf(selectedQuestionnaireId.intValue());
		this.getQuestionnaireUserList(true);
		this.getQuestionnaireUserList(false);
	}

	public List<Object[]> getUserSubmissionList() {
		return userSubmissionList;
	}
	
	/**
	 * refreshs the answers from database
	 */
	public void refreshAnswers() {
		this.setSelectedQuestionnaireId(this.selectedQuestionnaireId);
	}

	public List<Object[]> getUserCancelList() {
		return userCancelList;
	}

	/**
	 * Gets the user's personal answers for the current inspectioning questionnaire, if given
	 * @param userId the id for which the search has to be done
	 * @return a utility class containing all the user's personal info given for the current inspectioning questionnaire
	 */
	public UserPersonalInfo getUserInfo(int userId) {
		try {
		PersonalAnswer pa= em.createQuery("Select pa "
				+ "FROM Submission s,PersonalAnswer pa "
				+ "WHERE pa.submission=s AND s.userSender.id=:uId AND s.submissionQuestionnaire.id=:qId",PersonalAnswer.class)
				.setParameter("uId", userId)
				.setParameter("qId", selectedQuestionnaireId)
				.getSingleResult();
		return new UserPersonalInfo(pa.getAge(),pa.getExpertise(),pa.getSex());
		}
		catch(NoResultException e){
			return new UserPersonalInfo();
		}
	}
	
    @Remove
	public void remove() {}



}
