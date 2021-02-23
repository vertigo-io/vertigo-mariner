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
		int last = 0;
		int start = msg.indexOf("{{", last);
		final StringBuilder builder = new StringBuilder();
		while (start >= 0) {
			final int end = msg.indexOf("}}", start);
			if (end <= 0)
				throw new IllegalStateException();
			final var paramName = msg.substring(start + 2, end);
			builder
					.append(msg.substring(last, start))
					.append(map.getOrDefault(paramName, "not found:" + paramName));
			last = end + 2;
			start = msg.indexOf("{{", last);
		}
		builder
				.append(msg.substring(last));
		return builder.toString();
	}

}
