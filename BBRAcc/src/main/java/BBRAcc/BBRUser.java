package BBRAcc;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import BBR.BBRDataElement;
import BBR.BBRDataSet;

@SuppressWarnings("unused")
public class BBRUser extends BBRDataElement {
	private Long id;
	private String email;
	private String firstName;
	private String lastName;
	private String encodedPassword;
	private boolean approved;
	private BBRShop shop;
	private BBRPoS pos;

	public class BBRUserRole {
		public static final int ROLE_BBR_OWNER = 256;
		public static final int ROLE_SHOP_OWNER = 16;
		public static final int ROLE_SHOP_ADMIN = 8;
		public static final int ROLE_SHOP_BRANCH_ADMIN = 4;
		public static final int ROLE_SHOP_SPECIALIST = 2;
		public static final int ROLE_VISITOR = 1;
	}

	private int role = 0;

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

	public void setRole(int role) {
		this.role = role;
	}
	
	public int getRole() {
		return role;
	}

	public void setShop(BBRShop shop) {
		this.shop = shop;
	}
	
	public BBRShop getShop() {
		return shop;
	}

	public void setPos(BBRPoS pos) {
		this.pos = pos;
	}
	
	public BBRPoS getPos() {
		return pos;
	}
	public boolean comparePasswordTo(String comparingEncodedPassword) {
		return encodedPassword.equals(comparingEncodedPassword);
	}

	public BBRUser() {
	}
	
}
