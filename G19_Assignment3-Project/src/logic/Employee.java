package logic;

public class Employee {
	private String FirstName;
	private String LastName;
	//private String EmployeeNum;
	private String Email;
	private String Role;
	private String organizationalAffiliation;
	private String Password;
	private String Username;
	
	
	
	public Employee(String FirstName,String LastName,String Email,String Role,String organizationalAffiliation,String Password,String Username) {
		this.FirstName=FirstName;
		this.LastName=LastName;
		this.Email=Email;
		this.Role=Role;
		this.organizationalAffiliation=organizationalAffiliation;
		this.Password=Password;
		this.Username=Username;
	}
	public String getFirstName() {
		return FirstName;
	}
	public void setFirstName(String firstName) {
		FirstName = firstName;
	}
	public String getLastName() {
		return LastName;
	}
	public void setLastName(String lastName) {
		LastName = lastName;
	}
	
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getOrganizationalAffiliation() {
		return organizationalAffiliation;
	}
	public void setOrganizationalAffiliation(String organizationalAffiliation) {
		this.organizationalAffiliation = organizationalAffiliation;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getUsername() {
		return Username;
	}
	public void setUsername(String username) {
		Username = username;
	}
	public String getRole() {
		return Role;
	}
	public void setRole(String role) {
		Role = role;
	}
	
	
	

}
