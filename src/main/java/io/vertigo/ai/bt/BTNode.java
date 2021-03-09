package io.vertigo.ai.bt;

import java.util.List;
import java.util.function.Supplier;

import io.vertigo.core.lang.Assertion;

/**
 * BT are composed of nodes 'BTNode' with a single root 'BTRoot' 
 * This nodes are memory less, they don't store data.
 * 
 * a BTNode have only one method called eval()
 * this method returns a status 'BTStatus' which contains the following states :
 *  - Failed
 *  - Succeeded
 *  - Stopped
 * 
 * Nodes are splitted into two categories
 *  - Leaves 
 *  - Composites
 * 
 * Composite are composed from one to many other nodes 
 * They control the flow 
 *  - Sequence 
 *  - Selector
 *  - Switch (a special kind of selector)
 *  - Loop (a repeatable sequence)
 *  - Try 
 * 
 * Leaves are themselves splitted into
 *  - Conditions 
 *  - Tasks
 *   
 * @author pchretien
 */
@FunctionalInterface
public interface BTNode {
	BTStatus eval();

	default BTNode guardedBy(final BTCondition... conditions) {
		return guardedBy(List.of(conditions));
	}

	default BTNode guardedBy(final List<BTCondition> conditions) {
		Assertion.check()
				.isNotNull(conditions);
		//---
		return sequence(sequence(conditions), this);
	}

	/**
	 * Creates a sequence. 
	 * Succeeds when all nodes succeed
	 * Fails when one node fails, the next nodes are not evaluated
	 * @param nodes nodes 
	 * @return sequence
	 */
	static BTNode sequence(final BTNode... nodes) {
		return sequence(List.of(nodes));
	}

	static BTNode sequence(final List<? extends BTNode> nodes) {
		Assertion.check().isNotNull(nodes);
		//---
		return nodes.size() == 1
				? nodes.get(0)
				: new BTSequence(nodes);
	}

	/**
	 * Creates a selector. 
	 * Fails when all node fail
	 * Succeeds when one node succeeds, the newt nodes are not evaluated
	 * @param nodes nodes 
	 * @return selector
	 */
	static BTNode selector(final BTNode... nodes) {
		return selector(List.of(nodes));
	}

	static BTNode selector(final List<? extends BTNode> nodes) {
		return new BTSelector(nodes);
	}

	static BTNode doTry(final int tries, final BTNode... nodes) {
		return doTry(tries, List.of(nodes));
	}

	static BTNode doTry(final int tries, final List<BTNode> nodes) {
		return new BTTry(tries, sequence(nodes));
	}

	static BTNode loop(final BTNode... nodes) {
		return loop(List.of(nodes));
	}

	static BTNode loop(final List<BTNode> nodes) {
		return new BTLoop(BTLoop.MAX_LOOPS, succeed(), sequence(nodes), fail());
	}

	static BTNode loop(final int rounds, final BTNode... nodes) {
		return loop(rounds, List.of(nodes));
	}

	static BTNode loop(final int rounds, final List<BTNode> nodes) {
		return new BTLoop(rounds, succeed(), sequence(nodes), fail());
	}

	static BTNode loopWhile(final BTCondition whileCondition, final BTNode... nodes) {
		return loopWhile(whileCondition, List.of(nodes));
	}

	static BTNode loopWhile(final BTCondition whileCondition, final List<BTNode> nodes) {
		return new BTLoop(BTLoop.MAX_LOOPS, whileCondition, sequence(nodes), fail());
	}

	static BTNode loopUntil(final BTCondition untilCondition, final BTNode... nodes) {
		return loopUntil(untilCondition, List.of(nodes));
	}

	static BTNode loopUntil(final BTCondition untilCondition, final List<BTNode> nodes) {
		return new BTLoop(BTLoop.MAX_LOOPS, succeed(), sequence(nodes), untilCondition);
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
