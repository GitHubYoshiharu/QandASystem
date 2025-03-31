package entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

/**
 * The primary key class for the taglist_component database table.
 *
 */
@Embeddable
@Getter @Setter
public class TaglistComponentPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="exercise_id", updatable=false)
	private int exerciseId;

	@Column(name="tag_id", updatable=false)
	private int tagId;

	@Column(name="exercise_num", updatable=false)
	private int exerciseNum;

	public TaglistComponentPK() {}

	public TaglistComponentPK(int exerciseId, int exerciseNum, int tagId) {
		this.exerciseId = exerciseId;
		this.exerciseNum = exerciseNum;
		this.tagId = tagId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TaglistComponentPK)) {
			return false;
		}
		TaglistComponentPK castOther = (TaglistComponentPK)other;
		return
			(this.exerciseId == castOther.exerciseId)
			&& (this.tagId == castOther.tagId)
			&& (this.exerciseNum == castOther.exerciseNum);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.exerciseId;
		hash = hash * prime + this.tagId;
		hash = hash * prime + this.exerciseNum;

		return hash;
	}
}