package com.surpassun.cash.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 * A client.
 */
@Entity
@Table(name = "T_CLIENT")
public class Client extends AbstractAuditingEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

	@NotNull
	@Size(min = 6, max = 15)
	@Column(unique = true)
	private String code;
	
    private String firstname;
    private String lastname;
    private String address;
    private String postcode;
    private String city;
    private String phone;
    private String email;
    private Float totalConsumation;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "membershipLevelId")
    private MembershipLevel membershipLevel;
    
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime membershipStartDate;
    
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime birthday;
    
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime discountStartDate;
    
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime discountEndDate;
    
    @NotNull
    private Boolean activated = true;
    
    public Client() {
    }
    
    public Client(String firstname, String lastname, String code) {
    	this.firstname = firstname;
		this.lastname = lastname;
		this.code = code;
    }
    
	public Client(String code, String firstname, String lastname, String address, String postcode, String city, String phone, String email, DateTime birthday) {
		super();
		this.code = code;
		this.firstname = firstname;
		this.lastname = lastname;
		this.address = address;
		this.postcode = postcode;
		this.city = city;
		this.phone = phone;
		this.email = email;
		this.birthday = birthday;
		super.setCreatedBy("1");
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Float getTotalConsumation() {
		return totalConsumation;
	}

	public void setTotalConsumation(Float totalConsumation) {
		this.totalConsumation = totalConsumation;
	}

	public MembershipLevel getMembershipLevel() {
		return membershipLevel;
	}

	public void setMembershipLevel(MembershipLevel membershipLevel) {
		this.membershipLevel = membershipLevel;
	}

	public DateTime getMembershipStartDate() {
		return membershipStartDate;
	}

	public void setMembershipStartDate(DateTime membershipStartDate) {
		this.membershipStartDate = membershipStartDate;
	}

	public DateTime getBirthday() {
		return birthday;
	}

	public void setBirthday(DateTime birthday) {
		this.birthday = birthday;
	}

	public DateTime getDiscountStartDate() {
		return discountStartDate;
	}

	public void setDiscountStartDate(DateTime discountStartDate) {
		this.discountStartDate = discountStartDate;
	}

	public DateTime getDiscountEndDate() {
		return discountEndDate;
	}

	public void setDiscountEndDate(DateTime discountEndDate) {
		this.discountEndDate = discountEndDate;
	}

	public Boolean getActivated() {
		return activated;
	}

	public void setActivated(Boolean activated) {
		this.activated = activated;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		Client client = (Client)obj;
		if (client.getCode() == null) {
			return false;
		}
		if (this.getCode().equals(client.getCode())) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return this.getCode().hashCode();
	};
}
