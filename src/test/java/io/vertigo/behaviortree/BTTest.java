package io.vertigo.behaviortree;

import org.junit.jupiter.api.Test;

import BehaviorTree.BTNode;
import BehaviorTree.BTRoot;
import BehaviorTree.BTStatus;

public class BTTest {

	@Test
	public void test() {
		final State state = new State();
		System.out.println("before state >>" + state.toString());
		//state.values.put("name", "john");

		final BTRoot root = createBTRoot(state);
		var status = root.eval();
		while (status == BTStatus.Running) {
			status = root.eval();
		}

		System.out.println("---after state---");
		System.out.println("-----------------");
		System.out.println(state.toString());
		System.out.println("-----------------");
	}

	private static BTRoot createBTRoot(final State state) {
		final BTNode ticketGoal = BTNode.sequence(
				state.fulfill("name", "Quel est votre nom ?"),
				state.fulfill("from", "Quelle est votre ville de départ ?"),
				state.fulfill("to", "Quelle est votre ville d'arrivée ?"));
		return new BTRoot(ticketGoal);
	}
}
