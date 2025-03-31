package entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;


/**
 * The persistent class for the question_fav_user database table.
 *
 */
@Entity
@Table(name="question_fav_user")
@NamedQuery(name="QuestionFavUser.findAll", query="SELECT q FROM QuestionFavUser q")
@Getter @Setter
public class QuestionFavUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private QuestionFavUserPK id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="registed_time", insertable=false)
	private Date registedTime;

	//bi-directional many-to-one association to Question
	@ManyToOne
	@JoinColumn(name="question_id")
	private Question question;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;

	public QuestionFavUser() {
	}
}