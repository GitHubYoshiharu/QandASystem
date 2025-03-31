package bb;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

//継承による使用を前提とするため、抽象クラスとした
public abstract class AbstractBb implements Serializable {
	public abstract void init();

	//<!-- メッセージを作成しキューに入れるメソッド
	// ノーマルなタイプ
	public void facesMessage(String s) {
		FacesMessage msg = new FacesMessage(s);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	// メッセージの種類を指定できるタイプ
	/* ****************************************
	 * FacesMessage.SEVERITY_FATAL ... 致命的エラー(4)
	 * FacesMessage.SEVERITY_ERROR ... エラー(3)
	 * FacesMessage.SEVERITY_WARN ... 警告(2)
	 * FacesMessage.SEVERITY_INFO ... 情報(1)
	 *****************************************/
	public void facesMessage(FacesMessage.Severity severity, String s) {
		FacesMessage msg = new FacesMessage(s);
		msg.setSeverity(severity);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	// -->

	// サーブレット環境を取得する
	public ExternalContext getServlet() {
		return FacesContext.getCurrentInstance().getExternalContext();
	}
	// リクエストオブジェクトを取得する
	public HttpServletRequest getRequest() {
		return (HttpServletRequest) getServlet().getRequest();
	}
	// ログインしているユーザーのIDを返す
	public String getUserId() {
		return getRequest().getRemoteUser();
	}
}
