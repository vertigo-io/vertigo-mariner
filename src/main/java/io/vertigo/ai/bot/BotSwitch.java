package io.vertigo.ai.bot;

import static io.vertigo.ai.bt.BTNodes.selector;
import static io.vertigo.ai.bt.BTNodes.sequence;

import java.util.ArrayList;
import java.util.List;

import io.vertigo.ai.bt.BTCondition;
import io.vertigo.ai.bt.BTNode;
import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.Builder;

public final class BotSwitch implements Builder<BTNode> {
	private final BotEngine botEngine;
	private final String keyTemplate;
	private final List<BTNode> selectorNodes = new ArrayList<>();

	BotSwitch(final BotEngine botEngine, final String keyTemplate) {
		Assertion.check()
				.isNotNull(botEngine)
				.isNotBlank(keyTemplate);
		//---
		this.botEngine = botEngine;
		this.keyTemplate = keyTemplate;
	}

	private BTCondition buildGuard(final String compare) {
		return botEngine.eq(keyTemplate, compare);
	}

	public BotSwitch when(final String compare, final BTNode... nodes) {
		return when(compare, List.of(nodes));
	}

	public BotSwitch when(final String compare, final List<BTNode> nodes) {
		selectorNodes.add(
				sequence(nodes)
						.guardedBy(buildGuard(compare)));
		return this;
	}

	public BotSwitch whenOther(final BTNode... nodes) {
		return whenOther(List.of(nodes));
	}

	public BotSwitch whenOther(final List<BTNode> nodes) {
		selectorNodes.add(
				sequence(nodes));
		return this;
	}

	@Override
	public BTNode build() {
		return selector(selectorNodes);
	}
}
