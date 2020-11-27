package ejb.database;

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
	private int id;
	
	@ManyToOne
	private Question questionId;
	
	private String word;

	
	/*****	Setters *****/
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setQuestionId(Question id) {
		this.questionId = id;
	}
	
	public void setWord(String word) {
		this.word = word;
	}

	/*****	Getters *****/
	
	public int getId() {
		return this.id;
	}
	
	public Question getQuestionId(Question id) {
		return this.questionId;
	}
	
	public String getWord(String word) {
		return this.word;
	}
}
