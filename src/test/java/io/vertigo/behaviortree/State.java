package io.vertigo.behaviortree;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Predicate;

import BehaviorTree.BTNode;
import BehaviorTree.BTStatus;
import io.vertigo.core.util.StringUtil;

public final class State {
	private final Scanner sc = new Scanner(System.in);
	public final Map<String, String> values = new HashMap();

	private BTNode isFulFilled(final String key) {
		return () -> values.get(key) == null ? BTStatus.Failed : BTStatus.Succeeded;
	}

	private BTNode query(final String key, final String answer, final Predicate<String> validator) {
		return () -> {
			System.out.println(answer);
			//---
			final String input = sc.nextLine();
			if (validator.test(input)) {
				values.put(key, input);
				return BTStatus.Running;
			}
			System.err.println("error on parsing " + answer);
			return BTStatus.Failed;
		};
	}

	public BTNode fulfill(final String key, final String answer) {
		final Predicate<String> validator = t -> !StringUtil.isBlank(t);
		return BTNode.selector(
				isFulFilled(key),
				query(key, answer, validator));
	}

	@Override
	public String toString() {
		final var builder = new StringBuilder();
		for (final String key : values.keySet()) {
			builder.append(key)
					.append(" : ")
					.append(values.get(key))
					.append("\r\n");
		}
		return builder.toString();
	}

}
