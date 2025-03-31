package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;


/**
 * The persistent class for the answer database table.
 *
 */
@Entity
@NamedQuery(name="Answer.findAll", query="SELECT a FROM Answer a")
@Getter @Setter
public class Answer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="answer_id")
	private int answerId;

	@Lob
	private String content;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="post_time", insertable=false)
	private Date postTime;

	//bi-directional many-to-one association to Question
	@ManyToOne
	@JoinColumn(name="target_question_id")
	private Question question;

	//bi-directional many-to-one association to AnswerFavUser
	@OneToMany(mappedBy="answer")
	private List<AnswerVoteUser> answerVoteUsers;

	//bi-directional many-to-one association to AnswerTargetComment
	@OneToMany(mappedBy="answer")
	private List<AnswerTargetComment> answerTargetComments;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="post_user_id")
	private User user;

	//bi-directional many-to-one association to PostHistory
	@OneToMany(mappedBy="answer")
	private List<PostHistory> postHistories;

	//bi-directional many-to-one association to ReplyNotification
	@OneToMany(mappedBy="answer")
	private List<ReplyNotification> replyNotifications;

	public Answer() {
	}
}