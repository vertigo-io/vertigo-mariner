package io.vertigo.ai.bt;

import java.util.List;
import java.util.function.Supplier;

import io.vertigo.core.lang.Assertion;

public final class BTNodes {

	private BTNodes() {
		//private constructor
	}

	/**
	 * Creates a sequence. 
	 * Succeeds when all nodes succeed
	 * Fails when one node fails, the next nodes are not evaluated
	 * @param nodes nodes 
	 * @return sequence
	 */
	public static BTNode sequence(final BTNode... nodes) {
		return sequence(List.of(nodes));
	}

	public static BTNode sequence(final List<? extends BTNode> nodes) {
		Assertion.check()
				.isNotNull(nodes);
		//---
		return nodes.size() == 1
				? nodes.get(0) // A sequence of one element is equivalent to the element
				: new BTSequence(nodes);
	}

	/**
	 * Creates a selector. 
	 * Fails when all node fail
	 * Succeeds when one node succeeds, the newt nodes are not evaluated
	 * @param nodes nodes 
	 * @return selector
	 */
	public static BTNode selector(final BTNode... nodes) {
		return selector(List.of(nodes));
	}

	public static BTNode selector(final List<? extends BTNode> nodes) {
		Assertion.check()
				.isNotNull(nodes);
		//---
		return nodes.size() == 1
				? nodes.get(0) // A selector of one element is equivalent to the element
				: new BTSelector(nodes);
	}

	public static BTNode guard(final BTNode node, final BTCondition... conditions) {
		return guard(node, List.of(conditions));
	}

	public static BTNode guard(final BTNode node, final List<BTCondition> conditions) {
		Assertion.check()
				.isNotNull(node);
		//---
		return sequence(sequence(conditions), node);
	}

	public static BTNode doTry(final int tries, final BTNode... nodes) {
		return doTry(tries, List.of(nodes));
	}

	public static BTNode doTry(final int tries, final List<BTNode> nodes) {
		return new BTTry(tries, sequence(nodes));
	}

	public static BTNode loop(final BTNode... nodes) {
		return loop(List.of(nodes));
	}

	public static BTNode loop(final List<BTNode> nodes) {
		return new BTLoop(BTLoop.MAX_LOOPS, succeed(), sequence(nodes), fail());
	}

	public static BTNode loop(final int rounds, final BTNode... nodes) {
		return loop(rounds, List.of(nodes));
	}

	public static BTNode loop(final int rounds, final List<BTNode> nodes) {
		return new BTLoop(rounds, succeed(), sequence(nodes), fail());
	}

	public static BTNode loopWhile(final BTCondition whileCondition, final BTNode... nodes) {
		return loopWhile(whileCondition, List.of(nodes));
	}

	public static BTNode loopWhile(final BTCondition whileCondition, final List<BTNode> nodes) {
		return new BTLoop(BTLoop.MAX_LOOPS, whileCondition, sequence(nodes), fail());
	}

	public static BTNode loopUntil(final BTCondition untilCondition, final BTNode... nodes) {
		return loopUntil(untilCondition, List.of(nodes));
	}

	public static BTNode loopUntil(final BTCondition untilCondition, final List<BTNode> nodes) {
		return new BTLoop(BTLoop.MAX_LOOPS, succeed(), sequence(nodes), untilCondition);
	}

	public static BTCondition condition(final Supplier<Boolean> test) {
		return new BTCondition(test);
	}

	public static BTCondition succeed() {
		return BTCondition.SUCCEED;
	}

	public static BTCondition fail() {
		return BTCondition.FAIL;
	}

	public static BTNode stop() {
		//Stop is not a condition !
		return () -> BTStatus.Stopped;
	}
}
