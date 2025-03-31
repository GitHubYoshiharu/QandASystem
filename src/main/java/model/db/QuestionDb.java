package model.db;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import entity.Question;

@Stateless
public class QuestionDb extends AbstractDb<Question> {
	public QuestionDb( ) {
		super(Question.class);
	}

	public List<Question> selectByQId(List<Integer> qIdList) {
		StringBuilder query_sb = new StringBuilder();
		query_sb.append("SELECT A FROM Question as A WHERE A.questionId IN (");
		for(Integer qId: qIdList) {
			query_sb.append(qId + ",");
		}
		query_sb.deleteCharAt( query_sb.length() - 1);
		query_sb.append(")");

		// クエリ実行
		TypedQuery<Question> q = getEm().createQuery(query_sb.toString(), Question.class);
		return q.getResultList();
	}

	public Question dontUseCacheSelect(Object id) {
		Question question = super.select(id);
		getEm().refresh(question);
		return question;
	}

	// DBMSが自動挿入した投稿年月日を永続化コンテキストに反映する
	@Override
	public void insert(Question entity) {
		super.insert(entity);
		getEm().flush();
		getEm().refresh(entity);
	}
}
