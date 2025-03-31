package entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;


/**
 * The persistent class for the taglist_component database table.
 *
 */
@Entity
@Table(name="taglist_component")
@NamedQuery(name="TaglistComponent.findAll", query="SELECT t FROM TaglistComponent t")
@Getter @Setter
public class TaglistComponent implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TaglistComponentPK id;

	@Column(name="num_of_used")
	private int numOfUsed;

	public void incrementNumOfUsed() { this.numOfUsed++; }
	public void decrementNumOfUsed( ) { this.numOfUsed--; }

	//bi-directional many-to-one association to ExerciseNum
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="exercise_id", referencedColumnName="exercise_id", updatable=false, insertable=false),
		@JoinColumn(name="exercise_num", referencedColumnName="exercise_num", updatable=false, insertable=false)
		})
	private ExerciseNum exerciseNum;

	//bi-directional many-to-one association to Tag
	@ManyToOne
	@JoinColumn(name="tag_id", updatable=false, insertable=false)
	private Tag tag;

	public TaglistComponent() {}

	public TaglistComponent(ExerciseNum exerciseNum, Integer tagId) {
		this.id = new TaglistComponentPK(exerciseNum.getId().getExerciseId(), exerciseNum.getId().getExerciseNum(), tagId);
	}
}