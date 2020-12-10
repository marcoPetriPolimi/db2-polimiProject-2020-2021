package database;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

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
	
	public Question() {}
	
	public Question(String question, short type) {
		super();
		this.question = question;
		this.type = type;
	}

	private String question;
	
	private short type;
	
	@OneToMany (mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PossibleAnswer> questionAnswers;
	
	@OneToMany (mappedBy = "inclusionQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Inclusion> questionInclusions;
	
	public void addInclusion(Inclusion inclusion) {
		getQuestionInclusions().add(inclusion);
		inclusion.setQuestion(this);
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
	
	public void setQuestionInclusions(List<Inclusion> questionInclusions) {
		this.questionInclusions = questionInclusions;
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
	
	public List<Inclusion> getQuestionInclusions() {
		return questionInclusions;
	}

	public List<PossibleAnswer> getQuestionAnswers() {
		return questionAnswers;
	}

}
