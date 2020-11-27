package ejb.database;

import java.io.Serializable;

import javax.persistence.*;

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
	private int questionnaireId;
	@ManyToOne
	private int questionId;
	
	public Inclusion() {}
	public Inclusion(int id, int questionnaireId, int questionId) {
		this.id = id;
		this.questionnaireId = questionnaireId;
		this.questionId = questionId;
	}
	
	/* ******************
	 * 		SETTERS		*
	 ********************/
	public void setId(int id) {
		this.id = id;
	}
	
	public void setQuestionnaireId(int questionnaireId) {
		this.questionnaireId = questionnaireId;
	}
	
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	
	
	/* ******************
	 * 		GETTERS		*
	 ********************/
	public int getId() {
		return id;
	}
	
	public int getQuestionnaireId() {
		return questionnaireId;
	}
	
	public int getQuestionId() {
		return questionId;
	}
	

}
