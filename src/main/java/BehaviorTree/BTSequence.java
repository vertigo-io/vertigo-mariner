package BehaviorTree;

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
			//System.out.println(".....before eval node " + node);
			final var status = node.eval();
			//System.out.println(".....after  eval node " + node + " >>" + status);
			if (status == BTStatus.Failed)
				return BTStatus.Failed;
			if (status == BTStatus.Running)
				return BTStatus.Running;
		}
		return BTStatus.Succeeded;
	}
}
