package model.db;

import javax.ejb.Stateless;

import entity.ExerciseProblem;

@Stateless
public class ExerciseProblemDb extends AbstractDb<ExerciseProblem> {
	public ExerciseProblemDb() {
		super(ExerciseProblem.class);
	}
}
