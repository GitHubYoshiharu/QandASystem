package model.db;

import javax.ejb.Stateless;

import entity.AnswerTargetComment;

@Stateless
public class AnswerTargetCommentDb extends AbstractDb<AnswerTargetComment> {
	public AnswerTargetCommentDb() {
		super(AnswerTargetComment.class);
	}

	// DBMSが自動挿入した投稿年月日を永続化コンテキストに反映する
	@Override
	public void insert(AnswerTargetComment entity) {
		super.insert(entity);
		getEm().flush();
		getEm().refresh(entity);
	}
}
