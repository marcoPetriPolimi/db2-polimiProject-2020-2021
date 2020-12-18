package utils.userInfo;

public class UserPersonalInfo {
	private Integer age;
	private Integer expertise;
	private Character sex;


	public UserPersonalInfo(Integer age, Integer expertise, Character sex) {
		super();
		this.age = age;
		this.expertise = expertise;
		this.sex = sex;
	}
	
	public UserPersonalInfo() {
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public void setExpertise(Integer expertise) {
		this.expertise = expertise;
	}

	public void setSex(Character sex) {
		this.sex = sex;
	}
	
	public Integer getExpertise() {
		return expertise;
	}
	
	public Character getSex() {
		return sex;
	}
	
	public Integer getAge() {
		return age;
	}
}
