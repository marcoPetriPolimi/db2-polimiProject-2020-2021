package database;

/**
 * The persistent class for the productanswer database table.
 * 
 */

import java.io.Serializable;

import javax.persistence.*;

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
	
	public Questionnaire getQuestionnaireId() {
		return questionnaireId;
	}
	public void setQuestionnaireId(Questionnaire questionnaireId) {
		this.questionnaireId = questionnaireId;
	}
	public Question getQuestionId() {
		return questionId;
	}
	public void setQuestionId(Question questionId) {
		this.questionId = questionId;
	}
	public User getUserId() {
		return userId;
	}
	public void setUserId(User userId) {
		this.userId = userId;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
