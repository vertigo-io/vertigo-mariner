package io.vertigo.ai.bt;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BTUtilTest {
	@Test
	public void testFormatter0() {
		Assertions.assertEquals("hello world", BTUtils.format("hello world", Map.of()));
	}

	@Test
	public void testFormatter1() {
		Assertions.assertEquals("hello world", BTUtils.format("hello world", Map.of()));
		final var map = Map.of(
				"name", "joe",
				"lastName", "diMagio");
		Assertions.assertEquals("joe", BTUtils.format("{{name}}", map));
		Assertions.assertEquals("hello joe", BTUtils.format("hello {{name}}", map));
		Assertions.assertEquals("hello joe...", BTUtils.format("hello {{name}}...", map));
		Assertions.assertEquals("hello joe diMagio", BTUtils.format("hello {{name}} {{lastName}}", map));
		Assertions.assertThrows(IllegalStateException.class,
				() -> BTUtils.format("hello {{name}", map));
		Assertions.assertThrows(IllegalStateException.class,
				() -> BTUtils.format("hello {{name", map));
		Assertions.assertThrows(IllegalStateException.class,
				() -> BTUtils.format("hello name}}", map));
	}

	@Test
	public void testFormatter2() {
		final var map = Map.of(
				"u/1/name", "alan",
				"u/2/name", "ada",
				"u/idx", "2");
		Assertions.assertEquals("hello ada", BTUtils.format("hello {{u/{{u/idx}}/name}}", map));
	}
}
