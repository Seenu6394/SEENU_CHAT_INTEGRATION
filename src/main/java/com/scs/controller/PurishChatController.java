package com.scs.controller;

import java.util.HashMap;
import java.util.Random;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.apache.commons.codec.binary.Base64;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.scs.exception.ApiException;
import com.scs.model.BaseRequestModel;
import com.scs.model.PuristAgentModel;
import com.scs.model.PuristUserLogin;
import com.scs.model.PuristUserLoginRes;
import com.scs.model.RegisterPuristUser;
import com.scs.service.ChatService;
import com.scs.util.ApiConstants;
import com.scs.util.ErrorConstants;
import com.scs.util.Utility;

@RestController
@RequestMapping(ApiConstants.API)

public class PurishChatController {

	private static final Logger logger = Logger.getLogger(PurishChatController.class);

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ChatService chatService;

	@Autowired
	private Environment env;

	@PostMapping(value = ApiConstants.S4M_BOT_LOGIN, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object s4mUserLogin(@Valid @RequestBody PuristUserLogin puristLoginObj, BindingResult bindingResult,
			HttpSession session) throws ApiException {

		try {
			if (bindingResult.hasErrors()) {
				logger.debug(ApiConstants.BINDING_ERRORS);
				throw new ApiException(ErrorConstants.INVALIDDATA, Utility.getFirstErrorInformation(bindingResult));
			}
			return chatService.getUserLoginObject(puristLoginObj);

		} catch (ApiException ex) {
			logger.error(ex.getErrorCode() + " : " + ex.getErrorCode(), ex);
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
	}

	@PostMapping(value = ApiConstants.S4M_BOT_USER_REGISTER, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object puristUserCreation(@Valid @RequestBody RegisterPuristUser registerPuristUser,
			BindingResult bindingResult, HttpSession session) throws ApiException {

		try {
			if (bindingResult.hasErrors()) {
				logger.debug(ApiConstants.BINDING_ERRORS);
				throw new ApiException(ErrorConstants.INVALIDDATA, Utility.getFirstErrorInformation(bindingResult));
			}
			return chatService.userCreation(registerPuristUser);

		} catch (ApiException ex) {
			logger.error(ex.getErrorCode() + " : " + ex.getErrorCode(), ex);
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
	}

	@PostMapping(value = ApiConstants.FORWARD_CONV_TO_HUMAN, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object forwardConversationToHuman(@Valid @RequestBody BaseRequestModel baseRequestModel,
			BindingResult bindingResult, HttpSession session) throws ApiException {
		try {
			if (bindingResult.hasErrors()) {
				logger.debug(ApiConstants.BINDING_ERRORS);
				throw new ApiException(ErrorConstants.INVALIDDATA, Utility.getFirstErrorInformation(bindingResult));
			}
			return chatService.forwardConversation(baseRequestModel);
		} catch (ApiException ex) {
			logger.error(ex.getErrorCode() + " : " + ex.getErrorCode(), ex);
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
	}

	@PostMapping(value = ApiConstants.CREATE_ROOM_OPERATOR, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object createRoomAndOperator( HttpSession session) throws ApiException {
		try {
			
			
			return chatService.createRoomAndOperator();
		} catch (ApiException ex) {
			logger.error(ex.getErrorCode() + " : " + ex.getErrorCode(), ex);
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
	}
	
	@PostMapping(value = ApiConstants.CREATE_HUMAN_OPERATOR, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object createHumanOperator(@Valid @RequestBody BaseRequestModel baseRequestModel,
			BindingResult bindingResult, HttpSession session) throws ApiException {
		try {
			if (bindingResult.hasErrors()) {
				logger.debug(ApiConstants.BINDING_ERRORS);
				throw new ApiException(ErrorConstants.INVALIDDATA, Utility.getFirstErrorInformation(bindingResult));
			}
			
			return chatService.createHumanOperator(baseRequestModel);
		} catch (ApiException ex) {
			logger.error(ex.getErrorCode() + " : " + ex.getErrorCode(), ex);
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
	}

	@PostMapping(value = ApiConstants.PURIST_AGENT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object createPuristAgent(@RequestBody @Valid PuristAgentModel puristAgent, BindingResult bindingResult,
			HttpSession session) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			if (bindingResult.hasErrors()) {
				logger.debug(ApiConstants.BINDING_ERRORS);
				throw new ApiException(ErrorConstants.INVALIDDATA, Utility.getFirstErrorInformation(bindingResult));
			}

			chatService.createPuristAgent(puristAgent);

			responseObject.put(ApiConstants.API_RESPONSE, puristAgent);

		} catch (ApiException ex) {
			logger.error(ex.getErrorCode() + " : " + ex.getErrorCode(), ex);

			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));

			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject;
	}

	@PutMapping(value = ApiConstants.PURIST_AGENT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object updatePuristAgent(@RequestBody @Valid PuristAgentModel puristAgent, BindingResult bindingResult,
			HttpSession session) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			if (bindingResult.hasErrors()) {
				logger.debug(ApiConstants.BINDING_ERRORS);
				throw new ApiException(ErrorConstants.INVALIDDATA, Utility.getFirstErrorInformation(bindingResult));
			}

			chatService.updatePuristAgent(puristAgent);

		} catch (ApiException ex) {
			logger.error(ex.getErrorCode() + " : " + ex.getErrorCode(), ex);

			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));

			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject;
	}

	@DeleteMapping(value = ApiConstants.PURIST_AGENT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object deletePuristAgent(@RequestParam("puristAgentCode") String puristAgentCode) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {
			chatService.deletePuristAgent(puristAgentCode);

			responseObject.put(ApiConstants.API_RESPONSE, ApiConstants.SUCCESS);
		} catch (ApiException ex) {
			logger.error(ex.getErrorCode() + " : " + ex.getErrorCode(), ex);

			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));

			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
