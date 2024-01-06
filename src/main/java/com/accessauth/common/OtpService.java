package com.accessauth.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component 
public class OtpService {
	
	public static Map<Integer, Integer> otpMap = new HashMap<>();
	
	public static Long optGenerateTime; 
	
	public static String authToken;
	
	public Integer getOtp () {
		
		Random random = new Random();
		int otpValue = random.nextInt(999999);
		otpMap.put(otpValue, 90);
		optGenerateTime = System.currentTimeMillis();
		return otpValue;
	}
	
	public void invalidateOtp (int otp) {
		otpMap.remove(otp);
		optGenerateTime = null;
		authToken = null;
	}
	
}
