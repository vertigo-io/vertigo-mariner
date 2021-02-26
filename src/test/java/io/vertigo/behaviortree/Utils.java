package io.vertigo.behaviortree;

import java.util.Map;
import java.util.function.Predicate;

public final class Utils {
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
		final String BEGIN_TOKEN = "{{";
		final String END_TOKEN = "}}";
		int last = 0;

		final StringBuilder builder = new StringBuilder();
		for (int start = msg.indexOf(BEGIN_TOKEN, last); start >= 0; start = msg.indexOf(BEGIN_TOKEN, last)) {
			final int end = msg.indexOf(END_TOKEN, start);
			if (end < 0)
				throw new IllegalStateException();
			final var paramName = msg.substring(start + BEGIN_TOKEN.length(), end);
			builder
					.append(msg.substring(last, start))
					.append(map.getOrDefault(paramName, "not found:" + paramName));
			last = end + END_TOKEN.length();
		}
		return builder
				.append(msg.substring(last))
				.toString();
	}
}
