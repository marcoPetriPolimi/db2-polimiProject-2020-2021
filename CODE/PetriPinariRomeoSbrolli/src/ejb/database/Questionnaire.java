package ejb.database;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.*;

/**
 * This class is the EJB for the Questionnaire database table.
 * @author Giorgio Romeo
 */

@Entity
@Table (name = "Questionnaire", schema = "db2_project")
public class Questionnaire implements Serializable{
	
private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	/**
	 * Usage of the annotation Temporal is needed to translate a java.util.Date object type (https://javaee.github.io/javaee-spec/javadocs/javax/persistence/Temporal.html).
	 */
	@Temporal(TemporalType.DATE)
	private Date date;
	@Column(unique=true)
	private String name;
	
	@OneToMany (mappedBy = "questionnaireId")
	private Collection<ProductAnswer> productAnswers;
	@OneToMany (mappedBy = "questionnaireId")
	private Collection<PersonalAnswer> personalAnswers;
	
	public Questionnaire() {}
	public Questionnaire(int id, Date date, String name) {
		this.id = id;
		this.date = date;
		this.name = name;
	}
	
	/* ******************
	 * 		SETTERS		*
	 ********************/
	public void setId(int id) {
		this.id = id;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	/* ******************
	 * 		GETTERS		*
	 ********************/
	public int getId() {
		return id;
	}
	
	public Date getDate() {
		return date;
	}
	
	public String getName() {
		return name;
	}
	

}