package io.vertigo.ai.bot.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import io.vertigo.core.lang.Assertion;

final class BotUtils {

	private static final Pattern COMMAND_PATTERN = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");

	static List<String> splitLineIntoTokens(final String line) {
		Assertion.check()
				.isNotBlank(line);
		//---

		// Better than this simpler split, that uses an arbitrary number of caracters to look behind...
		// and is more robust to handle escaped single or double quotes if we want to
		//String[] split = s.split( "(?<!(\"|').{0,255}) | (?!.*\\1.*)" );

		return COMMAND_PATTERN.matcher(line)
				.results()
				.map(matchResult -> {
					if (matchResult.group(1) != null) {
						// double-quoted string without the quotes
						return matchResult.group(1);
					} else if (matchResult.group(2) != null) {
						// single-quoted string without the quotes
						return matchResult.group(2);
					} else {
						// unquoted word
						return matchResult.group();
					}
				}).collect(Collectors.toList());

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
