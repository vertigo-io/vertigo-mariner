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
	public final Map<String, String> values = new LinkedHashMap<>();

	private String get(final String key) {
		return values.get(Utils.format(key, values));
	}

	private void put(final String key, final String value) {
		values.put(Utils.format(key, values), value);
	}

	private BTNode isFulFilled(final String key) {
		return BTNode.condition(() -> get(key) != null);
	}

	private BTNode query(final String key, final String answer, final Predicate<String> validator) {
		return () -> {
			System.out.println(Utils.format(answer, values));
			//---
			final String input = sc.nextLine();
			if (validator.test(input)) {
				put(key, input);
				return BTStatus.Succeeded;
			}
			System.err.println("error parsing : result " + answer);
			return BTStatus.Failed;
		};
	}

	public BTNode inc(final String key) {
		return () -> {
			var s = get(key);
			if (s == null)
				s = "0";
			final int value = Integer.valueOf(s).intValue() + 1;
			put(key, "" + value);
			return BTStatus.Succeeded;
		};
	}

	/*	public BTNode init(final String key, final int value) {
			return () -> {
				if (get(key) == null) {
					put(key, "" + value);
				}
				return BTStatus.Succeeded;
			};
		}
	*/
	//	public BTNode set(final String key, final int value) {
	//		return () -> {
	//			values.put(key, "" + value);
	//			return BTStatus.Succeeded;
	//		};
	//	}

	public BTNode equals(final String key, final String result) {
		return BTNode.condition(() -> Objects.equals(get(key), Utils.format(result, values)));
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

	public BTNode clear(final String key) {
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

	public BTNode clearAll() {
		return () -> {
			values.clear();
			return BTStatus.Succeeded;
		};
	}

	public BTNode display(final String msg) {
		return () -> {
			System.out.println(Utils.format(msg, values));
			return BTStatus.Succeeded;
		};
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
					.append(get(key));
		}
		return builder.toString();
	}

}
