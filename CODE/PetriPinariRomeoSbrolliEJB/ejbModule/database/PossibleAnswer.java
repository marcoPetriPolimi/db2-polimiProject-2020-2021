package database;

import java.io.Serializable;


/**
 * The persistent class for the possibleanswer database table.
 * 
 */

import javax.persistence.*;

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
	
	public PossibleAnswer() {};
	
	public PossibleAnswer(String word) {
		super();
		this.word = word;
	}

	@ManyToOne
	@JoinColumn(name = "questionId")
	private Question question;
	
	private String word;

	
	/*****	Setters *****/
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setQuestion(Question q) {
		this.question = q;
	}
	
	public void setWord(String word) {
		this.word = word;
	}

	/*****	Getters *****/
	
	public int getId() {
		return this.id;
	}
	
	public Question getQuestion() {
		return this.question;
	}
	
	public String getWord(String word) {
		return this.word;
	}
}
