package com.accessauth.common;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

@Component
public class CryptoUtil {
	
	public  String Base64Encode(String dataToEncode) {
	  byte[] encodeBase64 = Base64.encodeBase64(dataToEncode.getBytes());
	  return new String(encodeBase64);
	}
	
	public String Base64Decode (String dataToDecode) {
		byte[] decodeBase64 = Base64.decodeBase64(dataToDecode);
		return new  String(decodeBase64);
	}

}
