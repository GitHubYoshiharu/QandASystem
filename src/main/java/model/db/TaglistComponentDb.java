package model.db;

import javax.ejb.Stateless;

import entity.TaglistComponent;

@Stateless
public class TaglistComponentDb extends AbstractDb<TaglistComponent> {
	public TaglistComponentDb() {
		super(TaglistComponent.class);
	}
}
