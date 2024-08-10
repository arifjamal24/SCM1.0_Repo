package com.scm.util;

import java.io.File;
import java.io.InputStream;

public interface EmailServices {

	// send email to single person
	boolean sendEmail(String to, String subject, String message);
	
	// send email to multiple person
	void sendEmail(String[] to, String subject, String message);
	
	// send email with html
	boolean sendEmailWithHtml(String to, String subject, String message);
	
	// send email with file
	void sendEmailWithFile(String to, String subject, String message, File file);
	
	// send email using InputOutput stream when we get our file direct from database or dynamic data file
	void sendEmailWithFile(String to, String subject, String message, InputStream file);
}
