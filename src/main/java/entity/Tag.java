package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;


/**
 * The persistent class for the tag database table.
 *
 */
@Entity
@NamedQuery(name="Tag.findAll", query="SELECT t FROM Tag t")
@Getter @Setter
public class Tag implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="tag_id")
	private int tagId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="registed_time", insertable=false)
	private Date registedTime;

	@Column(name="tag_name")
	private String tagName;

	//bi-directional many-to-many association to Question
	@ManyToMany(mappedBy="tags")
	private List<Question> questions;

	//bi-directional many-to-one association to QuestionTag
	@OneToMany(mappedBy="tag")
	private List<QuestionTag> questionTags;

	//bi-directional many-to-one association to TagSearchUser
	@OneToMany(mappedBy="tag")
	private List<TagSearchUser> tagSearchUsers;

	//bi-directional many-to-one association to TaglistComponent
	@OneToMany(mappedBy="tag")
	private List<TaglistComponent> taglistComponents;

	public Tag() {}
}