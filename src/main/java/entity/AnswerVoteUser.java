package entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;


/**
 * The persistent class for the answer_fav_user database table.
 *
 */
@Entity
@Table(name="answer_vote_user")
@NamedQuery(name="AnswerFavUser.findAll", query="SELECT a FROM AnswerVoteUser a")
@Getter @Setter
public class AnswerVoteUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private AnswerVoteUserPK id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="registed_time", insertable=false)
	private Date registedTime;

	//bi-directional many-to-one association to Answer
	@ManyToOne
	@JoinColumn(name="answer_id")
	private Answer answer;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;

	public AnswerVoteUser() {
	}
}