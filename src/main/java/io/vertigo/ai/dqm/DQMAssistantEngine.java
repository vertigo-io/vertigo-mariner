package io.vertigo.ai.dqm;

import static io.vertigo.ai.bt.BTNodes.selector;
import static io.vertigo.ai.bt.BTNodes.sequence;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import io.vertigo.ai.bb.BBBlackBoard;
import io.vertigo.ai.bot.BotEngine;
import io.vertigo.ai.bt.BTNode;
import io.vertigo.ai.bt.BTStatus;
import io.vertigo.core.lang.BasicType;
import io.vertigo.core.util.StringUtil;
import io.vertigo.datamodel.structure.definitions.FormatterException;

public final class DQMAssistantEngine extends BotEngine {
	private final DQMFormatter dqmFormatter = new DQMFormatter();

	private BTNode testType(final String key, final BasicType basicType) {
		return sequence(
				() -> {
					try {
						dqmFormatter.stringToValue(bb.get(key), basicType);
						return BTStatus.Succeeded;
					} catch (final FormatterException e) {
						return BTStatus.Failed;
					}
				},
				set(key + "/type", basicType.name()));

	}

	public BTNode probeType(final String key) {
		return selector(
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

	public BTNode normalize(final String key) {
		return () -> {
			bb.put(key, StringUtil.first2UpperCase(bb.get(key).toLowerCase()));
			return BTStatus.Succeeded;
		};
	}

	public BTNode injectData(final Supplier<Map<String, String>> dataSupplier) {
		return () -> {
			dataSupplier.get().forEach((key, value) -> bb.put("source/" + key, value));
			return BTStatus.Succeeded;
		};
	}

	public BTNode clearData() {
		return sequence(
				clear("source/*"),
				clear("target/*"));
	}

	public String read(final String key) {
		return bb.get(key);
	}

	public BTNode invoke(final Function<BBBlackBoard, BTStatus> method) {
		return () -> {
			try {
				return method.apply(bb);
			} catch (final Exception e) {
				return BTStatus.Failed;
			}
		};

	}

}
