package entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

/**
 * The primary key class for the exercise_num database table.
 *
 */
@Embeddable
@Getter @Setter
public class ExerciseNumPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="exercise_id", insertable=false)
	private int exerciseId;

	@Column(name="exercise_num", insertable=false)
	private int exerciseNum;

	public ExerciseNumPK() {
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ExerciseNumPK)) {
			return false;
		}
		ExerciseNumPK castOther = (ExerciseNumPK)other;
		return
			(this.exerciseId == castOther.exerciseId)
			&& (this.exerciseNum == castOther.exerciseNum);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.exerciseId;
		hash = hash * prime + this.exerciseNum;

		return hash;
	}
}