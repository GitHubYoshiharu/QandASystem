package entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

/**
 * The primary key class for the exercise_problem_student database table.
 *
 */
@Embeddable
@Getter @Setter
public class ExerciseProblemStudentPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="user_id", updatable=false)
	private String userId;

	@Column(name="exercise_id", updatable=false)
	private int exerciseId;

	@Column(name="exercise_num", updatable=false)
	private int exerciseNum;

	@Column(name="exercise_problem_num", updatable=false)
	private int exerciseProblemNum;

	public ExerciseProblemStudentPK() {
	}

	public ExerciseProblemStudentPK(String userId, int exerciseId, int exerciseNum, int exerciseProblemNum) {
		super();
		this.userId = userId;
		this.exerciseId = exerciseId;
		this.exerciseNum = exerciseNum;
		this.exerciseProblemNum = exerciseProblemNum;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ExerciseProblemStudentPK)) {
			return false;
		}
		ExerciseProblemStudentPK castOther = (ExerciseProblemStudentPK)other;
		return
			this.userId.equals(castOther.userId)
			&& (this.exerciseId == castOther.exerciseId)
			&& (this.exerciseNum == castOther.exerciseNum)
			&& (this.exerciseProblemNum == castOther.exerciseProblemNum);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.userId.hashCode();
		hash = hash * prime + this.exerciseId;
		hash = hash * prime + this.exerciseNum;
		hash = hash * prime + this.exerciseProblemNum;

		return hash;
	}
}