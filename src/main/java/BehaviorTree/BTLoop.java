package BehaviorTree;

import io.vertigo.core.lang.Assertion;

final class BTLoop implements BTNode {
	private final int MAX = 1_000;
	private final BTNode whileCondition;
	private final BTNode untilCondition;
	private final BTNode node;

	BTLoop(final BTNode whileCondition, final BTNode node, final BTNode untilCondition) {
		Assertion.check()
				.isNotNull(whileCondition)
				.isNotNull(untilCondition)
				.isNotNull(node);
		//---
		this.whileCondition = whileCondition;
		this.node = node;
		this.untilCondition = untilCondition;
	}

	@Override
	public BTStatus eval() {
		for (int i = 0; i < MAX; i++) {
			final var whileTest = whileCondition.eval();
			if (whileTest == BTStatus.Failed)
				return BTStatus.Succeeded;

			final var status = node.eval();
			//			if (status == BTStatus.Running)
			//				return BTStatus.Running;
			if (status == BTStatus.Failed)
				return BTStatus.Failed;

			final var untilTest = untilCondition.eval();
			if (untilTest == BTStatus.Succeeded)
				return BTStatus.Succeeded;
		}
		return BTStatus.Failed;
	}

}
