package entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;


/**
 * The persistent class for the exercise database table.
 *
 */
@Entity
@NamedQuery(name="Exercise.findAll", query="SELECT e FROM Exercise e")
@Getter @Setter
public class Exercise implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="exercise_id")
	private int exerciseId;

	@Column(name="begin_year")
	private int beginYear;

	@Column(name="course_name")
	private String courseName;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="kyoin_user_id")
	private User user;

	//bi-directional many-to-one association to ExerciseNum
	@OneToMany(mappedBy="exercise")
	private List<ExerciseNum> exerciseNums;

	//bi-directional many-to-many association to User
	@ManyToMany
	@JoinTable(
		name="exercise_student"
		, joinColumns={
			@JoinColumn(name="exercise_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="user_id")
			}
		)
	private List<User> users;

	public Exercise() {}
}