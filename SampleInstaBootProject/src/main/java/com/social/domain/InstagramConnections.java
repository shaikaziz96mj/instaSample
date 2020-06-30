package com.social.domain;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "instagram_connections")
public class InstagramConnections {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name = "external_id")
	private String externalId;
	@Column(name = "access_token")
	private String accessToken;
	@Column(name = "expiry_time")
	private String expiryTime;
	@Column(name = "name")
	private String name;
	@Column(name = "description")
	private String description;
	@Column(name = "created_on")
	private Calendar createdOn;
	@Column(name = "modified_on")
	private Calendar modifiedOn;
	
	public Long getId() {
		return id;
	}
	public String getExternalId() {
		return externalId;
	}
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getExpiryTime() {
		return expiryTime;
	}
	public void setExpiryTime(String expiryTime) {
		this.expiryTime = expiryTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Calendar getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Calendar createdOn) {
		this.createdOn = createdOn;
	}
	public Calendar getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(Calendar modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	
	public InstagramConnections(String externalId, String accessToken, String expiryTime, String name,
			String description, Calendar createdOn, Calendar modifiedOn) {
		super();
		this.externalId = externalId;
		this.accessToken = accessToken;
		this.expiryTime = expiryTime;
		this.name = name;
		this.description = description;
		this.createdOn = createdOn;
		this.modifiedOn = modifiedOn;
	}
	
	public InstagramConnections() {
		super();
	}
	
	@Override
	public String toString() {
		return "InstagramConnections [id=" + id + ", externalId=" + externalId + ", accessToken=" + accessToken
				+ ", expiryTime=" + expiryTime + ", name=" + name + ", description=" + description + ", createdOn="
				+ createdOn + ", modifiedOn=" + modifiedOn + "]";
	}
	
}