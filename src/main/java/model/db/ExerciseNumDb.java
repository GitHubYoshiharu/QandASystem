package model.db;

import javax.ejb.Stateless;

import entity.ExerciseNum;

@Stateless
public class ExerciseNumDb extends AbstractDb<ExerciseNum> {
	public ExerciseNumDb() {
		super(ExerciseNum.class);
	}
}
