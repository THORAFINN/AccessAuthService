package com.accessauth.service;

import java.util.StringTokenizer;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@EnableAutoConfiguration
public class EmailService {

	@Autowired
	private JavaMailSender javaMailSender;
	
	@Value("${spring.mail.from}")
	private String senderEmail;
	
	private static Logger logger = LoggerFactory.getLogger(EmailService.class);
	
	
	public void SendEmail (String to, String subject, String body, boolean isHtml, String toName) {
		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
			
			mimeMessageHelper.setFrom(senderEmail);
			
			StringTokenizer stringTokenizer = new StringTokenizer(to,",");
			while(stringTokenizer.hasMoreTokens()) {
				mimeMessageHelper.setTo(stringTokenizer.nextToken()); 
			}
			
			mimeMessageHelper.setSubject(subject);
			mimeMessageHelper.setText(body, isHtml);
			
			javaMailSender.send(mimeMessage);
			
		} catch (Exception e) {
			logger.info("Erroir occur when sending email to {}", to);
			logger.error(e.getMessage(), e);
		}
	}
	
}
