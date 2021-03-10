package io.vertigo.ai.dqm;

import static io.vertigo.ai.bt.BTNodes.loop;
import static io.vertigo.ai.bt.BTNodes.selector;
import static io.vertigo.ai.bt.BTNodes.sequence;
import static io.vertigo.ai.bt.BTNodes.stop;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import io.vertigo.ai.bt.BTNode;
import io.vertigo.ai.bt.BTRoot;
import io.vertigo.ai.bt.BTStatus;
import io.vertigo.core.node.AutoCloseableNode;
import io.vertigo.core.node.config.ModuleConfig;
import io.vertigo.core.node.config.NodeConfig;
import io.vertigo.core.util.InjectorUtil;

public class DQMAssistantTest {

	@Inject
	private ContactServices contactServices;

	@Test
	public void test() {

		final NodeConfig nodeConfig = NodeConfig.builder()
				.addModule(
						ModuleConfig.builder("myModule")
								.addComponent(ContactServices.class)
								.build())
				.build();

		try (final AutoCloseableNode node = new AutoCloseableNode(nodeConfig)) {
			InjectorUtil.injectMembers(this);

			final DQMAssistantEngine dqmEngine = new DQMAssistantEngine();

			new BTRoot(
					sequence(
							dqmEngine.invoke(contactServices::init),
							loop(
									selector(
											dqmEngine.invoke(contactServices::shouldContinue),
											stop()),
									dqmEngine.invoke(contactServices::doBefore),
									sequence(
											printState(dqmEngine),
											//---
											properField(dqmEngine, "lastname"),
											properField(dqmEngine, "firstname"),
											properField(dqmEngine, "birthdate"),
											properField(dqmEngine, "salary"),
											//---
											printState(dqmEngine)),
									dqmEngine.invoke(contactServices::doAfter),
									dqmEngine.clear("target/*")))).run();
		}

	}

	private static BTNode properField(final DQMAssistantEngine dqmEngine, final String fieldName) {
		return sequence(
				dqmEngine.fulfill("source/" + fieldName, fieldName),
				dqmEngine.copy("source/" + fieldName, "target/" + fieldName),
				dqmEngine.normalize("target/" + fieldName),
				dqmEngine.probeType("target/" + fieldName),
				dqmEngine.confirm("target/" + fieldName, "Press Enter to confirm value {{" + "target/" + fieldName + "}} or type the correct value"));
	}

	private static BTNode printState(final DQMAssistantEngine state) {
		return () -> {
			System.out.println(state.toString());
			return BTStatus.Succeeded;
		};
	}

}
