package io.vertigo.mariner.map;

import io.vertigo.core.lang.Cardinality;
import io.vertigo.datamodel.structure.model.Entity;
import io.vertigo.datamodel.structure.model.UID;
import io.vertigo.datamodel.structure.stereotype.Field;
import io.vertigo.datamodel.structure.util.DtObjectUtil;

public final class Bloc implements Entity {
	/** SerialVersionUID. */
	private static final long serialVersionUID = 1L;

	@Field(smartType = "STyId", type = "ID", cardinality = Cardinality.ONE, label = "id")
	private Long id;

	@Field(smartType = "STyCoordinate", label = "x")
	private Integer x;

	@Field(smartType = "STyCoordinate", label = "y")
	private Integer y;

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public final Integer getX() {
		return x;
	}

	public final Integer getY() {
		return y;
	}

	public final void setX(final int x) {
		this.x = x;
	}

	public final void setCols(final int y) {
		this.y = y;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}

	@Override
	public UID<Bloc> getUID() {
		return UID.of(Bloc.class, id);
	}
}
