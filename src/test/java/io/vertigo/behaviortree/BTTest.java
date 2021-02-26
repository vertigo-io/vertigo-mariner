package io.vertigo.behaviortree;

import static BehaviorTree.BTNode.loopUntil;
import static BehaviorTree.BTNode.selector;
import static BehaviorTree.BTNode.sequence;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import BehaviorTree.BTNode;
import BehaviorTree.BTRoot;

public class BTTest {
	@Test
	public void test() {
		final State state = new State();
		//state.values.put("name", "john");
		new BTRoot(BTNode.loop(goal(state))).exec();

	}

	private static BTNode goal(final State state) {
		return sequence(
				state.fulfill("u/name", "Hello I'm Alan what is your name ?"),
				state.fulfill("i/name", "Hi {{u/name}} please select [W]eather, [T]icket, [G]ame or e[X]it ?", "W", "G", "T", "X"),
				selector(
						sequence(
								state.eq("i/name", "X"),
								state.display("bye bye {{u/name}}"),
								BTNode.stop()),
						sequence(
								dispatch(state),
								rate(state))),
				state.clear("i/*"),
				state.clear("rate/*"));
	}

	private static BTNode dispatch(final State state) {
		return selector(
				weather(state)
						.guardedBy(state.eq("i/name", "W")),
				game(state)
						.guardedBy(state.eq("i/name", "G")),
				ticket(state)
						.guardedBy(state.eq("i/name", "T")));
	}

	private static BTNode weather(final State state) {
		return sequence(
				state.fulfill("w/city", "Please choose a city"),
				state.display("It's sunny in {{w/city}} !"),
				state.clear("w/*"));
	}

	private static BTNode ticket(final State state) {
		return sequence(
				state.display("You have chosen to book a ticket, I have some questions..."),
				state.fulfill("t/return", "Do you want a return ticket  ? Y/N", "Y", "N"),
				state.fulfill("t/from", "from ?"),
				state.fulfill("t/to", "to ?"),
				state.fulfill("t/count", "How many tickets ?",
						Utils.isInteger().and(s -> Integer.valueOf(s) > 0 && Integer.valueOf(s) < 10)),
				loopUntil(state.eq("t/idx", "{{t/count}}"),
						sequence(
								state.inc("t/idx"),
								state.fulfill("t/{{t/idx}}/name", "What is the name of the {{t/idx}} person ?"),
								state.display("The ticket {{t/idx}} is booked for {{t/{{t/idx}}/name}}"))),
				state.display("Thank you, your ticket from {{t/from}} to {{t/to}} for {{t/count}} persons will be sent..."),
				state.clear("t/*"));
	}

	private static BTNode game(final State state) {
		return sequence(
				state.display("You have chosen to play !"),
				state.display("{{u/name}}, you must find the number I have chosen between 0 and 100"),
				state.set("g/target",
						Double.valueOf(Math.floor(Math.random() * 101)).intValue()),
				loopUntil(state.eq("g/target", "{{g/choice}}"),
						sequence(
								state.clear("g/choice"),
								state.fulfill("g/choice", "What is your choice ?"),
								state.inc("g/rounds"),
								selector(
										state.display("select down !")
												.guardedBy(state.gt("g/target", "{{g/choice}}")),
										state.display("select up !")
												.guardedBy(state.lt("g/target", "{{g/choice}}")),
										BTNode.succeed()))),
				state.display("Bravo {{u/name}} you have found the right number {{g/target}} in {{g/rounds}} rounds"));
	}

	private static BTNode rate(final State state) {
		return sequence(
				state.fulfill("rate/rating", "Please rate the response [0, 1, 2, 3, 4, 5]", "0", "1", "2", "3", "4", "5"),
				state.display("You have rated {{rate/rating}}"));
	}

	@Test
	public void testFormatter() {
		final Map map = new HashMap<>();
		map.put("name", "joe");
		map.put("lastName", "diMagio");
		Assertions.assertEquals("joe", Utils.format("{{name}}", map));
		Assertions.assertEquals("hello joe", Utils.format("hello {{name}}", map));
		Assertions.assertEquals("hello joe...", Utils.format("hello {{name}}...", map));
		Assertions.assertEquals("hello joe diMagio", Utils.format("hello {{name}} {{lastName}}", map));
		Assertions.assertThrows(IllegalStateException.class,
				() -> Utils.format("hello {{name}", map));
		Assertions.assertThrows(IllegalStateException.class,
				() -> Utils.format("hello {{name", map));
		Assertions.assertThrows(IllegalStateException.class,
				() -> Utils.format("hello name}}", map));
		map.put("u/1/name", "alan");
		map.put("u/2/name", "ada");
		map.put("u/idx", "2");
		Assertions.assertEquals("hello ada", Utils.format("hello {{u/{{u/idx}}/name}}", map));
	}
}
