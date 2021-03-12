package io.vertigo.ai.dqm;

import java.util.List;

import io.vertigo.ai.bb.BBBlackBoard;
import io.vertigo.ai.bt.BTStatus;
import io.vertigo.core.node.component.Component;

public class ContactServices implements Component {

	final List<ContactData> fakeContacts = List.of(
			new ContactData(null, "Jean", "19/05/1980", "10 000", "true"),
			new ContactData("Martin", "Romain", "19/05/1977", "200", "false"),
			new ContactData("Dubois", "Marie", "20/06/1981", "13000", "true"),
			new ContactData("Petit", "Philippe", "18/04/1979", "0", "false"));

	public final BTStatus init(final BBBlackBoard bb) {
		bb.incr("linenumber");
		return BTStatus.Succeeded;
	}

	public final boolean shouldContinue(final BBBlackBoard bb) {
		return Integer.parseInt(bb.get("linenumber")) <= fakeContacts.size();
	}

	public BTStatus doBefore(final BBBlackBoard bb) {
		fakeContacts.get(Integer.parseInt(bb.get("linenumber")) - 1)
				.forEach((key, value) -> bb.put("source/" + key, value));
		return BTStatus.Succeeded;
	}

	public BTStatus doAfter(final BBBlackBoard bb) {
		bb.remove("source/*");
		bb.incr("linenumber");
		return BTStatus.Succeeded;
	}

}
