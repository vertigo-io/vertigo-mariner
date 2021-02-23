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
			//breaks the loop when the while condition failed
			if (whileTest.isFailed())
				return BTStatus.Succeeded;

			final var status = node.eval();
			//loop when succeeded until a fail or a stop
			if (!status.isSucceeded()) {
				return status;
			}

			final var untilTest = untilCondition.eval();
			//breaks the loop when the until condition succeeded
			if (untilTest.isSucceeded())
				return BTStatus.Succeeded;
		}
		return BTStatus.Failed;
	}

}
