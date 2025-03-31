package entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;


/**
 * The persistent class for the question_tag database table.
 *
 */
@Entity
@Table(name="question_tag")
@NamedQuery(name="QuestionTag.findAll", query="SELECT q FROM QuestionTag q")
@Getter @Setter
public class QuestionTag implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private QuestionTagPK id;

	//bi-directional many-to-one association to Question
	@ManyToOne
	@JoinColumn(name="question_id")
	private Question question;

	//bi-directional many-to-one association to Tag
	@ManyToOne
	@JoinColumn(name="tag_id")
	private Tag tag;

	public QuestionTag() {
	}
}