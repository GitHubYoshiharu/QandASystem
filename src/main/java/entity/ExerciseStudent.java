package entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;


/**
 * The persistent class for the exercise_student database table.
 *
 */
@Entity
@Table(name="exercise_student")
@NamedQuery(name="ExerciseStudent.findAll", query="SELECT e FROM ExerciseStudent e")
@Getter @Setter
public class ExerciseStudent implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ExerciseStudentPK id;

	//bi-directional many-to-one association to ExerciseProblemStudent
	@OneToMany(mappedBy="exerciseStudent")
	private List<ExerciseProblemStudent> exerciseProblemStudents;

	public ExerciseStudent() {
	}
}