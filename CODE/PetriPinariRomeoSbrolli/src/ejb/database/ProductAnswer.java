package ejb.database;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the productanswer database table.
 * @author Cristian Sbrolli
 */
@Entity
public class ProductAnswer implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	private Questionnaire questionnaireId;
	
	@ManyToOne
	private Question questionId;
	
	@ManyToOne
	private User userId;
	
	private String word;
	
	public ProductAnswer() {}
	
	public ProductAnswer(User user,Question question,Questionnaire questionnaire,String word) {
		this.userId = user;
		this.questionId = question;
		this.questionnaireId = questionnaire;
		this.word = word;
	}
	
	/* ******************
	 * 		SETTERS		*
	 ********************/
	
	public void setQuestionnaireId(Questionnaire questionnaireId) {
		this.questionnaireId = questionnaireId;
	}
	
	public void setQuestionId(Question questionId) {
		this.questionId = questionId;
	}
	
	public void setUserId(User userId) {
		this.userId = userId;
	}
	
	public void setWord(String word) {
		this.word = word;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	/* ******************
	 * 		GETTERS		*
	 ********************/
	
	public Questionnaire getQuestionnaireId() {
		return questionnaireId;
	}
	
	public Question getQuestionId() {
		return questionId;
	}

	public User getUserId() {
		return userId;
	}

	public String getWord() {
		return word;
	}

	public int getId() {
		return id;
	}


}