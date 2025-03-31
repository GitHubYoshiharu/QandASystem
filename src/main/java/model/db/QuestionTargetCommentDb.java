package model.db;

import javax.ejb.Stateless;

import entity.QuestionTargetComment;

@Stateless
public class QuestionTargetCommentDb extends AbstractDb<QuestionTargetComment> {
	public QuestionTargetCommentDb() {
		super(QuestionTargetComment.class);
	}

	// DBMSが自動挿入した投稿年月日を永続化コンテキストに反映する
	@Override
	public void insert(QuestionTargetComment entity) {
		super.insert(entity);
		getEm().flush();
		getEm().refresh(entity);
	}
}
