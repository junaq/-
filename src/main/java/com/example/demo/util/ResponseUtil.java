package com.example.demo.util;

import lombok.Data;

@Data
public class ResponseUtil {

	// 状态码
	public final static int STATUS_CODE_SUCCESS = 200;
	public final static int STATUS_CODE_FAILURE = 300;
	public final static int STATUS_CODE_TIMEOUT = 301;
	public final static int STATUS_CODE_FORBIDDEN = 403;

	private int statusCode;
	private String meassage;
	private String token;

	public ResponseUtil(int statusCode, String meassage) {
		super();
		this.statusCode = statusCode;
		this.meassage = meassage;
	}

	public static ResponseUtil ok(String meassage) {

		return new ResponseUtil(STATUS_CODE_SUCCESS, meassage);
	}

	public static ResponseUtil error(String meassage) {

		return new ResponseUtil(STATUS_CODE_FAILURE, meassage);
	}
	
	
	public ResponseUtil setToken(String token){
		this.token=token;
		return this;
	}
	
	public String toString (){
		return "{statusCode:"+this.statusCode+",meassage:"+this.meassage+",token:"+this.token+"}";
	}

}
