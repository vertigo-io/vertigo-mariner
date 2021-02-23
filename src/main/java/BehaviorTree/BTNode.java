package BehaviorTree;

import java.util.function.Supplier;

@FunctionalInterface
public interface BTNode {
	//	String getKey();

	BTStatus eval();

	static BTNode sequence(final BTNode... nodes) {
		return new BTSequence(nodes);
	}

	static BTNode selector(final BTNode... nodes) {
		return new BTSelector(nodes);
	}

	static BTNode loopUntil(final BTNode untilCondition, final BTNode node) {
		return new BTLoop(sucess(), node, untilCondition);
	}

	static BTNode condition(final Supplier<Boolean> test) {
		return () -> test.get() ? BTStatus.Succeeded : BTStatus.Failed;
	}

	static BTNode sucess() {
		return () -> BTStatus.Succeeded;
	}

	static BTNode failed() {
		return () -> BTStatus.Failed;
	}
}
