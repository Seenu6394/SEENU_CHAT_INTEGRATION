package com.scs.util;

public class ApiConstants {

	public static final String S4M_BOT_LOGIN = "/s4mBotLogin";
	public static final String S4M_BOT_USER_REGISTER = "/s4mBotUserRegister";
	public static final String FORWARD_CONV_TO_HUMAN = "/forwardConvToHuman";
	public static final String CREATE_ROOM_OPERATOR = "/createRoomOperator";
	public static final String CREATE_HUMAN_OPERATOR = "/createHumanOperator";
	public static final String OPERATOR_EXTN = "_OPERATOR";
	public static final String ROOM_EXTN = "_ROOM";
	public static final String BOT = "_BOT";
	public static final String HUMAN = "_HUMAN";
	public static final String PURIST_AGENT = "/puristAgent";
	
	
	public static final String API = "/api";
	public static final String LOGOUT = "/signOut";
	public static final String LOGIN_SUCCESS = "/loginSuccess";
	public static final String LOGIN_FAILURE = "/loginFailure";

	public static final String BINDING_ERRORS = "++---- ERRORS---++";

	public static final String APP_SOURCE = "/api";

	public static final String API_RESPONSE = "data";

	public static final String API_REQUEST = "apiServicesRequest";
	public static final String API_RESP_ERR_CODE = "errorCode";
	public static final String API_RESP_ERR_MSG = "errorDescription";
	public static final String API_RESP_STATUS = "status";

	public static final String SUCCESS = "success";
	public static final String VERIFY = "verify";
	public static final String APP_MESSAGES_RESOURCE_BASENAME = "messages";
	public static final String WELCOME_SCS = "scs-api";

	public static final String DATE_FORMAT = "dd/MM/yyyy";
	public static final String EIDA_DATE_FORMAT = "yyyy/MM/dd";
	public static final String MERCHANT_SUMMARY_MONTH_FORMAT = "MMyyyy";
	public static final String HISTORY_MONTH_FORMAT = "MM/yyyy";
	public static final String HISTORY_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String MOBILE_SERVICES = "MOBILE_SERVICES_";

	public static final int SESSION_MAX_INTERVAL = 5 * 60;
	public static final int TOKEN_VALID_INTERVAL = 600;
	public static final int SESSION_MAX_CONCURRENT = 1;

	public static final int PWD_MIN_LENGTH = 8;
	
	public static final String LINE_SEPARATOR = "line.separator";
	
	public static final String ATTEMPT_FAILURE = "attempt_failure";
	public static final String LOGIN_FAILURE_STATUS = "login_failure_status";
	public static final int MAX_ATTEMPTS = 5;

	public static final String PURIST_USER_LOGINURL = "http://api.puristchat.com/login";
	public static final String PURIST_ADMIN_KEY = "ziULmsF8rdeqdWmvxAIt6p2J9XLpgWs";
	public static final String PURIST_PASSWORD = "12341234";
	public static final String PURIST_ADMIN_TOKEN_URL = "http://api.puristchat.com/admin/v1/oauth/token";
	public static final String PURIST_ADMIN_USERNAME = "admin@s4m.ae";
	public static final String PURIST_ADMIN_PASSWORD = "12341234";
	public static final String PURIST_CREATE_USER_URL = "http://api.puristchat.com/admin/v1/users";
	public static final String PURIST_FORWARD_CONVERSATION = "http://api.puristchat.com/admin/v1/operators/{operatorID}/conversations/{conversationID}/forward?room_id={roomID}";
	public static final String PURIST_CREATE_CATEGORY_URL = "http://api.puristchat.com/admin/v1/support-categories/";
	public static final String PURIST_CREATE_OPERATOR_URL = "http://api.puristchat.com/admin/v1/operators";
	
	
	
	public static final String PURIST_BOT_OPERATOR_ASSIGNED_MESSAGE = "We are connecting you to bot";
	public static final String PURIST_HUMAN_OPERATOR_ASSIGNED_MESSAGE = "We are connecting you to an agent";
	public static final String PURIST_WAIT_MESSAGE = "Please wait, we'll connect you to the next available agent";
	public static final String PURIST_WELCOME_MESSAGE = "Welcome to ";
	public static final String PURIST_REJECT_MESSAGE = "Our Support agents are too busy, please try again later";
	public static final String PURIST_USERNAME_EXTENSION = ".s4m";
	
	private ApiConstants() {

	}

}
