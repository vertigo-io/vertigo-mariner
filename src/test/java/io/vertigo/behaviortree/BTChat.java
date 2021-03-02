package io.vertigo.behaviortree;

import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

import BehaviorTree.BTBlackBoard;
import BehaviorTree.BTCondition;
import BehaviorTree.BTNode;
import BehaviorTree.BTStatus;
import io.vertigo.core.util.StringUtil;

public final class BTChat {
	private final Scanner sc = new Scanner(System.in);
	private final BTBlackBoard bb = new BTBlackBoard();

	public BTNode set(final String keyTemplate, final int value) {
		return set(keyTemplate, "" + value);
	}

	public BTNode set(final String keyTemplate, final String value) {
		return () -> {
			bb.put(bb.format(keyTemplate), value);
			return BTStatus.Succeeded;
		};
	}

	private String answer(final String question) {
		System.out.println(bb.format(question));
		//---
		return sc.nextLine();
	}

	private BTNode query(final String keyTemplate, final String question, final Predicate<String> validator) {
		return BTNode.doTtry(3, () -> {
			final String response = answer(question);
			if (validator.test(response)) {
				bb.put(bb.format(keyTemplate), response);
				return BTStatus.Succeeded;
			}
			System.err.println("error parsing : " + response);
			return BTStatus.Failed;
		});
	}

	public BTCondition isFulFilled(final String keyTemplate) {
		return BTNode.condition(() -> bb.get(bb.format(keyTemplate)) != null);
	}

	public BTNode fulfill(final String keyTemplate, final String question) {
		final Predicate<String> validator = t -> !StringUtil.isBlank(t);
		return fulfill(keyTemplate, question, validator);
	}

	public BTNode fulfill(final String keyTemplate, final String question, final String... choices) {
		final List<String> choiceList = List.of(choices);
		final Predicate<String> validator = t -> choiceList.contains(t);
		return fulfill(keyTemplate, question, validator);
	}

	public BTNode fulfill(final String keyTemplate, final String question, final Predicate<String> validator) {
		return BTNode.selector(
				isFulFilled(keyTemplate),
				query(keyTemplate, question, validator));
	}

	public BTNode display(final String msg) {
		return () -> {
			System.out.println(bb.format(msg));
			return BTStatus.Succeeded;
		};
	}

	public BTNode inc(final String keyTemplate) {
		return () -> {
			bb.inc(bb.format(keyTemplate));
			return BTStatus.Succeeded;
		};
	}

	public BTCondition eq(final String keytemplate, final String compare) {
		return BTNode.condition(() -> bb.eq(bb.format(keytemplate), compare));
	}

	public BTCondition gt(final String keyTemplate, final String compare) {
		return BTNode.condition(() -> bb.gt(bb.format(keyTemplate), compare));
	}

	public BTCondition lt(final String keyTemplate, final String compare) {
		return BTNode.condition(() -> bb.lt(bb.format(keyTemplate), compare));
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
