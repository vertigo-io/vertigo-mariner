package io.vertigo.ai.bb;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import io.vertigo.core.lang.Assertion;

public final class BBBlackBoard {
	private final Map<String, String> values = new LinkedHashMap<>();
	private static final String KEY_REGEX = "[a-z]+(/[a-z0-9]*)*";
	private static final String KEY_PATTERN_REGEX = "(" + KEY_REGEX + "[\\*]?)|[\\*]";

	private static void checkKey(final String key) {
		Assertion.check()
				.isNotBlank(key)
				.isTrue(key.matches(KEY_REGEX), "the key '{0}' must contain only a-z 1-9 words separated with /", key);
	}

	private static void checkKeyPattern(final String keyPattern) {
		Assertion.check()
				.isNotBlank(keyPattern)
				.isTrue(keyPattern.matches(KEY_PATTERN_REGEX), "the key pattern '{0}' must contain only a-z 1-9 words separated with / and is finished by a * or nothing", keyPattern);
	}

	/**
	 * Returns if the key exists
	 * @param key the key
	 * @return if the key exists
	 */

	public boolean exists(final String key) {
		checkKey(key);
		//---
		return values.containsKey(key);
	}

	/**
	 * Returns all the keys matching the pattern
	 * @param keyPattern the pattern
	 * @return A list of keys
	 */
	public Set<String> keys(final String keyPattern) {
		checkKeyPattern(keyPattern);
		//---
		if (keyPattern.endsWith("*")) {
			final var prefix = keyPattern.replaceAll("\\*", "");
			return values.keySet().stream()
					.filter(s -> s.startsWith(prefix))
					.collect(Collectors.toSet());
		}
		final var key = keyPattern;
		return values.containsKey(key)
				? Set.of(key)
				: Collections.emptySet();
	}

	public Set<String> keys() {
		return values.keySet();
	}

	public void clear() {
		values.clear();
	}

	public void clear(final String keyPattern) {
		checkKeyPattern(keyPattern);
		if (keyPattern.endsWith("*")) {
			final var prefix = keyPattern.replaceAll("\\*", "");
			values.keySet().removeIf(s -> s.startsWith(prefix));
		}
		final var key = keyPattern;
		values.remove(key);
	}

	/**
	 * Returns the value or null if the key does not exist
	 * @param key the key
	 * @return the value mapped with the key or null if the key does not exist
	 */
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
		return BBUtils.format(msg, values);
	}

	public void dec(final String key) {
		dec(key, 1);
	}

	public void dec(final String key, final int decrement) {
		inc(key, -decrement);
	}

	public void inc(final String key) {
		inc(key, 1);
	}

	public void inc(final String key, final int increment) {
		checkKey(key);
		//---
		var s = get(key);
		if (s == null) {
			s = "0";
		}
		final int value = Integer.valueOf(s).intValue() + increment;
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
