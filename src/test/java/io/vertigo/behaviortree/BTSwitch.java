package io.vertigo.behaviortree;

import java.util.ArrayList;
import java.util.List;

import BehaviorTree.BTCondition;
import BehaviorTree.BTNode;
import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.Builder;

public final class BTSwitch implements Builder<BTNode> {
	private final BTChat chat;
	private final String keyTemplate;
	private final List<BTNode> nodes = new ArrayList<>();

	BTSwitch(final BTChat chat, final String keyTemplate) {
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

	public BTSwitch when(final String compare, final BTNode node) {
		nodes.add(node.guardedBy(buildGuard(compare)));
		return this;
	}

	@Override
	public BTNode build() {
		return BTNode.selector(nodes.toArray(new BTNode[nodes.size()]));
	}
}
