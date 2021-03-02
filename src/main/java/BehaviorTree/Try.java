package BehaviorTree;

import io.vertigo.core.lang.Assertion;

final class BTTry implements BTNode {
	private final int tries;
	private final BTNode node;

	BTTry(final int tries, final BTNode node) {
		Assertion.check()
				.isTrue(tries > 0, "tries must be > 0")
				.isNotNull(node);
		//---
		this.tries = tries;
		this.node = node;
	}

	@Override
	public BTStatus eval() {
		for (int i = 0; i < tries; i++) {
			final var status = node.eval();
			//loops until succeeded or stop
			if (status.isSucceeded() || status.isStopped()) {
				return status;
			}
		}
		return BTStatus.Failed;
	}

}
