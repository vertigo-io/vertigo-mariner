package io.vertigo.behaviortree;

import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

import BehaviorTree.BTBlackBoard;
import BehaviorTree.BTCondition;
import BehaviorTree.BTNode;
import BehaviorTree.BTStatus;
import io.vertigo.core.util.StringUtil;

public final class State {
	private final Scanner sc = new Scanner(System.in);
	public final BTBlackBoard bb = new BTBlackBoard();

	public BTNode set(final String key, final int value) {
		return set(key, "" + value);
	}

	public BTNode set(final String key, final String value) {
		return () -> {
			bb.put(key, value);
			return BTStatus.Succeeded;
		};
	}

	private String answer(final String question) {
		System.out.println(bb.format(question));
		//---
		return sc.nextLine();
	}

	private BTNode query(final String key, final String question, final Predicate<String> validator) {
		final String response = answer(question);
		if (validator.test(response)) {
			return set(key, response);
		}
		System.err.println("error parsing : result " + question);
		return BTNode.fail();
	}

	public BTCondition isFulFilled(final String key) {
		return BTNode.condition(() -> bb.get(key) != null);
	}

	public BTNode fulfill(final String key, final String question) {
		final Predicate<String> validator = t -> !StringUtil.isBlank(t);
		return fulfill(key, question, validator);
	}

	public BTNode fulfill(final String key, final String question, final String... choices) {
		final List<String> choiceList = List.of(choices);
		final Predicate<String> validator = t -> choiceList.contains(t);
		return fulfill(key, question, validator);
	}

	public BTNode fulfill(final String key, final String question, final Predicate<String> validator) {
		return BTNode.selector(
				isFulFilled(key),
				query(key, question, validator));
	}

	public BTNode display(final String msg) {
		return () -> {
			System.out.println(bb.format(msg));
			return BTStatus.Succeeded;
		};
	}

	public BTNode inc(final String key) {
		return () -> {
			bb.inc(key);
			return BTStatus.Succeeded;
		};
	}

	public BTCondition eq(final String key, final String compare) {
		return BTNode.condition(() -> bb.eq(key, compare));
	}

	public BTCondition gt(final String key, final String compare) {
		return BTNode.condition(() -> bb.gt(key, compare));
	}

	public BTCondition lt(final String key, final String compare) {
		return BTNode.condition(() -> bb.lt(key, compare));
	}

	public BTNode clear(final String keyPattern) {
		return () -> {
			bb.clear(keyPattern);
			return BTStatus.Succeeded;
		};
	}

	public BTNode clearAll() {
		return () -> {
			bb.clearAll();
			return BTStatus.Succeeded;
		};
	}

	@Override
	public String toString() {
		return bb.toString();
	}
}
