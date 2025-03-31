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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;


/**
 * The persistent class for the question_target_comment database table.
 *
 */
@Entity
@Table(name="question_target_comment")
@NamedQuery(name="QuestionTargetComment.findAll", query="SELECT q FROM QuestionTargetComment q")
@Getter @Setter
public class QuestionTargetComment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="comment_id")
	private int commentId;

	@Lob
	private String content;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="post_time", insertable=false)
	private Date postTime;

	@Column(name="post_user_id")
	private String postUserId;

	//bi-directional many-to-one association to Question
	@ManyToOne
	@JoinColumn(name="target_question_id")
	private Question question;

	//bi-directional many-to-one association to PostHistory
	@OneToMany(mappedBy="questionTargetComment")
	private List<PostHistory> postHistories;

	//bi-directional many-to-one association to ReplyNotification
	@OneToMany(mappedBy="questionTargetComment")
	private List<ReplyNotification> replyNotifications;

	public QuestionTargetComment() {
	}
}