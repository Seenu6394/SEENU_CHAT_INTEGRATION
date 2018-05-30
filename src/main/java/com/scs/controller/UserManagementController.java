package com.scs.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.scs.exception.ApiException;
import com.scs.util.ApiConstants;
import com.scs.util.ErrorConstants;
import com.scs.util.SessionUtil;
import com.scs.util.StorageConstants;
import com.scs.util.Utility;

@RestController
@RequestMapping(ApiConstants.API)

public class UserManagementController {

	private static final Logger logger = Logger.getLogger(UserManagementController.class);
	private static final String  RESPONSE = "response";
	

	@Autowired
	private MessageSource messageSource;

	
	@GetMapping(value = ApiConstants.LOGIN_SUCCESS, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object login(HttpSession session) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			responseObject.put("userName", authentication.getName());
			for (GrantedAuthority authority : authentication.getAuthorities()) {
				responseObject.put("userRole", authority.getAuthority());

			}

			
			SessionUtil.setValue(session, StorageConstants.USER, responseObject);

		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));

			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject;
	}

	@PostMapping(value = ApiConstants.LOGIN_FAILURE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object loginFailure(HttpServletRequest request, HttpSession session) throws ApiException {

		Object status = null;

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

				responseObject.put("INVALID_PASSWORD", "Username or password is invalid.");

		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject;
	}
	
	@GetMapping(value = ApiConstants.LOGOUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object logout(HttpSession session) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();
		try {

			session.invalidate();
			responseObject.put(RESPONSE, ApiConstants.SUCCESS);
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject;
	}

}
