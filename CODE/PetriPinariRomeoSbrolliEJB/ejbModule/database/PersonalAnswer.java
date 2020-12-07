package database;

import java.io.Serializable;

import javax.persistence.*;

/**
 * The persistent class for the personalanswer database table.
 * @author Cristian Sbrolli
 */


@Entity
@Table(name = "personalanswer", schema = "db2_project")
public class PersonalAnswer implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer age;
	private Character sex;
	private Integer expertise;

	@ManyToOne
	@JoinColumn(name = "submissionId")
	private Submission submission;
	
	public PersonalAnswer(User user,Questionnaire questionnaire,Integer age,Character sex,Integer expertise) {
		this.age=age;
		this.sex=sex;
		this.expertise=expertise;
	}
	
	public PersonalAnswer() {}
	
	
	/* ******************
	 * 		SETTERS		*
	 ********************/
	public void setAge(Integer age) {
		this.age = age;
	}

	public void setSex(Character sex) {
		this.sex = sex;
	}
	
	public void setExpertise(Integer expertise) {
		this.expertise = expertise;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setSubmission(Submission submission) {
		this.submission = submission;
	}

	
	/* ******************
	 * 		GETTERS		*
	 ********************/
	
	public Integer getAge() {
		return age;
	}


	public Character getSex() {
		return sex;
	}


	public Integer getExpertise() {
		return expertise;
	}


	
	public int getId() {
		return id;
	}
	
	public Submission getSubmission() {
		return submission;
	}

	
}
