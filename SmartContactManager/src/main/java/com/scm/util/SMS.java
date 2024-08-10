package com.scm.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SMS {

	public boolean sendSMS(String phone, String message) {
		boolean status = false;
		
		// Fast2SMS gateway

		try {
			
			final String authorization = "QFas4vG7CzkOKr1nVDdNfIL5RMYWb0pjX9BuxlAPhmgqtZTwSJUsBm93rqQp7jv8xJFMlGyfke2ThCSa";
			final String sender_id 	   = "FSTSMS";
			final String route 		   = "dlt";
			
			
			message = URLEncoder.encode(message,"UTF-8");
			final String url = "https://www.fast2sms.com/dev/bulkV2?authorization="+authorization+
							   "&sender_id="+sender_id+"&message="+message+"&route="+route+
							   "&numbers="+phone;

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return status;
	}
	
}
