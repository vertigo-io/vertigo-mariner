package io.vertigo.ai.bot.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BotUilsTest {

	@Test
	public void test() {
		var tokens = BotUtils.splitLineIntoTokens("  test  ");
		Assertions.assertEquals(1, tokens.size());
		Assertions.assertEquals("test", tokens.get(0));

		tokens = BotUtils.splitLineIntoTokens("  hello test  ");
		Assertions.assertEquals(2, tokens.size());
		Assertions.assertEquals("test", tokens.get(1));

		tokens = BotUtils.splitLineIntoTokens("  hello 	  test  ");
		Assertions.assertEquals(2, tokens.size());
		Assertions.assertEquals("test", tokens.get(1));

		tokens = BotUtils.splitLineIntoTokens("  hello 	  'two words' world");
		Assertions.assertEquals(3, tokens.size());
		Assertions.assertEquals("two words", tokens.get(1));
	}

}
