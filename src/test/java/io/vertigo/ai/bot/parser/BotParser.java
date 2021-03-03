package io.vertigo.ai.bot.parser;

import java.util.List;

import io.vertigo.ai.bot.BotEngine;
import io.vertigo.ai.bt.BTNode;
import io.vertigo.core.lang.Assertion;

public final class BotParser {

	public static BTNode parse(final List<String> lines) {
		Assertion.check()
				.isNotNull(lines);
		//---
		final BotEngine botEngine = new BotEngine();
		BotCompositeBuilder botCompositeBuilder = new BotCompositeBuilder();
		for (final var line : lines) {
			botCompositeBuilder = botCompositeBuilder.parseLine(botEngine, line);
		}
		return botCompositeBuilder.build();
	}

}
