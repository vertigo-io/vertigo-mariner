package BehaviorTree;

import java.util.function.Supplier;

@FunctionalInterface
public interface BTNode {
	BTStatus eval();

	static BTNode sequence(final BTNode... nodes) {
		return new BTSequence(nodes);
	}

	static BTNode selector(final BTNode... nodes) {
		return new BTSelector(nodes);
	}

	static BTNode loop(final BTNode node) {
		return new BTLoop(succeed(), node, fail());
	}

	static BTNode loopUntil(final BTNode untilCondition, final BTNode node) {
		return new BTLoop(succeed(), node, untilCondition);
	}

	static BTNode condition(final Supplier<Boolean> test) {
		return () -> test.get() ? BTStatus.Succeeded : BTStatus.Failed;
	}

	static BTNode succeed() {
		return () -> BTStatus.Succeeded;
	}

	static BTNode fail() {
		return () -> BTStatus.Failed;
	}

	static BTNode stop() {
		return () -> BTStatus.Stopped;
	}
}
