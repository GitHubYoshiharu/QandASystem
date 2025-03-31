package model.db;

import javax.ejb.Stateless;

import entity.QuestionViewUser;

@Stateless
public class QuestionViewUserDb extends AbstractDb<QuestionViewUser> {
	public QuestionViewUserDb() {
		super(QuestionViewUser.class);
	}
}
