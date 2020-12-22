package database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import utils.Const;

/**
 * This class is the EJB for the Questionnaire database table.
 * @author Giorgio Romeo
 */

@Entity
@Table (name = "Questionnaire", schema = "db2_project")
public class Questionnaire implements Serializable{
	private static final long serialVersionUID = Const.EJBVersion;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	/**
	 * Usage of the annotation Temporal is needed to translate a java.util.Date object type (https://javaee.github.io/javaee-spec/javadocs/javax/persistence/Temporal.html).
	 */
	@Temporal(TemporalType.DATE)
	private Date date;
	
	@Temporal(TemporalType.DATE)
	private Date presDate;
	
	@Column(unique=true)
	private String name;

	@OneToMany (mappedBy = "submissionQuestionnaire", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Submission> submissions;

	@ManyToOne
	@JoinColumn(name= "product")
	private Product product;
	
	@ManyToOne
	@JoinColumn(name = "creatorId")
	private User creator;
	
	@ManyToMany ( cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.EAGER)
	@JoinTable(name="inclusion",
	joinColumns=@JoinColumn(name="questionnaireId"),
	inverseJoinColumns=@JoinColumn(name="questionId"))
	private List<Question> questions;
	
	public Questionnaire() {}
	public Questionnaire(String name) {
		questions = new ArrayList<>();
		this.date = new Date();
		this.name = name;
	}
	
	public void addQuestion(Question question) {
		questions.add(question);
	}
	
	
	/* ******************
	 * 		SETTERS		*
	 ********************/
	public void setId(int id) {
		this.id = id;
	}
	
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
	
	public void setCreator(User creator) {
		this.creator = creator;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	public void setSubmissions(List<Submission> submissions) {
		this.submissions = submissions;
	}
	
	public void setProduct(Product product) {
		this.product = product;
	}
	
	public void setPresDate(Date presDate) {
		this.presDate = presDate;
	}
	
	/* ******************
	 * 		GETTERS		*
	 ********************/
	public int getId() {
		return id;
	}
	
	public User getCreator() {
		return creator;
	}
	
	public Date getDate() {
		return date;
	}
	
	public String getName() {
		return name;
	}

	public List<Submission> getSubmissions() {
		return submissions;
	}
	
	public Product getProduct() {
		return product;
	}
	
	public Date getPresDate() {
		return presDate;
	}

	public List<Question> getQuestions() {
		return questions;
	}
}
