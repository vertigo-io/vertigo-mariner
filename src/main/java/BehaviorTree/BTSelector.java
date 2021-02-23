package BehaviorTree;

import java.util.List;

import io.vertigo.core.lang.Assertion;

final class BTSelector implements BTNode {
	private final List<BTNode> nodes;

	BTSelector(final BTNode... nodes) {
		Assertion.check()
				.isNotNull(nodes);
		//---
		this.nodes = List.of(nodes);
	}

	@Override
	public BTStatus eval() {
		for (final BTNode node : nodes) {
			final var status = node.eval();
			//continue when failed until a success or a stop
			if (!status.isFailed()) {
				return status;
			}
		}
		return BTStatus.Failed;
	}
}
