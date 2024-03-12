import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

/*------------------------------------------------- TransactionTempValues -----
 |  class testemail
 |
 |  Purpose: This class contains the methods to send the email with attachments
 |                                                   
 *-------------------------------------------------------------------*/
public class testemail {
    DataSingleton data = DataSingleton.getInstance();

    public static void main(String[] args) {

    }

    public void sendemail(String filename) {
        String to = data.getEmail();
        String senderName = "Easy ATM";
        String senderEmail = "oopeasyatm@gmail.com";
        String password = "qhbxkemulplsythv";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, password);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail, senderName));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("Transaction History  ");
            BodyPart messageBodyPart = new MimeBodyPart();
            String messageContent = "<html><body><p style=\"font-size:20px;\">This is your file: "
                    + "</p></body></html>";

            Multipart multipart = new MimeMultipart();

            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart, messageContent);

            Transport.send(message);

            System.out.println("Email sent successfully.");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}