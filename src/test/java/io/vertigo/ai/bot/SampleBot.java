package io.vertigo.ai.bot;

import static io.vertigo.ai.bt.BTNode.loopUntil;
import static io.vertigo.ai.bt.BTNode.selector;
import static io.vertigo.ai.bt.BTNode.sequence;

import org.junit.jupiter.api.Test;

import io.vertigo.ai.bt.BTNode;
import io.vertigo.ai.bt.BTRoot;
import io.vertigo.ai.bt.BTUtils;

public class SampleBot {
	@Test
	public void test() {
		final Bot bot = new Bot();
		//state.values.put("name", "john");
		new BTRoot(BTNode.loop(goal(bot))).exec();

	}

	private static BTNode goal(final Bot bot) {
		return sequence(
				bot.fulfill("u/name", "Hello I'm Alan what is your name ?"),
				bot.fulfill("i/name", "Hi {{u/name}} please select [W]eather, [T]icket, [G]ame or e[X]it ?", "W", "G", "T", "X"),
				bot.doSwitch("i/name")
						.when("X", sequence(
								bot.display("bye bye {{u/name}}"),
								BTNode.stop()))
						.whenOther(sequence(
								dispatch(bot),
								rate(bot)))
						.build(),
				bot.clear("i/*"),
				bot.clear("rate/*"));
	}

	private static BTNode dispatch(final Bot bot) {
		return bot.doSwitch("i/name")
				.when("W", weather(bot))
				.when("G", game(bot))
				.when("T", ticket(bot))
				.build();

		//		return  selector(
		//				weather(bTChat)
		//						.guardedBy(bTChat.eq("i/name", "W")),
		//				game(bTChat)
		//						.guardedBy(bTChat.eq("i/name", "G")),
		//				ticket(bTChat)
		//						.guardedBy(bTChat.eq("i/name", "T")));
	}

	private static BTNode weather(final Bot bot) {
		return sequence(
				bot.fulfill("w/city", "Please choose a city"),
				bot.display("It's sunny in {{w/city}} !"),
				bot.clear("w/*"));
	}

	private static BTNode ticket(final Bot bot) {
		return sequence(
				bot.display("You have chosen to book a ticket, I have some questions..."),
				bot.fulfill("t/return", "Do you want a return ticket  ? Y/N", "Y", "N"),
				bot.fulfill("t/from", "from ?"),
				bot.fulfill("t/to", "to ?"),
				bot.fulfill("t/count", "How many tickets ?",
						BTUtils.isInteger().and(s -> Integer.valueOf(s) > 0 && Integer.valueOf(s) < 10)),
				loopUntil(bot.eq("t/idx", "{{t/count}}"),
						sequence(
								bot.inc("t/idx"),
								bot.fulfill("t/{{t/idx}}/name", "What is the name of the {{t/idx}} person ?"),
								bot.display("The ticket {{t/idx}} is booked for {{t/{{t/idx}}/name}}"))),
				bot.display("Thank you, your ticket from {{t/from}} to {{t/to}} for {{t/count}} persons will be sent..."),
				bot.clear("t/*"));
	}

	private static BTNode game(final Bot bot) {
		return sequence(
				bot.display("You have chosen to play !"),
				bot.display("{{u/name}}, you must find the number I have chosen between 0 and 100"),
				bot.set("g/target",
						Double.valueOf(Math.floor(Math.random() * 101)).intValue()),
				loopUntil(bot.eq("g/target", "{{g/choice}}"),
						sequence(
								bot.clear("g/choice"),
								bot.fulfill("g/choice", "What is your choice ?"),
								bot.inc("g/rounds"),
								selector(
										bot.display("select down !")
												.guardedBy(bot.gt("g/target", "{{g/choice}}")),
										bot.display("select up !")
												.guardedBy(bot.lt("g/target", "{{g/choice}}")),
										BTNode.succeed()))),
				bot.display("Bravo {{u/name}} you have found the right number {{g/target}} in {{g/rounds}} rounds"));
	}

	private static BTNode rate(final Bot bot) {
		return sequence(
				bot.fulfill("rate/rating", "Please rate the response [0, 1, 2, 3, 4, 5]", "0", "1", "2", "3", "4", "5"),
				bot.display("You have rated {{rate/rating}}"));
	}
}
