package io.vertigo.mariner.map;

import io.vertigo.core.lang.Cardinality;
import io.vertigo.datamodel.structure.model.Entity;
import io.vertigo.datamodel.structure.model.UID;
import io.vertigo.datamodel.structure.stereotype.Field;
import io.vertigo.datamodel.structure.util.DtObjectUtil;

public final class Map implements Entity {
	/** SerialVersionUID. */
	private static final long serialVersionUID = 1L;

	@Field(smartType = "STyId", type = "ID", cardinality = Cardinality.ONE, label = "id")
	private Long id;

	@Field(smartType = "STySize", label = "rows")
	private Integer rows;

	@Field(smartType = "STySize", label = "cols")
	private Integer cols;

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public final Integer getRows() {
		return rows;
	}

	public final Integer getCols() {
		return cols;
	}

	public final void setRows(final int rows) {
		this.rows = rows;
	}

	public final void setCols(final int cols) {
		this.cols = cols;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}

	@Override
	public UID<Map> getUID() {
		return UID.of(Map.class, id);
	}
}
