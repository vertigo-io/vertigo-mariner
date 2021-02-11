package io.vertigo.mariner.commons;

import io.vertigo.datamodel.smarttype.annotations.SmartTypeDefinition;
import io.vertigo.datamodel.smarttype.annotations.SmartTypeProperty;

public enum SmartTypes {

	//	@SmartTypeDefinition(Double.class)
	//	@FormatterDefault
	//	@SmartTypeProperty(property = "storeType", value = "NUMERIC")
	//	Montant,

	@SmartTypeDefinition(Long.class)
	@SmartTypeProperty(property = "storeType", value = "NUMERIC")
	Id,

	@SmartTypeDefinition(String.class)
	//	@FormatterDefault
	//	@Constraint(clazz = ConstraintStringLength.class, arg = "10", msg = "")
	//	@SmartTypeProperty(property = "storeType", value = "VARCHAR(10)")
	Code,

	@SmartTypeDefinition(Integer.class)
	Size,

	@SmartTypeDefinition(Integer.class)
	Coordinate
	//	@SmartTypeDefinition(String.class)
	//	@FormatterDefault
	//	@Constraint(clazz = ConstraintStringLength.class, arg = "100", msg = "")
	//	@SmartTypeProperty(property = "storeType", value = "VARCHAR(100)")
	//	Label,

}
