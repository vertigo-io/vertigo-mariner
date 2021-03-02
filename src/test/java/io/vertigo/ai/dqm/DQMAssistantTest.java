package io.vertigo.ai.dqm;

import static io.vertigo.ai.bt.BTNode.sequence;

import org.junit.jupiter.api.Test;

import io.vertigo.ai.bt.BTNode;
import io.vertigo.ai.bt.BTRoot;
import io.vertigo.ai.bt.BTStatus;
import io.vertigo.ai.dqm.DQMAssistantEngine;

public class DQMAssistantTest {
	@Test
	public void test() {
		final DQMAssistantEngine state = new DQMAssistantEngine();
		state.bb.put("source/name", "john");
		new BTRoot(goal(state)).run();

	}

	private static BTNode goal(final DQMAssistantEngine state) {
		return sequence(
				printState(state),
				properField(state, "name"),
				printState(state),
				properField(state, "lastname"),
				printState(state));
	}

	private static BTNode properField(final DQMAssistantEngine state, final String fieldName) {
		return sequence(
				state.fulfill("source/" + fieldName, fieldName),
				state.copy("source/" + fieldName, "target/" + fieldName),
				state.normalize("target/" + fieldName),
				state.confirm("target/" + fieldName));
	}

	private static BTNode printState(final DQMAssistantEngine state) {
		return () -> {
			System.out.println(state.toString());
			return BTStatus.Succeeded;
		};
	}

}
