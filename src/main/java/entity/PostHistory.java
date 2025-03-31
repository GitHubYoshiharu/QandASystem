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
 * The persistent class for the post_history database table.
 *
 * ※※※Caution：Triggerにコメントを書いてない(書けない)ので、以下の記述は消さないで※※※
 * postClassification:
 *  "1" ... 質問
 *  "2" ... 回答
 *  "3" ... 質問に対するコメント
 *  "4" ... 回答に対するコメント
 */
@Entity
@Table(name="post_history")
@NamedQuery(name="PostHistory.findAll", query="SELECT p FROM PostHistory p")
@Getter @Setter
public class PostHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="post_history_id")
	private int postHistoryId;

	@Column(name="post_classification")
	private String postClassification;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="post_student_user_id")
	private User user;

	//bi-directional many-to-one association to Question
	@ManyToOne
	@JoinColumn(name="post_question_id")
	private Question question;

	//bi-directional many-to-one association to Answer
	@ManyToOne
	@JoinColumn(name="post_answer_id")
	private Answer answer;

	//bi-directional many-to-one association to QuestionTargetComment
	@ManyToOne
	@JoinColumn(name="post_question_target_comment_id")
	private QuestionTargetComment questionTargetComment;

	//bi-directional many-to-one association to AnswerTargetComment
	@ManyToOne
	@JoinColumn(name="post_answer_target_comment_id")
	private AnswerTargetComment answerTargetComment;

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

	public PostHistory() {}

	//<!-- postClassificationの値によって使い分けるコンストラクタ
	// 質問
	public PostHistory(String postClassification, Question question, User user) {
		this.postClassification = postClassification;
		this.question = question;
		this.user = user;
	}
	// 回答
	public PostHistory(String postClassification,  Answer answer, User user) {
		this.postClassification = postClassification;
		this.answer = answer;
		this.user = user;
	}
	// 質問に対するコメント
	public PostHistory(String postClassification, QuestionTargetComment questionTargetComment, User user) {
		this.postClassification = postClassification;
		this.questionTargetComment = questionTargetComment;
		this.user = user;
	}
	// 回答に対するコメント
	public PostHistory(String postClassification, AnswerTargetComment answerTargetComment, User user) {
		this.postClassification = postClassification;
		this.answerTargetComment = answerTargetComment;
		this.user = user;
	}
}