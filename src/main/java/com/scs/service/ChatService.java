package com.scs.service;

import com.scs.exception.ApiException;
import com.scs.model.BaseRequestModel;
import com.scs.model.PuristAgentModel;
import com.scs.model.PuristUserLogin;
import com.scs.model.RegisterPuristUser;

public interface ChatService {

	public Object getUserLoginObject(PuristUserLogin puristLoginObj) throws ApiException;

	public Object userCreation(RegisterPuristUser registerPuristUser) throws ApiException;

	public Object forwardConversation(BaseRequestModel baseRequestModel) throws ApiException;

	public Object puristAdminLogin() throws ApiException;

	public Object createRoomAndOperator( ) throws ApiException;

	public Object createPuristAgent(PuristAgentModel puristAgent) throws ApiException;

	public Object updatePuristAgent(PuristAgentModel puristAgent) throws ApiException;

	public Object deletePuristAgent(String puristAgentCode) throws ApiException;
	
	public Object createHumanOperator(BaseRequestModel baseRequestModel) throws ApiException ; 
}
