package model.db;

import javax.ejb.Stateless;

import entity.Exercise;

@Stateless
public class ExerciseDb extends AbstractDb<Exercise> {
	public ExerciseDb() {
		super(Exercise.class);
	}
}
