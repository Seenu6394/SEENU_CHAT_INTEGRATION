package com.scs.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.scs.entity.model.PuristBCCategory;
import com.scs.exception.ApiException;
import com.scs.model.BaseRequestModel;
import com.scs.model.JsonSuccessMessage;
import com.scs.model.PuristAdminTokenReq;
import com.scs.model.PuristAgentModel;
import com.scs.model.PuristCategory;
import com.scs.model.PuristOperator;
import com.scs.model.PuristUserLogin;
import com.scs.model.PuristUserLoginRes;
import com.scs.model.RegisterPuristUser;
import com.scs.model.ResponseID;
import com.scs.model.SupportCategoryID;
import com.scs.service.ChatService;
import com.scs.util.ApiConstants;
import com.scs.util.ErrorConstants;
import com.scs.util.Utility;

@Service("PuristChatService")

public class PuristChatServiceImpl implements ChatService {

	private static final Logger logger = Logger.getLogger(PuristChatServiceImpl.class);

	String loginUrl = ApiConstants.PURIST_USER_LOGINURL;
	String adminKey = ApiConstants.PURIST_ADMIN_KEY;
	String password = ApiConstants.PURIST_PASSWORD;

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private Environment env;

	ObjectMapper mapper = new ObjectMapper();

	public Object getUserLoginObject(PuristUserLogin puristLoginObj) throws ApiException {

		PuristUserLoginRes puristUserLoginResp = null;

		try {

			String plainCreds = adminKey + ":" + password;
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "Basic " + Utility.getBase64Credits(plainCreds));
			headers.setContentType(MediaType.APPLICATION_JSON);
			String userName = puristLoginObj.getP_username();/*.substring(0, puristLoginObj.getP_username().indexOf("@"))
					+ env.getProperty("scs.system.appname").toLowerCase() + ApiConstants.PURIST_USERNAME_EXTENSION;*/

			puristLoginObj.setP_username(userName);
			String loginReqBody = Utility.getJsonForRequest(puristLoginObj);
			HttpEntity<String> loginHttpEntity = new HttpEntity<String>(loginReqBody, headers);
			ResponseEntity<String> loginResponse = Utility.sendHttpRequest(loginHttpEntity, HttpMethod.POST, loginUrl);

			logger.info("----------------------got user login response------------------------------------------");
			puristUserLoginResp = mapper.readValue(loginResponse.getBody(), PuristUserLoginRes.class);

			return puristUserLoginResp;
		} catch (ApiException ae) {
			logger.info("Exception: " + ae.getMessage());
			throw ae;
		} catch (Exception ex) {
			logger.info("Exception: " + ex.getMessage());
			throw new ApiException(ErrorConstants.USER_LOGIN_EXCEPTION, "");
		}
	}

	public Object userCreation(RegisterPuristUser registerPuristUser) throws ApiException {

		String response = "";

		try {

			PuristUserLoginRes adminloginResponse = (PuristUserLoginRes) puristAdminLogin();
			String accessToken = adminloginResponse.getAccess_token();
			logger.info("accessToken: " + accessToken);

			HttpHeaders createUserHeaders = new HttpHeaders();
			createUserHeaders.add("Authorization", "Bearer " + accessToken);
			createUserHeaders.setContentType(MediaType.APPLICATION_JSON);

			String userName = registerPuristUser.getEmail().substring(0, registerPuristUser.getEmail().indexOf("@"))
					+ env.getProperty("scs.system.appname").toLowerCase() + ApiConstants.PURIST_USERNAME_EXTENSION;
			registerPuristUser.setUsername(userName);
			String createUserReqBody = Utility.getJsonForRequest(registerPuristUser);
			logger.info("createUserReqBody: " + createUserReqBody);
			if (createUserReqBody != null && createUserReqBody != "") {
				HttpEntity<String> createUserHttpEntity = new HttpEntity<String>(createUserReqBody, createUserHeaders);
				ResponseEntity<String> createuserResponse = Utility.sendHttpRequest(createUserHttpEntity,
						HttpMethod.POST, ApiConstants.PURIST_CREATE_USER_URL);
				response = createuserResponse.getBody();
			}

			return response;
		} catch (HttpClientErrorException hcee) {
			logger.info("Exception: " + hcee.getMessage());
			response = hcee.getResponseBodyAsString();
			throw new ApiException(ErrorConstants.PLEASE_PROVIDE_EMAIL, "");

		} catch (Exception ex) {
			logger.info("Exception: " + ex.getMessage());
			throw new ApiException(ErrorConstants.USER_LOGIN_EXCEPTION, "");
		}

	}

	public Object forwardConversation(BaseRequestModel baseRequestModel) throws ApiException {

		try {

			PuristUserLoginRes puristUserLoginRes = (PuristUserLoginRes) puristAdminLogin();
			String adminAccessToken = puristUserLoginRes.getAccess_token();
			PuristBCCategory puristAgentRec = (PuristBCCategory) getPuristAgentRecord(baseRequestModel.getAgentCode());

			logger.info("puristAgentRec: " + puristAgentRec.toString());
			String httpUrl = ApiConstants.PURIST_FORWARD_CONVERSATION
					.replace("{operatorID}", puristAgentRec.getAgentID().toString())
					.replace("{conversationID}", baseRequestModel.getConversationID())
					.replace("{roomID}", puristAgentRec.getRoomID().toString());

			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "Bearer " + adminAccessToken);
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);

			ResponseEntity<String> response = Utility.sendHttpRequest(httpEntity, HttpMethod.POST, httpUrl);

			return response.getBody();
		} catch (ApiException ae) {
			logger.info("Exception: " + ae.getMessage());
			throw ae;
		} catch (Exception ex) {

			logger.info("Exception: " + ex.getMessage());
			throw new ApiException(ErrorConstants.USER_NOT_FOUND_EXCEPTION, "");
		}
	}

	private Object getPuristAgentRecord(String agentCode) throws ApiException {

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<PuristBCCategory> query = builder.createQuery(PuristBCCategory.class);
			Root<PuristBCCategory> root = query.from(PuristBCCategory.class);
			query.where(builder.equal(root.get("puristBCCategoryCode"), agentCode));
			query.select(root);
			return session.createQuery(query).getSingleResult();
		}
	}

	public Object puristAdminLogin() throws ApiException {

		try {

			// do admin login -> user registration ->userlogin

			logger.info("----------------------user not register------------------------------------------");
			String admintokenUrl = ApiConstants.PURIST_ADMIN_TOKEN_URL;

			PuristAdminTokenReq admintokenReq = new PuristAdminTokenReq();
			admintokenReq.setClient_id("na");
			admintokenReq.setGrant_type("password");
			admintokenReq.setUsername(ApiConstants.PURIST_ADMIN_USERNAME);
			admintokenReq.setPassword(ApiConstants.PURIST_ADMIN_PASSWORD);

			HttpHeaders adminLoginheaders = new HttpHeaders();
			adminLoginheaders.setContentType(MediaType.APPLICATION_JSON);

			String adminReqBody = Utility.getJsonForRequest(admintokenReq);
			logger.info("adminReqBody: " + adminReqBody);
			HttpEntity<String> adminHttpEntity = new HttpEntity<String>(adminReqBody, adminLoginheaders);
			ResponseEntity<String> adminLoginResponse = Utility.sendHttpRequest(adminHttpEntity, HttpMethod.POST,
					admintokenUrl);

			if (adminLoginResponse.getStatusCode().equals(HttpStatus.CREATED)) {
				logger.info("----------------------admin logged in successfully------------------------------------");

				return mapper.readValue(adminLoginResponse.getBody(),
						PuristUserLoginRes.class);

			} else {
				logger.info(
						"----------------------admin login failed for user creation------------------------------------------");
				throw new ApiException(ErrorConstants.USER_LOGIN_EXCEPTION, "");
			}

		} catch (HttpClientErrorException hcee) {
			logger.info("Exception: " + hcee.getMessage());
			throw new ApiException(ErrorConstants.PLEASE_PROVIDE_EMAIL, "");

		} catch (Exception ex) {
			logger.info("Exception: " + ex.getMessage());
			throw new ApiException(ErrorConstants.USER_LOGIN_EXCEPTION, "");
		}

	}

	public Object createRoomAndOperator() throws ApiException {

		try {
			
			String appName = env.getProperty("scs.system.appname");
			logger.info("Creating room and operator for application: " + appName);
			
			String botRoom = appName + ApiConstants.BOT + ApiConstants.ROOM_EXTN;
			String humanRoom = appName + ApiConstants.HUMAN + ApiConstants.ROOM_EXTN;

			PuristUserLoginRes puristUserLoginRes = (PuristUserLoginRes) puristAdminLogin();
			String adminAccessToken = puristUserLoginRes.getAccess_token();

			Long botRoomID = createRoom(botRoom, ApiConstants.PURIST_BOT_OPERATOR_ASSIGNED_MESSAGE, adminAccessToken,
					appName);
			
			logger.info("Create bot roomID: "+botRoomID);
			List<Long> botOperatorRoomIDs = new ArrayList<>();
			botOperatorRoomIDs.add(botRoomID);
			Long botOperatorID = createOperator(botOperatorRoomIDs, env.getProperty("scs.system.useremail"), adminAccessToken);
			logger.info("Create botOperatorID: "+botOperatorID);
			PuristAgentModel bcPuristBotAgent = new PuristAgentModel();
			bcPuristBotAgent.setAgentID(botOperatorID);
			bcPuristBotAgent.setAgentName(env.getProperty("scs.system.useremail"));
			bcPuristBotAgent.setPuristAgentCode(appName+"_BOT");
			bcPuristBotAgent.setRoomID(botRoomID);
			bcPuristBotAgent.setRoomName(botRoom);
			createPuristAgent(bcPuristBotAgent);
			
			
			Long humanRoomID = createRoom(humanRoom, ApiConstants.PURIST_HUMAN_OPERATOR_ASSIGNED_MESSAGE, adminAccessToken, appName);
			logger.info("Create human roomID: "+humanRoomID);
			PuristAgentModel bcPuristHumanAgent = new PuristAgentModel();
			bcPuristHumanAgent.setPuristAgentCode(appName+ApiConstants.BOT);
			bcPuristHumanAgent.setRoomID(humanRoomID);
			bcPuristHumanAgent.setRoomName(botRoom);
			createPuristAgent(bcPuristHumanAgent);

			return new JsonSuccessMessage(ApiConstants.SUCCESS);
		} catch (ApiException ae) {
			logger.info("Exception: " + ae.getMessage());
			throw ae;
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage());
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION,
					"Requested service you are trying is not available");
		}
	}

	public Long createRoom(String roomName, String operAssignMsg, String adminAccessToken, String appName)
			throws ApiException {

		try {
			// headers for creating rooms and operators
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "Bearer " + adminAccessToken);
			headers.setContentType(MediaType.APPLICATION_JSON);

			// creating room for bot
			PuristCategory category = new PuristCategory();
			category.setName(roomName);
			category.setEnabled(true);
			category.setMax_size(30);
			category.setOperator_assigned_message(operAssignMsg);
			category.setReject_message(ApiConstants.PURIST_REJECT_MESSAGE);
			category.setWait_message(ApiConstants.PURIST_WAIT_MESSAGE);
			category.setWelcome_message(ApiConstants.PURIST_WELCOME_MESSAGE + appName);

			String categoryReqBody = Utility.getJsonForRequest(category);
			logger.info("botCategoryReqBody: " + categoryReqBody);
			HttpEntity<String> categoryHttpEntity = new HttpEntity<>(categoryReqBody, headers);
			ResponseEntity<String> categoryResponse = Utility.sendHttpRequest(categoryHttpEntity, HttpMethod.POST,
					ApiConstants.PURIST_CREATE_CATEGORY_URL);

			ResponseID categoryResponseID = mapper.readValue(categoryResponse.getBody(), ResponseID.class);

			logger.info("------Created roomID:---------" + categoryResponseID.getId() + "---for room-----" + roomName);
			return categoryResponseID.getId();

		} catch (HttpClientErrorException hcee) {
			logger.info("Exception: " + hcee.getMessage());
			logger.info("User may be already existed");
			throw new ApiException(ErrorConstants.SERVICE_UNAVAILABLE,
					"Requested service you are trying is not available");
		} catch (HttpServerErrorException hsee) {
			logger.info("Exception: " + hsee.getMessage());
			throw new ApiException(ErrorConstants.SERVICE_UNAVAILABLE,
					"Requested service you are trying is not available");
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage());
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, "");
		}
	}
	
	public Object createHumanOperator(BaseRequestModel baseRequestModel) throws ApiException  {
		try {
			
			String appName = env.getProperty("scs.system.appname");
			
			PuristUserLoginRes puristUserLoginRes = (PuristUserLoginRes) puristAdminLogin();
			String adminAccessToken = puristUserLoginRes.getAccess_token();
			
			PuristBCCategory puristAgentRec = (PuristBCCategory) getPuristAgentRecord(appName+ApiConstants.HUMAN);
			List<SupportCategoryID> SupportCategories = new ArrayList<>();
			SupportCategories.add(new SupportCategoryID(puristAgentRec.getRoomID()));
			PuristOperator operator = new PuristOperator();

			operator.setName(baseRequestModel.getEmail());
			operator.setEmail(baseRequestModel.getEmail());
			operator.setUsername(baseRequestModel.getEmail().substring(0, baseRequestModel.getEmail().indexOf("@")));
			operator.setChatEnabled(true);
			operator.setEnabled(true);
			operator.setMaxConversations(5);
			operator.setPassword(password);
			operator.setSupportCategoryIds(SupportCategories);
			operator.setTicketingEnabled(false);

			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "Bearer " + adminAccessToken);
			headers.setContentType(MediaType.APPLICATION_JSON);
			String operatorReqBody = Utility.getJsonForRequest(operator);
			logger.info("operatorReqBody: " + operatorReqBody);
			HttpEntity<String> categoryHttpEntity = new HttpEntity<>(operatorReqBody, headers);
			ResponseEntity<String> operatorResponse = Utility.sendHttpRequest(categoryHttpEntity, HttpMethod.POST,
					ApiConstants.PURIST_CREATE_OPERATOR_URL);

			ResponseID operatorResponseID = mapper.readValue(operatorResponse.getBody(), ResponseID.class);
			logger.info("---------operatorID------------" + operatorResponseID.getId());

			return new JsonSuccessMessage(ApiConstants.SUCCESS);
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage());
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, "");
		}
		
	}
	
	

	public Long createOperator(List<Long> roomIDs, String email, String adminAccessToken) throws ApiException {
		try {

			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "Bearer " + adminAccessToken);
			headers.setContentType(MediaType.APPLICATION_JSON);

			List<SupportCategoryID> SupportCategories = new ArrayList<>();
			for (Long roomID : roomIDs) {
				SupportCategories.add(new SupportCategoryID(roomID));
			}

			PuristOperator operator = new PuristOperator();

			operator.setName(email);
			operator.setEmail(email);
			operator.setUsername(email.substring(0, email.indexOf("@")));
			operator.setChatEnabled(true);
			operator.setEnabled(true);
			operator.setMaxConversations(5);
			operator.setPassword(password);
			operator.setSupportCategoryIds(SupportCategories);
			operator.setTicketingEnabled(false);

			String operatorReqBody = Utility.getJsonForRequest(operator);
			logger.info("operatorReqBody: " + operatorReqBody);
			HttpEntity<String> categoryHttpEntity = new HttpEntity<>(operatorReqBody, headers);
			ResponseEntity<String> operatorResponse = Utility.sendHttpRequest(categoryHttpEntity, HttpMethod.POST,
					ApiConstants.PURIST_CREATE_OPERATOR_URL);

			ResponseID operatorResponseID = mapper.readValue(operatorResponse.getBody(), ResponseID.class);
			logger.info("---------operatorID------------" + operatorResponseID.getId());

			return operatorResponseID.getId();
		} catch (HttpClientErrorException hcee) {
			logger.info("Exception: " + hcee.getMessage());
			logger.info("User may be already existed");
			throw new ApiException(ErrorConstants.SERVICE_UNAVAILABLE,
					"Requested service you are trying is not available");
		} catch (HttpServerErrorException hsee) {
			logger.info("Exception: " + hsee.getMessage());
			throw new ApiException(ErrorConstants.SERVICE_UNAVAILABLE,
					"Requested service you are trying is not available");
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage());
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, "");
		}
	}

	@Override
	public Object createPuristAgent(PuristAgentModel puristAgent) throws ApiException {

		Transaction tx = null;
		PuristBCCategory purisAgent = new PuristBCCategory();
		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();

			purisAgent.setAgentID(puristAgent.getAgentID());
			purisAgent.setAgentName(puristAgent.getAgentName());
			purisAgent.setPuristBCCategoryCode(puristAgent.getPuristAgentCode());
			purisAgent.setPuristBCCategoryID(puristAgent.getPuristAgentID());
			purisAgent.setRoomID(puristAgent.getRoomID());
			purisAgent.setRoomName(puristAgent.getRoomName());

			session.save(purisAgent);
			tx.commit();

		} catch (HibernateException ex) {
			logger.error("+++++ ReDbServicesImpl.createRegexMapping END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ ReDbServicesImpl.createRegexMapping END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		}
		return purisAgent;
	}

	@Override
	public Object updatePuristAgent(PuristAgentModel puristAgent) throws ApiException {

		Transaction tx = null;
		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaUpdate<PuristBCCategory> update = builder.createCriteriaUpdate(PuristBCCategory.class);
			Root<PuristBCCategory> root = update.from(PuristBCCategory.class);

			update.set(root.get("puristAgentCode"), puristAgent.getPuristAgentCode());
			update.set(root.get("agentID"), puristAgent.getAgentID());
			update.set(root.get("roomID"), puristAgent.getRoomID());
			update.set(root.get("roomName"), puristAgent.getRoomName());
			update.set(root.get("agentID"), puristAgent.getAgentID());

			update.where(builder.equal(root.get("puristAgentCode"), puristAgent.getPuristAgentCode()));
			session.createQuery(update).executeUpdate();

			tx.commit();

		} catch (HibernateException ex) {
			logger.error("+++++ ReDbServicesImpl.updateRe END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ ReDbServicesImpl.updateRe END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		}

		return ApiConstants.SUCCESS;
	}

	@Override
	public Object deletePuristAgent(String puristAgentCode) throws ApiException {

		Transaction tx = null;
		PuristBCCategory puristAgent = new PuristBCCategory();
		try (Session session = sessionFactory.openSession()) {

			tx = session.beginTransaction();

			puristAgent.setPuristBCCategoryCode(puristAgentCode);
			session.delete(puristAgent);
			tx.commit();

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error("+++++ IntentDbServicesImpl.deleteRegEx END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error("+++++ IntentDbServicesImpl.deleteRegEx END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}
		return null;

	}

}