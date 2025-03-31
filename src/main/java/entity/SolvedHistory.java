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
 * The persistent class for the solved_history database table.
 *
 * ※※※Caution：Triggerにコメントを書いてない(書けない)ので、以下の記述は消さないで※※※
 * solvedClassification:
 *  "1" ... 自己解決
 *  "2" ... 投稿された回答による解決
 */
@Entity
@Table(name="solved_history")
@NamedQuery(name="SolvedHistory.findAll", query="SELECT s FROM SolvedHistory s")
@Getter @Setter
public class SolvedHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="solved_history_id")
	private int solvedHistoryId;

	@Column(name="solved_classification")
	private String solvedClassification;

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

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="question_fav_user_id")
	private User user;

	//bi-directional many-to-one association to Question
	@ManyToOne
	@JoinColumn(name="solved_question_id")
	private Question question;

	public SolvedHistory() {}

	public SolvedHistory(String solvedClassification, Question question, User user) {
		this.solvedClassification = solvedClassification;
		this.question = question;
		this.user = user;
	}
}