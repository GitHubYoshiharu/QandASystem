package model.db;

import javax.ejb.Stateless;
import javax.persistence.Query;

import entity.QuestionFavUser;

@Stateless
public class QuestionFavUserDb extends AbstractDb<QuestionFavUser> {
	public QuestionFavUserDb() {
		super(QuestionFavUser.class);
	}

	// オーバーロード
	public void insert(int questionId, String userId) {
		StringBuilder query_sb = new StringBuilder("INSERT INTO question_fav_user (question_id, user_id) VALUES(");
		query_sb.append(questionId + ", '");
		query_sb.append(userId + "')");
		// クエリ実行
		Query q = getEm().createNativeQuery(query_sb.toString());
		q.executeUpdate();
	}
}
