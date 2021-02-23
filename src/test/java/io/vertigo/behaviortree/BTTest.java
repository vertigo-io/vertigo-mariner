package io.vertigo.behaviortree;

import static BehaviorTree.BTNode.loopUntil;
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
						sequence(
								state.equals(INTENTION, "X"),
								BTNode.failed()),
						sequence(
								dispatch(state),
								rate(state),
								state.clearAll())));
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
				state.display("It's sunny in {{w/city}} !"));
	}

	private static BTNode ticket(final State state) {
		return sequence(
				state.display("You have chosen to book a ticket, I have some questions..."),
				state.fulfill("t/return", "Do you want a return ticket  ? Y/N", "Y", "N"),
				state.fulfill("t/from", "from ?"),
				state.fulfill("t/to", "to ?"),
				state.fulfill("t/count", "How many tickets ?",
						Utils.isInteger().and(s -> Integer.valueOf(s) > 0 && Integer.valueOf(s) < 10)),
				loopUntil(state.equals2("t/idx", "t/count"),
						sequence(
								state.inc("t/idx"),
								state.fulfill("t/name/{{t/idx}}", "What is the name of the {{t/idx}} person ?"),
								state.display("The ticket {{t/idx}} is booked"))),
				state.display("Thank you, your ticket from {{t/from}} to {{t/to}} for {{t/count}} persons will be sent..."));
	}

	private static BTNode rate(final State state) {
		return sequence(
				state.fulfill("rate/rating", "Please rate the response [0, 1, 2, 3, 4, 5]", "0", "1", "2", "3", "4", "5"),
				state.display("You have rated {{rate/rating}}"));
	}
}
