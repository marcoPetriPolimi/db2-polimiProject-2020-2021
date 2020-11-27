package ejb.database;

import javax.persistence.*;

// The persistent class for the offensiveWord database table.
// @author Etion Pinari

@Entity
@Table(name = "OffensiveWord", schema = "db2_project")
public class OffensiveWord {
	
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
