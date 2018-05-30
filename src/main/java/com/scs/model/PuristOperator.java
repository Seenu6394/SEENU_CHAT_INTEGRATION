package com.scs.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PuristOperator {

	private String name;
	
	private String email;
	
	private String username;
	
	@JsonProperty("max_conversations")
	private int max_conversations;
	
	private String password;
	
	private boolean enabled;
	
	@JsonProperty("chat_enabled")
	private boolean chat_enabled;
	
	@JsonProperty("ticketing_enabled")
	private boolean ticketing_enabled;

	@JsonProperty("support_category_ids")
	private List<SupportCategoryID> support_category_ids;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getMaxConversations() {
		return this.max_conversations;
	}

	public void setMaxConversations(int max_conversations) {
		this.max_conversations = max_conversations;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean getEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean getChatEnabled() {
		return this.chat_enabled;
	}

	public void setChatEnabled(boolean chat_enabled) {
		this.chat_enabled = chat_enabled;
	}

	public boolean getTicketingEnabled() {
		return this.ticketing_enabled;
	}

	public void setTicketingEnabled(boolean ticketing_enabled) {
		this.ticketing_enabled = ticketing_enabled;
	}

	public List<SupportCategoryID> getSupportCategoryIds() {
		return this.support_category_ids;
	}

	public void setSupportCategoryIds(List<SupportCategoryID> support_category_ids) {
		this.support_category_ids = support_category_ids;
	}
}
