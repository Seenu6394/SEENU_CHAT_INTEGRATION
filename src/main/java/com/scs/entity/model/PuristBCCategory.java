
package com.scs.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_purist_bc_category")
public class PuristBCCategory {
	
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
	@Column(name = "purist_bc_category_id", unique = true, nullable = false)
	private Long puristBCCategoryID;
	
	@Column(name = "purist_bc_category_code")
	private String puristBCCategoryCode;
	

	@Column(name = "agent_id")
	private Long agentID;
	
	@Column(name = "agent_name")
	private String agentName;
	
	@Column(name = "room_id")
	private Long roomID;
	
	@Column(name = "room_name")
	private String roomName;

	public PuristBCCategory() {
		super();
		
	}

	public PuristBCCategory(Long puristBCCategoryID, String puristBCCategoryCode, Long agentID, String agentName,
			Long roomID, String roomName) {
		super();
		this.puristBCCategoryID = puristBCCategoryID;
		this.puristBCCategoryCode = puristBCCategoryCode;
		this.agentID = agentID;
		this.agentName = agentName;
		this.roomID = roomID;
		this.roomName = roomName;
	}

	public Long getAgentID() {
		return agentID;
	}

	public void setAgentID(Long agentID) {
		this.agentID = agentID;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public Long getRoomID() {
		return roomID;
	}

	public void setRoomID(Long roomID) {
		this.roomID = roomID;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public Long getPuristBCCategoryID() {
		return puristBCCategoryID;
	}

	public void setPuristBCCategoryID(Long puristBCCategoryID) {
		this.puristBCCategoryID = puristBCCategoryID;
	}

	public String getPuristBCCategoryCode() {
		return puristBCCategoryCode;
	}

	public void setPuristBCCategoryCode(String puristBCCategoryCode) {
		this.puristBCCategoryCode = puristBCCategoryCode;
	}
	
}


