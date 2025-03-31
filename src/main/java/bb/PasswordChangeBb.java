package bb;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import entity.User;
import lombok.Getter;
import lombok.Setter;
import model.db.UserDb;
import model.logic.SHA256EncodeLogic;

@Named
@RequestScoped
public class PasswordChangeBb extends AbstractBb implements Serializable {
	@Getter @Setter
	@NotNull(message="未入力") @Pattern(regexp="[!\"#$%&'()*+,-./:;<=>?@[¥]^_`{|}~a-zA-Z0-9]{8,}", message="正規表現{regexp}に一致しません。")
	private String changedPassword_first;

	@Getter @Setter
	@NotNull(message="未入力") @Pattern(regexp="[!\"#$%&'()*+,-./:;<=>?@[¥]^_`{|}~a-zA-Z0-9]{8,}", message="正規表現{regexp}に一致しません。")
	private String changedPassword_second;

	@EJB private UserDb userDb;
	@EJB private SHA256EncodeLogic sHA256EncodeLogic;

	@Override
	public void init() {}

	public String passwordChange() {
		if ( changedPassword_first.equals(changedPassword_second) ) {
			String encodedPassword = sHA256EncodeLogic.execute(changedPassword_first);
			User user = userDb.select( this.getUserId() );
			user.setPassword(encodedPassword);
			userDb.update(user);
			facesMessage("パスワードを変更しました。");
			return "/authed/passwordChange.xhtml";
		} else {
			facesMessage(FacesMessage.SEVERITY_ERROR, "１回目と２回目の入力パスワードが異なっています。");
			return "/authed/passwordChange.xhtml";
		}
	}
}
