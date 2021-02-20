package io.vertigo.behaviortree;

import org.junit.jupiter.api.Test;

import BehaviorTree.BTNode;
import BehaviorTree.BTRoot;
import BehaviorTree.BTStatus;

public class BTTest {

	@Test
	public void test() {
		final State state = new State();
		System.out.println("-----------------");
		System.out.println("--before state---");
		System.out.print("-----------------");
		System.out.println(state.toString());
		System.out.println("-----------------");
		//state.values.put("name", "john");

		final BTRoot root = new BTRoot(goal(state));
		var status = root.eval();
		while (status == BTStatus.Running) {
			status = root.eval();
		}
		System.out.println("finished");
		System.out.println("-----------------");
		System.out.println("--after state----");
		System.out.print("-----------------");
		System.out.println(state.toString());
		System.out.println("-----------------");
	}

	private static final String INTENTION = "intention";

	private static BTNode goal(final State state) {
		return BTNode.sequence(
				state.fulfill(INTENTION, "Select [W]eather, [T]icket or e[X]it ?", "W", "T", "X"),
				BTNode.selector(
						state.equals(INTENTION, "X"),
						BTNode.sequence(
								dispatch(state),
								state.clearAll(),
								rate(state),
								BTNode.running())));
	}

	private static BTNode dispatch(final State state) {
		return BTNode.selector(
				BTNode.sequence(
						state.equals(INTENTION, "W"),
						weather(state)),
				BTNode.sequence(
						state.equals(INTENTION, "T"),
						ticket(state)));
	}

	private static BTNode weather(final State state) {
		return state.fulfill("home", "Please choose a city");
	}

	private static BTNode rate(final State state) {
		return BTNode.sequence(
				state.fulfill("rate", "Please rate the response [0, 1, 2, 3, 4, 5]", "0", "1", "2", "3", "4", "5"),
				display("You have rated " + state.get("rate")));
	}

	private static BTNode display(final String msg) {
		return () -> {
			System.out.println(msg);
			return BTStatus.Succeeded;
		};
	}

	private static BTNode ticket(final State state) {
		/**
		 * Il s'agit de créer un ticket qui contient
		 * - nom
		 * - choix aller/retour O/N controlé par une liste
		 * - ville de déaprt
		 * - ville d'arrivée
		 * Ces données sont gérées dans l'état du monde
		 * Certaines des données peuvent déjà être renseignée		 * 
		 */
		return BTNode.sequence(
				display("You have chosen to book a ticket, I have some questions..."),
				state.fulfill("name", "What is your name ?"),
				state.fulfill("return", "Do you want a return ticket  ? Y/N", "Y", "N"),
				state.fulfill("from", "from ?"),
				state.fulfill("to", "to ?"),
				display("Thank you, your ticket will be sent ..."));
	}
}
