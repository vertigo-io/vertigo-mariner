package io.vertigo.mariner;

import java.util.List;

import io.vertigo.core.node.AutoCloseableNode;
import io.vertigo.core.node.config.BootConfig;
import io.vertigo.core.node.config.NodeConfig;
import io.vertigo.core.plugins.resource.classpath.ClassPathResourceResolverPlugin;
import io.vertigo.studio.StudioFeatures;
import io.vertigo.studio.generator.GeneratorConfig;
import io.vertigo.studio.generator.GeneratorManager;
import io.vertigo.studio.notebook.Notebook;
import io.vertigo.studio.source.Source;
import io.vertigo.studio.source.SourceManager;

public class Studio {

	private static NodeConfig buildNodeConfig() {
		return NodeConfig.builder()
				.withBoot(BootConfig.builder()
						.withLocales("fr_FR")
						.addPlugin(ClassPathResourceResolverPlugin.class)
						.build())
				// ---StudioFeature
				.addModule(new StudioFeatures()
						.withSource()
						.withVertigoSource()
						.withVertigoMda()
						.withVertigoMda()
						.build())
				.build();

	}

	public static void main(final String[] args) {
		try (final AutoCloseableNode studioApp = new AutoCloseableNode(buildNodeConfig())) {
			final SourceManager sourceManager = studioApp.getComponentSpace().resolve(SourceManager.class);
			final GeneratorManager generatorManager = studioApp.getComponentSpace().resolve(GeneratorManager.class);
			//-----

			final GeneratorConfig generatorConfig = GeneratorConfig.builder("io.vertigo.mariner")
					.addProperty("vertigo.domain.java", "true")
					.addProperty("vertigo.domain.java.generateDtResources", "false")
					.addProperty("vertigo.domain.sql", "true")
					.addProperty("vertigo.domain.sql.targetSubDir", "javagen/sqlgen")
					.addProperty("vertigo.domain.sql.baseCible", "H2")
					.addProperty("vertigo.domain.sql.generateDrop", "true")
					.addProperty("vertigo.domain.sql.generateMasterData", "true")
					.addProperty("vertigo.task", "true")
					.addProperty("vertigo.search", "true")
					.build();

			generatorManager.clean(generatorConfig);
			final List<Source> resources = List.of(
					Source.of("kpr", "io/vertigo/mariner/gen.kpr"));
			//					Source.of("classes", SmartTypes.class.getCanonicalName().toString()));
			final Notebook notebook = sourceManager.read(resources);
			generatorManager.generate(notebook, generatorConfig)
					.displayResultMessage(System.out);
		}
	}

}
