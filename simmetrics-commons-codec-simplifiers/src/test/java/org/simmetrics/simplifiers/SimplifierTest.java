/*-
 * #%L
 * Simmetrics Apache Commons Codec Simplifiers
 * %%
 * Copyright (C) 2014 - 2020 Simmetrics Authors
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package org.simmetrics.simplifiers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.simmetrics.matchers.ImplementsToString.implementsToString;

abstract class SimplifierTest {
	protected static final class T {
		final String expected;
		final String string;

		public T(String string, String expected) {
			this.string = string;
			this.expected = expected;
		}

		public String string() {
			return string;
		}

	}

	private static void testSimplified(String expected, String simplified) {
		assertNotNull(simplified);
		assertEquals(expected, simplified);
	}

	protected Simplifier simplifier;

	protected T[] tests;

	@Test
	void containsEmptyTest() {
		for (T t : tests) {
			if (t.string.isEmpty()) {
				return;
			}
		}

		fail("Test must contain a case with empty string");
	}

	protected abstract Simplifier getSimplifier();

	protected abstract T[] getTests();

	@BeforeEach
	final void setUp() throws Exception {
		simplifier = getSimplifier();
		tests = getTests();
	}

	@Test
	final void shouldImplementToString() {
		assertThat(simplifier, implementsToString());
	}

	@Test
	final void simplfy() {
		for (T t : tests) {
			testSimplified(t.expected, simplifier.simplify(t.string));
		}
	}

	@Test
	final void simplfyNullPointerException() {
		Assertions.assertThrows(NullPointerException.class, () -> simplifier.simplify(null));
	}

	public final void generateSimplified() {
		System.out.println(simplifier);
		for (T t : tests) {
			System.out.println(format("new T(\"%s\", \"%s\"),", t.string,
					simplifier.simplify(t.string)));
		}
	}
}
