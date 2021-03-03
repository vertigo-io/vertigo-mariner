package io.vertigo.ai.dqm;

import io.vertigo.ai.bot.BotEngine;
import io.vertigo.ai.bt.BTNode;
import io.vertigo.ai.bt.BTStatus;
import io.vertigo.core.lang.BasicType;
import io.vertigo.core.util.StringUtil;
import io.vertigo.datamodel.structure.definitions.FormatterException;

public final class DQMAssistantEngine extends BotEngine {
	private final DQMFormatter dqmFormatter = new DQMFormatter();

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

	public BTNode normalize(final String key) {
		return () -> {
			bb.put(key, StringUtil.first2UpperCase(bb.get(key).toLowerCase()));
			return BTStatus.Succeeded;
		};
	}
}
