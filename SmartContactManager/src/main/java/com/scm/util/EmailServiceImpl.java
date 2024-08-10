package com.scm.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailServices {

	// if we DOES NOT want to create a constructor of
	// EmailServiceImpl(JavaMailSender mailSender)
	// then use @Autowired on JavaMailSender object

	private JavaMailSender mailSender;

	public EmailServiceImpl(JavaMailSender mailSender) {
		super();
		this.mailSender = mailSender;
	}

	private Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

	@Override
	public boolean sendEmail(String to, String subject, String message) {
		boolean status = false;
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom("arifjamal24@outlook.com");
		simpleMailMessage.setTo(to);
		simpleMailMessage.setSubject(subject);
		simpleMailMessage.setText(message);

		try {
			mailSender.send(simpleMailMessage);
			logger.info("Email has been sent...");
			status = true;
			return status;
		} catch (Exception e) {
			return status;
		}
	}

	@Override
	public void sendEmail(String[] to, String subject, String message) {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom("arifjamal24@outlook.com");
		simpleMailMessage.setTo(to);
		simpleMailMessage.setSubject(subject);
		simpleMailMessage.setText(message);

		mailSender.send(simpleMailMessage);
		logger.info("bulk Email has been sent...");

	}

	@Override
	public boolean sendEmailWithHtml(String to, String subject, String html) {
		boolean status = false;
		MimeMessage mime = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(mime, true, "UTF-8");
			helper.setTo(to);
			helper.setFrom("arifjamal24@outlook.com");
			helper.setSubject(subject);
			helper.setText(html, true);

			mailSender.send(mime);
			status = true;
			logger.info("html content Email has been sent...");
			return status;
		} catch (MessagingException e) {
			e.printStackTrace();
			return status;
		}

	}

	@Override
	public void sendEmailWithFile(String to, String subject, String message, File file) {
		MimeMessage mime = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(mime, true, "UTF-8");
			helper.setTo(to);
			helper.setFrom("arifjamal24@outlook.com");
			helper.setSubject(subject);
			helper.setText(message);
			FileSystemResource sys = new FileSystemResource(file);
			helper.addAttachment(sys.getFilename(), file);

			mailSender.send(mime);
			logger.info("html content Email has been sent...");

		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void sendEmailWithFile(String to, String subject, String message, InputStream is) {
		MimeMessage mime = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(mime, true, "UTF-8");
			helper.setTo(to);
			helper.setFrom("arifjamal24@outlook.com");
			helper.setSubject(subject);
			helper.setText(message);
			// File file = new File("/src/main/resources/email/testPDF.pdf");
			File file = new File("testPDF.pdf");
			Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
			FileSystemResource sys = new FileSystemResource(file);
			helper.addAttachment(sys.getFilename(), file);

			mailSender.send(mime);
			logger.info("html content Email has been sent...");

		} catch (MessagingException | IOException e) {
			e.printStackTrace();
		}

	}

}
