package BBR;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SuppressWarnings("unused")
public class BBRUser extends BBRDataElement {
	private Long id;
	private String email;
	private String firstName;
	private String lastName;
	private String encodedPassword;
	private boolean approved;
	private BBRDataSet<BBRShop> shops;
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getLastName() {
		return lastName;
	}

	public void setEncodedPassword(String encodedPassword) {
		this.encodedPassword = encodedPassword;
	}

	public String getEncodedPassword() {
		return encodedPassword;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	
	public boolean getApproved() {
		return approved;
	}
	public boolean comparePasswordTo(String comparingEncodedPassword) {
		return encodedPassword.equals(comparingEncodedPassword);
	}

	public BBRUser() {
	}
}
