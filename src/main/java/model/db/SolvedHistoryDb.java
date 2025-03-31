package model.db;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import entity.SolvedHistory;

@Stateless
public class SolvedHistoryDb extends AbstractDb<SolvedHistory> {
	public SolvedHistoryDb() {
		super(SolvedHistory.class);
	}

	public List<SolvedHistory> selectByUserId(String userId) {
		String query = "SELECT A FROM SolvedHistory A WHERE A.user.userId = '" + userId + "'";
		TypedQuery<SolvedHistory> q = getEm().createQuery(query, SolvedHistory.class);
		return q.getResultList();
	}
}
