package application;

public class Person {
	
	private String name = "";
	private String email = "";
	private String password = "";
	private int age = 0;
	
	Person() {
		
	}
	
	Person(String name,int age, String email, String password){
		this.name = name;
		this.age = age;
		this.email = email;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
	
	
}



