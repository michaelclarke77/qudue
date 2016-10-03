package processing;

import java.util.Properties;
import java.io.UnsupportedEncodingException;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import databaseConnection.UpdateUsers;
import javafx.stage.Stage;

/**
 * Class to send an Email from server to user defined email address
 */
public class SendEmail {
	private final String senderEmail = "qudueemail@gmail.com";
	private final String senderName = "QuDue Services";
	private final String senderPassword = "7RXQZ123";

	String subject = "Forgot your Password?";
	String password;

	String messageHeader = "Hi!\n\n"
			+ "This is an email from QuDue, the essay writing tool. If you didn't request this email please ignore it."
			+ "\n\nIf you did request this email, then it seems that you have forgotten your password but don't worry here is a brand new one for you : \n\n\t";

	String messageFooter = "\n\nYou can change this password once you have logged in by going to Account page.\n\n"
			+ "All the best, \n\n The QuDue Team";

	public SendEmail() {
	}

	public boolean send(Stage stage, String receiverEmail) {
		
		boolean flag = true;
		//create random password
		password = GeneratePassword.generate();
		// The email message body
		String messageBody = messageHeader + password + messageFooter;
		// Calls createSessionObject
		Session session = createSessionObject();
		try {
			// the message to be sent
			javax.mail.Message message = createMessage(receiverEmail, subject, messageBody, session);
			// send the message
			Transport.send(message);
			//display confirmation
			System.out.println("Email Sent");
			InformationDialog.display(stage, "Email", "Email Sent.\nPlease check your email to retrieve the password.");
			// catch any email sending exceptions
		} catch (MessagingException | UnsupportedEncodingException exception) {
			flag = false;
			InformationDialog.display(stage, "Error",
					"There was a problem sending the email. Please try again.");
			exception.printStackTrace();
		}
		
		if (flag == true){
			//Make sure the DB is updated with this new password, but only if the email sends
			UpdateUsers updateUsers = new UpdateUsers();
			updateUsers.updatePassword(password, receiverEmail);
		}
		
		return flag;
		
	}

	/*
	 * Creates the email to be sent
	 */
	private javax.mail.Message createMessage(String email, String subject, String messageBody, Session session)
			throws MessagingException, UnsupportedEncodingException {
		javax.mail.Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(senderEmail, senderName));
		message.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(email, email));
		message.setSubject(subject);
		message.setText(messageBody);
		return message;
	}

	/*
	 * This method authenticates the email sender
	 */
	private Session createSessionObject() {
		// the properties of the email server
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
		return Session.getInstance(properties, new javax.mail.Authenticator() {
			// the validation for the account of the email sender
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(senderEmail, senderPassword);
			}
		});
	}

}
