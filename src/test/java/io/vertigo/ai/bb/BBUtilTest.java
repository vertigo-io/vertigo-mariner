package io.vertigo.ai.bb;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BBUtilTest {
	@Test
	public void testFormatter0() {
		Assertions.assertEquals("hello world", BBUtils.format("hello world", Map.of()));
	}

	@Test
	public void testFormatter1() {
		Assertions.assertEquals("hello world", BBUtils.format("hello world", Map.of()));
		final var map = Map.of(
				"name", "joe",
				"lastName", "diMagio");
		Assertions.assertEquals("joe", BBUtils.format("{{name}}", map));
		Assertions.assertEquals("hello joe", BBUtils.format("hello {{name}}", map));
		Assertions.assertEquals("hello joe...", BBUtils.format("hello {{name}}...", map));
		Assertions.assertEquals("hello joe diMagio", BBUtils.format("hello {{name}} {{lastName}}", map));
		Assertions.assertThrows(IllegalStateException.class,
				() -> BBUtils.format("hello {{name}", map));
		Assertions.assertThrows(IllegalStateException.class,
				() -> BBUtils.format("hello {{name", map));
		Assertions.assertThrows(IllegalStateException.class,
				() -> BBUtils.format("hello name}}", map));
	}

	@Test
	public void testFormatter2() {
		final var map = Map.of(
				"u/1/name", "alan",
				"u/2/name", "ada",
				"u/idx", "2");
		Assertions.assertEquals("hello ada", BBUtils.format("hello {{u/{{u/idx}}/name}}", map));
	}
}
