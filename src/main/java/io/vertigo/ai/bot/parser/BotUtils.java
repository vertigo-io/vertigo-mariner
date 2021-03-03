package io.vertigo.ai.bot.parser;

import java.util.ArrayList;
import java.util.List;

import io.vertigo.core.lang.Assertion;

final class BotUtils {

	static List<String> splitLineIntoTokens(final String line) {
		Assertion.check()
				.isNotBlank(line);
		//---
		return List.of(line.trim().split("\\s+"));
		//		return List.of(line.trim().split("('[^']+')|([^' ]+)"));
	}

	static BotCommand tokensToCommand(final List<String> tokens) {
		Assertion.check()
				.isNotNull(tokens)
				.isTrue(tokens.size() > 0, "there must be at least one token");
		//---
		return BotCommand.find(tokens.get(0));
	}

	static List<String> tokensToArgs(final List<String> tokens) {
		Assertion.check()
				.isNotNull(tokens)
				.isTrue(tokens.size() > 0, "there must be at least one token");
		//---
		final List<String> args = new ArrayList<>(tokens);
		args.remove(0);
		return List.copyOf(args);
	}
}
