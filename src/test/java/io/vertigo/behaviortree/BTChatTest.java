package io.vertigo.behaviortree;

import static BehaviorTree.BTNode.loopUntil;
import static BehaviorTree.BTNode.selector;
import static BehaviorTree.BTNode.sequence;

import org.junit.jupiter.api.Test;

import BehaviorTree.BTNode;
import BehaviorTree.BTRoot;
import BehaviorTree.BTUtils;

public class BTChatTest {
	@Test
	public void test() {
		final BTChat bTChat = new BTChat();
		//state.values.put("name", "john");
		new BTRoot(BTNode.loop(goal(bTChat))).exec();

	}

	private static BTNode goal(final BTChat bTChat) {
		return sequence(
				bTChat.fulfill("u/name", "Hello I'm Alan what is your name ?"),
				bTChat.fulfill("i/name", "Hi {{u/name}} please select [W]eather, [T]icket, [G]ame or e[X]it ?", "W", "G", "T", "X"),
				bTChat.doSwitch("i/name")
						.when("X", sequence(
								bTChat.display("bye bye {{u/name}}"),
								BTNode.stop()))
						.whenOther(sequence(
								dispatch(bTChat),
								rate(bTChat)))
						.build(),
				bTChat.clear("i/*"),
				bTChat.clear("rate/*"));
	}

	private static BTNode dispatch(final BTChat bTChat) {
		return bTChat.doSwitch("i/name")
				.when("W", weather(bTChat))
				.when("G", game(bTChat))
				.when("T", ticket(bTChat))
				.build();

		//		return  selector(
		//				weather(bTChat)
		//						.guardedBy(bTChat.eq("i/name", "W")),
		//				game(bTChat)
		//						.guardedBy(bTChat.eq("i/name", "G")),
		//				ticket(bTChat)
		//						.guardedBy(bTChat.eq("i/name", "T")));
	}

	private static BTNode weather(final BTChat bTChat) {
		return sequence(
				bTChat.fulfill("w/city", "Please choose a city"),
				bTChat.display("It's sunny in {{w/city}} !"),
				bTChat.clear("w/*"));
	}

	private static BTNode ticket(final BTChat bTChat) {
		return sequence(
				bTChat.display("You have chosen to book a ticket, I have some questions..."),
				bTChat.fulfill("t/return", "Do you want a return ticket  ? Y/N", "Y", "N"),
				bTChat.fulfill("t/from", "from ?"),
				bTChat.fulfill("t/to", "to ?"),
				bTChat.fulfill("t/count", "How many tickets ?",
						BTUtils.isInteger().and(s -> Integer.valueOf(s) > 0 && Integer.valueOf(s) < 10)),
				loopUntil(bTChat.eq("t/idx", "{{t/count}}"),
						sequence(
								bTChat.inc("t/idx"),
								bTChat.fulfill("t/{{t/idx}}/name", "What is the name of the {{t/idx}} person ?"),
								bTChat.display("The ticket {{t/idx}} is booked for {{t/{{t/idx}}/name}}"))),
				bTChat.display("Thank you, your ticket from {{t/from}} to {{t/to}} for {{t/count}} persons will be sent..."),
				bTChat.clear("t/*"));
	}

	private static BTNode game(final BTChat bTChat) {
		return sequence(
				bTChat.display("You have chosen to play !"),
				bTChat.display("{{u/name}}, you must find the number I have chosen between 0 and 100"),
				bTChat.set("g/target",
						Double.valueOf(Math.floor(Math.random() * 101)).intValue()),
				loopUntil(bTChat.eq("g/target", "{{g/choice}}"),
						sequence(
								bTChat.clear("g/choice"),
								bTChat.fulfill("g/choice", "What is your choice ?"),
								bTChat.inc("g/rounds"),
								selector(
										bTChat.display("select down !")
												.guardedBy(bTChat.gt("g/target", "{{g/choice}}")),
										bTChat.display("select up !")
												.guardedBy(bTChat.lt("g/target", "{{g/choice}}")),
										BTNode.succeed()))),
				bTChat.display("Bravo {{u/name}} you have found the right number {{g/target}} in {{g/rounds}} rounds"));
	}

	private static BTNode rate(final BTChat bTChat) {
		return sequence(
				bTChat.fulfill("rate/rating", "Please rate the response [0, 1, 2, 3, 4, 5]", "0", "1", "2", "3", "4", "5"),
				bTChat.display("You have rated {{rate/rating}}"));
	}
}
