package ejb.database;

import javax.persistence.*;

//The persistent class for the question database table.
//@author Etion Pinari

@Entity
@Table(name = "question", schema = "db2_project")
public class Question {

	@Id
	private int id;
	
	private String question;
	
	private short type;
	
	/*****	Setters *****/
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setQuestion(String question) {
		this.question = question;
	}
	
	public void setType(short type) {
		this.type = type;
	}
	
	
	/*****	Getters *****/
	
	public int getId() {
		return this.id ;
	}
	
	public String getQuestion() {
		return this.question;
	}
	
	public short getType() {
		return this.type;
	}
}
