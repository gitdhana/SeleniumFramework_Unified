package com.ufw.Utilities;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.ufw.Base.AutoConfigConstants;



public class EmailMonitoring
{
	//public static void sendMail(String mailServer, String from,String username, String password,String port, String[] to, String subject, String messageBody, String attachmentPath, String attachmentName) throws MessagingException, AddressException
	public void sendMail(String mailServer, String from, String[] to, String subject, String messageBody, String attachmentPath, String attachmentName, Map<String, Integer> testResults) throws MessagingException, AddressException
	{
		boolean debug = false;
		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		//		props.put("mail.smtp.EnableSSL.enable","true");
		props.put("mail.smtp.auth", "true");

		props.put("mail.smtp.host", mailServer); 
		props.put("mail.debug", "true");

		//	     props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");   
		props.setProperty("mail.smtp.socketFactory.fallback", "false");   
		props.setProperty("mail.smtp.port", "465");   
		props.setProperty("mail.smtp.socketFactory.port", "465"); 


		Authenticator auth = new SMTPAuthenticator();
		Session session = Session.getDefaultInstance(props, auth);

		session.setDebug(debug);

		try
		{		

			Transport bus = session.getTransport("smtp");
			bus.connect();
			Message message = new MimeMessage(session);

			//X-Priority values are generally numbers like 1 (for highest priority), 3 (normal) and 5 (lowest).

			message.addHeader("X-Priority", "1");
			message.setFrom(new InternetAddress(from));
			InternetAddress[] addressTo = new InternetAddress[to.length];
			for (int i = 0; i < to.length; i++)
				addressTo[i] = new InternetAddress(to[i]);
			message.setRecipients(Message.RecipientType .TO, addressTo);
			message.setSubject(subject);


			BodyPart body = new MimeBodyPart();
			
			
			//=====  Set values to the place holders for sending HTML email template  =====
			
			Map<String, String> input = new HashMap<String, String>();
			input.put("Unified Framework", AutoConfigConstants.PROJECT_NAME);
			input.put("Content In", "English");
			input.put("TotalTestsExecuted", testResults.get("totalTestsExec").toString());
			input.put("TotalTestsPassed", testResults.get("totalPassedTests").toString());
			input.put("TotalTestsFailed", testResults.get("totalFailedTests").toString());
			input.put("TotalTestsSkipped", testResults.get("totalSkippedTests").toString());
			
			//=====  Read the HTML and put HTML in BodyPart  =====
			
			String htmlText = readEmailFromHtml(messageBody, input);
			body.setContent(htmlText, "text/html");
			
			//=====  Sending result file as an attachment  =====
			
			MimeBodyPart attachment = new MimeBodyPart();
			DataSource source = new FileDataSource(attachmentPath);
			attachment.setDataHandler(new DataHandler(source));
			attachment.setFileName(attachmentName);
			
			File att = new File(new File(attachmentPath), attachmentName);
			attachment.attachFile(att);
			
			MimeMultipart multipart = new MimeMultipart();
			multipart.addBodyPart(body);
			multipart.addBodyPart(attachment);
			
			
			//=====  Sending CAR LOGO as an inline image attachment  =====
			
			multipart.addBodyPart(sendInlineImage(AutoConfigConstants.CAR_LOGO_PATH, "<carImageSource>"));
			
			//=====  Sending COMPANY logo as an inline image attachment  =====
			
			multipart.addBodyPart(sendInlineImage(AutoConfigConstants.COMPANY_LOGO_PATH, "<imageSource>"));
			
			//=====  Putting everything together and sending email  =====
			
			message.setContent(multipart);
			Transport.send(message);
			System.out.println("Successfully Sent mail to All Users");
			bus.close();
			
		}
		catch (MessagingException mex)
		{
			mex.printStackTrace();
		}	
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
	} 

	private class SMTPAuthenticator extends javax.mail.Authenticator
	{

		public PasswordAuthentication getPasswordAuthentication()
		{
			String username = AutoConfigConstants.FROM;
			String password = AutoConfigConstants.PASSWORD;
			return new PasswordAuthentication(username, password);
		}
	}
	
	protected String readEmailFromHtml(String filePath, Map<String, String> input)
	{
		String msg = readContentFromFile(filePath);
		try
		{
			Set<Entry<String, String>> entries = input.entrySet();
			for(Map.Entry<String, String> entry : entries)
			{
				msg = msg.replace(entry.getKey().trim(), entry.getValue().trim());
			}
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		return msg;
	}
	
	//=====  Method to read HTML from file as a String  =====
	
	private String readContentFromFile(String fileName)
	{
		StringBuffer contents = new StringBuffer();
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			try
			{
				String line = null;
				while((line = reader.readLine()) != null)
				{
					contents.append(line);
					contents.append(System.getProperty("line.separator"));
				}
			}
			finally
			{
				reader.close();
			}
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		return contents.toString();
	}
	
	
	//=====  Method to add image source in the attachment email  =====
	
	private MimeBodyPart sendInlineImage(String imageSource, String contentIDValue)
	{
		MimeBodyPart attachment = new MimeBodyPart();
		try
		{
			DataSource source = new FileDataSource(imageSource);
			attachment.setDataHandler(new DataHandler(source));
			attachment.setHeader("Content-ID", contentIDValue);
		}
		catch(MessagingException mex)
		{
			mex.printStackTrace();
		}
		return attachment;
	}
	
	
}
