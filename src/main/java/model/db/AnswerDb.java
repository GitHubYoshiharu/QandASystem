package model.db;

import javax.ejb.Stateless;

import entity.Answer;

@Stateless
public class AnswerDb extends AbstractDb<Answer> {
	public AnswerDb() {
		super(Answer.class);
	}

	// DBMSが自動挿入した投稿年月日を永続化コンテキストに反映する
	@Override
	public void insert(Answer entity) {
		super.insert(entity);
		getEm().flush();
		getEm().refresh(entity);
	}
}
