package io.vertigo.behaviortree;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import BehaviorTree.BTNode;
import BehaviorTree.BTStatus;

public final class State {
	private final Scanner sc = new Scanner(System.in);
	public final Map<String, String> values = new HashMap();

	private BTNode isFulFilled(final String key) {
		return () -> values.get(key) == null ? BTStatus.Failed : BTStatus.Succeeded;
	}

	private BTNode query(final String key, final String answer) {
		return () -> {
			System.out.println(answer);
			//---
			final String input = sc.nextLine();
			values.put(key, input);
			//---
			return BTStatus.Running;
		};
	}

	public BTNode fulfill(final String key, final String answer) {
		return BTNode.selector(
				isFulFilled(key),
				query(key, answer));
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
