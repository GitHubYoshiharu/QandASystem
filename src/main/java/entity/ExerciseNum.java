package entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;


/**
 * The persistent class for the exercise_num database table.
 *
 */
@Entity
@Table(name="exercise_num")
@NamedQuery(name="ExerciseNum.findAll", query="SELECT e FROM ExerciseNum e")
@Getter @Setter
public class ExerciseNum implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ExerciseNumPK id;

	//bi-directional many-to-one association to Exercise
	@ManyToOne
	@JoinColumn(name="exercise_id", insertable=false, updatable=false)
	private Exercise exercise;

	//bi-directional many-to-one association to ExerciseProblem
	@OneToMany(mappedBy="exerciseNum")
	private List<ExerciseProblem> exerciseProblems;

	//bi-directional many-to-one association to TaglistComponent
	@OneToMany(mappedBy="exerciseNum")
	private List<TaglistComponent> taglistComponents;

	public ExerciseNum() {}
}