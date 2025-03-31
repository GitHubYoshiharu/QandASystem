package entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;


/**
 * The persistent class for the fav_history database table.
 *
 */
@Entity
@Table(name="fav_history")
@NamedQuery(name="FavHistory.findAll", query="SELECT f FROM FavHistory f")
@Getter @Setter
public class FavHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="fav_history_id")
	private int favHistoryId;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="fav_regist_student_user_id")
	private User user;

	//bi-directional many-to-one association to Question
	@ManyToOne
	@JoinColumn(name="fav_target_question_id")
	private Question question;

    //<!-- マイページ表示用のフィールド
	@Transient
	private String message;

	@Transient
	private String content;

	@Transient
	private int questionId;

	@Transient
	private Date notifiedTime;
	//-->

	public FavHistory() {}

	public FavHistory(User user, Question question) {
		this.user = user;
		this.question = question;
	}
}