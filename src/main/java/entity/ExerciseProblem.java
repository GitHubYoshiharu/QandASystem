package entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;


/**
 * The persistent class for the exercise_problem database table.
 *
 */
@Entity
@Table(name="exercise_problem")
@NamedQuery(name="ExerciseProblem.findAll", query="SELECT e FROM ExerciseProblem e")
@Getter @Setter
public class ExerciseProblem implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ExerciseProblemPK id;

	//bi-directional many-to-one association to ExerciseNum
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="exercise_id", referencedColumnName="exercise_id", insertable=false, updatable=false),
		@JoinColumn(name="exercise_num", referencedColumnName="exercise_num", insertable=false, updatable=false)
		})
	private ExerciseNum exerciseNum;

	//bi-directional many-to-one association to ExerciseProblemStudent
	@OneToMany(mappedBy="exerciseProblem")
	private List<ExerciseProblemStudent> exerciseProblemStudents;

	public ExerciseProblem() {
	}
}