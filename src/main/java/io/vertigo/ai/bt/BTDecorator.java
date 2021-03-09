package io.vertigo.ai.bt;

import java.util.function.Function;

import io.vertigo.core.lang.Assertion;

final class BTDecorator implements BTNode {
	private final BTNode node;
	private final Function<BTStatus, BTStatus> transformer;

	BTDecorator(final BTNode node, final Function<BTStatus, BTStatus> transformer) {
		Assertion.check()
				.isNotNull(node)
				.isNotNull(transformer);
		//---
		this.node = node;
		this.transformer = transformer;
	}

	@Override
	public BTStatus eval() {
		final var status = node.eval();
		return transformer.apply(status);
	}
}
