package BehaviorTree;

import java.util.function.Supplier;

import io.vertigo.core.lang.Assertion;

public final class BTCondition implements BTNode {
	private final Supplier<Boolean> test;

	BTCondition(final Supplier<Boolean> test) {
		Assertion.check()
				.isNotNull(test);
		//---
		this.test = test;
	}

	@Override
	public BTStatus eval() {
		return test.get()
				? BTStatus.Succeeded
				: BTStatus.Failed;
	}
}
