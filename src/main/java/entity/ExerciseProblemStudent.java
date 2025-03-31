package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;


/**
 * The persistent class for the exercise_problem_student database table.
 *
 */
@Entity
@Table(name="exercise_problem_student")
@NamedQuery(name="ExerciseProblemStudent.findAll", query="SELECT e FROM ExerciseProblemStudent e")
@Getter @Setter
public class ExerciseProblemStudent implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ExerciseProblemStudentPK id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="last_modified_time", insertable=false, updatable=false)
	private Date lastModifiedTime;

	@Column(name="score")
	private BigDecimal score;

	//<!-- ユーザによって選択されたタグのid
	@Transient
	private Integer tagListComponentListIndex;

	// valueに意味は無い
	@Transient
	private SelectItem[] registeredTagItems;

	// オーバーロード
	public void setRegisteredTagItems() {
		this.registeredTagItems = new SelectItem[this.tags.size()];
		for(int i = 0; i < this.tags.size(); i++) {
			Tag tag = this.tags.get(i);
			this.registeredTagItems[i] = new SelectItem(null, tag.getTagName(), null, true);
		}
	}
	//-->

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="last_modified_user_id")
	private User user;

	@ManyToMany
	@JoinTable(
		name="exercise_problem_student_used_tag"
		, joinColumns={
			@JoinColumn(name="user_id", referencedColumnName="user_id"),
			@JoinColumn(name="exercise_id", referencedColumnName="exercise_id"),
			@JoinColumn(name="exercise_num", referencedColumnName="exercise_num"),
			@JoinColumn(name="exercise_problem_num", referencedColumnName="exercise_problem_num")
			}
		, inverseJoinColumns={
			@JoinColumn(name="tag_id", referencedColumnName="tag_id")
			}
		)
	private List<Tag> tags;

	//bi-directional many-to-one association to ExerciseProblem
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="exercise_id", referencedColumnName="exercise_id", insertable = false, updatable = false),
		@JoinColumn(name="exercise_num", referencedColumnName="exercise_num", insertable = false, updatable = false),
		@JoinColumn(name="exercise_problem_num", referencedColumnName="exercise_problem_num", insertable = false, updatable = false)
		})
	private ExerciseProblem exerciseProblem;

	//bi-directional many-to-one association to ExerciseStudent
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="exercise_id", referencedColumnName="exercise_id", insertable = false, updatable = false),
		@JoinColumn(name="user_id", referencedColumnName="user_id", insertable = false, updatable = false)
		})
	private ExerciseStudent exerciseStudent;

	public ExerciseProblemStudent( ) {}

	public ExerciseProblemStudent(ExerciseProblemPK exerciseProblemPK, String userId) {
		this.id = new ExerciseProblemStudentPK(userId
				                                                           ,exerciseProblemPK.getExerciseId()
				                                                           ,exerciseProblemPK.getExerciseNum()
				                                                           ,exerciseProblemPK.getExerciseProblemNum());
	}

	@Override
	public boolean equals(Object obj) {
		return this.id.equals(obj);
	}
}