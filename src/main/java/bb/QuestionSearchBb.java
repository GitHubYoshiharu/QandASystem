package bb;

import static java.util.Comparator.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import entity.Exercise;
import entity.Question;
import entity.Tag;
import entity.TagSearchUser;
import entity.User;
import lombok.Getter;
import lombok.Setter;
import model.db.ExerciseDb;
import model.db.QuestionDb;
import model.db.QuestionTagDb;
import model.db.TagDb;
import model.db.TagSearchUserDb;
import model.db.UserDb;

@Named
@ViewScoped
public class QuestionSearchBb extends AbstractBb implements Serializable {
	@Getter @Setter
	private String spaceDividedSearchTag;
	@Getter @Setter
	private Integer solvedDivision = 3;
	@Getter @Setter
	private Integer exNum = null;
	@Getter @Setter
	private Integer proNum = null;
	@Getter @Setter
	private Integer exerciseId = null;
	@Getter @Setter
	private Integer sortType = 1;
	@Getter @Setter
	private List<Question> hitQuestionList = new ArrayList<>();

	// 講義選択項目に使用する、講義名とidのMap
	@Getter @Setter
	private Map<String, Integer> courseItems;
	// 検索ユーザ
	@Getter @Setter
	private User user;

	@EJB private TagDb tagDb;
	@EJB private QuestionDb questionDb;
	@EJB private QuestionTagDb questionTagDb;
	@EJB private TagSearchUserDb tagSearchUserDb;
	@EJB private ExerciseDb exerciseDb;
	@EJB private UserDb userDb;

	@Override
	@PostConstruct
	public void init() {
		// 講義選択項目を作成する
		courseItems = new LinkedHashMap<>();
		List<Exercise> allCourseList = exerciseDb.selectAll();
		for(Exercise e: allCourseList) {
			courseItems.put( e.getCourseName() + " " + e.getBeginYear(), e.getExerciseId() );
		}
		// ログインユーザを取得する
		user = userDb.select( this.getUserId() );
	}

	public String questionSearch() {
		String[] searchTags = this.spaceDividedSearchTag.split("[\\s　]+");
		List<Tag> hitTagList = new ArrayList<>();
		List<Question> hitQuestionList = new ArrayList<>();
		if ( !(searchTags == null) ) {
			hitTagList = tagDb.selectByTagName(searchTags);
			if ( hitTagList.size() != 0 ) {
				// 登録されていないタグが検索で指定された場合は、
				// その時点でAND検索にヒットしないので、結果を返す
				if ( hitTagList.size() != searchTags.length ) {
					this.hitQuestionList = hitQuestionList;
					System.out.println("0件ヒットしました。");
					return null;
				}
				// タグ検索数を加算する
				for(Tag searchedTag: hitTagList) {
					TagSearchUser tagSearchUser = new TagSearchUser();
					tagSearchUser.setTag(searchedTag);
					tagSearchUser.setUser(user);
					tagSearchUserDb.insert(tagSearchUser);
				}
				// 質問検索
				List<Integer> qIdList = questionTagDb.selectQIdByTag(hitTagList);
				hitQuestionList = questionDb.selectByQId(qIdList);
				if (hitQuestionList.size() != 0) {
					// 絞り込み条件を適応する
					questionNarrowing(hitQuestionList);
					// ソート条件を適応する
					questionSort(hitQuestionList);
				}
			}
		}
		this.hitQuestionList = hitQuestionList;
		System.out.println(hitQuestionList.size() + "件ヒットしました。");
		return null;
	}

	// ※removeは要素を自動的に前方に詰めるので、ループ中にremoveするとインデックスがずれてしまう
	private void questionNarrowing(List<Question> hitQuestionList) {
		ArrayList<Question> removeQuestionList = new ArrayList<>();
		for (int i = 0; i < hitQuestionList.size(); i++) {
			if (exerciseId != null) {
				if (hitQuestionList.get(i).getExerciseNum() != null
					&& hitQuestionList.get(i).getExerciseNum().getId().getExerciseId() != exerciseId) {
					removeQuestionList.add(hitQuestionList.get(i));
					continue;
				}
			}
			if (exNum != null) {
				if (hitQuestionList.get(i).getExerciseNum() != null
					&& hitQuestionList.get(i).getExerciseNum().getId().getExerciseNum() != exNum) {
					removeQuestionList.add(hitQuestionList.get(i));
					continue;
				}
			}
			if (proNum != null) {
				if (hitQuestionList.get(i).getExerciseProblem() != null
					&& hitQuestionList.get(i).getExerciseProblem().getId().getExerciseProblemNum() != proNum) {
					removeQuestionList.add(hitQuestionList.get(i));
					continue;
				}
			}
			switch(solvedDivision) {
				case 1: // 未解決
					if ( hitQuestionList.get(i).isHasSolved() ) {
						removeQuestionList.add(hitQuestionList.get(i));
						continue;
					}
					break;
				case 2: // 解決
					if ( ! hitQuestionList.get(i).isHasSolved() ) {
						removeQuestionList.add(hitQuestionList.get(i));
						continue;
					}
					break;
				case 3: // 全て
					// 何もしない
					break;
			}
		}
		// ループ中にセットされた質問を全てリストから取り除くことで絞り込む
		hitQuestionList.removeAll(removeQuestionList);
	}

	private void questionSort(List<Question> hitQuestionList) {
		switch(sortType) {
			case 1: // 質問投稿年月日順
				hitQuestionList.sort( comparing( x -> x.getPostTime(), reverseOrder() ) );
				break;
			case 2: // 質問閲覧数順
				hitQuestionList.sort( comparing( x -> x.getQuestionViewUsers().size(), reverseOrder() ) );
				break;
			case 3: // 質問お気に入り登録数順
				hitQuestionList.sort( comparing( x -> x.getQuestionFavUsers().size(), reverseOrder() ) );
				break;
			case 4: // 回答数順
				hitQuestionList.sort( comparing( x -> x.getAnswers().size(), reverseOrder() ) );
				break;
			case 5: // TODO: 予測質問お気に入り登録数順
				break;
		}
	}
}
