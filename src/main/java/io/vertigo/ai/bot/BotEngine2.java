package io.vertigo.ai.bot;

import static io.vertigo.ai.bt.BTNodes.condition;
import static io.vertigo.ai.bt.BTNodes.selector;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import io.vertigo.ai.bb.BBBlackBoard;
import io.vertigo.ai.bt.BTCondition;
import io.vertigo.ai.bt.BTNode;
import io.vertigo.ai.bt.BTStatus;
import io.vertigo.core.util.StringUtil;

public class BotEngine2 {
	//	private final Scanner sc = new Scanner(System.in);
	public final BBBlackBoard bb = new BBBlackBoard();

	public BTNode set(final String keyTemplate, final int value) {
		return set(keyTemplate, String.valueOf(value));
	}

	public BTNode copy(final String sourceKeyTemplate, final String targetKeyTemplate) {
		return () -> {
			bb.put(bb.format(targetKeyTemplate), bb.get(bb.format(sourceKeyTemplate)));
			return BTStatus.Succeeded;
		};
	}

	public BTNode set(final String keyTemplate, final String value) {
		return () -> {
			bb.put(bb.format(keyTemplate), value);
			return BTStatus.Succeeded;
		};
	}

	public BTNode inc(final String keyTemplate) {
		return () -> {
			bb.incr(bb.format(keyTemplate));
			return BTStatus.Succeeded;
		};
	}

	public BTNode clear(final String keyPattern) {
		return () -> {
			bb.remove(keyPattern);
			return BTStatus.Succeeded;
		};
	}

	public BTNode clear() {
		return () -> {
			bb.removeAll();
			return BTStatus.Succeeded;
		};
	}

	public BTNode fulfill(final String keyTemplate, final String question, final Predicate<String> validator) {
		return selector(
				isFulFilled(keyTemplate),
				query(keyTemplate, question, validator));
	}

	private BTNode query(final String keyTemplate, final String question, final Predicate<String> validator) {
		return () -> {
			System.out.println(bb.format(question));
			bb.put("bot/response", keyTemplate);
			return BTStatus.Running;
		};
		//		return doTry(3, () -> {
		//		()-> /*final String response =*/ answer(keyTemplate, question);
		//		return BTNodes.running();
		//			if (validator.test(response)) {
		//				bb.put(bb.format(keyTemplate), response);
		//				return BTStatus.Succeeded;
		//			}
		//			System.err.println("error parsing : " + response);
		//			return BTStatus.Failed;
		//		});
	}

	public BTCondition isFulFilled(final String keyTemplate) {
		return condition(() -> bb.get(bb.format(keyTemplate)) != null);
	}

	//2 args
	public BTNode fulfill(final String keyTemplate, final String question) {
		final Predicate<String> validator = t -> !StringUtil.isBlank(t);
		return fulfill(keyTemplate, question, validator);
	}

	private static Predicate<String> buildChoices(final String choice, final String... otherChoices) {
		final List<String> choices = new ArrayList<>();
		choices.add(choice);
		choices.addAll(List.of(otherChoices));
		return t -> choices.contains(t);
	}

	//3+ args
	public BTNode fulfill(final String keyTemplate, final String question, final String choice, final String... otherChoices) {
		return fulfill(keyTemplate, question, buildChoices(choice, otherChoices));
	}

	public BTNode display(final String msg) {
		return () -> {
			System.out.println(bb.format(msg));
			return BTStatus.Succeeded;
		};
	}

	public BTCondition eq(final String keyTemplate, final String compare) {
		return condition(() -> bb.eq(bb.format(keyTemplate), compare));
	}

	public BTCondition gt(final String keyTemplate, final String compare) {
		return condition(() -> bb.gt(bb.format(keyTemplate), compare));
	}

	public BTCondition lt(final String keyTemplate, final String compare) {
		return condition(() -> bb.lt(bb.format(keyTemplate), compare));
	}

	public BotSwitch doSwitch(final String keyTemplate) {
		return new BotSwitch(this, keyTemplate);
	}

	/*	public BTNode confirm(final String keyTemplate, final String confirmMsg) {
			return () -> {
				final String response = answer(confirmMsg);
				if (response.isBlank()) {
					return BTStatus.Succeeded;
				}
				bb.put(bb.format(keyTemplate), response);
				return BTStatus.Succeeded;
			};
		}*/

	@Override
	public String toString() {
		return bb.toString();
	}
}
