package io.vertigo.ai.dqm;

import java.util.HashMap;

public class ContactData extends HashMap<String, String> {
	public ContactData(final String lastname, final String firstname, final String birthDate, final String salary, final String married) {
		put("lastname", lastname);
		put("firstname", firstname);
		put("birthdate", birthDate);
		put("salary", salary);
		put("married", married);
	}
}
