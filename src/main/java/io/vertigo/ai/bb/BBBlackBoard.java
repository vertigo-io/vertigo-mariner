package io.vertigo.ai.bb;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import io.vertigo.core.lang.Assertion;

public final class BBBlackBoard {
	private enum Type {
		String,
		Integer,
		List
	}

	private static final String KEY_REGEX = "[a-z]+(/[a-z0-9]*)*";
	private static final String KEY_PATTERN_REGEX = "(" + KEY_REGEX + "[\\*]?)|[\\*]";

	private final Map<String, Type> keys = new LinkedHashMap<>();
	private final Map<String, String> values = new LinkedHashMap<>();
	private final Map<String, BBList> lists = new LinkedHashMap<>();

	//	private static <E> List<E> merge(final E element, final E[] elements) {
	//		Assertion.check()
	//				.isNotNull(element)
	//				.isNotNull(elements);
	//		//---
	//		final var list = new ArrayList<E>();
	//		list.add(element);
	//		Arrays.stream(elements).forEach(e -> list.add(e));
	//		return Collections.unmodifiableList(list);
	//	}

	private static void checkKey(final String key) {
		Assertion.check()
				.isNotBlank(key)
				.isTrue(key.matches(KEY_REGEX), "the key '{0}' must contain only a-z 1-9 words separated with /", key);
	}

	private void checkType(final String key, final Type type) {
		Assertion.check()
				.isNotNull(key)
				.isNotNull(type);
		//---
		final Type t = keys.get(key);
		if (t != null && !type.equals(t)) {
			throw new IllegalStateException("the type of the key " + t + " is not the one expected " + type);
		}
	}

	private static void checkKeyPattern(final String keyPattern) {
		Assertion.check()
				.isNotBlank(keyPattern)
				.isTrue(keyPattern.matches(KEY_PATTERN_REGEX), "the key pattern '{0}' must contain only a-z 1-9 words separated with / and is finished by a * or nothing", keyPattern);
	}

	private static String formatToString(final Integer i) {
		return i == null
				? null
				: String.valueOf(i);
	}

	private static Integer formatToInteger(final String s) {
		return s == null
				? null
				: Integer.valueOf(s);
	}

	/**
	 * Returns if the keys exist
	 * 
	 * @param key the key
	 * @return if the key exists
	 */
	public boolean exists(final String key) {
		checkKey(key);
		//---
		return keys.containsKey(key);
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
			return keys.keySet().stream()
					.filter(s -> s.startsWith(prefix))
					.collect(Collectors.toSet());
		}
		final var key = keyPattern;
		return keys.containsKey(key)
				? Set.of(key)
				: Collections.emptySet();
	}

	public Set<String> keys() {
		return keys.keySet();
	}

	public void removeAll() {
		values.clear();
		keys.clear();
		lists.clear();
	}

	public void remove(final String keyPattern) {
		checkKeyPattern(keyPattern);
		if (keyPattern.endsWith("*")) {
			final var prefix = keyPattern.replaceAll("\\*", "");
			values.keySet().removeIf(s -> s.startsWith(prefix));
			lists.keySet().removeIf(s -> s.startsWith(prefix));
			keys.keySet().removeIf(s -> s.startsWith(prefix));
		} else {
			final var key = keyPattern;
			values.remove(key);
			lists.remove(key);
			keys.remove(key);
		}
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

	public Integer getInteger(final String key) {
		final String value = get(key);
		checkType(key, Type.Integer);
		return formatToInteger(value);
	}

	public void putInteger(final String key, final int value) {
		doPut(key, Type.Integer, formatToString(value));
	}

	public void put(final String key, final String value) {
		doPut(key, Type.String, value);
	}

	private void doPut(final String key, final Type type, final String value) {
		checkKey(key);
		//---
		final Type previousType = keys.put(key, type);
		if (previousType != null && type != previousType) {
			throw new IllegalStateException("the type is already defined" + previousType);
		}
		values.put(key, value);
	}

	public String format(final String msg) {
		return BBUtils.format(msg, values);
	}

	public void append(final String key, final String something) {
		String value = get(key);
		if (value == null) {
			value = "";
		}
		put(key, value + something);
	}

	void decr(final String key) {
		incrBy(key, -1);
	}

	public void incr(final String key) {
		incrBy(key, 1);
	}

	public void incrBy(final String key, final int value) {
		checkKey(key);
		checkType(key, Type.Integer);
		//---
		Integer i = getInteger(key);
		if (i == null) {
			i = 0;
		}
		putInteger(key, i + value);
	}

	private int compare(final String key, final String compare) {
		checkKey(key);
		//---
		final Type type = keys.get(key);
		final String k = get(key);
		final String c = format(compare);
		if (k == null) {
			return c == null
					? 0
					: -1;
		}

		switch (type) {
			case String:
				return k.compareTo(c);
			case Integer:
				return Integer.valueOf(k).compareTo(Integer.valueOf(c));
			default:
				throw new IllegalStateException();
		}
	}

	public boolean lt(final String key, final String compare) {
		return compare(key, compare) < 0;
	}

	public boolean eq(final String key, final String compare) {
		return compare(key, compare) == 0;
	}

	public boolean gt(final String key, final String compare) {
		return compare(key, compare) > 0;
	}

	//------------------------------------
	//- List                             -
	//- All methods are prefixed with l  -
	//------------------------------------
	private BBList getListOrCreate(final String key) {
		checkKey(key);
		//---
		BBList list = lists.get(key);
		if (list != null) {
			return list;
		}
		list = new BBList();
		lists.put(key, list);
		return list;
	}

	private BBList getListOrEmpty(final String key) {
		checkKey(key);
		//---
		final BBList list = lists.get(key);
		return list == null
				? BBList.EMPTY
				: list;
	}

	public int llen(final String key) {
		return getListOrEmpty(key)
				.len();
	}

	public void lpush(final String key, final String value) {
		getListOrCreate(key)
				.push(value);
	}

	public String lpop(final String key) {
		return getListOrEmpty(key)
				.pop();
	}

	public String lpeek(final String key) {
		return getListOrEmpty(key)
				.peek();
	}

	public String lget(final String key, final int idx) {
		return getListOrEmpty(key)
				.get(idx);
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
		//		for (final String key : values.keySet()) {
		//			builder
		//					.append("\r\n")
		//					.append("---")
		//					.append(key)
		//					.append(" : ")
		//					.append(get(key));
		//		}
		return builder.toString();
	}
}
