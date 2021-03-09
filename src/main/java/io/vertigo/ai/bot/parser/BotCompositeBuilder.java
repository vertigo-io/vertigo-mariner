package io.vertigo.ai.bot.parser;

import static io.vertigo.ai.bt.BTNodes.selector;
import static io.vertigo.ai.bt.BTNodes.sequence;

import java.util.ArrayList;
import java.util.List;

import io.vertigo.ai.bot.BotEngine;
import io.vertigo.ai.bot.parser.BotCommand.BotCompositeCommand;
import io.vertigo.ai.bt.BTNode;
import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.Builder;

public final class BotCompositeBuilder implements Builder<BTNode> {
	private final List<BTNode> nodes = new ArrayList<>();
	private final BotCompositeCommand compositeCommand;
	private final BotCompositeBuilder compositeParent;

	BotCompositeBuilder() {
		this.compositeCommand = BotCompositeCommand.sequence;
		this.compositeParent = null;
	}

	BotCompositeBuilder(final BotCompositeCommand compositeCommand, final BotCompositeBuilder compositeParent) {
		Assertion.check()
				.isNotNull(compositeCommand)
				.isNotNull(compositeParent);
		//---
		this.compositeCommand = compositeCommand;
		this.compositeParent = compositeParent;
	}

	private void addNode(final BTNode node) {
		Assertion.check().isNotNull(node);
		//---
		this.nodes.add(node);
	}

	/**
	 * Returns the currentBuilder
	 */
	public BotCompositeBuilder parseLine(final BotEngine botEngine, final String line) {
		Assertion.check()
				.isNotNull(botEngine)
				.isNotNull(line);
		//---
		if (line.startsWith("#") || line.isBlank()) {
			//we clean all comments and blank lines
			return this;
		}
		//from now 
		//We must have a command (or a pseudo-command like 'end') at the start of the line
		final var tokens = BotUtils.splitLineIntoTokens(line);
		final BotCommand command = BotUtils.tokensToCommand(tokens);
		final List<String> args = BotUtils.tokensToArgs(tokens);
		if (command.isLeaf()) {
			// we have just to add the correponding node
			final var node = BotLeafParser.parseLeaf(botEngine, command, args);
			addNode(node);
			return this;
		}
		if (command.isComposite()) {
			switch ((BotCompositeCommand) command) {
				case end:
					final var compositeNode = this.createComposite();
					compositeParent.addNode(compositeNode);
					return compositeParent;
				case selector:
				case sequence:
					return new BotCompositeBuilder((BotCompositeCommand) command, this);
				default:
			}
		}
		throw new IllegalStateException();
	}

	private BTNode createComposite() {
		switch (compositeCommand) {
			case selector:
				return selector(nodes);
			case sequence:
				return sequence(nodes);
			case end:
			default:
				throw new IllegalStateException();
		}
	}

	@Override
	public BTNode build() {
		Assertion.check()
				.isNull(compositeParent, "there must be an unclosed composite > adds a end somewhere");
		//---
		return createComposite();
	}
}
