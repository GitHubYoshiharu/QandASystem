package bb;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import entity.User;
import lombok.Getter;
import lombok.Setter;
import model.db.UserDb;
import model.logic.DummySendStringViaEmailLogic;
import model.logic.GenerateRandomPasswordLogic;
import model.logic.SHA256EncodeLogic;

@Named
@RequestScoped
public class LoginBb extends AbstractBb implements Serializable{
	@Getter @Setter
	@NotNull(message="未入力")
	private String userId_login;

	@Getter @Setter
	@NotNull(message="未入力") @Pattern(regexp="[!\"#$%&'()*+,-./:;<=>?@[¥]^_`{|}~a-zA-Z0-9]{8,}", message="正規表現{regexp}に一致しません。")
	private String password;

	@Getter @Setter
	@NotNull(message="未入力") @Pattern(regexp="^[a-z][0-9]{7}", message="正規表現{regexp}に一致しません。")
	private String userId_regist;

	@EJB private UserDb userDb;
	@EJB private GenerateRandomPasswordLogic generateRandomPasswordLogic;
	@EJB private SHA256EncodeLogic sHA256EncodeLogic;
	//@EJB private SendStringViaEmailLogic sendStringViaEmailLogic;
	@EJB private DummySendStringViaEmailLogic dummySendStringViaEmailLogic;

	@Override
	public void init() {}

	// ログイン処理
	public String login() {
		// ログイン済みの時は、ログイン後のページに
		// 飛ばしてあげないと、ログイン画面から出られなくなる
		if ( !(this.getUserId() == null) ) {
			return "/authed/questionSearch.xhtml";
		}
		HttpServletRequest request = getRequest();
		try {
			request.login(userId_login, password);
		} catch(ServletException e) {
			facesMessage(FacesMessage.SEVERITY_ERROR, "ログインに失敗しました。");
			return "/login.xhtml";
		}
		return "/authed/questionSearch.xhtml";
	}

	// ユーザ登録処理
	public String regist() {
		if ( userDb.canRegist(userId_regist) ) {
			String randomPassword = generateRandomPasswordLogic.execute();
			String encodedPassword = sHA256EncodeLogic.execute(randomPassword);
			// メールでランダムパスワードを通知する
			// メール送信成功時のみユーザ登録処理を継続させたいので、非同期メソッドにはしていない
			// ※メールを送信しないロジックとすり替えておく
			if ( dummySendStringViaEmailLogic.execute( randomPassword, userId_regist ) ) {
				User user = new User();
				user.setUserId(userId_regist);
				user.setPassword(encodedPassword);
				userDb.update(user);
				facesMessage("password = " + randomPassword); // DEBUG
				facesMessage("パスワードが記載されたメールを送信しました。そのパスワードでログインして下さい。");
			} else {
				facesMessage(FacesMessage.SEVERITY_ERROR, "メールの送信に失敗しました。ユーザ登録は中断されました。");
			}
		} else {
			facesMessage(FacesMessage.SEVERITY_FATAL, "既にユーザ登録されているか、あなたにはユーザ登録権限がありません。パスワードが記載されたメールが届いていないか確認してください。");
		}

		return "/login.xhtml";
	}
}
