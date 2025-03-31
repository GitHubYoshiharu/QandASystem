package entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;


/**
 * The persistent class for the reply_notification database table.
 *
 * ※※※Caution：Triggerにコメントを書いてない(書けない)ので、以下の記述は消さないで※※※
 * replyNotificationClassification:
 *  "1" ... 投稿した質問に対する回答
 *  "2" ... 投稿した質問に対するコメント
 *  "3" ... お気に入り登録した質問に対する回答
 *  "4" ... お気に入り登録した質問に対するコメント
 *  "5" ... 投稿した回答に対するコメント
 *
 *  replyPostClassification:
 *   "1" ... 回答
 *   "2" ... 質問に対するコメント
 *   "3" ... 回答に対するコメント
 */
@Entity
@Table(name="reply_notification")
@NamedQuery(name="ReplyNotification.findAll", query="SELECT r FROM ReplyNotification r")
@Getter @Setter
public class ReplyNotification implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="reply_notification_id")
	private int replyNotificationId;

	@Column(name="reply_notification_classification")
	private String replyNotificationClassification;

	@Column(name="reply_post_classification")
	private String replyPostClassification;

	//bi-directional many-to-one association to Answer
	@ManyToOne
	@JoinColumn(name="reply_answer_id")
	private Answer answer;

	//bi-directional many-to-one association to QuestionTargetComment
	@ManyToOne
	@JoinColumn(name="reply_question_target_comment_id")
	private QuestionTargetComment questionTargetComment;

	//bi-directional many-to-one association to AnswerTargetComment
	@ManyToOne
	@JoinColumn(name="reply_answer_target_comment_id")
	private AnswerTargetComment answerTargetComment;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="reply_notification_target_student_user_id")
	private User user;

    //<!-- マイページ表示用のフィールド
	@Transient
	private String message;

	@Transient
	private String content;

	@Transient
	private int questionId;

	@Transient
	private Date notifiedTime;
	//-->

	public ReplyNotification() {}

	//<!-- replyPostClassificationの値によって使い分けるコンストラクタ
	// 回答
	public ReplyNotification(String replyNotificationClassification, String replyPostClassification, User user, Answer answer) {
		this.replyNotificationClassification = replyNotificationClassification;
		this.replyPostClassification = replyPostClassification;
		this.user = user;
		this.answer = answer;
	}
	// 質問に対するコメント
	public ReplyNotification(String replyNotificationClassification, String replyPostClassification, User user, QuestionTargetComment questionTargetComment) {
		this.replyNotificationClassification = replyNotificationClassification;
		this.replyPostClassification = replyPostClassification;
		this.user = user;
		this.questionTargetComment = questionTargetComment;
	}
	// 回答に対するコメント
	public ReplyNotification(String replyNotificationClassification, String replyPostClassification, User user, AnswerTargetComment answerTargetComment) {
		this.replyNotificationClassification = replyNotificationClassification;
		this.replyPostClassification = replyPostClassification;
		this.user = user;
		this.answerTargetComment = answerTargetComment;
	}
	//-->
}