package model.logic;

import java.util.Date;
import java.util.Properties;

import javax.ejb.Stateless;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Stateless
public class SendStringViaEmailLogic {
	public boolean execute(String sendString, String gakusekiBango) {
		// 研究用に作成したGmailアカウントからメールを送信する
		final String fromAddress = "qandasystem@gmail.com";
		final String fromHost = "smtp.gmail.com";
		String toAddress = gakusekiBango + "@hogehoge.jp";

		// <!-- プロパティの設定
		Properties props = System.getProperties();
		props.setProperty("mail.smtp.host", fromHost);
		props.setProperty("mail.smtp.port", "587");
        props.setProperty("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        //タイムアウトも一応
        props.setProperty("mail.smtp.connectiontimeout", "5000");
        props.setProperty("mail.smtp.timeout", "5000");
		//サーバとの会話内容を出力してくれる！
        props.setProperty("mail.smtp.debug", "true");
        // -->

      //パスワード認証つきのセッションを作成
        Session session
        	= Session.getInstance( props, new PasswordAuthenticatior("qandasystem","qanda") );

		try {
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(fromAddress));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress, false));
			msg.setSubject("QandA System Login Password Notification", "utf-8");
			msg.setSentDate(new Date());
			msg.setText("Please login using the following password : " + sendString);
			Transport.send(msg);

		} catch (AddressException e) {
			e.printStackTrace();
			return false;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) { // ひどいコード
			e.printStackTrace();
			return false;
		}

		return true;
	}
}

//パスワードを保持するクラスを作る必要がある？
class PasswordAuthenticatior extends Authenticator{
      private String username;
      private String password;

      PasswordAuthenticatior(String username, String password) {
        this.username = username;
        this.password = password;
      }

      @Override
      public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }
}
