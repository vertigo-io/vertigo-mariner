package io.vertigo.ai.dqm;

import static io.vertigo.ai.bt.BTNode.sequence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import io.vertigo.ai.bt.BTNode;
import io.vertigo.ai.bt.BTRoot;
import io.vertigo.ai.bt.BTStatus;

public class DQMAssistantTest {

	@Test
	public void test() {
		final DQMAssistantEngine state = new DQMAssistantEngine();
		final BTNode lineHandleNode = handleLine(state);
		final List<ContactData> contacts = createFakeContacts();

		contacts.forEach(contact -> new BTRoot(sequence(
				injectData(contact, state),
				lineHandleNode,
				clearData(state))).run());

	}

	private static BTNode injectData(final Map<String, String> data, final DQMAssistantEngine state) {
		return () -> {
			data.forEach((key, value) -> state.bb.put("source/" + key, value));
			return BTStatus.Succeeded;
		};
	}

	private static BTNode clearData(final DQMAssistantEngine state) {
		return sequence(
				state.clear("source/*"),
				state.clear("target/*"));
	}

	private static BTNode handleLine(final DQMAssistantEngine state) {
		return sequence(
				printState(state),
				sequence(
						properField(state, "lastname"),
						properField(state, "firstname"),
						properField(state, "birthdate"),
						properField(state, "salary")),
				printState(state));
	}

	private static BTNode properField(final DQMAssistantEngine state, final String fieldName) {
		return sequence(
				state.fulfill("source/" + fieldName, fieldName),
				state.copy("source/" + fieldName, "target/" + fieldName),
				state.normalize("target/" + fieldName),
				state.probeType("target/" + fieldName),
				state.confirm("target/" + fieldName));
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
