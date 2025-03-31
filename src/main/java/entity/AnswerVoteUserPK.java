package entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

/**
 * The primary key class for the answer_fav_user database table.
 *
 */
@Embeddable
@Getter @Setter
public class AnswerVoteUserPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="answer_id", insertable=false, updatable=false)
	private int answerId;

	@Column(name="user_id", insertable=false, updatable=false)
	private String userId;

	public AnswerVoteUserPK() {
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof AnswerVoteUserPK)) {
			return false;
		}
		AnswerVoteUserPK castOther = (AnswerVoteUserPK)other;
		return
			(this.answerId == castOther.answerId)
			&& this.userId.equals(castOther.userId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.answerId;
		hash = hash * prime + this.userId.hashCode();

		return hash;
	}
}