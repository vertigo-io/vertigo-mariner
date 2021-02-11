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

	public BTStatus eval() {
		//System.out.print(".....before eval root ");
		return rootNode.eval();
		//System.out.print(".....after  eval root >>" + status);
	}
}
