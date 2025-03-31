package bb;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.Pattern;

import entity.Exercise;
import entity.ExerciseNum;
import entity.Tag;
import entity.TaglistComponent;
import lombok.Getter;
import lombok.Setter;
import model.db.ExerciseDb;
import model.db.TagDb;
import model.db.TaglistComponentDb;

@Named
@ViewScoped
public class TagListEditBb extends AbstractBb {
	//<!-- 演習回検索に関連するフィールド
	// 登録されている全講義を取得しておき、検索で指定された値を用いて、
	// AP側でこのコレクションの中から検索する
	private List<Exercise> allCourseList;
	//<!-- 選択肢
	@Getter @Setter
	private Map<Integer, Integer> beginYearItems;
	@Getter @Setter
	private Map<String, String> courseNameItems;
	@Getter @Setter
	private Map<String, String> kyoinNameItems;
	@Getter @Setter
	private Map<Integer, Integer> exerciseNumItems;
	@Getter @Setter
	private Map<String, String> taglistItems; // この演習回に登録されているタグリストのタグの選択肢
	@Getter @Setter
	private Map<String, String> registeredTagItems; // 登録されている全てのタグの選択肢
	//-->
	//<!-- ユーザ選択値
	@Getter @Setter
	private Integer selectedBeginYear;
	@Getter @Setter
	private String selectedCourseName;
	@Getter @Setter
	private String selectedKyoinName;
	@Getter @Setter
	private Integer selectedExerciseNum;
	@Getter @Setter
	private String selectedTaglistTagName;
	@Getter @Setter
	private String selectedRegisteredTagName;
	//-->
	//-->

	// 登録されているすべてのタグのリスト
	@Getter @Setter
	private List<Tag> registeredTagList;

	// 検索してヒットした演習回
	@Getter @Setter
	private ExerciseNum searchedExerciseNum;

	// ユーザ入力タグ名
	@Getter @Setter @Pattern(regexp="[^!\"#$%&'()*+,-./:;<=>?@[¥]^`{|}~]+")
	private String registerAndAddTagName;

	@EJB private ExerciseDb exerciseDb;
	@EJB private TagDb tagDb;
	@EJB private TaglistComponentDb taglistComponentDb;

	@Override
	@PostConstruct
	public void init() {
		// 登録されている講義を全て取得する
		allCourseList = exerciseDb.selectAll();
		// 開講年度、講義名、教員名の選択肢を作成する
		beginYearItems = new LinkedHashMap<>();
		courseNameItems = new LinkedHashMap<>();
		kyoinNameItems = new LinkedHashMap<>();
		for(Exercise e: allCourseList) {
			beginYearItems.put(e.getBeginYear(), e.getBeginYear());
			courseNameItems.put(e.getCourseName(), e.getCourseName());
			kyoinNameItems.put( e.getUser().getUserName(), e.getUser().getUserName());
		}
		// 演習番号の選択肢は、1から15の範囲にする
		exerciseNumItems = new LinkedHashMap<>();
		for(int i = 1; i <= 15; i++) exerciseNumItems.put(i, i);

		// 登録されているタグを全て取得し、タグの選択肢を作成する
		registeredTagList = tagDb.selectAll();
		registeredTagItems = new LinkedHashMap<>();
		for(int i = 0; i < registeredTagList.size(); i++) {
			Tag tag = registeredTagList.get(i);
			registeredTagItems.put(tag.getTagName(), tag.getTagName());
		}
	}

	public String exerciseNumSearch() {
		for(Exercise e: allCourseList) {
			// 指定された4つの条件に当てはまる演習回を検索する
			if(e.getBeginYear() == selectedBeginYear
				&& e.getCourseName().equals(selectedCourseName)
				&& e.getUser().getUserName().equals(selectedKyoinName)) {
				for(ExerciseNum en: e.getExerciseNums()) {
					if(en.getId().getExerciseNum() == selectedExerciseNum) { // ヒットした場合
						// ページ下部に表示する演習回をセット
						searchedExerciseNum = en;

						// 指定された演習回に登録されたタグリストから、タグの選択肢を作成する
						taglistItems = new LinkedHashMap<>();
						for(int i = 0; i < searchedExerciseNum.getTaglistComponents().size(); i++) {
							TaglistComponent tc = searchedExerciseNum.getTaglistComponents().get(i);
							taglistItems.put(tc.getTag().getTagName(), tc.getTag().getTagName());
						}
						return null;
					}
				}
			}
		}
		this.facesMessage("該当する演習回は見つかりませんでした");
		return null;
	}

	public String addTagFromAddTagList() {
		if( taglistItems.get(selectedRegisteredTagName) == null ) { // 同名のタグがタグリスト中に存在していない場合
			// タグリストへ追加
			taglistItems.put(selectedRegisteredTagName, selectedRegisteredTagName);
			// タグ名からタグIDを検索する
			Integer targetTagId = null;
			for(Tag tag: registeredTagList) {
				if(tag.getTagName().equals(selectedRegisteredTagName)) {
					targetTagId = tag.getTagId();
					break;
				}
			}
			TaglistComponent newTc =  new TaglistComponent(searchedExerciseNum, targetTagId);
			searchedExerciseNum.getTaglistComponents().add(newTc);
			// テーブルへの追加
			taglistComponentDb.insert(newTc);
		} else { // 既に存在している場合
			return null;
		}
		selectedRegisteredTagName = null;
		return null;
	}

	public String addTagFromTextForm() {
		if( registeredTagItems.get(registerAndAddTagName) == null ) { // 同名のタグが登録済みタグリスト中に存在していない場合
			// タグを新規登録する
			Tag insertTag = new Tag();
			insertTag.setTagName(registerAndAddTagName);
			tagDb.insert(insertTag);
			// 登録済みタグリストへ追加
			registeredTagItems.put(insertTag.getTagName(), insertTag.getTagName());
			// タグリストへ追加
			taglistItems.put(insertTag.getTagName(), insertTag.getTagName());
			TaglistComponent newTc =  new TaglistComponent(searchedExerciseNum, insertTag.getTagId());
			searchedExerciseNum.getTaglistComponents().add(newTc);
			// テーブルへの追加
			taglistComponentDb.insert(newTc);

		} else { // 既に存在している場合
			if ( taglistItems.get(registerAndAddTagName) == null ) { // 同名のタグがタグリスト中に存在していない場合
				// タグリストへ追加
				taglistItems.put(registerAndAddTagName, registeredTagItems.get(registerAndAddTagName));
				// タグ名からタグIDを検索する
				Integer targetTagId = null;
				for(Tag tag: registeredTagList) {
					if(tag.getTagName().equals(registerAndAddTagName)) {
						targetTagId = tag.getTagId();
						break;
					}
				}
				TaglistComponent newTc =  new TaglistComponent(searchedExerciseNum, targetTagId);
				searchedExerciseNum.getTaglistComponents().add(newTc);
				// テーブルへの追加
				taglistComponentDb.insert(newTc);
			} else { // 既に存在している場合
				return null;
			}
		}
		registerAndAddTagName = null;
		return null;
	}

	public String deleteTagFromTagList() {
		// タグリスト選択肢から削除
		taglistItems.remove(selectedTaglistTagName);
		// タグ名からタグIDを検索する
		Integer targetTagId = null;
		for(Tag tag: registeredTagList) {
			if(tag.getTagName().equals(selectedTaglistTagName)) {
				targetTagId = tag.getTagId();
				break;
			}
		}
		// タグリストから削除するタグのインデックスを検索する
		for(int i = 0; i < searchedExerciseNum.getTaglistComponents().size(); i++) {
			TaglistComponent tc = searchedExerciseNum.getTaglistComponents().get(i);
			if(tc.getId().getTagId() == targetTagId) {
				// タグリストから削除する
				searchedExerciseNum.getTaglistComponents().remove(i);
				// DB上のタグリストから削除
				taglistComponentDb.delete(tc);
				break;
			}
		}
		selectedTaglistTagName = null;
		return null;
	}
}
