package BBRAcc;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.persistence.*;

import BBR.BBRDataElement;
import BBR.BBRDataSet;

@SuppressWarnings("unused")
@Entity
@Table(name="services")
public class BBRService extends BBRDataElement {
	@Id
	@GeneratedValue
	@Column(name="SERVICE_ID")
	private Long id;

	@Column(name="TITLE")
	private String title;
	
	@Column(name="STATUS")
	private int status;
	
	@Column(name="DEMO")
	private Boolean demo;

	@Column(name="BASIC")
	private Boolean basic;

	public class BBRServiceStatus {
		public static final int SERVICE_INITIALIZED = 0;
		public static final int SERVICE_ACTIVE = 1;
		public static final int SERVICE_CLOSED = 2;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Boolean isDemo() {
		return demo;
	}

	public void setDemo(Boolean demo) {
		this.demo = demo;
	}

	public Boolean isBasic() {
		return basic;
	}

	public void setBasic(Boolean basic) {
		this.basic = basic;
	}

}
