package bb;

import static java.util.Comparator.*;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import entity.Answer;
import entity.AnswerTargetComment;
import entity.AnswerVoteUser;
import entity.AnswerVoteUserPK;
import entity.FavHistory;
import entity.PostHistory;
import entity.Question;
import entity.QuestionFavUser;
import entity.QuestionFavUserPK;
import entity.QuestionTargetComment;
import entity.QuestionViewUser;
import entity.ReplyNotification;
import entity.SolvedHistory;
import entity.User;
import lombok.Getter;
import lombok.Setter;
import model.db.AnswerDb;
import model.db.AnswerTargetCommentDb;
import model.db.AnswerVoteUserDb;
import model.db.FavHistoryDb;
import model.db.PostHistoryDb;
import model.db.QuestionDb;
import model.db.QuestionFavUserDb;
import model.db.QuestionTargetCommentDb;
import model.db.QuestionViewUserDb;
import model.db.ReplyNotificationDb;
import model.db.SolvedHistoryDb;
import model.db.UserDb;

@Named
@ViewScoped
public class QuestionViewBb extends AbstractBb implements Serializable {
	// 表示する質問
	@Getter @Setter
	private Question viewQuestion;
	// このページを閲覧しているユーザ
	private User viewUser;

	//<!-- このページ上で使用するフラグ
	// 閲覧ユーザ   質問投稿ユーザ   |   質問お気に入り   質問コメント   回答投稿   回答お気に入り         回答コメント
	// --------------------------------------------------------------------------------------------------------------
	// A                 A                        |   不可                   可                   不可          可                            可
	// A                 B                        |   可                      可                   可             (自分の回答以外)可   可
	// --------------------------------------------------------------------------------------------------------------
	// 回数制限                                 |   1回                    無し                1回           1回                           無し
	@Getter @Setter
	private boolean hasRegistedQuestionFav = false;
	@Getter @Setter
	private ArrayList<Boolean> hasRegistedAnswerVote;
	@Getter @Setter
	private boolean hasPostedAnswer = false;
	// ※「is～」だと、Getterの名前と重複してFacelets側でエラーが出るよ（クソ仕様）
	@Getter @Setter
	private boolean questionPostUser;
	@Getter @Setter
	private ArrayList<Boolean> answerPostUser;
	//-->

	//<!-- ユーザに入力されたデータ
	@Getter @Setter
	private String postedQuestionComment;
	@Getter @Setter
	private String postedAnswer;
	@Getter @Setter
	private String postedAnswerComment;
	//-->

	@EJB private QuestionDb questionDb;
	@EJB private QuestionFavUserDb questionFavUserDb;
	@EJB private AnswerVoteUserDb answerVoteUserDb;
	@EJB private UserDb userDb;
	@EJB private AnswerDb answerDb;
	@EJB private QuestionTargetCommentDb questionTargetCommentDb;
	@EJB private AnswerTargetCommentDb answerTargetCommentDb;
	@EJB private QuestionViewUserDb questionViewUserDb;
	@EJB private ReplyNotificationDb replyNotificationDb;
	@EJB private FavHistoryDb favHistoryDb;
	@EJB private PostHistoryDb postHistoryDb;
	@EJB private SolvedHistoryDb solvedHistoryDb;

	@Override
	@PostConstruct
	public void init() {
		// 質問検索画面、またはマイページから受け取った質問IDを使って表示する質問をselect
		int viewQuestionId = Integer.parseInt( this.getRequest().getParameter("questionId") );
		viewQuestion = questionDb.dontUseCacheSelect(viewQuestionId);

		// このページを閲覧しているユーザを取得する
		viewUser = userDb.select(this.getUserId());

		// 質問の閲覧数を加算する
		QuestionViewUser questionViewUser = new QuestionViewUser();
		questionViewUser.setQuestion(viewQuestion);
		questionViewUser.setUser(viewUser);
		questionViewUserDb.insert(questionViewUser);
		viewQuestion.getQuestionViewUsers().add(questionViewUser);

		// <!-- フラグを立てる
		// フラグ配列の初期化
		hasRegistedAnswerVote = new ArrayList<>();
		answerPostUser = new ArrayList<>();
		// この質問の投稿者か否か
		questionPostUser = viewQuestion.getUser().getUserId().equals( this.getUserId() );
		// この質問を既にお気に入り登録しているか否か
		for ( QuestionFavUser user : viewQuestion.getQuestionFavUsers() ) {
			if ( user.getId().getUserId().equals( this.getUserId() ) ) {
				hasRegistedQuestionFav = true;
				break;
			}
		}

		// 回答に既に投票しているか否か
		for (int i = 0; i < viewQuestion.getAnswers().size(); i++) {
			Answer answer = viewQuestion.getAnswers().get(i);
			hasRegistedAnswerVote.add(false);
			// その回答の投票数が0であれば、
			// 自動的に投票していないと判明する
			if (answer.getAnswerVoteUsers().size() == 0) continue;
			// その回答の投票ユーザの中に閲覧ユーザがいれば、
			// 既にお気に入り登録していると分かる
			for( AnswerVoteUser favUser : answer.getAnswerVoteUsers() ) {
				if ( favUser.getId().getUserId().equals( this.getUserId() ) ) {
					hasRegistedAnswerVote.set(i, true);
					break;
				}
			}
		}
		// この質問に既に回答を投稿しているかどうか & どの回答の投稿者か
		for (int i = 0; i < viewQuestion.getAnswers().size(); i++) {
			Answer answer = viewQuestion.getAnswers().get(i);
			if ( !questionPostUser && answer.getUser().getUserId().equals( this.getUserId() ) ) {
				hasPostedAnswer = true;
				answerPostUser.add(true);
			} else {
				answerPostUser.add(false);
			}
		}
		// 回答を投票数降順にソートする
		viewQuestion.getAnswers().sort( comparing( x -> x.getAnswerVoteUsers().size(), reverseOrder() ) );
		// -->
	}

	public String questionFavRegist() {
		QuestionFavUser questionFavUser = new QuestionFavUser();
		QuestionFavUserPK questionFavUserPK = new QuestionFavUserPK();
		questionFavUserPK.setQuestionId( viewQuestion.getQuestionId() );
		questionFavUserPK.setUserId(viewUser.getUserId());
		questionFavUser.setId(questionFavUserPK);
		viewQuestion.getQuestionFavUsers().add(questionFavUser);
		questionFavUserDb.insert( viewQuestion.getQuestionId(), this.getUserId() );
		hasRegistedQuestionFav = true;
		// 当該ユーザの質問お気に入り履歴を登録する
		FavHistory favHistory = new FavHistory(viewUser, viewQuestion);
		favHistoryDb.insert(favHistory);
		return null;
	}

	public String answerVoteRegist(int index) {
		AnswerVoteUser answerVoteUser = new AnswerVoteUser();
		AnswerVoteUserPK answerVoteUserPK = new AnswerVoteUserPK();
		answerVoteUserPK.setAnswerId(viewQuestion.getAnswers().get(index).getAnswerId());
		answerVoteUserPK.setUserId(viewUser.getUserId());
		answerVoteUser.setId(answerVoteUserPK);
		viewQuestion.getAnswers().get(index).getAnswerVoteUsers().add(answerVoteUser);
		answerVoteUserDb.insert( viewQuestion.getAnswers().get(index).getAnswerId(), this.getUserId() );
		// FIXME: *****************************************************
		// 良質な回答を参考にしやすいようにソートしているが、
	    // indexの対応関係が崩れる恐れがあるため、現在のコードでは、バグが発生する。
		// これに対応するための行為として考えられるのは、以下の2つである。
		// 1. 以下の投票数降順ソート処理を消す
		// 2. 回答をindex(リスト順序)ではなく、回答IDによって制御する
		// ************************************************************
		// 回答を投票数降順にソートする
	    // viewQuestion.getAnswers().sort( comparing( x -> x.getAnswerVoteUsers().size(), reverseOrder() ) );
		hasRegistedAnswerVote.set(index, true);
		return null;
	}

	public String answerPost() {
		Answer answer = new Answer();
		answer.setContent(postedAnswer);
		answer.setQuestion(viewQuestion);
		answer.setUser(viewUser);
		answerDb.insert(answer);
		viewQuestion.getAnswers().add(answer);
		// IndexOutOfBoundsを回避するため、
		// 回答投稿者であっても投票未済フラグを追加する
		hasRegistedAnswerVote.add(false);
		hasPostedAnswer = true;
		answerPostUser.add(true);
		postedAnswer = null;
		// 当該ユーザの回答投稿履歴を登録する
		PostHistory postHistory = new PostHistory("2", answer, viewUser);
		postHistoryDb.insert(postHistory);
		// 質問投稿者への、回答が投稿されたことの通知
		ReplyNotification replyNotificationToQuestionPoster
			= new ReplyNotification("1", "1", viewQuestion.getUser(), answer);
		replyNotificationDb.insert(replyNotificationToQuestionPoster);
		// この質問のお気に入り登録者への、回答が投稿されたことの通知
		for(QuestionFavUser questionFavUser: viewQuestion.getQuestionFavUsers()) {
			ReplyNotification replyNotificationToQuestionFavRegister
			= new ReplyNotification("3", "1", questionFavUser.getUser(), answer);
			replyNotificationDb.insert(replyNotificationToQuestionFavRegister);
		}
		return null;
	}

	public String questionTargetCommentPost() {
		QuestionTargetComment questionTargetComment = new QuestionTargetComment();
		questionTargetComment.setContent(postedQuestionComment);
		questionTargetComment.setQuestion(viewQuestion);
		questionTargetComment.setPostUserId(viewUser.getUserId());
		questionTargetCommentDb.insert(questionTargetComment);
		viewQuestion.getQuestionTargetComments().add(questionTargetComment);
		postedQuestionComment = null;
		// 当該ユーザの質問対象コメント投稿履歴を登録する
		PostHistory postHistory = new PostHistory("3", questionTargetComment, viewUser);
		postHistoryDb.insert(postHistory);
		// 質問投稿者への、コメントが投稿されたことの通知
		// ただし、質問投稿者自らがコメントを投稿した場合は通知しない（投稿通知もされて冗長なため）
		if(!questionPostUser) {
			ReplyNotification replyNotificationToQuestionPoster
			= new ReplyNotification("2", "2", viewQuestion.getUser(), questionTargetComment);
			replyNotificationDb.insert(replyNotificationToQuestionPoster);
		}
		// この質問のお気に入り登録者への、コメントが投稿されたことの通知
		for(QuestionFavUser questionFavUser: viewQuestion.getQuestionFavUsers()) {
			ReplyNotification replyNotificationToQuestionFavRegister
			= new ReplyNotification("4", "2", questionFavUser.getUser(), questionTargetComment);
			replyNotificationDb.insert(replyNotificationToQuestionFavRegister);
		}
		return null;
	}

	public String answerTargetCommentPost(int index) {
		AnswerTargetComment answerTargetComment = new AnswerTargetComment();
		answerTargetComment.setContent(postedAnswerComment);
		answerTargetComment.setAnswer(viewQuestion.getAnswers().get(index));
		answerTargetComment.setPostUserId(viewUser.getUserId());
		answerTargetCommentDb.insert(answerTargetComment);
		viewQuestion.getAnswers().get(index).getAnswerTargetComments().add(answerTargetComment);
		postedAnswerComment = null;
		// 当該ユーザの回答対象コメント投稿履歴を登録する
		PostHistory postHistory = new PostHistory("4", answerTargetComment, viewUser);
		postHistoryDb.insert(postHistory);
		// 回答投稿者への、コメントが投稿されたことの通知
		// ただし、回答投稿者自らがコメントを投稿した場合は通知しない（投稿通知もされて冗長なため）
		if(!answerPostUser.get(index)) {
			ReplyNotification replyNotificationToAnswerPoster
			= new ReplyNotification("5", "3", viewQuestion.getAnswers().get(index).getUser(), answerTargetComment);
			replyNotificationDb.insert(replyNotificationToAnswerPoster);
		}
		return null;
	}

	public String solvedRegist() {
		viewQuestion.setHasSolved(true);
		questionDb.update(viewQuestion);
		// この質問お気に入り登録者への、質問が自己解決されたことの通知
		for(QuestionFavUser questionFavUser: viewQuestion.getQuestionFavUsers()) {
			SolvedHistory solvedHistoryToQuestionFavRegister
			= new SolvedHistory("1", viewQuestion, questionFavUser.getUser());
			solvedHistoryDb.insert(solvedHistoryToQuestionFavRegister);
		}
		return null;
	}

	// ベストアンサーを決めるということは、質問を解決済みすることと同義となる
	public String bestAnswerRegist(int index) {
		viewQuestion.setBestAnswerId(viewQuestion.getAnswers().get(index).getAnswerId());
		viewQuestion.setHasSolved(true);
		questionDb.update(viewQuestion);
		// この質問お気に入り登録者への、質問が投稿された回答によって解決されたことの通知
		for(QuestionFavUser questionFavUser: viewQuestion.getQuestionFavUsers()) {
			SolvedHistory solvedHistoryToQuestionFavRegister
			= new SolvedHistory("2", viewQuestion, questionFavUser.getUser());
			solvedHistoryDb.insert(solvedHistoryToQuestionFavRegister);
		}
		return null;
	}
}
