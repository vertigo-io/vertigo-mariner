package io.vertigo.ai.dqm;

import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

import io.vertigo.ai.bt.BTBlackBoard;
import io.vertigo.ai.bt.BTCondition;
import io.vertigo.ai.bt.BTNode;
import io.vertigo.ai.bt.BTStatus;
import io.vertigo.core.lang.BasicType;
import io.vertigo.core.util.StringUtil;
import io.vertigo.datamodel.structure.definitions.FormatterException;

public final class DQMAssistantEngine {
	private final Scanner sc = new Scanner(System.in);
	public final BTBlackBoard bb = new BTBlackBoard();
	private final DQMFormatter dqmFormatter = new DQMFormatter();

	public BTNode set(final String key, final int value) {
		return set(key, "" + value);
	}

	public BTNode set(final String key, final String value) {
		return () -> {
			bb.put(key, value);
			return BTStatus.Succeeded;
		};
	}

	private String answer(final String question) {
		System.out.println(bb.format(question));
		//---
		return sc.nextLine();
	}

	private BTNode query(final String key, final String question, final Predicate<String> validator) {
		return () -> {
			final String response = answer(question);
			if (validator.test(response)) {
				bb.put(key, response);
				return BTStatus.Succeeded;
			}
			System.err.println("error parsing : result " + question);

			return BTStatus.Failed;
		};
	}

	private BTNode testType(final String key, final BasicType basicType) {
		return BTNode.sequence(() -> {
			try {
				dqmFormatter.stringToValue(bb.get(key), basicType);
				return BTStatus.Succeeded;
			} catch (final FormatterException e) {
				return BTStatus.Failed;
			}
		}, set(key + "/type", basicType.name()));

	}

	public BTNode probeType(final String key) {
		return BTNode.selector(
				testType(key, BasicType.Integer),
				testType(key, BasicType.Long),
				testType(key, BasicType.Double),
				testType(key, BasicType.BigDecimal),
				testType(key, BasicType.Boolean),
				testType(key, BasicType.LocalDate),
				testType(key, BasicType.Instant),
				testType(key, BasicType.String)// fallback
		);

	}

	public BTCondition isFulFilled(final String key) {
		return BTNode.condition(() -> bb.get(key) != null);
	}

	public BTNode normalize(final String key) {
		return () -> {
			bb.put(key, StringUtil.first2UpperCase(bb.get(key).toLowerCase()));
			return BTStatus.Succeeded;
		};
	}

	public BTNode confirm(final String key) {
		return () -> {
			final String response = answer("Press Enter to confirm value {{" + key + "}} or type the correct value");
			if (response.isBlank()) {
				return BTStatus.Succeeded;
			}
			bb.put(key, response);
			return BTStatus.Succeeded;
		};
	}

	public BTNode copy(final String sourceKey, final String targetKey) {
		return () -> {
			bb.put(targetKey, bb.get(sourceKey));
			return BTStatus.Succeeded;
		};
	}

	public BTNode fulfill(final String key, final String question) {
		final Predicate<String> validator = t -> !StringUtil.isBlank(t);
		return fulfill(key, question, validator);
	}

	public BTNode fulfill(final String key, final String question, final String... choices) {
		final List<String> choiceList = List.of(choices);
		final Predicate<String> validator = t -> choiceList.contains(t);
		return fulfill(key, question, validator);
	}

	public BTNode fulfill(final String key, final String question, final Predicate<String> validator) {
		return BTNode.selector(
				isFulFilled(key),
				query(key, question, validator));
	}

	public BTNode display(final String msg) {
		return () -> {
			System.out.println(bb.format(msg));
			return BTStatus.Succeeded;
		};
	}

	public BTNode inc(final String key) {
		return () -> {
			bb.inc(key);
			return BTStatus.Succeeded;
		};
	}

	public BTCondition eq(final String key, final String compare) {
		return BTNode.condition(() -> bb.eq(key, compare));
	}

	public BTCondition gt(final String key, final String compare) {
		return BTNode.condition(() -> bb.gt(key, compare));
	}

	public BTCondition lt(final String key, final String compare) {
		return BTNode.condition(() -> bb.lt(key, compare));
	}

	public BTNode clear(final String keyPattern) {
		return () -> {
			bb.clear(keyPattern);
			return BTStatus.Succeeded;
		};
	}

	public BTNode clearAll() {
		return () -> {
			bb.clearAll();
			return BTStatus.Succeeded;
		};
	}

	@Override
	public String toString() {
		return bb.toString();
	}
}
