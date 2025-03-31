package model.db;

import javax.ejb.Stateless;
import javax.persistence.Query;

import entity.TagSearchUser;

@Stateless
public class TagSearchUserDb extends AbstractDb<TagSearchUser> {
	public TagSearchUserDb() {
		super(TagSearchUser.class);
	}

	// オーバーロード
	public void insert(int tagId, String userId) {
		StringBuilder query_sb = new StringBuilder("INSERT INTO tag_search_user (tag_id, user_id) VALUES(");
		query_sb.append(tagId + ", '");
		query_sb.append(userId + "', '");
		// クエリ実行
		Query q = getEm().createNativeQuery(query_sb.toString());
		q.executeUpdate();
	}
}
