package entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

/**
 * The primary key class for the exercise_student database table.
 *
 */
@Embeddable
@Getter @Setter
public class ExerciseStudentPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="user_id", insertable=false)
	private String userId;

	@Column(name="exercise_id", insertable=false)
	private int exerciseId;

	public ExerciseStudentPK() {
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ExerciseStudentPK)) {
			return false;
		}
		ExerciseStudentPK castOther = (ExerciseStudentPK)other;
		return
			this.userId.equals(castOther.userId)
			&& (this.exerciseId == castOther.exerciseId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.userId.hashCode();
		hash = hash * prime + this.exerciseId;

		return hash;
	}
}