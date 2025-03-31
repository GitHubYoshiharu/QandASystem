package bb;

import static java.util.Comparator.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import entity.Exercise;
import entity.ExerciseNum;
import entity.ExerciseProblem;
import entity.ExerciseProblemStudent;
import entity.Tag;
import entity.TaglistComponent;
import entity.User;
import lombok.Getter;
import lombok.Setter;
import model.db.ExerciseDb;
import model.db.ExerciseProblemStudentDb;
import model.db.TaglistComponentDb;
import model.db.UserDb;

@Named
@ViewScoped
public class ExerciseScoreRegisterBb extends AbstractBb {
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
	private Map<String, Integer> taglistItems;
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
	//-->
	//-->

	// 検索してヒットした演習回
	// フィールドから推移的に全ての表示に必要な値を取得できる
	@Getter @Setter
	private ExerciseNum searchedExerciseNum;
	// ページを見ているユーザ
	@Getter @Setter
	private User viewUser;

	@EJB private ExerciseDb exerciseDb;
	@EJB private UserDb userDb;
	@EJB private ExerciseProblemStudentDb exerciseProblemStudentDb;
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

		// ページを見ているユーザを取得する
		viewUser = userDb.select(this.getUserId());
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

						// JSFページとバインドさせるために、学生ごとに、演習点が未登録の場合は、
						// ExerciseProblemStudentインスタンスを生成してリストに追加する
						for(User student: searchedExerciseNum.getExercise().getUsers()) {
							student.setExerciseProblemStudents(new ArrayList<ExerciseProblemStudent>());
							for(ExerciseProblem ep: searchedExerciseNum.getExerciseProblems()) {
								// 検索用・追加用にインスタンスを生成する
								ExerciseProblemStudent eps = new ExerciseProblemStudent(ep.getId(), student.getUserId());
								// 既に登録されているかどうかをDB上で検索する
								ExerciseProblemStudent epsFound = exerciseProblemStudentDb.select(eps.getId());
								if(epsFound != null) {
									// 既に当該演習点が登録されている場合、それを追加する
									epsFound.setRegisteredTagItems();
									student.getExerciseProblemStudents().add(epsFound);
								} else {
									// 登録されていなかった場合、先ほど生成したオブジェクトを追加する
									student.getExerciseProblemStudents().add(eps);
								}
							}
							// 問題番号昇順でソートする
							student.getExerciseProblemStudents().sort( comparing( x -> x.getId().getExerciseProblemNum(), naturalOrder() ) );
						}

						// 指定された演習回に登録されたタグリストから、タグの選択肢を作成する
						taglistItems = new LinkedHashMap<>();
						for(int i = 0; i < searchedExerciseNum.getTaglistComponents().size(); i++) {
							TaglistComponent tc = searchedExerciseNum.getTaglistComponents().get(i);
							taglistItems.put(tc.getTag().getTagName(), i);
						}
						return null;
					}
				}
			}
		}
		this.facesMessage("該当する演習回は見つかりませんでした");
		return null;
	}

	public String addTag(int exerciseProblemIndex, int exerciseProblemStudentIndex) {
		ExerciseProblemStudent targetExerciseProblemStudent
			= searchedExerciseNum.getExercise().getUsers().get(exerciseProblemStudentIndex).getExerciseProblemStudents().get(exerciseProblemIndex);
		if(targetExerciseProblemStudent.getTags() == null) {
			targetExerciseProblemStudent.setTags(new ArrayList<Tag>());
		}
		// 既に登録されているタグの場合、登録処理を行わない
		for(Tag tag: targetExerciseProblemStudent.getTags()) {
			// 左辺：既に登録されているタグ
			// 右辺：今回ユーザに選択されたタグ
			if( tag.getTagId() == searchedExerciseNum.getTaglistComponents().get(targetExerciseProblemStudent.getTagListComponentListIndex()).getTag().getTagId() ) {
				return null;
			}
		}
		// 登録タグに選択されたタグを追加
		targetExerciseProblemStudent.getTags().add(searchedExerciseNum.getTaglistComponents().get(targetExerciseProblemStudent.getTagListComponentListIndex()).getTag());
		// フィールドのtagsから表示用の選択肢を作成してセットする、引数無しのsetter
		targetExerciseProblemStudent.setRegisteredTagItems();
		// 減点タグ使用数を加算する
		searchedExerciseNum.getTaglistComponents().get(targetExerciseProblemStudent.getTagListComponentListIndex()).incrementNumOfUsed();
		taglistComponentDb.update(searchedExerciseNum.getTaglistComponents().get(targetExerciseProblemStudent.getTagListComponentListIndex()));
		targetExerciseProblemStudent.setTagListComponentListIndex(null);
		return null;
	}

	public String resetTag(int exerciseProblemIndex, int exerciseProblemStudentIndex) {
		ExerciseProblemStudent targetExerciseProblemStudent
			= searchedExerciseNum.getExercise().getUsers().get(exerciseProblemStudentIndex).getExerciseProblemStudents().get(exerciseProblemIndex);
		// 登録されていた全ての減点タグの使用数を減算する
		for(TaglistComponent tagListComponent: searchedExerciseNum.getTaglistComponents()) {
			for(Tag tag: targetExerciseProblemStudent.getTags()) {
				if(tag.getTagId() == tagListComponent.getTag().getTagId()) {
					tagListComponent.decrementNumOfUsed();
					taglistComponentDb.update(tagListComponent);
					break;
				}
			}
		}
		targetExerciseProblemStudent.setRegisteredTagItems(null);
		targetExerciseProblemStudent.setTags(null);
		return null;
	}

	public String registerExerciseInfo(int exerciseProblemIndex, int exerciseProblemStudentIndex) {
		ExerciseProblemStudent targetExerciseProblemStudent
			= searchedExerciseNum.getExercise().getUsers().get(exerciseProblemStudentIndex).getExerciseProblemStudents().get(exerciseProblemIndex);
		targetExerciseProblemStudent.setUser(viewUser);
		if(targetExerciseProblemStudent.getLastModifiedTime() == null) {
			exerciseProblemStudentDb.insert(targetExerciseProblemStudent);
		} else {
			exerciseProblemStudentDb.update(targetExerciseProblemStudent);
		}
		return null;
	}
}
