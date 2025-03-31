package entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter @Setter
public class ExerciseProblemStudentUsedTagPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="user_id", insertable=false)
	private String userId;

	@Column(name="exercise_id", insertable=false)
	private int exerciseId;

	@Column(name="exercise_num", insertable=false)
	private int exerciseNum;

	@Column(name="exercise_problem_num", insertable=false)
	private int exerciseProblemNum;

	@Column(name="tag_id", insertable=false)
	private int tagId;

	public ExerciseProblemStudentUsedTagPK() {
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + exerciseId;
		result = prime * result + exerciseNum;
		result = prime * result + exerciseProblemNum;
		result = prime * result + tagId;
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExerciseProblemStudentUsedTagPK other = (ExerciseProblemStudentUsedTagPK) obj;
		if (exerciseId != other.exerciseId)
			return false;
		if (exerciseNum != other.exerciseNum)
			return false;
		if (exerciseProblemNum != other.exerciseProblemNum)
			return false;
		if (tagId != other.tagId)
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
}
