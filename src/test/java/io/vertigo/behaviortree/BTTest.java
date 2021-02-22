package io.vertigo.behaviortree;

import java.util.function.Predicate;

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
		while (status != BTStatus.Succeeded) {
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
								rate(state),
								state.clearAll(),
								BTNode.running())));
	}

	private static BTNode dispatch(final State state) {
		return BTNode.selector(
				BTNode.sequence(
						state.equals(INTENTION, "W"),
						//						state.clear("w/*"),
						weather(state)),
				BTNode.sequence(
						state.equals(INTENTION, "T"),
						//state.clear("t/*"),
						ticket(state)));
	}

	private static BTNode weather(final State state) {
		return BTNode.sequence(
				state.fulfill("w/city", "Please choose a city"),
				state.display("w/display", "It's sunny in {{city}}"));
	}

	private static BTNode rate(final State state) {
		return BTNode.sequence(
				//state.clear("rate/*"),
				state.fulfill("rate/rating", "Please rate the response [0, 1, 2, 3, 4, 5]", "0", "1", "2", "3", "4", "5"),
				state.display("rate/display", "You have rated {{rate/rating}}"));
	}

	public static Predicate<String> isInteger() {
		return s -> {
			try {
				Integer.parseInt(s);
				return true;
			} catch (final Exception e) {
				return false;
			}
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
				//				state.clear("t/*"),
				state.display("t/begin", "You have chosen to book a ticket, I have some questions..."),
				state.fulfill("t/name", "What is your name ?"),
				state.fulfill("t/return", "Do you want a return ticket  ? Y/N", "Y", "N"),
				state.fulfill("t/from", "from ?"),
				state.fulfill("t/to", "to ?"),
				state.fulfill("t/count", "How many", BTTest.isInteger()),
				state.display("t/end", "Thank you, your ticket will be sent ..."));
	}
}
