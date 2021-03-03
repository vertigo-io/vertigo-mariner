package io.vertigo.ai.bot.parser;

import java.util.Optional;

import io.vertigo.core.lang.Assertion;

/**
 * Commands are splitted into 2 categories
 *  - Leaves composed of 
 *     - BotTaskCommand
 *     - ConditionComand
 *  - Composites   
 *  
 * @author pchretien
 *
 */
interface BotCommand {
	static BotCommand find(final String command) {
		return BotTaskCommand.find(command)
				.orElseGet(() -> BotConditionCommand.find(command)
						.orElseGet(() -> BotCompositeCommand.find(command)
								.orElseThrow(IllegalArgumentException::new)));
	}

	default boolean isTask() {
		return this instanceof BotTaskCommand;
	}

	default boolean isCondition() {
		return this instanceof BotConditionCommand;
	}

	default boolean isLeaf() {
		return isCondition() || isTask();
	}

	default boolean isComposite() {
		return this instanceof BotCompositeCommand;
	}

	/**
	 * Each Task command is composed following this pattern
	 * command "param1" param2 param3 "param4"
	 * command
	 * command param1
	 * 
	 * @author pchretien
	 */
	enum BotTaskCommand implements BotCommand {
		//---leaves
		fulfill, //at least 2
		display, //exactly 1
		inc, //exactly 1
		clear, //exactly 1
		clearAll, //exactly 0
		set; //exactly 2

		static Optional<BotCommand> find(final String command) {
			Assertion.check().isNotBlank(command);
			//---
			try {
				return Optional.of(valueOf(command));
			} catch (final Exception e) {
			}
			return Optional.empty();
		}
	}

	enum BotConditionCommand implements BotCommand {
		eq, //exactly 2
		gt, //exactly 2
		lt, //exactly 2
		succeed, //0
		fail;//0

		static Optional<BotCommand> find(final String command) {
			Assertion.check().isNotBlank(command);
			//---
			try {
				return Optional.of(valueOf(command));
			} catch (final Exception e) {
			}
			return Optional.empty();
		}
	}

	/**
	 * Each Composite command is composed following this pattern
	 * 
	 * command "param1"
	 * ..
	 * end command
	 * 
	 * sequence 
	 * ..
	 * end sequence
	 * 
	 * try 3
	 * end try     
	 * 
	 * 
	 * switch "keyTemplate"
	 * case "compare" 
	 *   ...
	 * case "compare" 
	 *  ..
	 * default 
	 * 	...
	 * end switch 
	 * @author pchretien
	 */
	enum BotCompositeCommand implements BotCommand {
		sequence,
		selector,
		//xswitch,
		//xcase,
		//xdefault,
		end; // pseudo command

		static Optional<BotCommand> find(final String command) {
			Assertion.check().isNotBlank(command);
			//---
			try {
				return Optional.of(valueOf(command));
			} catch (final Exception e) {
			}
			return Optional.empty();
		}
	}
}
