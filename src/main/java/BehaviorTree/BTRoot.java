package BehaviorTree;

import io.vertigo.core.lang.Assertion;

public final class BTRoot {
	private final BTNode rootNode;

	public BTRoot(final BTNode rootNode) {
		Assertion.check()
				.isNotNull(rootNode);
		//---
		this.rootNode = rootNode;
	}

	public void run() {
		var status = rootNode.eval();
		while (status != BTStatus.Succeeded) {
			status = rootNode.eval();
		}
	}
}
