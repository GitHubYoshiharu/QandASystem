package entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;


/**
 * The persistent class for the exercise_problem_student_used_tag database table.
 *
 */
@Entity
@Table(name="exercise_problem_student_used_tag")
@NamedQuery(name="ExerciseProblemStudentUsedTag.findAll", query="SELECT e FROM ExerciseProblemStudentUsedTag e")
@Getter @Setter
public class ExerciseProblemStudentUsedTag implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ExerciseProblemStudentUsedTagPK id;

	public ExerciseProblemStudentUsedTag() {
	}
}