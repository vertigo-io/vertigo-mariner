package io.vertigo.mariner.equipment;

import io.vertigo.core.lang.Cardinality;
import io.vertigo.datamodel.structure.model.Entity;
import io.vertigo.datamodel.structure.model.UID;
import io.vertigo.datamodel.structure.stereotype.Field;
import io.vertigo.datamodel.structure.util.DtObjectUtil;

public final class Equipment implements Entity {
	/** SerialVersionUID. */
	private static final long serialVersionUID = 1L;

	@Field(smartType = "STyId", type = "ID", cardinality = Cardinality.ONE, label = "id")
	private Long id;

	@Field(smartType = "STyCode", label = "rows")
	private String code;

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public final String getCode() {
		return code;
	}

	public final void setCode(final String code) {
		this.code = code;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}

	@Override
	public UID<Equipment> getUID() {
		return UID.of(Equipment.class, id);
	}
}
