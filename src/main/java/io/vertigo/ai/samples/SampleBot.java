package io.vertigo.ai.samples;

import static io.vertigo.ai.bt.BTNodes.guard;
import static io.vertigo.ai.bt.BTNodes.loopUntil;
import static io.vertigo.ai.bt.BTNodes.selector;
import static io.vertigo.ai.bt.BTNodes.sequence;
import static io.vertigo.ai.bt.BTNodes.succeed;

import io.vertigo.ai.bot.BotEngine;
import io.vertigo.ai.bt.BTNode;
import io.vertigo.ai.bt.BTNodes;
import io.vertigo.ai.bt.BTRoot;
import io.vertigo.ai.bt.BTStatus;

public class SampleBot {
	private final BotEngine botEngine;
	private final BTRoot root;

	public static void main(final String[] args) {
		new SampleBot().run();
	}

	public SampleBot() {
		this.botEngine = new BotEngine();
		root = new BTRoot(
				sequence(
						botEngine.fulfill("u/name", "Hello I'm Alan what is your name ?"),
						//intents
						BTNodes.transform(main(), BTStatus.Succeeded),
						//anyway we stay polite
						botEngine.display("bye bye {{u/name}}")));
	}

	public void run() {
		root.run();
	}

	private BTNode main() {
		return loopUntil(botEngine.eq("i/name", "X"),
				botEngine.fulfill("i/name", "Hi {{u/name}} please select [W]eather, [T]icket, [G]ame or e[X]it ?", "W", "G", "T", "X"),
				botEngine.doSwitch("i/name")
						.when("W", weather())
						.when("G", game())
						.when("T", ticket())
						.build(),
				rate(),
				botEngine.clear("i/*"),
				botEngine.clear("rate/*"));
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
								guard(
										botEngine.display("select down !"),
										botEngine.gt("g/target", "{{g/choice}}")),
								guard(
										botEngine.display("select up !"),
										botEngine.lt("g/target", "{{g/choice}}")),
								succeed())),
				botEngine.display("Bravo {{u/name}} you have found the right number {{g/target}} in {{g/rounds}} rounds"));
	}

	private BTNode rate() {
		return sequence(
				botEngine.fulfill("rate/rating", "Please rate the response [0, 1, 2, 3, 4, 5]", "0", "1", "2", "3", "4", "5"),
				botEngine.display("You have rated {{rate/rating}}"));
	}
}
