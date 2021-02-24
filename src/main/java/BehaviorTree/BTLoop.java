package BehaviorTree;

import io.vertigo.core.lang.Assertion;

final class BTLoop implements BTNode {
	/* This is a security to break the loop if the condition is never attempted */
	private static final int MAX = 1_000;
	private final BTCondition whileCondition;
	private final BTCondition untilCondition;
	private final BTNode node;

	BTLoop(final BTCondition whileCondition, final BTNode node, final BTCondition untilCondition) {
		this(MAX, whileCondition, node, untilCondition);
	}

	BTLoop(final int loops, final BTCondition whileCondition, final BTNode node, final BTCondition untilCondition) {
		Assertion.check()
				.isTrue(loops >= 0, "loops must be >= 0")
				.isNotNull(whileCondition)
				.isNotNull(node)
				.isNotNull(untilCondition);
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
			//loops when succeeded until a fail or a stop
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
