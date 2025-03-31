package entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

/**
 * The primary key class for the question_fav_user database table.
 *
 */
@Embeddable
@Getter @Setter
public class QuestionFavUserPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="question_id", insertable=false, updatable=false)
	private int questionId;

	@Column(name="user_id", insertable=false, updatable=false)
	private String userId;

	public QuestionFavUserPK() {
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof QuestionFavUserPK)) {
			return false;
		}
		QuestionFavUserPK castOther = (QuestionFavUserPK)other;
		return
			(this.questionId == castOther.questionId)
			&& this.userId.equals(castOther.userId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.questionId;
		hash = hash * prime + this.userId.hashCode();

		return hash;
	}
}