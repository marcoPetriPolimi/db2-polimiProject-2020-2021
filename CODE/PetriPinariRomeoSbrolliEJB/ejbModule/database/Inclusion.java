package database;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * This class is the EJB for the Inclusion database table.
 * @author Giorgio Romeo
 */

@Entity
@Table (name = "Inclusion", schema = "db2_project")
public class Inclusion implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	@JoinColumn(name = "questionnaireId")
	private Questionnaire inclusionQuestionnaire;
	@ManyToOne
	@JoinColumn(name = "questionId")
	private Question inclusionQuestion;
	
	/* ******************
	 * 		SETTERS		*
	 ********************/
	public void setId(int id) {
		this.id = id;
	}
	
	public void setQuestionnaire(Questionnaire inclusionQuestionnaire) {
		this.inclusionQuestionnaire = inclusionQuestionnaire;
	}
	
	public void setQuestion(Question inclusionQuestion) {
		this.inclusionQuestion = inclusionQuestion;
	}
	
	
	/* ******************
	 * 		GETTERS		*
	 ********************/
	public int getId() {
		return id;
	}
	
	public Questionnaire getQuestionnaire() {
		return inclusionQuestionnaire;
	}
	
	public Question getQuestion() {
		return inclusionQuestion;
	}
	

}
