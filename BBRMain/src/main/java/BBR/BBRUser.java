package BBR;

public class BBRUser {
	private Long id;
	private String email;
	private String firstName;
	private String lastName;
	private BBRShop shop;
	
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

	public void setShop(BBRShop shop) {
		this.shop = shop;
	}
	
	public BBRShop getShop() {
		return shop;
	}

}
