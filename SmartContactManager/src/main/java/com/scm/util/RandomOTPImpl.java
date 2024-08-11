package com.scm.util;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class RandomOTPImpl implements RandomOTP {

	@Override
	public int otp() {
		Random r = new Random(System.currentTimeMillis());
		int otp = (1 + r.nextInt(9)) * 100000 + r.nextInt(100000);	
		return otp;
	}

}
