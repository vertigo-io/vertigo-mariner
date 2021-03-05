package io.vertigo.ai.bot.parser;

import java.util.ArrayList;
import java.util.List;

import io.vertigo.ai.bot.BotEngine;
import io.vertigo.ai.bot.parser.BotCommand.BotConditionCommand;
import io.vertigo.ai.bot.parser.BotCommand.BotTaskCommand;
import io.vertigo.ai.bt.BTNode;
import io.vertigo.core.lang.Assertion;

final class BotLeafParser {

	static BTNode parseLeaf(final BotEngine botEngine, final BotCommand command, final List<String> args) {
		Assertion.check()
				.isNotNull(botEngine)
				.isNotNull(command)
				.isNotNull(args)
				.isTrue(command.isLeaf(), "The command must be a leaf");
		//---
		if (command.isTask()) {
			return parseTask(botEngine, (BotTaskCommand) command, args);
		} else if (command.isCondition()) {
			return parseCondition(botEngine, (BotConditionCommand) command, args);
		}
		throw new IllegalArgumentException();
	}

	private static BTNode parseTask(final BotEngine botEngine, final BotTaskCommand command, final List<String> args) {
		Assertion.check()
				.isNotNull(botEngine)
				.isNotNull(command)
				.isNotNull(args);
		//---
		switch (command) {
			case fulfill: {
				Assertion.check().isTrue(args.size() > 1, "args must have at least 2 elements with fulfill command");
				//---
				final var keyTemplate = args.get(0);
				final var question = args.get(1);
				if (args.size() == 2) {
					return botEngine.fulfill(keyTemplate, question);
				}
				//with 3 and more args : choices
				final var choices = new ArrayList<>(args);
				//We have to remove the first 2 elements
				choices.remove(0);
				choices.remove(0);
				final var choice = choices.remove(0);
				return botEngine.fulfill(keyTemplate, question, choice, choices.toArray(new String[choices.size()]));
			}
			case display:
				Assertion.check().isTrue(args.size() == 1, "args must have exactly 1 element with display command");
				//---
				final var msg = args.get(0);
				return botEngine.display(msg);
			case inc: {
				Assertion.check().isTrue(args.size() == 1, "args must have exactly 1 element with inc command");
				//---
				final var keyTemplate = args.get(0);
				return botEngine.inc(keyTemplate);
			}
			case clear: {
				Assertion.check().isTrue(args.size() == 1, "args must have exactly 1 element with clear command");
				//---
				final var keyPattern = args.get(0);
				return botEngine.clear(keyPattern);
			}
			case clearAll:
				Assertion.check().isTrue(args.size() == 0, "args must have no element with clearAll command");
				//---
				return botEngine.clear();
			case set: {
				Assertion.check().isTrue(args.size() == 2, "args must have exactly 2 elements with set command");
				//---
				final var keyTemplate = args.get(0);
				final var value = args.get(1);
				return botEngine.set(keyTemplate, value);
			}
			default:
				throw new IllegalStateException();
		}
	}

	private static BTNode parseCondition(final BotEngine botEngine, final BotConditionCommand command, final List<String> args) {
		Assertion.check()
				.isNotNull(botEngine)
				.isNotNull(command)
				.isNotNull(args);
		//---
		switch (command) {
			case eq: {
				Assertion.check().isTrue(args.size() == 2, "args must have exactly 2 elements with eq command");
				//---
				final var keyTemplate = args.get(0);
				final var compare = args.get(1);
				return botEngine.eq(keyTemplate, compare);
			}
			case gt: {
				Assertion.check().isTrue(args.size() == 2, "args must have exactly 2 elements with eq command");
				//---
				final var keyTemplate = args.get(0);
				final var compare = args.get(1);
				return botEngine.gt(keyTemplate, compare);
			}
			case lt: {
				Assertion.check().isTrue(args.size() == 2, "args must have exactly 2 elements with lt command");
				//---
				final var keyTemplate = args.get(0);
				final var compare = args.get(1);
				return botEngine.lt(keyTemplate, compare);
			}
			case succeed: {
				Assertion.check().isTrue(args.size() == 0, "args must have no element with succeed command");
				//---
				return BTNode.succeed();
			}
			case fail: {
				Assertion.check().isTrue(args.size() == 0, "args must have no element with fail command");
				//---
				return BTNode.fail();
			}
			default:
				throw new IllegalStateException();
		}
	}

}
