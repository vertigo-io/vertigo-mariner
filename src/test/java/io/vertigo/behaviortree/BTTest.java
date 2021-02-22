package io.vertigo.behaviortree;

import static BehaviorTree.BTNode.selector;
import static BehaviorTree.BTNode.sequence;

import org.junit.jupiter.api.Test;

import BehaviorTree.BTNode;
import BehaviorTree.BTRoot;

public class BTTest {
	private static final String INTENTION = "intention";

	@Test
	public void test() {
		final State state = new State();
		//state.values.put("name", "john");
		new BTRoot(goal(state)).run();
	}

	private static BTNode goal(final State state) {
		return sequence(
				state.fulfill(INTENTION, "Select [W]eather, [T]icket or e[X]it ?", "W", "T", "X"),
				selector(
						state.equals(INTENTION, "X"),
						sequence(
								dispatch(state),
								rate(state),
								state.clearAll(),
								BTNode.running())));
	}

	private static BTNode dispatch(final State state) {
		return selector(
				sequence(
						state.equals(INTENTION, "W"),
						//						state.clear("w/*"),
						weather(state)),
				sequence(
						state.equals(INTENTION, "T"),
						//state.clear("t/*"),
						ticket(state)));
	}

	private static BTNode weather(final State state) {
		return sequence(
				state.fulfill("w/city", "Please choose a city"),
				state.display("w/display", "It's sunny in {{w/city}}"));
	}

	private static BTNode rate(final State state) {
		return sequence(
				state.fulfill("rate/rating", "Please rate the response [0, 1, 2, 3, 4, 5]", "0", "1", "2", "3", "4", "5"),
				state.display("rate/display", "You have rated {{rate/rating}}"));
	}

	private static BTNode ticket(final State state) {
		return sequence(
				state.display("t/begin", "You have chosen to book a ticket, I have some questions..."),
				state.fulfill("t/name", "What is your name ?"),
				state.fulfill("t/return", "Do you want a return ticket  ? Y/N", "Y", "N"),
				state.fulfill("t/from", "from ?"),
				state.fulfill("t/to", "to ?"),
				state.fulfill("t/count", "How many tickets ?", Utils.isInteger()),
				state.display("t/end", "Thank you, your ticket will be sent ..."));
	}
}
