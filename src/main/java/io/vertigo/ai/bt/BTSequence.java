package io.vertigo.ai.bt;

import java.util.List;

import io.vertigo.core.lang.Assertion;

final class BTSequence implements BTNode {
	private final List<BTNode> nodes;

	BTSequence(final BTNode... nodes) {
		Assertion.check()
				.isNotNull(nodes);
		//---
		this.nodes = List.of(nodes);
	}

	@Override
	public BTStatus eval() {
		for (final BTNode node : nodes) {
			final var status = node.eval();
			//continue when succeeded until a fail or a stop
			if (!status.isSucceeded()) {
				return status;
			}
		}
		return BTStatus.Succeeded;
	}
}
