package com.scm.services;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;

@Component
public class SessionHelper {

	public void removeMessageFromSession() {
		try {
		
			// RequestContextHolder hold the current session
			HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
									.getRequest().getSession();
			session.removeAttribute("message");
			System.out.println("remove attirvuge call");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
