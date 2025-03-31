package entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;


/**
 * The persistent class for the user database table.
 *
 */
@Entity
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
@Getter @Setter
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="user_id")
	private String userId;

	@Column(name="is_admin")
	private boolean isAdmin;

	@Column(name="is_kyoin")
	private boolean isKyoin;

	private String password;

	@Column(name="user_name")
	private String userName;

	@Transient
	private List<ExerciseProblemStudent> exerciseProblemStudents;

	//bi-directional many-to-one association to Exercise
	@OneToMany(mappedBy="user")
	private List<Exercise> kyoinHasExercises;

	//bi-directional many-to-many association to Exercise
	@ManyToMany(mappedBy="users")
	private List<Exercise> studentHasExercises;

	//bi-directional many-to-one association to FavHistory
	@OneToMany(mappedBy="user")
	private List<FavHistory> favHistories;

	//bi-directional many-to-one association to PostHistory
	@OneToMany(mappedBy="user")
	private List<PostHistory> postHistories;

	//bi-directional many-to-one association to PostHistory
	@OneToMany(mappedBy="user")
	private List<SolvedHistory> solvedHistories;

	//bi-directional many-to-one association to ReplyNotification
	@OneToMany(mappedBy="user")
	private List<ReplyNotification> replyNotifications;

	public User() {}
}