package database;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: userLog
 *
 */
@Entity
public class UserLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "userId")
	private User userId;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date datetime;
	
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public User getUserId() {
		return userId;
	}


	public void setUserId(User userId) {
		this.userId = userId;
	}


	public Date getDate() {
		return datetime;
	}


	public void setDate(Date date) {
		this.datetime = date;
	}


	public UserLog() {
		super();
	}


	public UserLog(User id2, Date date) {
		this.userId= id2;
		this.datetime=date;
	}
   
}
