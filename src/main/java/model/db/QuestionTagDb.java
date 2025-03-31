package model.db;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import entity.QuestionTag;
import entity.Tag;

@Stateless
public class QuestionTagDb extends AbstractDb<QuestionTag> {
	public QuestionTagDb( ) {
		super(QuestionTag.class);
	}

	@SuppressWarnings("unchecked")
	public List<Integer> selectQIdByTag(List<Tag> tagList) {
		// AND検索
		// ※パラメータはWHERE句の中にしか入れられないので、可読性が低いけど、お兄さん許して！
		StringBuilder query_sb = new StringBuilder();
		query_sb.append("SELECT DISTINCT A0.question_id FROM question_tag as A0 ");
		for(int i = 0; i < tagList.size(); i++) {
			query_sb.append("JOIN (SELECT question_id FROM question_tag WHERE tag_id = ");
			query_sb.append(tagList.get(i).getTagId());
			query_sb.append(") as A" + (i+1) );
			query_sb.append(" ON A" + i + ".question_id = A" + (i+1) + ".question_id ");
		}

		// クエリ実行
		Query q = getEm().createNativeQuery(query_sb.toString());
		return (List<Integer>) q.getResultList();
	}
}
