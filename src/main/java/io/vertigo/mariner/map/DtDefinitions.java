package io.vertigo.mariner.map;

import java.util.Iterator;

import io.vertigo.core.util.ListBuilder;

public final class DtDefinitions implements Iterable<Class<?>> {

	@Override
	public Iterator<Class<?>> iterator() {
		return new ListBuilder<Class<?>>()
				.add(Map.class)
				.build()
				.iterator();
	}
}
