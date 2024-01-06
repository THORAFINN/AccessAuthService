package com.accessauth.modeldto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonInclude(Include.NON_NULL)
public class ResponseDTO {
	
	@JsonProperty(value = "code")
	private Integer code;
	
	@JsonProperty(value = "error_code")
	private String errorCode;
	
	@JsonProperty(value = "error_message")
	private String errorMessage;
	
	@JsonProperty(value = "authorization_password")
	private String authorizationPassword;

	public String getAuthorizationPassword() {
		return authorizationPassword;
	}

	public void setAuthorizationPassword(String authorizationPassword) {
		this.authorizationPassword = authorizationPassword;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public ResponseDTO(Integer code, String errorCode, String errorMessage) {
		this.code = code;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	
	public ResponseDTO(Integer code, String authorizationPassword) {
		this.code = code;
		this.authorizationPassword = authorizationPassword;
	}
	
	public ResponseDTO (Integer code) {
		this.code = code;
	}

	public ResponseDTO() {
		super();
	}
	
	
	
	

}
