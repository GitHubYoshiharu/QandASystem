package model.db;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import entity.PostHistory;

@Stateless
public class PostHistoryDb extends AbstractDb<PostHistory> {
	public PostHistoryDb() {
		super(PostHistory.class);
	}

	public List<PostHistory> selectByUserId(String userId) {
		String query = "SELECT A FROM PostHistory A WHERE A.user.userId = '" + userId + "'";
		TypedQuery<PostHistory> q = getEm().createQuery(query, PostHistory.class);
		return q.getResultList();
	}
}
