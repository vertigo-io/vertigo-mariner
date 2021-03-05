package io.vertigo.ai.samples;

import static io.vertigo.ai.bt.BTNode.loopUntil;
import static io.vertigo.ai.bt.BTNode.selector;
import static io.vertigo.ai.bt.BTNode.sequence;

import io.vertigo.ai.bot.BotEngine;
import io.vertigo.ai.bt.BTNode;
import io.vertigo.ai.bt.BTRoot;

public class SampleBot {
	private final BotEngine botEngine;
	private final BTRoot root;

	public static void main(final String[] args) {
		new SampleBot().run();
	}

	public SampleBot() {
		this.botEngine = new BotEngine();
		root = new BTRoot(BTNode.loop(main()));
	}

	public void run() {
		root.run();
	}

	private BTNode main() {
		return sequence(
				botEngine.fulfill("u/name", "Hello I'm Alan what is your name ?"),
				botEngine.fulfill("i/name", "Hi {{u/name}} please select [W]eather, [T]icket, [G]ame or e[X]it ?", "W", "G", "T", "X"),
				botEngine.doSwitch("i/name")
						.when("X",
								botEngine.display("bye bye {{u/name}}"),
								BTNode.stop())
						.whenOther(
								dispatch(),
								rate())
						.build(),
				botEngine.clear("i/*"),
				botEngine.clear("rate/*"));
	}

	private BTNode dispatch() {
		return botEngine.doSwitch("i/name")
				.when("W", weather())
				.when("G", game())
				.when("T", ticket())
				.build();

		//		return  selector(
		//				weather(bTChat)
		//						.guardedBy(bTChat.eq("i/name", "W")),
		//				game(bTChat)
		//						.guardedBy(bTChat.eq("i/name", "G")),
		//				ticket(bTChat)
		//						.guardedBy(bTChat.eq("i/name", "T")));
	}

	private BTNode weather() {
		return sequence(
				botEngine.fulfill("w/city", "Please choose a city"),
				botEngine.display("It's sunny in {{w/city}} !"),
				botEngine.clear("w/*"));
	}

	private BTNode ticket() {
		return sequence(
				botEngine.display("You have chosen to book a ticket, I have some questions..."),
				botEngine.fulfill("t/return", "Do you want a return ticket  ? Y/N", "Y", "N"),
				botEngine.fulfill("t/from", "from ?"),
				botEngine.fulfill("t/to", "to ?"),
				botEngine.fulfill("t/count", "How many tickets ?",
						BTUtils.isInteger().and(s -> Integer.valueOf(s) > 0 && Integer.valueOf(s) < 10)),
				loopUntil(botEngine.eq("t/idx", "{{t/count}}"),
						botEngine.inc("t/idx"),
						botEngine.fulfill("t/{{t/idx}}/name", "What is the name of the {{t/idx}} person ?"),
						botEngine.display("The ticket {{t/idx}} is booked for {{t/{{t/idx}}/name}}")),
				botEngine.display("Thank you, your ticket from {{t/from}} to {{t/to}} for {{t/count}} persons will be sent..."),
				botEngine.clear("t/*"));
	}

	private BTNode game() {
		return sequence(
				botEngine.display("You have chosen to play !"),
				botEngine.display("{{u/name}}, you must find the number I have chosen between 0 and 100"),
				botEngine.set("g/target",
						Double.valueOf(Math.floor(Math.random() * 101)).intValue()),
				loopUntil(botEngine.eq("g/target", "{{g/choice}}"),
						botEngine.clear("g/choice"),
						botEngine.fulfill("g/choice", "What is your choice ?"),
						botEngine.inc("g/rounds"),
						selector(
								botEngine.display("select down !")
										.guardedBy(botEngine.gt("g/target", "{{g/choice}}")),
								botEngine.display("select up !")
										.guardedBy(botEngine.lt("g/target", "{{g/choice}}")),
								BTNode.succeed())),
				botEngine.display("Bravo {{u/name}} you have found the right number {{g/target}} in {{g/rounds}} rounds"));
	}

	private BTNode rate() {
		return sequence(
				botEngine.fulfill("rate/rating", "Please rate the response [0, 1, 2, 3, 4, 5]", "0", "1", "2", "3", "4", "5"),
				botEngine.display("You have rated {{rate/rating}}"));
	}
}
