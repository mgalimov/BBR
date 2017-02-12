package BBRAcc;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;

import javax.persistence.*;

import jdk.nashorn.internal.objects.annotations.Getter;
import BBR.BBRDataElement;
import BBR.BBRDataSet;

@SuppressWarnings("unused")
@Entity
@Table(name="users")
public class BBRUser extends BBRDataElement {
	@Id
	@GeneratedValue
	@Column(name="USER_ID")
	private Long id;
	
	@Column(name="EMAIL")
	private String email;
	
	@Column(name="FIRSTNAME")
	private String firstName;
	
	@Column(name="LASTNAME")
	private String lastName;
	
	@Column(name="PASS")
	private String encodedPassword;
	
	@Column(name="APPROVED")
	private boolean approved;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private BBRShop shop;

	@ManyToOne(fetch=FetchType.EAGER)
	private BBRPoS pos;
	
	@Column(name="LANG")
	private String language;

	public class BBRUserRole {
		public static final int ROLE_BBR_OWNER = 256;
		public static final int ROLE_SHOP_ADMIN = 8;
		public static final int ROLE_POS_ADMIN = 4;
		public static final int ROLE_POS_SPECIALIST = 2;
		public static final int ROLE_VISITOR = 1;
	}

	@Column(name="ROLE")	
	private int role = 0;

	@Column(name="PHOTO", columnDefinition="LONGBLOB")
	private Blob photo;

	@Column(name="PHOTOEXT")
	private String photoExt;
	
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
		language = BBRAccReg.defaultLanguage;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
	public String getLanguage() {
		return language;
	}

	public Blob getPhoto() {
		return photo;
	}

	public void setPhoto(Blob photo) {
		this.photo = photo;
	}

	public String getPhotoExt() {
		return photoExt;
	}

	public void setPhotoExt(String photoExt) {
		this.photoExt = photoExt;
	}
}
