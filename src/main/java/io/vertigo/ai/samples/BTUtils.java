package io.vertigo.ai.samples;

import java.util.function.Predicate;

final class BTUtils {
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
}
