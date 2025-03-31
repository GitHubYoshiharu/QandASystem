package bb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import entity.Exercise;
import entity.ExerciseProblem;
import entity.ExerciseProblemPK;
import entity.PostHistory;
import entity.Question;
import entity.Tag;
import entity.User;
import lombok.Getter;
import lombok.Setter;
import model.db.ExerciseNumDb;
import model.db.ExerciseProblemDb;
import model.db.PostHistoryDb;
import model.db.QuestionDb;
import model.db.TagDb;
import model.db.UserDb;

@Named
@ViewScoped
public class QuestionPostBb extends AbstractBb implements Serializable {
	@Getter @Setter
	@Min(1) @Max(15)
	private Integer exNum;
	@Getter @Setter
	@Min(1) @Max(10)
	private Integer proNum;
	@Getter @Setter
	private Integer exerciseId;
	@Getter @Setter
	@Pattern(regexp="[^!\"#$%&'()*+,-./:;<=>?@[¥]^`{|}~]+")
	private String spaceDividedTag;
	@Getter @Setter
	private List<String> tagList;
	@Getter @Setter
	@Size(max=100) @NotNull
	private String title;
	@Getter @Setter @NotNull
	private String content;

	// ログイン学生
	@Getter @Setter
	private User postUser;
	// 講義選択項目に使用する、講義名とidのMap
	@Getter @Setter
	private Map<String, Integer> courseItems;

	@EJB private QuestionDb questionDb;
	@EJB private TagDb tagDb;
	@EJB private UserDb userDb;
	@EJB private PostHistoryDb postHistoryDb;
	@EJB private ExerciseNumDb exerciseNumDb;
	@EJB private ExerciseProblemDb exerciseProblemDb;

	@Override
	@PostConstruct
	public void init() {
		// 入力タグ格納リストをインスタンス化
		this.tagList = new ArrayList<String>();

		// ログインユーザを取得する
		postUser = userDb.select( this.getUserId() );

		// ログインユーザ用の講義選択項目を作成する
		courseItems = new LinkedHashMap<>();
		for( Exercise e: postUser.getStudentHasExercises() ) {
			courseItems.put( e.getCourseName(), e.getExerciseId() );
		}
	}

	public String questionPost() {
		//<!-- 入力されたタグの中に、まだ登録されていないタグがあれば、新規登録する
		// タグ登録の仕組み上、バリデーションが付けられないので、以下で代用する
		if (tagList.size() == 0) {
			this.facesMessage(FacesMessage.SEVERITY_ERROR, "タグを１つ以上追加してください。");
			return "/authed/questionPost.xhtml";
		}

		ArrayList<Tag> registTagList = new ArrayList<>();
		for(String tagName: tagList) {
			Tag tag = new Tag();
			tag.setTagName(tagName);
			registTagList.add(tag);
		}
		tagDb.insertNewTag(registTagList);
		//-->

		//<!-- 質問のデータをセットし、投稿する
		Question question = new Question();
		//<!-- 演習番号・演習問題番号を質問にセットする
		if (exNum == null || exerciseId == null || proNum == null) {} //登録できないので、しない
		else {
			// 演習番号、及び演習問題番号をセットする
			ExerciseProblem exerciseProblem = new ExerciseProblem();
			ExerciseProblemPK exerciseProblemPK = new ExerciseProblemPK();
			exerciseProblemPK.setExerciseId(exerciseId);
			exerciseProblemPK.setExerciseNum(exNum);
			exerciseProblemPK.setExerciseProblemNum(proNum);
			exerciseProblem.setId(exerciseProblemPK);
			question.setExerciseProblem(exerciseProblem);
		}
		//-->
		question.setTitle(title);
		question.setContent(content);
		question.setTags(registTagList);
		question.setUser(postUser);
		questionDb.insert(question);
		//-->

		// 当該ユーザの投稿履歴を登録する
		PostHistory postHistory = new PostHistory("1", question, postUser);
		postHistoryDb.insert(postHistory);
		this.facesMessage("質問の投稿に成功しました。");
		return "/authed/questionPost.xhtml";
	}

	public String addTag() {
		if (spaceDividedTag == null) return null;
		// 「\s」では全角スペースを表現できない
		String[] addTags = this.spaceDividedTag.split("[\\s　]+");
		for(String addTag: addTags) {
			if(!this.tagList.contains(addTag)) {
				this.tagList.add(addTag);
			}
		}
		this.spaceDividedTag = null;
		return null;
	}

	public String deleteTag(int index) {
		this.tagList.remove(index);
		return null;
	}
}
