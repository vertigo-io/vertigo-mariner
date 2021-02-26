package io.vertigo.behaviortree;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import BehaviorTree.BTCondition;
import BehaviorTree.BTNode;
import BehaviorTree.BTStatus;

public final class BTBlackBoard {
	private final Map<String, String> values = new LinkedHashMap<>();

	public String get(final String key) {
		return values.get(Utils.format(key, values));
	}

	public void put(final String key, final String value) {
		values.put(Utils.format(key, values), value);
	}

	public String format(final String msg) {
		return Utils.format(msg, values);
	}

	public BTCondition isFulFilled(final String key) {
		return BTNode.condition(() -> get(key) != null);
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

	public BTNode set(final String key, final int value) {
		return () -> {
			values.put(key, "" + value);
			return BTStatus.Succeeded;
		};
	}

	public BTCondition eq(final String key, final String result) {
		return BTNode.condition(() -> Objects.equals(get(key),
				Utils.format(result, values)));
	}

	public BTCondition gt(final String key, final String compare) {
		return BTNode.condition(() -> {
			final int k = Integer.valueOf(get(key)).intValue();
			final int c = Integer.valueOf(Utils.format(compare, values)).intValue();
			return c > k;
		});
	}

	public BTCondition lt(final String key, final String compare) {
		return BTNode.condition(() -> {
			final int k = Integer.valueOf(get(key)).intValue();
			final int c = Integer.valueOf(Utils.format(compare, values)).intValue();
			return c < k;
		});
	}

	public BTNode clear(final String keyPattern) {
		return () -> {
			if (keyPattern.endsWith("*")) {
				final var prefix = keyPattern.substring(0, keyPattern.length() - 2);
				values.keySet().removeIf(s -> s.startsWith(prefix));
			} else {
				values.remove(keyPattern);
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
