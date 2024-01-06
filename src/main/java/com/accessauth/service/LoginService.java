package com.accessauth.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.accessauth.common.CryptoUtil;
import com.accessauth.common.JwtUtil;
import com.accessauth.common.OtpService;
import com.accessauth.model.db.Login;
import com.accessauth.model.db.User;
import com.accessauth.modeldto.LoginUserDto;
import com.accessauth.modeldto.ResponseDTO;
import com.accessauth.repository.LoginRepository;
import com.accessauth.repository.UserRepository;

@Service
public class LoginService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private LoginRepository loginRepo;
	
	@Autowired
	private OtpService otpService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private TemplateReader templateReader;
	
	@Autowired
	private CryptoUtil cryptoUtil;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private CustomUserDetailService userDetailService;
	
	public ResponseDTO validateAdminLogin (LoginUserDto dto) {
		
		ResponseDTO responseDto = validteRequestBody(dto);
		
		if (responseDto != null) {
			return responseDto;
		}
		
		Optional<Login> optionalLogin = this.loginRepo.findByUsernameAndPassword(dto.getUserName(), dto.getPassword());
		
		if (!optionalLogin.isPresent()) {
			return new ResponseDTO(2, "AAE103", "Invalid userName and password ");
		}
		
		Login login = optionalLogin.get();
		
		Optional<User> optionalUser = this.userRepo.findByLogin_idAndRid(login.getId(), 1);
		
		if (!optionalUser.isPresent()) {
			return new ResponseDTO(2, "AAE104", "Invalid login attempt, not admin user");
		}
		
		Integer otp = otpService.getOtp();
		String emailBody = templateReader.getEmailBodyOfOtp(otp);
		String emailSubject = "StreamIt - Admin Login Otp";
		emailService.SendEmail("roshan20022000@gmail.com", emailSubject, emailBody, true, login.getName());
		String strToEncode = otp+dto.getUserName();
		String authorizationTokken = cryptoUtil.Base64Encode(strToEncode);
		OtpService.authToken = authorizationTokken;
		return new ResponseDTO(1, authorizationTokken);
	}
	
	
	
	private ResponseDTO validteRequestBody (LoginUserDto dto) {
		String userName = dto.getUserName();
		String password = dto.getPassword();
		
		if (userName == null || userName.length()<=0) {
			return new ResponseDTO(2, "AAE101", "Invalid or missing value for admin username");
		}
		
		if (password == null || userName.length()<=0) {
			return new ResponseDTO(2, "AAE102", "Invalid or missing value for admin password");
		}
		return null;
	}
	
	public ResponseDTO validateOtp (Integer otp) {
		
		if (!OtpService.otpMap.containsKey(otp)) {
			return new ResponseDTO(2, "AAE105", "Invalid otp");
		}
		
		Integer expiryTime = OtpService.otpMap.get(otp);
		
		Long currentTimeMillis = System.currentTimeMillis();
		
		if ((currentTimeMillis.intValue() - expiryTime)>OtpService.optGenerateTime){
			return new ResponseDTO(2, "AAE106", "otp expired please regenrate a new otp");
		}
		
		otpService.invalidateOtp(otp);
		
		return new ResponseDTO(1);
	}
	
	public String refreshTokken (String authToken) {
		String username = jwtUtil.extractUsername(authToken);
		if (username == null) {
			return null;
		}
		UserDetails userDetails = userDetailService.loadUserByUsername(username);
		if (jwtUtil.validateToken(authToken, userDetails)) {
			return jwtUtil.generateToken(username);
		}
		return null;
	}
	
	
	public Login validateAuthToken (String authToken) {
		String extractUsername = jwtUtil.extractUsername(authToken);
		Optional<Login> optionalUserName = loginRepo.findByUsername(extractUsername);
		if (optionalUserName.isPresent()) {
			Login login = optionalUserName.get();
			return login;
		}
        return null;
	}

}
