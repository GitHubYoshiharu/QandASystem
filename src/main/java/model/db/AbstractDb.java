package model.db;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import lombok.Getter;

// 継承による使用を前提とするため、抽象クラスとした
public abstract class AbstractDb<T> {
	@PersistenceContext(name="qandaPU")
	@Getter
	private EntityManager em;
	protected Class<T> entityClass;

	public AbstractDb(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	//<!-- CRUD処理を実現するメソッド群（名前はSQL風）
	public T select(Object id) {
		return em.find(this.entityClass, id);
	}
	public void update(T entity) {
		em.merge(entity);
	}
	public void delete(T entity) {
		// 削除対象のエンティティを先にmergeすることで（値は更新しない）、
		// そのエンティティを永続性コンテキストの中に置く（じゃないと削除できない）
		em.remove(em.merge(entity));
	}
	public void insert(T entity) {
		em.persist(entity);
	}
	//-->

	//<!-- その他便利メソッド群
	// 該当テーブルからレコードを全件取得する
	public List<T> selectAll() {
		String query = "SELECT A FROM " + entityClass.getCanonicalName() + " A";
		TypedQuery<T> q = getEm().createQuery(query, entityClass);
		return q.getResultList();
	}

	// 指定範囲のレコードを取得する
	// 実際のレコード数が少なければ、可能な限り取得する
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> selectRange(int firstIndex, int lastIndex) {
		CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
		cq.select(cq.from(entityClass));
		Query q = em.createQuery(cq);
		q.setMaxResults(lastIndex - firstIndex + 1);
		q.setFirstResult(firstIndex);
		return q.getResultList();
	}

	// 総レコード数を取得する
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int count() {
		CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
		Root<T> rt = cq.from(entityClass);
		cq.select(em.getCriteriaBuilder().count(rt));
		Query q = em.createQuery(cq);
		return ((Long) q.getSingleResult()).intValue();
	}
	// -->
}
