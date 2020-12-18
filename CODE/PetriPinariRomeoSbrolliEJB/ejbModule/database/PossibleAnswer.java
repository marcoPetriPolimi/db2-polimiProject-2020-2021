package database;

import java.io.Serializable;

/**
 * The persistent class for the possibleanswer database table.
 * 
 */
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

//The persistent class for the possibleAnswer database table.
//author Etion

@Entity
@Table(name = "possibleAnswer", schema = "db2_project")
public class PossibleAnswer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "questionId")
	private Question question;
	
	private String answerText;

	public PossibleAnswer() {};
	
	public PossibleAnswer(String answerText) {

		this.answerText = answerText;
	}
	
	/*****	Setters *****/
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setQuestion(Question q) {
		this.question = q;
	}
	
	public void setanswerText(String answerText) {
		this.answerText = answerText;
	}

	/*****	Getters *****/
	
	public int getId() {
		return this.id;
	}
	
	public Question getQuestion() {
		return this.question;
	}
	
	public String getanswerText() {
		return this.answerText;
	}
}
