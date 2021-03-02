package io.vertigo.ai.bot;

import java.util.ArrayList;
import java.util.List;

import io.vertigo.ai.bt.BTCondition;
import io.vertigo.ai.bt.BTNode;
import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.Builder;

public final class BotSwitch implements Builder<BTNode> {
	private final Bot chat;
	private final String keyTemplate;
	private final List<BTNode> selectorNodes = new ArrayList<>();

	BotSwitch(final Bot chat, final String keyTemplate) {
		Assertion.check()
				.isNotNull(chat)
				.isNotBlank(keyTemplate);
		//---
		this.chat = chat;
		this.keyTemplate = keyTemplate;
	}

	private BTCondition buildGuard(final String compare) {
		return chat.eq(keyTemplate, compare);
	}

	public BotSwitch when(final String compare, final BTNode... nodes) {
		return when(compare, List.of(nodes));
	}

	public BotSwitch when(final String compare, final List<BTNode> nodes) {
		selectorNodes.add(
				BTNode.sequence(nodes)
						.guardedBy(buildGuard(compare)));
		return this;
	}

	public BotSwitch whenOther(final BTNode... nodes) {
		return whenOther(List.of(nodes));
	}

	public BotSwitch whenOther(final List<BTNode> nodes) {
		selectorNodes.add(
				BTNode.sequence(nodes));
		return this;
	}

	@Override
	public BTNode build() {
		return BTNode.selector(selectorNodes);
	}
}
