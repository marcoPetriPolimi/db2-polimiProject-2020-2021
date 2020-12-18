package database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

// The persistent class for the offensiveWord database table.
// @author Etion Pinari

@Entity
@Table(name = "OffensiveWord", schema = "db2_project")
public class OffensiveWord {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private int id;
	
	@Column(unique=true)
	private String word;
	
	
	
	/*****	Setters *****/
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setWord(String word) {
		this.word = word;
	}
	
	
	
	/*****	Getters *****/
	
	public int getId() {
		return this.id;
	}
	
	public String getWord() {
		return this.word;
	}
	
}
