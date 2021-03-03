package io.vertigo.ai.bot;

import java.util.List;

import org.junit.jupiter.api.Test;

import io.vertigo.ai.bot.parser.BotParser;
import io.vertigo.ai.bt.BTRoot;

public class BotParserTest {

	@Test
	public void test0() {
		final var lines = List.of(
				"inc mem/0",
				"inc mem/1",
				"display {{mem/0}}_{{mem/1}}",
				"clear mem/*",
				"inc mem/0",
				"inc mem/1",
				"display {{mem/0}}_{{mem/1}}",
				"inc mem/0",
				"inc mem/0",
				"inc mem/0",
				"clear mem/0",
				"inc mem/0",
				"inc mem/1",
				"display {{mem/0}}_{{mem/1}}",
				"clearAll",
				"set mem/0 9",
				"inc mem/1",
				"display {{mem/0}}_{{mem/1}}");
		BotParser.parse(lines);
		final var root = new BTRoot(BotParser.parse(lines));
		root.run();
	}

	@Test
	public void test1() {
		final var lines = List.of(
				"display I_m_Alan",
				"fulfill u/name hello_what_is_your_name_?",
				"display welcome_{{u/name}},nice_to_see_you");
		BotParser.parse(lines);
		final var root = new BTRoot(BotParser.parse(lines));
		root.run();
	}

	@Test
	public void test2() {
		final var lines = List.of(
				"fulfill mem A_B_C? A B C",
				"selector",
				"  sequence",
				"    eq mem A",
				"    display alpha",
				"  end",
				"  sequence",
				"    eq mem B",
				"    display bravo",
				"  end",
				"  sequence",
				"    eq mem C",
				"    display charlie",
				"  end",
				"end");
		BotParser.parse(lines);
		final var root = new BTRoot(BotParser.parse(lines));
		root.run();
	}

	@Test
	public void test3() {
		final var lines = List.of(
				"selector",
				"  sequence",
				"    fail",
				"    display alpha",
				"  end",
				"  sequence",
				"    succeed",
				"    display bravo",
				"  end",
				"  sequence",
				"    succeed",
				"    display charlie",
				"  end",
				"end");
		BotParser.parse(lines);
		final var root = new BTRoot(BotParser.parse(lines));
		root.run();
	}

}
