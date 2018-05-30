package com.scs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)

public class PuristAgentModel {

	private Long puristAgentID;

	private String puristAgentCode;

	private Long agentID;

	private String agentName;

	private Long roomID;

	private String roomName;

	public Long getPuristAgentID() {
		return puristAgentID;
	}

	public void setPuristAgentID(Long puristAgentID) {
		this.puristAgentID = puristAgentID;
	}

	public String getPuristAgentCode() {
		return puristAgentCode;
	}

	public void setPuristAgentCode(String puristAgentCode) {
		this.puristAgentCode = puristAgentCode;
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

}
