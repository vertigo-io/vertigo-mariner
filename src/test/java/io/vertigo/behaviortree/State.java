package io.vertigo.behaviortree;

import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

import BehaviorTree.BTNode;
import BehaviorTree.BTStatus;
import io.vertigo.core.util.StringUtil;

public final class State {
	private final Scanner sc = new Scanner(System.in);
	public final BTBlackBoard bb = new BTBlackBoard();

	private BTNode query(final String key, final String answer, final Predicate<String> validator) {
		return () -> {
			System.out.println(bb.format(answer));
			//---
			final String input = sc.nextLine();
			if (validator.test(input)) {
				bb.put(key, input);
				return BTStatus.Succeeded;
			}
			System.err.println("error parsing : result " + answer);
			return BTStatus.Failed;
		};
	}

	public BTNode fulfill(final String key, final String answer) {
		final Predicate<String> validator = t -> !StringUtil.isBlank(t);
		return fulfill(key, answer, validator);
	}

	public BTNode fulfill(final String key, final String answer, final String... choices) {
		final List<String> choiceList = List.of(choices);
		final Predicate<String> validator = t -> choiceList.contains(t);
		return fulfill(key, answer, validator);
	}

	public BTNode fulfill(final String key, final String answer, final Predicate<String> validator) {
		return BTNode.selector(
				bb.isFulFilled(key),
				query(key, answer, validator));
	}

	public BTNode display(final String msg) {
		return () -> {
			System.out.println(bb.format(msg));
			return BTStatus.Succeeded;
		};
	}

	@Override
	public String toString() {
		return bb.toString();
	}
}
