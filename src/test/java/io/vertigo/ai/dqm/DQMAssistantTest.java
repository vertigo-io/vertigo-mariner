package io.vertigo.ai.dqm;

import static io.vertigo.ai.bt.BTNode.sequence;

import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.vertigo.ai.bt.BTNode;
import io.vertigo.ai.bt.BTRoot;
import io.vertigo.ai.bt.BTStatus;

public class DQMAssistantTest {

	@Test
	public void test() {
		final DQMAssistantEngine state = new DQMAssistantEngine();
		final List<ContactData> contacts = createFakeContacts();

		new BTRoot(
				sequence(
						state.inc("linenumber"), // setup
						BTNode.loop(
								BTNode.selector(
										state.gt("linenumber", "" + contacts.size()),
										BTNode.stop()),
								state.injectData(() -> contacts.get(Integer.parseInt(state.read("linenumber")) - 1)),
								sequence(
										printState(state),
										//---
										properField(state, "lastname"),
										properField(state, "firstname"),
										properField(state, "birthdate"),
										properField(state, "salary"),
										//---
										printState(state)),
								state.clearData(),
								state.inc("linenumber")))).run();

	}

	private static BTNode properField(final DQMAssistantEngine state, final String fieldName) {
		return sequence(
				state.fulfill("source/" + fieldName, fieldName),
				state.copy("source/" + fieldName, "target/" + fieldName),
				state.normalize("target/" + fieldName),
				state.probeType("target/" + fieldName),
				state.confirm("target/" + fieldName, "Press Enter to confirm value {{" + "target/" + fieldName + "}} or type the correct value"));
	}

	private static BTNode printState(final DQMAssistantEngine state) {
		return () -> {
			System.out.println(state.toString());
			return BTStatus.Succeeded;
		};
	}

	public static class ContactData extends HashMap<String, String> {
		public ContactData(final String lastname, final String firstname, final String birthDate, final String salary, final String married) {
			put("lastname", lastname);
			put("firstname", firstname);
			put("birthdate", birthDate);
			put("salary", salary);
			put("married", married);
		}
	}

	private static final List<ContactData> createFakeContacts() {
		return List.of(
				new ContactData(null, "Jean", "19/05/1980", "10 000", "true"),
				new ContactData("Martin", "Romain", "19/05/1977", "200", "false"),
				new ContactData("Dubois", "Marie", "20/06/1981", "13000", "true"),
				new ContactData("Petit", "Philippe", "18/04/1979", "0", "false"));
	}

}
