package io.vertigo.behaviortree;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Predicate;

import BehaviorTree.BTNode;
import BehaviorTree.BTStatus;
import io.vertigo.core.util.StringUtil;

public final class State {
	private final Scanner sc = new Scanner(System.in);
	public final Map<String, String> values = new LinkedHashMap();

	private BTNode isFulFilled(final String key) {
		return BTNode.condition(() -> values.get(key) != null);
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
			System.err.println("error parsing : result " + answer);
			return BTStatus.Failed;
		};
	}

	public BTNode equals(final String key, final String result) {
		return BTNode.condition(() -> Objects.equals(values.get(key), result));
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
				isFulFilled(key),
				query(key, answer, validator));
	}

	/*	public BTNode clear(final String key) {
			return () -> {
				if (key.endsWith("*")) {
					final var prefix = key.substring(0, key.length() - 2);
					values.keySet().removeIf(s -> s.startsWith(prefix));
				} else {
					values.remove(key);
				}
				return BTStatus.Succeeded;
			};
		}
	*/
	public BTNode clearAll() {
		return () -> {
			values.clear();
			return BTStatus.Succeeded;
		};
	}

	public BTNode display(final String key, final String msg) {
		return BTNode.selector(
				equals(key, "ok"),
				() -> {
					System.out.println(msg);
					values.put(key, "ok");
					return BTStatus.Succeeded;
				});
	}

	@Override
	public String toString() {
		final var builder = new StringBuilder();
		for (final String key : values.keySet()) {
			builder
					.append("\r\n")
					.append("---")
					.append(key)
					.append(" : ")
					.append(values.get(key));
		}
		return builder.toString();
	}

}
