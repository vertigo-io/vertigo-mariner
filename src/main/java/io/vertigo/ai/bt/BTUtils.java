package io.vertigo.ai.bt;

import java.util.Map;
import java.util.function.Predicate;

import io.vertigo.core.lang.Assertion;

public final class BTUtils {
	public static Predicate<String> isInteger() {
		return s -> {
			try {
				Integer.parseInt(s);
				return true;
			} catch (final Exception e) {
				return false;
			}
		};
	}

	public static String format(final String msg, final Map<String, String> map) {
		Assertion.check()
				.isNotBlank(msg)
				.isNotNull(map);
		//---
		final String START_TOKEN = "{{";
		final String END_TOKEN = "}}";

		final StringBuilder builder = new StringBuilder(msg);
		int start = 0;
		int end;
		while ((end = builder.indexOf(END_TOKEN, start)) >= 0) {
			start = builder.lastIndexOf(START_TOKEN, end);
			if (start < 0) {
				throw new IllegalStateException("An end token '" + END_TOKEN + "+'has been found without a start token " + START_TOKEN);
			}
			final var paramName = builder.substring(start + START_TOKEN.length(), end);
			final var paramVal = map.getOrDefault(paramName, "not found:" + paramName);
			builder.replace(start, end + END_TOKEN.length(), paramVal);
		}
		if (builder.indexOf(START_TOKEN) > 0) {
			throw new IllegalStateException("A start token '" + START_TOKEN + "+'has been found without an end token " + END_TOKEN);
		}
		return builder.toString();
	}
}