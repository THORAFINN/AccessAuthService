package com.accessauth.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class TemplateReader {
	
	@Value("classpath:/Template/emailTemplate.html") 
	private Resource emailTemplateResouce;
	
	private static Logger logger = LoggerFactory.getLogger(TemplateReader.class);
	
	/**
	 * 
	 * <p>
	 * @param otp
	 * @return
	 */
	public String getEmailBodyOfOtp (Integer otp) {
		try {
			InputStream inputStream = emailTemplateResouce.getInputStream();
		    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)); 
			String rawEmailBody = bufferedReader.lines().collect(Collectors.joining());
		    String emailBody = rawEmailBody.replace("{{otpValue}}", String.valueOf(otp));
		    return emailBody;
		} catch (Exception e) {
			logger.info("error occurred when reading email Template ");
			logger.error(e.getMessage(),e);
			return null;
		}
	}

}
