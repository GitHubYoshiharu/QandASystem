package model.db;

import javax.ejb.Stateless;

import entity.ExerciseProblemStudent;

@Stateless
public class ExerciseProblemStudentDb extends AbstractDb<ExerciseProblemStudent> {
	public ExerciseProblemStudentDb() {
		super(ExerciseProblemStudent.class);
	}

	// DBMSが自動挿入した投稿年月日を永続化コンテキストに反映する
	@Override
	public void insert(ExerciseProblemStudent entity) {
		super.insert(entity);
		getEm().flush();
		getEm().refresh(entity);
	}
}
