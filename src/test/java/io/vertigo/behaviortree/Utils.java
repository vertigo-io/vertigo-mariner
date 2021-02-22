package io.vertigo.behaviortree;

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

}
