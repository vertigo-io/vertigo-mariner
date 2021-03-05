package io.vertigo.ai.bb;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BBBlackBoardTest {

	@Test
	public void testExists() {
		final var bb = new BBBlackBoard();
		Assertions.assertFalse(bb.exists("samplekey"));
		bb.inc("samplekey");
		Assertions.assertTrue(bb.exists("samplekey"));
		//--only some characters are accepted ; blanks are not permitted
		Assertions.assertThrows(Exception.class, () -> bb.exists("sample key"));
	}

	@Test
	public void testKeys() {
		var bb = new BBBlackBoard();
		Assertions.assertEquals(0, bb.keys("test").size());
		Assertions.assertEquals(0, bb.keys("test/*").size());
		Assertions.assertEquals(0, bb.keys("*").size());
		//---
		bb = new BBBlackBoard();
		bb.inc("test");
		Assertions.assertEquals(1, bb.keys("test").size());
		Assertions.assertEquals(0, bb.keys("test/*").size());
		Assertions.assertEquals(1, bb.keys("*").size());
		//---
		bb = new BBBlackBoard();
		bb.inc("test");
		bb.inc("test/1");
		bb.inc("test/2");
		Assertions.assertEquals(1, bb.keys("test").size());
		Assertions.assertEquals(2, bb.keys("test/*").size());
		Assertions.assertEquals(3, bb.keys("*").size());
		//--check the key pattern
		final var bb2 = new BBBlackBoard();
		Assertions.assertThrows(Exception.class,
				() -> bb2.keys(" sample"));
		Assertions.assertThrows(Exception.class,
				() -> bb2.keys("sample**"));
		Assertions.assertThrows(Exception.class,
				() -> bb2.keys("sample key"));
		Assertions.assertThrows(Exception.class,
				() -> bb2.keys("/samplekey").isEmpty());
		Assertions.assertThrows(Exception.class,
				() -> bb2.keys("samplekey/*/test").isEmpty());
		//--- keys and keys("*")
		bb = new BBBlackBoard();
		bb.inc("test");
		bb.inc("test/1");
		bb.inc("test/3");
		bb.inc("test/4");
		Assertions.assertEquals(4, bb.keys("*").size());
		Assertions.assertEquals(4, bb.keys().size());
	}

	@Test
	public void testGetPut() {
		final var bb = new BBBlackBoard();
		Assertions.assertEquals(null, bb.get("sample/key"));
		bb.put("sample/key", "test");
		Assertions.assertEquals("test", bb.get("sample/key"));
		bb.put("sample/key", "test2");
		Assertions.assertEquals("test2", bb.get("sample/key"));
	}

	@Test
	public void testFormat() {
		final var bb = new BBBlackBoard();
		Assertions.assertEquals("", bb.format(""));
		Assertions.assertEquals("hello", bb.format("hello"));
		bb.put("sample/key", "test");
		Assertions.assertEquals("hello test", bb.format("hello {{sample/key}}"));
	}

	@Test
	public void testInc() {
		final var bb = new BBBlackBoard();
		Assertions.assertEquals(null, bb.get("key"));
		bb.inc("key");
		Assertions.assertEquals("1", bb.get("key"));
		bb.inc("key");
		Assertions.assertEquals("2", bb.get("key"));
		bb.inc("key", 10);
		Assertions.assertEquals("12", bb.get("key"));
	}

	@Test
	public void testDec() {
		final var bb = new BBBlackBoard();
		Assertions.assertEquals(null, bb.get("key"));
		bb.inc("key", 10);
		Assertions.assertEquals("10", bb.get("key"));
		bb.dec("key");
		Assertions.assertEquals("9", bb.get("key"));
		bb.dec("key");
		Assertions.assertEquals("8", bb.get("key"));
		bb.dec("key", 5);
		Assertions.assertEquals("3", bb.get("key"));
	}

	@Test
	public void testClear() {
		final var bb = new BBBlackBoard();
		Assertions.assertEquals(0, bb.keys().size());
		bb.inc("sample/1");
		bb.inc("sample/2");
		bb.inc("sample/3");
		bb.inc("sample/4");
		Assertions.assertEquals(4, bb.keys().size());
		bb.clear("sample/1");
		Assertions.assertEquals(3, bb.keys().size());
		bb.clear("*");
		Assertions.assertEquals(0, bb.keys().size());
		bb.inc("sample/1");
		bb.inc("sample/2");
		bb.inc("sample/3");
		bb.inc("sample/4");
		bb.clear();
		Assertions.assertEquals(0, bb.keys().size());
	}

}
