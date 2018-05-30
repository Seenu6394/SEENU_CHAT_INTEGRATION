package com.scs.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;


public class PuristCategory {

	private boolean enabled;

	private String welcome_message;

	private String operator_assigned_message;

	private String wait_message;

	private String name;

	private String reject_message;

	private Integer max_size;

	private List<OperatorIDs> operator_ids;

	public PuristCategory() {
		super();
	}

	
	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getWelcome_message() {
		return welcome_message;
	}

	public void setWelcome_message(String welcome_message) {
		this.welcome_message = welcome_message;
	}

	public String getOperator_assigned_message() {
		return operator_assigned_message;
	}

	public void setOperator_assigned_message(String operator_assigned_message) {
		this.operator_assigned_message = operator_assigned_message;
	}

	public String getWait_message() {
		return wait_message;
	}

	public void setWait_message(String wait_message) {
		this.wait_message = wait_message;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReject_message() {
		return reject_message;
	}

	public void setReject_message(String reject_message) {
		this.reject_message = reject_message;
	}

	public Integer getMax_size() {
		return max_size;
	}

	public void setMax_size(Integer max_size) {
		this.max_size = max_size;
	}

	public List<OperatorIDs> getOperator_ids() {
		return operator_ids;
	}

	public void setOperator_ids(List<OperatorIDs> operator_ids) {
		this.operator_ids = operator_ids;
	}

	@Override
	public String toString() {
		return "ClassPojo [enabled = " + enabled + ", welcome_message = " + welcome_message
				+ ", operator_assigned_message = " + operator_assigned_message + ", wait_message = " + wait_message
				+ ", name = " + name + ", reject_message = " + reject_message + ", max_size = " + max_size
				+ ", operator_ids = " + operator_ids + "]";
	}
}
