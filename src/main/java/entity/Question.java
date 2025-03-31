package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;


/**
 * The persistent class for the question database table.
 *
 */
@Entity
@NamedQuery(name="Question.findAll", query="SELECT q FROM Question q")
@Getter @Setter
public class Question implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="question_id")
	private int questionId;

	@Lob
	private String content;

	@Column(name="has_solved")
	private boolean hasSolved;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="post_time", insertable=false)
	private Date postTime;

	@Lob
	private String title;

	@Column(name="best_answer_id")
	private Integer bestAnswerId;

	//bi-directional many-to-one association to Answer
	@OneToMany(mappedBy="question")
	private List<Answer> answers;

	//bi-directional many-to-many association to Tag
	@ManyToMany
	@JoinTable(
		name="question_tag"
		, joinColumns={
			@JoinColumn(name="question_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="tag_id")
			}
		)
	private List<Tag> tags;

	//bi-directional many-to-one association to QuestionFavUser
	@OneToMany(mappedBy="question")
	private List<QuestionFavUser> questionFavUsers;

	//bi-directional many-to-one association to QuestionTag
	@OneToMany(mappedBy="question")
	private List<QuestionTag> questionTags;

	//bi-directional many-to-one association to QuestionTargetComment
	@OneToMany(mappedBy="question")
	private List<QuestionTargetComment> questionTargetComments;

	//bi-directional many-to-one association to QuestionViewUser
	@OneToMany(mappedBy="question")
	private List<QuestionViewUser> questionViewUsers;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="post_user_id")
	private User user;

	/*
	 * ※exerciseNum, exerciseProblemのいずれかしかWritableにできないので、
	 * 演習番号のみの登録は仕様上禁止する
	 */
	//bi-directional many-to-one association to ExerciseNum
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="exercise_id", referencedColumnName="exercise_id", insertable = false, updatable = false),
		@JoinColumn(name="exercise_num", referencedColumnName="exercise_num", insertable = false, updatable = false)
		})
	private ExerciseNum exerciseNum;

	//bi-directional many-to-one association to ExerciseProblem
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="exercise_id", referencedColumnName="exercise_id", updatable=false),
		@JoinColumn(name="exercise_num", referencedColumnName="exercise_num", updatable=false),
		@JoinColumn(name="exercise_problem_num", referencedColumnName="exercise_problem_num", updatable=false)
		})
	private ExerciseProblem exerciseProblem;

    @Override
	// questionIdで比較する
	public boolean equals(Object obj) {
		return this.questionId == ( (Question)obj ).getQuestionId();
	}

	public Question() {
	}
}