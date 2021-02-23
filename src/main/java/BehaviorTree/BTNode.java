package BehaviorTree;

import java.util.function.Supplier;

import io.vertigo.core.lang.Assertion;

@FunctionalInterface
public interface BTNode {
	BTStatus eval();

	default BTNode guardedBy(final BTCondition condition) {
		Assertion.check()
				.isNotNull(condition);
		//---
		return sequence(condition, this);
	}

	static BTNode sequence(final BTNode... nodes) {
		return new BTSequence(nodes);
	}

	static BTNode selector(final BTNode... nodes) {
		return new BTSelector(nodes);
	}

	static BTNode loop(final BTNode node) {
		return new BTLoop(succeed(), node, fail());
	}

	static BTNode loopUntil(final BTCondition untilCondition, final BTNode node) {
		return new BTLoop(succeed(), node, untilCondition);
	}

	static BTCondition condition(final Supplier<Boolean> test) {
		return new BTCondition(test);
	}

	static BTCondition succeed() {
		return condition(() -> true);
	}

	static BTCondition fail() {
		return condition(() -> false);
	}

	static BTNode stop() {
		//It's not a condition !
		return () -> BTStatus.Stopped;
	}
}
