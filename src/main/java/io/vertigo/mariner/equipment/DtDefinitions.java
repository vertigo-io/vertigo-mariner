package io.vertigo.mariner.equipment;

import java.util.Iterator;

import io.vertigo.core.util.ListBuilder;

public final class DtDefinitions implements Iterable<Class<?>> {

	@Override
	public Iterator<Class<?>> iterator() {
		return new ListBuilder<Class<?>>()
				.add(Equipment.class)
				.build()
				.iterator();
	}
}
