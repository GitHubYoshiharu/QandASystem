package bb;

import static java.util.Comparator.*;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import entity.FavHistory;
import entity.PostHistory;
import entity.QuestionFavUser;
import entity.ReplyNotification;
import entity.SolvedHistory;
import entity.User;
import lombok.Getter;
import lombok.Setter;
import model.db.UserDb;

@Named
@RequestScoped
public class MypageBb extends AbstractBb {
	// ログインユーザ
	private User user;

	// 表示する用
	@Getter @Setter
	private List<ReplyNotification> viewReplyNotificationList;
	@Getter @Setter
	private List<PostHistory> viewPostHistoryList;
	@Getter @Setter
	private List<FavHistory> viewFavHistoryList;
	@Getter @Setter
	private List<SolvedHistory> viewSolvedHistoryList;

	@EJB private UserDb userDb;

	@Override
	@PostConstruct
	public void init() {
		user = userDb.select(this.getUserId());
		viewReplyNotificationList = user.getReplyNotifications();
		viewPostHistoryList          = user.getPostHistories();
		viewFavHistoryList           = user.getFavHistories();
		viewSolvedHistoryList      = user.getSolvedHistories();

		// このページで表示やパラメータにつかう値をセットする
		for(ReplyNotification replyNotification: viewReplyNotificationList) {
			setDisplayValue(replyNotification);
		}
		for(PostHistory postHistory: viewPostHistoryList) {
			setDisplayValue(postHistory);
		}
		for(FavHistory favHistory: viewFavHistoryList) {
			setDisplayValue(favHistory);
		}
		for(SolvedHistory solvedHistory: viewSolvedHistoryList) {
			setDisplayValue(solvedHistory);
		}

		// 通知年月日時刻降順にソートする
		viewReplyNotificationList.sort( comparing( x -> x.getNotifiedTime(), reverseOrder() ) );
		viewPostHistoryList.sort( comparing( x -> x.getNotifiedTime(), reverseOrder() ) );
		viewFavHistoryList.sort( comparing( x -> x.getNotifiedTime(), reverseOrder() ) );
		// TODO: viewSolvedHistoryList.sort( comparing( x -> x.getNotifiedTime(), reverseOrder() ) );
	}

	public void setDisplayValue(ReplyNotification replyNotification) {
		StringBuilder setMessage = new StringBuilder("あなたが");
		switch( replyNotification.getReplyNotificationClassification() ) {
			case "1": // 投稿した質問に対する回答
				// セットするメッセージを作成
				setMessage.append("投稿した質問に対して");
				setMessage.append(replyNotification.getAnswer().getUser().getUserId());
				setMessage.append("さんが回答を投稿しました。");
				// 表示する本文をセット
				replyNotification.setContent(replyNotification.getAnswer().getContent());
				// 遷移先の質問ページの質問IDをセット
				replyNotification.setQuestionId(replyNotification.getAnswer().getQuestion().getQuestionId());
				// 通知年月日時刻をセット
				replyNotification.setNotifiedTime(replyNotification.getAnswer().getPostTime());
				break;
			case "2": // 投稿した質問に対するコメント
				// セットするメッセージを作成
				setMessage.append("投稿した質問に対して");
				setMessage.append(replyNotification.getQuestionTargetComment().getPostUserId());
				setMessage.append("さんがコメントを投稿しました。");
				// 表示する本文をセット
				replyNotification.setContent(replyNotification.getQuestionTargetComment().getContent());
				// 遷移先の質問ページの質問IDをセット
				replyNotification.setQuestionId(replyNotification.getQuestionTargetComment().getQuestion().getQuestionId());
				// 通知年月日時刻をセット
				replyNotification.setNotifiedTime(replyNotification.getQuestionTargetComment().getPostTime());
				break;
			case "3": // お気に入り登録した質問に対する回答
				// セットするメッセージを作成
				setMessage.append("お気に入り登録した質問に対して");
				setMessage.append(replyNotification.getAnswer().getUser().getUserId());
				setMessage.append("さんが回答を投稿しました。");
				// 表示する本文をセット
				replyNotification.setContent(replyNotification.getAnswer().getContent());
				// 遷移先の質問ページの質問IDをセット
				replyNotification.setQuestionId(replyNotification.getAnswer().getQuestion().getQuestionId());
				// 通知年月日時刻をセット
				replyNotification.setNotifiedTime(replyNotification.getAnswer().getPostTime());
				break;
			case "4": // お気に入り登録した質問に対するコメント
				// セットするメッセージを作成
				setMessage.append("お気に入り登録した質問に対して");
				setMessage.append(replyNotification.getQuestionTargetComment().getPostUserId());
				setMessage.append("さんがコメントを投稿しました。");
				// 表示する本文をセット
				replyNotification.setContent(replyNotification.getQuestionTargetComment().getContent());
				// 遷移先の質問ページの質問IDをセット
				replyNotification.setQuestionId(replyNotification.getQuestionTargetComment().getQuestion().getQuestionId());
				// 通知年月日時刻をセット
				replyNotification.setNotifiedTime(replyNotification.getQuestionTargetComment().getPostTime());
				break;
			case "5": // 投稿した回答に対するコメント
				// セットするメッセージを作成
				setMessage.append("投稿した回答に対して");
				setMessage.append(replyNotification.getAnswerTargetComment().getPostUserId());
				setMessage.append("さんがコメントを投稿しました。");
				// 表示する本文をセット
				replyNotification.setContent(replyNotification.getAnswerTargetComment().getContent());
				// 遷移先の質問ページの質問IDをセット
				replyNotification.setQuestionId(replyNotification.getAnswerTargetComment().getAnswer().getQuestion().getQuestionId());
				// 通知年月日時刻をセット
				replyNotification.setNotifiedTime(replyNotification.getAnswerTargetComment().getPostTime());
				break;
		}
		// 表示メッセージをセット
		replyNotification.setMessage(setMessage.toString());
	}

	public void setDisplayValue(PostHistory postHistory) {
		StringBuilder setMessage = new StringBuilder("あなたが");
		switch( postHistory.getPostClassification() ) {
			case "1": // 質問
				// セットするメッセージを作成
				setMessage.append("質問");
				// 表示する本文をセット
				postHistory.setContent(postHistory.getQuestion().getContent());
				// 遷移先の質問ページの質問IDをセット
				postHistory.setQuestionId(postHistory.getQuestion().getQuestionId());
				// 通知年月日時刻をセット
				postHistory.setNotifiedTime(postHistory.getQuestion().getPostTime());
				break;
			case "2": // 回答
				// セットするメッセージを作成
				setMessage.append("回答");
				// 表示する本文をセット
				postHistory.setContent(postHistory.getAnswer().getContent());
				// 遷移先の質問ページの質問IDをセット
				postHistory.setQuestionId(postHistory.getAnswer().getQuestion().getQuestionId());
				// 通知年月日時刻をセット
				postHistory.setNotifiedTime(postHistory.getAnswer().getPostTime());
				break;
			case "3": // 質問対象コメント
				// セットするメッセージを作成
				setMessage.append("質問に対してコメント");
				// 表示する本文をセット
				postHistory.setContent(postHistory.getQuestionTargetComment().getContent());
				// 遷移先の質問ページの質問IDをセット
				postHistory.setQuestionId(postHistory.getQuestionTargetComment().getQuestion().getQuestionId());
				// 通知年月日時刻をセット
				postHistory.setNotifiedTime(postHistory.getQuestionTargetComment().getPostTime());
				break;
			case "4": // 回答対象コメント
				// セットするメッセージを作成
				setMessage.append("回答に対してコメント");
				// 表示する本文をセット
				postHistory.setContent(postHistory.getAnswerTargetComment().getContent());
				// 遷移先の質問ページの質問IDをセット
				postHistory.setQuestionId(postHistory.getAnswerTargetComment().getAnswer().getQuestion().getQuestionId());
				// 通知年月日時刻をセット
				postHistory.setNotifiedTime(postHistory.getAnswerTargetComment().getPostTime());
				break;
		}
		setMessage.append("を投稿しました。");
		// 表示メッセージをセット
		postHistory.setMessage(setMessage.toString());
	}

	public void setDisplayValue(FavHistory favHistory) {
		// セットするメッセージを作成及びセット
		StringBuilder setMessage = new StringBuilder("あなたが質問をお気に入り登録しました。");
		favHistory.setMessage(setMessage.toString());
		// 表示する本文をセット
		favHistory.setContent(favHistory.getQuestion().getContent());
		// 遷移先の質問ページの質問IDをセット
		favHistory.setQuestionId(favHistory.getQuestion().getQuestionId());
		// 通知年月日時刻をセット
		for(QuestionFavUser questionFavUser: favHistory.getQuestion().getQuestionFavUsers()) {
			if( questionFavUser.getUser().getUserId().equals(this.getUserId()) ) {
				favHistory.setNotifiedTime(questionFavUser.getRegistedTime());
				break;
			}
		}
	}

	public void setDisplayValue(SolvedHistory solvedHistory) {
		StringBuilder setMessage = new StringBuilder("あなたがお気に入り登録した質問が");
		switch( solvedHistory.getSolvedClassification() ) {
			case "1": // 自己解決
				// セットするメッセージを作成
				setMessage.append("投稿者によって自己解決されました。");
				break;
			case "2": // 回答による解決
				// セットするメッセージを作成
				setMessage.append("投稿された回答によって解決されました。");
				break;
		}
		// 表示する本文をセット
		solvedHistory.setContent(solvedHistory.getQuestion().getContent());
		// 遷移先の質問ページの質問IDをセット
		solvedHistory.setQuestionId(solvedHistory.getQuestion().getQuestionId());
		// 表示メッセージをセット
		solvedHistory.setMessage(setMessage.toString());
	}
}
