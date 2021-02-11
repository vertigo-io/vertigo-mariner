package io.vertigo.mariner;

import io.vertigo.commons.CommonsFeatures;
import io.vertigo.connectors.javalin.JavalinFeatures;
import io.vertigo.core.node.config.BootConfig;
import io.vertigo.core.node.config.DefinitionProviderConfig;
import io.vertigo.core.node.config.ModuleConfig;
import io.vertigo.core.node.config.NodeConfig;
import io.vertigo.core.param.Param;
import io.vertigo.core.plugins.resource.classpath.ClassPathResourceResolverPlugin;
import io.vertigo.datamodel.DataModelFeatures;
import io.vertigo.datamodel.impl.smarttype.ModelDefinitionProvider;
import io.vertigo.mariner.commons.SmartTypes;
import io.vertigo.mariner.equipment.EquipmentWebServices;
import io.vertigo.mariner.map.MapServices;
import io.vertigo.mariner.map.MapWebServices;
import io.vertigo.vega.VegaFeatures;

public final class Config {
	public static final int WS_PORT = 8088;

	static NodeConfig buildNodeConfig() {
		return NodeConfig.builder()
				.withBoot(BootConfig.builder()
						.withLocales("fr_FR")
						.addPlugin(ClassPathResourceResolverPlugin.class)
						.build())
				.addModule(new CommonsFeatures().build())
				.addModule(new DataModelFeatures().build())
				.addModule(new JavalinFeatures()
						.withEmbeddedServer(Param.of("port", Integer.toString(WS_PORT))).build())
				.addModule(new VegaFeatures()
						.withWebServices()
						//				.withWebServicesTokens(Param.of("tokens", "tokens"))
						//				.withWebServicesSecurity()
						//.withWebServicesRateLimiting()
						.withWebServicesSwagger()
						.withWebServicesCatalog()
						.withJavalinWebServerPlugin()
						.build())
				//----------------------
				//-- Business Modules --
				//----------------------
				.addModule(ModuleConfig.builder("Commons")
						.addDefinitionProvider(DefinitionProviderConfig.builder(ModelDefinitionProvider.class)
								.addDefinitionResource("smarttypes", SmartTypes.class.getName())
								.build())
						.build())
				.addModule(ModuleConfig.builder("Map")
						.addComponent(MapWebServices.class)
						.addComponent(MapServices.class,
								Param.of("rows", "3"),
								Param.of("cols", "3"))
						.addDefinitionProvider(DefinitionProviderConfig.builder(ModelDefinitionProvider.class)
								.addDefinitionResource("dtobjects", io.vertigo.mariner.map.DtDefinitions.class.getName())
								.build())
						//.addDefinitionProvider(TaskDefinitionProvider.class)
						.build())
				.addModule(ModuleConfig.builder("equipments")
						.addComponent(EquipmentWebServices.class)
						//.addComponent(TestComponentTaskAnnotation.class)
						.addDefinitionProvider(DefinitionProviderConfig.builder(ModelDefinitionProvider.class)
								.addDefinitionResource("dtobjects", io.vertigo.mariner.equipment.DtDefinitions.class.getName())
								.build())
						//.addDefinitionProvider(TaskDefinitionProvider.class)
						.build())
				.build();
	}
}
