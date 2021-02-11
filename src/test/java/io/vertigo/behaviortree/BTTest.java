package io.vertigo.behaviortree;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import BehaviorTree.BTNode;
import BehaviorTree.BTRoot;
import BehaviorTree.BTSelector;
import BehaviorTree.BTSequence;
import BehaviorTree.BTStatus;

public class BTTest {
	public final class State {
		private final Scanner sc = new Scanner(System.in);
		public final Map<String, String> values = new HashMap();

		private BTNode isFulFilled(final String key) {
			return () -> values.get(key) == null ? BTStatus.Failed : BTStatus.Succeeded;
		}

		private BTNode query(final String key, final String answer) {
			return () -> {
				System.out.println(answer);
				//---
				final String input = sc.nextLine();
				values.put(key, input);
				//---
				return BTStatus.Running;
			};
		}

		public BTNode fulfill(final String key, final String answer) {
			return new BTSelector(
					isFulFilled(key),
					query(key, answer));
		}
	}

	@Test
	public void test() {
		final State state = new State();
		//state.values.put("name", "john");

		final BTRoot root = createBTRoot(state);
		var status = root.eval();
		while (status == BTStatus.Running) {
			status = root.eval();
		}
	}

	private static BTRoot createBTRoot(final State state) {
		final BTNode ticketGoal = new BTSequence(
				state.fulfill("name", "Quel est votre nom ?"),
				state.fulfill("from", "Quelle est votre ville de départ ?"),
				state.fulfill("to", "Quelle est votre ville d'arrivée ?"));
		return new BTRoot(ticketGoal);
	}
}
