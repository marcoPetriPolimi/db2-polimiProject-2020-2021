package ejb.database;

import java.io.Serializable;

import javax.persistence.*;

/**
 * This class is the EJB for the Creation database table.
 * @author Giorgio Romeo
 */

@Entity
@Table(name = "Creation", schema = "db2_project")
public class Creation implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	private int creatorId;
	@ManyToOne
	private int questionnaireId;
	
	public Creation() {}
	public Creation(int id, int creatorId, int questionnaireId) {
		this.id = id;
		this.creatorId = creatorId;
		this.questionnaireId = questionnaireId;
	}
	
	/* ******************
	 * 		SETTERS		*
	 ********************/
	public void setId(int id) {
		this.id = id;
	}
	
	public void setCreatorId(int creatorId) {
		this.creatorId = creatorId;
	}
	
	public void setQuestionnaireId(int questionnaireId) {
		this.questionnaireId = questionnaireId;
	}
	
	
	/* ******************
	 * 		GETTERS		*
	 ********************/
	public int getId() {
		return id;
	}
	
	public int getCreatorId() {
		return creatorId;
	}
	
	public int getQuestionnaireId() {
		return questionnaireId;
	}
	

}
