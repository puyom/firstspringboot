package com.springboot.firstspringboot.common;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;
import java.util.Random;

@Component
//@PropertySource(value = "classpath*:config/mail.properties")
public class MailUtils {


	/**
	 * 发送邮件
	 * @param message
	 * @return 为""发送失败
	 */
	public String sendEmail(Message message){
		try {
			Transport.send(message);
			return "1";
		} catch (MessagingException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 创建邮件连接session
	 * @return
	 */
	public Session getSession(){
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "SMTP");
		props.setProperty("mail.host", "smtp.126.com");
		props.setProperty("mail.smtp.auth", "true");
		/*再linux上发送邮件需要加property--ssl*/
		props.setProperty("mail.smtp.ssl.enable", "true");

		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("lanpu9007@126.com", "lanpukeji9007");//密码
			}
		};
		return Session.getInstance(props, auth);
	}

	/**
	 * 发送附件的(pdf)
	 * @param session getSession方法获得
	 * @param path    附件地址
	 * @param email   收件人邮箱
	 * @return
	 * @throws Exception
	 */
	public MimeMessage createImageMail(Session session, String path, String email) throws Exception {
		//创建邮件
		MimeMessage message = new MimeMessage(session);
		// 设置邮件的基本信息
		//发件人
		message.setFrom(new InternetAddress("lanpu9007@126.com","Strain","UTF-8"));
		//收件人
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
		//邮件标题
		message.setSubject("结果邮件");

		// 准备邮件数据
		// 准备邮件正文数据
		MimeBodyPart text = new MimeBodyPart();
		text.setContent("菌株结果", "text/html;charset=UTF-8");
		// 准备图片数据
		MimeBodyPart image = new MimeBodyPart();
		DataHandler dh = new DataHandler(new FileDataSource(path));
		image.setDataHandler(dh);
		//TODO 发送图片名称后缀需要加
		image.setFileName("result.zip");


		// 描述数据关系
		MimeMultipart mm = new MimeMultipart();
		mm.addBodyPart(text);
		mm.addBodyPart(image);

		message.setContent(mm);
		message.saveChanges();
		//将创建好的邮件写入到盘符以文件的形式进行保存
//		message.writeTo(new FileOutputStream("/usr/local/result/ImageMail.eml"));
//		message.writeTo(new FileOutputStream("D:\\ImageMail.eml"));
		//返回创建好的邮件
		return message;
	}

	//菌株重新命名后发送邮件
	public MimeMessage createImageMail2(Session session, String path, String email) throws Exception {
		//创建邮件
		MimeMessage message = new MimeMessage(session);
		// 设置邮件的基本信息
		//发件人
		message.setFrom(new InternetAddress("lanpu9007@126.com","Strain and Person","UTF-8"));
		//收件人
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
		//邮件标题
		message.setSubject("结果邮件");

		// 准备邮件数据
		// 准备邮件正文数据
		MimeBodyPart text = new MimeBodyPart();
		text.setContent("注册信息", "text/html;charset=UTF-8");
		// 准备图片数据
		MimeBodyPart image = new MimeBodyPart();
		DataHandler dh = new DataHandler(new FileDataSource(path));
		image.setDataHandler(dh);
		//TODO 发送图片名称后缀需要加
		image.setFileName("result.text");
		// 描述数据关系
		MimeMultipart mm = new MimeMultipart();
		mm.addBodyPart(text);
		mm.addBodyPart(image);

		message.setContent(mm);
		message.saveChanges();
		//将创建好的邮件写入到盘符以文件的形式进行保存
//		message.writeTo(new FileOutputStream("/usr/local/result/ImageMail.eml"));
//		message.writeTo(new FileOutputStream("D:\\ImageMail.eml"));
		//返回创建好的邮件
		return message;
	}

	/**
	 * 用于生成验证码的方法
	 * @return
	 */
	public  int getCode(){
		Random random = new Random();
		int max = 999999;
		int min = 100000;
		int code = random.nextInt(max);
		if(code<min){
			code+= min;
		}
		return code;
	}

	/**
	 * 发送验证码的
	 * @param session
	 * @param email 收件人邮箱
	 * @param code  验证码
	 * @return
	 * @throws Exception
	 */

	public MimeMessage createImageMail1(Session session, String email, int code) throws Exception {
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress("lanpu9007@126.com","邮件","UTF-8")); // 设置发送者
		message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(email));
		message.setSubject("验证邮件");


		String emailMsg="您的验证码是<b>"+code+"</b>请尽快输入";
		message.setContent(emailMsg, "text/html;charset=utf-8");
		return message;
	}

}
