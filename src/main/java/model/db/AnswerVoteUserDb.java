package model.db;

import javax.ejb.Stateless;
import javax.persistence.Query;

import entity.AnswerVoteUser;

@Stateless
public class AnswerVoteUserDb extends AbstractDb<AnswerVoteUser> {
	public AnswerVoteUserDb() {
		super(AnswerVoteUser.class);
	}

	// オーバーロード
	public void insert(int answerId, String userId) {
		StringBuilder query_sb = new StringBuilder("INSERT INTO answer_vote_user (answer_id, user_id) VALUES(");
		query_sb.append(answerId + ", '");
		query_sb.append(userId + "')");
		// クエリ実行
		Query q = getEm().createNativeQuery(query_sb.toString());
		q.executeUpdate();
	}
}
