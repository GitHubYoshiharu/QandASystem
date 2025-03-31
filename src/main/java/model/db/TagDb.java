package model.db;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import entity.Tag;

@Stateless
public class TagDb extends AbstractDb<Tag> implements Serializable {
	public TagDb() {
		super(Tag.class);
	}

	public void insertNewTag(List<Tag> tagList) {
		// 既に登録されているタグのタグ名のリストを取得する
		String query = "SELECT A FROM Tag A WHERE A.tagName IN(";
		for(Tag tag: tagList) {
			query += "'" + tag.getTagName() + "',";
		}
		query = query.substring( 0, query.length() - 1 ) + ")";
		TypedQuery<Tag> q = getEm().createQuery(query, Tag.class);
		List<Tag> duplicateTagList = q.getResultList();

		// 登録されていないタグは新規登録する
		// また、question_tagテーブルに登録するため、
		// 既に登録されているタグのidを取得してセットする。
		boolean isDuplicate = false;
		for(Tag tag: tagList) {
			for(Tag duplicateTag: duplicateTagList) {
				if ( tag.getTagName().equals( duplicateTag.getTagName() ) ) {
					tag.setTagId( duplicateTag.getTagId() );
					isDuplicate = true;
				}
			}
			// 新規登録する
			// 登録年月日を永続化コンテキストに反映するため
			if (!isDuplicate) {
				this.insert(tag);
				getEm().flush();
				getEm().refresh(tag);
			}
			isDuplicate = false;
		}
	}

	public List<Tag> selectByTagName(String[] tagNameList) {
		// タグ名からタグエンティティのリストを取得する
		// BINARYキャストによって、大文字小文字を区別する
		String query = "SELECT A FROM Tag A WHERE A.tagName IN(";
		for(String tagName: tagNameList) {
			query += "CAST('" + tagName + "' AS BINARY),";
		}
		query = query.substring( 0, query.length() - 1 ) + ")";
		TypedQuery<Tag> q = getEm().createQuery(query, Tag.class);
		return q.getResultList();
	}
}
