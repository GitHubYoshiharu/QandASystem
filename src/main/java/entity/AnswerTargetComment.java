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
 * The persistent class for the answer_target_comment database table.
 *
 */
@Entity
@Table(name="answer_target_comment")
@NamedQuery(name="AnswerTargetComment.findAll", query="SELECT a FROM AnswerTargetComment a")
@Getter @Setter
public class AnswerTargetComment implements Serializable {
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

	//bi-directional many-to-one association to Answer
	@ManyToOne
	@JoinColumn(name="target_answer_id")
	private Answer answer;

	//bi-directional many-to-one association to PostHistory
	@OneToMany(mappedBy="answerTargetComment")
	private List<PostHistory> postHistories;

	//bi-directional many-to-one association to ReplyNotification
	@OneToMany(mappedBy="answerTargetComment")
	private List<ReplyNotification> replyNotifications;

	public AnswerTargetComment() {
	}
}