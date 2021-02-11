package BehaviorTree;

import java.util.List;

import io.vertigo.core.lang.Assertion;

public final class BTSelector implements BTNode {
	private final List<BTNode> nodes;

	public BTSelector(final BTNode... nodes) {
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
			if (status == BTStatus.Succeeded)
				return BTStatus.Succeeded;
			if (status == BTStatus.Running)
				return BTStatus.Running;
		}
		return BTStatus.Failed;
	}
}
