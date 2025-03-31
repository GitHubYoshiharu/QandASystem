package model.db;

import javax.ejb.Stateless;

import entity.User;

@Stateless
public class UserDb extends AbstractDb<User>{
	public UserDb() {
		super(User.class);
	}

	// 以下の2つの条件を満たす時、trueを返す
	// 1. 渡された学籍番号のレコードが存在する → ユーザ登録権限がある
	// 2. 当該レコードのpasswordの値がnull → まだユーザ登録が行われていない
	public boolean canRegist(String gakusekiBango) {
		User user = this.select(gakusekiBango);
		if(user != null && user.getPassword() == null) {
			return true;
		} else {
			return false;
		}
	}
}
