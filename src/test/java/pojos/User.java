package pojos;

public class User {
	private String user_first_name;
	private String user_last_name;
	private String user_contact_number;
	private String user_email_id;
	private UserAddress userAddress;
	
	public User() {
		
	}
	public User(String firstName, String lastName, String contactNumber, String emailId,UserAddress userAddress) {
		this.user_first_name = firstName;
		this.user_last_name = lastName;
		this.user_contact_number = contactNumber;
		this.user_email_id = emailId;
		this.userAddress = userAddress;
	}
	
	public String getUser_first_name() {
		return user_first_name;
	}
	public void setUser_First_Name(String firstName) {
		this.user_first_name = firstName;
	}
	public String getUser_last_name() {
		return user_last_name;
	}
	public void setUser_last_name(String lastName) {
		this.user_last_name = lastName;
	}
	public String getUser_contact_number() {
		return user_contact_number;
	}
	public void setUser_contact_number(String contactNumber) {
		this.user_contact_number = contactNumber;
	}
	public String getUser_email_id() {
		return user_email_id;
	}
	public void setUser_email_id(String emailId) {
		this.user_email_id = emailId;
	}
	public UserAddress getUserAddress() {
		return userAddress;
	}
	public void setUserAddress(UserAddress userAddress) {
		this.userAddress = userAddress;
	}
	
	
	

}
