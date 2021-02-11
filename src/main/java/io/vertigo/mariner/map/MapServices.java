package io.vertigo.mariner.map;

import javax.inject.Inject;

import io.vertigo.core.node.component.Component;
import io.vertigo.core.param.ParamValue;

public class MapServices implements Component {
	private final Map map;

	@Inject
	public MapServices(
			@ParamValue("rows") Integer rows,
			@ParamValue("cols") Integer cols) {
		map = new Map();
		map.setCols(cols);
		map.setRows(rows);
	}

	public Map getMap() {
		return map;
	}
}
