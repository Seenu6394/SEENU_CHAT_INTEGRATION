package com.scs.config;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.scs.controller.PurishChatController;
import com.scs.exception.ApiException;
import com.scs.service.ChatService;
import com.scs.util.ErrorConstants;
import com.scs.util.Utility;

@Component
public class StartupApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger logger = Logger.getLogger(StartupApplicationListener.class);

	@Autowired
	private ChatService chatService;

	@Autowired
	private Environment env;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		logger.info("-------------creating room an operator---------------");
		try {
			if (env.getProperty("scs.runmode").equals("PROD")) {
			
				
				chatService.createRoomAndOperator();
			}
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));

		}
	}
}