package model.db;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import entity.FavHistory;

@Stateless
public class FavHistoryDb extends AbstractDb<FavHistory> {
	public FavHistoryDb() {
		super(FavHistory.class);
	}

	public List<FavHistory> selectByUserId(String userId) {
		String query = "SELECT A FROM FavHistory A WHERE A.user.userId = '" + userId + "'";
		TypedQuery<FavHistory> q = getEm().createQuery(query, FavHistory.class);
		return q.getResultList();
	}
}
