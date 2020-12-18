package services;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import database.*;



@Stateful
public class QuestionnaireAdminService {
	private Integer selectedQuestionnaireId;
	private List<Object[]> userSubmissionMap;
	private List<Object[]> userCancelMap;


	@PersistenceContext(unitName = "PetriPinariRomeoSbrolliEJB")
	private EntityManager em;

	public QuestionnaireAdminService() {
		selectedQuestionnaireId=null;
		userSubmissionMap= new ArrayList<Object[]>();
		userCancelMap= new ArrayList<Object[]>();
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
			userSubmissionMap = answers;
		}
		else {
			userCancelMap = answers;
		}
	}



	public Map<Question, List<String>> getUserSubmission(int userId) {
		List<ProductAnswer> productAnswers= em
				.createQuery("SELECT pa "
							+ "FROM ProductAnswer pa,Submission s "
							+ "WHERE s.userSender = :uId AND s.submissionQuestionnaire = :qId AND s.submitted = 1 AND pa.submission=s.id"
							+ "ORDER BY pa.questionId ASC",ProductAnswer.class)
				.setParameter("qId",selectedQuestionnaireId)
				.setParameter("uId",userId)
				.getResultList();
		List<Question> questions= em.createQuery("Select q "
				+ "FROM Question q,Inclusion i "
				+ "WHERE i.inclusionQuestion = q.id AND i.inclusionQuestionnaire =:qID "
				+ "ORDER BY q.id",Question.class)
				.setParameter("qId", selectedQuestionnaireId)
				.getResultList();

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

	public List<Object[]> getUserSubmissionMap() {
		return userSubmissionMap;
	}

	public List<Object[]> getUserCancelMap() {
		return userCancelMap;
	}

    @Remove
	public void remove() {}

}
