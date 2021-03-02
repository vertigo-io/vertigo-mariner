package BehaviorTree;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import io.vertigo.core.lang.Assertion;

public final class BTBlackBoard {
	private final Map<String, String> values = new LinkedHashMap<>();
	private static final String REGEX = "[a-z]*(/[a-z0-9]*)*";

	private void checkKey(final String key) {
		Assertion.check()
				.isNotBlank(key)
				.isTrue(key.matches(REGEX), "the key''{0}' must contain only a-z 1-9 words separated with /", key);
	}

	public String get(final String key) {
		checkKey(key);
		//---
		return values.get(key);
	}

	public void put(final String key, final String value) {
		checkKey(key);
		//---
		values.put(key, value);
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
		return Objects.equals(get(key), format(compare));
	}

	public boolean gt(final String key, final String compare) {
		checkKey(key);
		//---
		final int k = Integer.valueOf(get(key)).intValue();
		final int c = Integer.valueOf(format(compare)).intValue();
		return c > k;
	}

	public boolean lt(final String key, final String compare) {
		checkKey(key);
		//---
		final int k = Integer.valueOf(get(key)).intValue();
		final int c = Integer.valueOf(format(compare)).intValue();
		return c < k;
	}

	public void clear(final String keyPattern) {
		Assertion.check()
				.isNotBlank(keyPattern);
		//---
		if (keyPattern.endsWith("*")) {
			final var prefix = keyPattern.substring(0, keyPattern.length() - 2);
			values.keySet().removeIf(s -> s.startsWith(prefix));
		} else {
			//This is not a pattern, just a key
			final var key = keyPattern;
			checkKey(key);
			values.remove(key);
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
