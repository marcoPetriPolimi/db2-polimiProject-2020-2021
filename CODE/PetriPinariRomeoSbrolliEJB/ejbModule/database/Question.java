package database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import utils.Const;

//The persistent class for the question database table.
//@author Etion Pinari

@Entity
@Table(name = "question", schema = "db2_project")
public class Question implements Serializable {
	private static final long serialVersionUID = Const.EJBVersion;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String question;
	private short type;
	
	@OneToMany (mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
	private List<PossibleAnswer> questionAnswers;
	
	public Question() {}
	
	public Question(String question, short type) {
		super();
		questionAnswers= new ArrayList<>();
		this.question = question;
		this.type = type;
	}
	
	public void addAnswer(PossibleAnswer possAnswer) {
		getQuestionAnswers().add(possAnswer);
		possAnswer.setQuestion(this);
	}

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
	
	public void setQuestionAnswers(List<PossibleAnswer> questionAnswers) {
		this.questionAnswers = questionAnswers;
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

	public List<PossibleAnswer> getQuestionAnswers() {
		return questionAnswers;
	}

}
