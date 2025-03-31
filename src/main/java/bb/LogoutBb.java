package bb;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Named
@RequestScoped
public class LogoutBb extends AbstractBb {

	@Override
	public void init() {}

	// ログアウト処理
	public String logout() {
		getServlet().invalidateSession(); // セッションを終了する
		HttpServletRequest request = getRequest();
		try {
			request.logout();
		} catch(ServletException e) {}
		return "/login.xhtml?faces-redirect=true";
	}
}
