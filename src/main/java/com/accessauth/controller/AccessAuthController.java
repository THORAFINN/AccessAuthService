package com.accessauth.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accessauth.common.JwtUtil;
import com.accessauth.common.OtpService;
import com.accessauth.model.db.Login;
import com.accessauth.model.db.User;
import com.accessauth.modeldto.AuthRequest;
import com.accessauth.modeldto.LoginUserDto;
import com.accessauth.modeldto.ResponseDTO;
import com.accessauth.modeldto.SaveUserDto;
import com.accessauth.repository.UserRepository;
import com.accessauth.service.LoginService;

@RequestMapping(path = "/access" )
@RestController
public class AccessAuthController {
	
	private static Logger logger = LoggerFactory.getLogger(AccessAuthController.class); 
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private UserRepository userRepository; 
	
	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping(path = "/admin/login")
	public ResponseEntity<ResponseDTO> AdminLogin (@RequestBody LoginUserDto dto){
		logger.info("Admin login starts ..");
		logger.debug("request body {}",dto);
		try {
			ResponseDTO responseDTO = loginService.validateAdminLogin(dto);
			return new ResponseEntity<>(responseDTO,HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(path = "/admin/validate/{otp}")
	public ResponseEntity<ResponseDTO> validateOtp (@PathVariable Integer otp,@RequestHeader("Authorization") String authTokken) {
		logger.info("validate otp method start ...");
		logger.debug("input otp {}", otp);
		if (authTokken == null ||  authTokken.length()<=0) {
			return  new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		if (!authTokken.equals(OtpService.authToken)) {
			return  new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		ResponseDTO responseDto = loginService.validateOtp(otp);
		return new ResponseEntity<>(responseDto,HttpStatus.OK);
	}
	
	@PostMapping("/token/generate")
	public String generateJwtToken (@RequestBody AuthRequest authrequest) throws Exception {
		try {
		     authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authrequest.getUserName(), authrequest.getPassword()));
		} catch (Exception e) {
			throw e;
		}
		return jwtUtil.generateToken(authrequest.getUserName());
	}	
	
	@PostMapping("/token/refresh")
	public ResponseEntity<String> refreshToken (@RequestHeader("Authorization") String authTokken) {
		if (authTokken != null && authTokken.startsWith("Bearer ")) {
            authTokken = authTokken.substring(7);
        } else {
        	return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
		String refreshTokken = loginService.refreshTokken(authTokken);
		if (refreshTokken == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>(refreshTokken,HttpStatus.OK);
		}
	}
	
	@PostMapping("/validate/token")
	public ResponseEntity<Login> validateToken (@RequestHeader ("Authorization") String authTokken){
		if (authTokken != null && authTokken.startsWith("Bearer ")) {
            authTokken = authTokken.substring(7);
        } else {
        	return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
		Login login = loginService.validateAuthToken(authTokken);
		return new ResponseEntity<>(login,HttpStatus.OK);
		
	}
	
	@PostMapping("/user/save")
	public ResponseDTO saveUser (@RequestBody SaveUserDto dto) {
		Login login = new Login(dto.getName(), dto.getUserName(), encoder.encode(dto.getPassword()), new Date(), dto.getEmail());
		User user = new User(login, 2, new Date());
		this.userRepository.save(user);
		return new ResponseDTO(1);	
		
	}
	
	
}
