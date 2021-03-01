package BehaviorTree;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import io.vertigo.core.lang.Assertion;

public final class BTBlackBoard {
	private final Map<String, String> values = new LinkedHashMap<>();
	//	private static final var PATTERN = "[a-z]";

	private void checkKey(final String key) {
		Assertion.check()
				.isNotBlank(key);
		//		.isTrue(, msg, params)

	}

	public String get(final String key) {
		checkKey(key);
		//---
		return values.get(BTUtils.format(key, values));
	}

	public void put(final String key, final String value) {
		checkKey(key);
		//---
		values.put(BTUtils.format(key, values), value);
	}

	public String format(final String msg) {
		return BTUtils.format(msg, values);
	}

	public void inc(final String key) {
		checkKey(key);
		//---
		var s = get(key);
		if (s == null)
			s = "0";
		final int value = Integer.valueOf(s).intValue() + 1;
		put(key, "" + value);
	}

	public boolean eq(final String key, final String compare) {
		checkKey(key);
		//---
		return Objects.equals(get(key), BTUtils.format(compare, values));
	}

	public boolean gt(final String key, final String compare) {
		checkKey(key);
		//---
		final int k = Integer.valueOf(get(key)).intValue();
		final int c = Integer.valueOf(BTUtils.format(compare, values)).intValue();
		return c > k;
	}

	public boolean lt(final String key, final String compare) {
		checkKey(key);
		//---
		final int k = Integer.valueOf(get(key)).intValue();
		final int c = Integer.valueOf(BTUtils.format(compare, values)).intValue();
		return c < k;
	}

	public void clear(final String keyPattern) {
		if (keyPattern.endsWith("*")) {
			final var prefix = keyPattern.substring(0, keyPattern.length() - 2);
			values.keySet().removeIf(s -> s.startsWith(prefix));
		} else {
			values.remove(keyPattern);
		}
	}

	public void clearAll() {
		values.clear();
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
