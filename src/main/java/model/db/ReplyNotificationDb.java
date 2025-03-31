package model.db;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import entity.ReplyNotification;

@Stateless
public class ReplyNotificationDb extends AbstractDb<ReplyNotification> {
	public ReplyNotificationDb() {
		super(ReplyNotification.class);
	}

	public List<ReplyNotification> selectByUserId(String userId) {
		String query = "SELECT A FROM ReplyNotification A WHERE A.user.userId = '" + userId + "'";
		TypedQuery<ReplyNotification> q = getEm().createQuery(query, ReplyNotification.class);
		return q.getResultList();
	}
}
