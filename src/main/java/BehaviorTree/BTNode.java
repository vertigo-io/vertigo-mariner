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

	/**
	 * Creates a sequence. 
	 * Succeeds when all nodes succeed
	 * Fails when one node fails, the next nodes are not evaluated
	 * @param nodes nodes 
	 * @return sequence
	 */
	static BTNode sequence(final BTNode... nodes) {
		return new BTSequence(nodes);
	}

	/**
	 * Creates a selector. 
	 * Fails when all node fail
	 * Succeeds when one node succeeds, the newt nodes are not evaluated
	 * @param nodes nodes 
	 * @return selector
	 */
	static BTNode selector(final BTNode... nodes) {
		return new BTSelector(nodes);
	}

	static BTNode doTry(final int tries, final BTNode node) {
		return new BTTry(tries, node);
	}

	static BTNode loop(final BTNode node) {
		return new BTLoop(BTLoop.MAX_LOOPS, succeed(), node, fail());
	}

	static BTNode loop(final int rounds, final BTNode node) {
		return new BTLoop(rounds, succeed(), node, fail());
	}

	static BTNode loopWhile(final BTCondition whileCondition, final BTNode node) {
		return new BTLoop(BTLoop.MAX_LOOPS, whileCondition, node, fail());
	}

	static BTNode loopUntil(final BTCondition untilCondition, final BTNode node) {
		return new BTLoop(BTLoop.MAX_LOOPS, succeed(), node, untilCondition);
	}

	static BTCondition condition(final Supplier<Boolean> test) {
		return new BTCondition(test);
	}

	static BTCondition succeed() {
		return BTCondition.SUCCEED;
	}

	static BTCondition fail() {
		return BTCondition.FAIL;
	}

	static BTNode stop() {
		//Stop is not a condition !
		return () -> BTStatus.Stopped;
	}
}
