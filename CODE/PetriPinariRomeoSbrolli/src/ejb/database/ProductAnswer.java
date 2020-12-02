package ejb.database;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the ProductAnswer database table.
 * @author Cristian Sbrolli
 */
@Entity
public class ProductAnswer implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	private Question questionId;
	
	@ManyToOne
	private Submission submissionId;

	private String word;
	
	public ProductAnswer() {}
	
	public ProductAnswer(User user,Question question,Questionnaire questionnaire,String word) {
		this.questionId = question;
		this.word = word;
	}
	
	/* ******************
	 * 		SETTERS		*
	 ********************/
	
	public void setSubmissionId(Submission submissionId) {
		this.submissionId = submissionId;
	}
	
	public void setQuestionId(Question questionId) {
		this.questionId = questionId;
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
	
	public Question getQuestionId() {
		return questionId;
	}


	public String getWord() {
		return word;
	}

	public int getId() {
		return id;
	}
	
	public Submission getSubmissionId() {
		return submissionId;
	}


}