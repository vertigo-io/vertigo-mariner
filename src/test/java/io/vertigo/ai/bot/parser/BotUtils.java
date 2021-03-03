package io.vertigo.ai.bot.parser;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.vertigo.core.lang.Assertion;

final class BotUtils {

	@Test
	public void test() {
		var tokens = splitLineIntoTokens("  test  ");
		Assertions.assertEquals(1, tokens.size());
		Assertions.assertEquals("test", tokens.get(0));

		tokens = splitLineIntoTokens("  hello test  ");
		Assertions.assertEquals(2, tokens.size());
		Assertions.assertEquals("test", tokens.get(1));

		tokens = splitLineIntoTokens("  hello 	  test  ");
		Assertions.assertEquals(2, tokens.size());
		Assertions.assertEquals("test", tokens.get(1));

		tokens = splitLineIntoTokens("  hello 	  'two words' world");
		Assertions.assertEquals(3, tokens.size());
		Assertions.assertEquals("two words", tokens.get(1));
	}

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
