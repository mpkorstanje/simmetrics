/*-
 * #%L
 * Simmetrics Core
 * %%
 * Copyright (C) 2014 - 2021 Simmetrics Authors
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

package com.github.mpkorstanje.simmetrics.tokenizers;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static com.github.mpkorstanje.simmetrics.matchers.ImplementsToString.implementsToString;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class TokenizerTest {

	protected static class T {
		private final String string;
		private final String[] tokens;

		public T(String string, String... tokens) {
			this.string = string;
			this.tokens = tokens;
		}

		public String string() {
			return string;
		}

		public List<String> tokensAsList() {
			return asList(tokens);
		}

		public Set<String> tokensAsSet() {
			return new HashSet<>(tokensAsList());
		}

		public Multiset<String> tokensAsMultiset() {
			return HashMultiset.create(tokensAsList());
		}

	}

	private static void testTokens(String string, Collection<String> expected,
			Collection<String> actual) {
		assertEquals(expected, actual, string + " did not tokenize correctly");
		assertFalse(actual.contains(null), actual + " contained null");
	}

	protected T[] tests;

	protected Tokenizer tokenizer;

	protected abstract T[] getTests();

	protected boolean supportsTokenizeToList() {
		return true;
	}

	protected boolean supportsTokenizeToSet() {
		return true;
	}
	protected boolean supportsTokenizeToMultiset() {
		return true;
	}

	protected abstract Tokenizer getTokenizer();

	@BeforeEach
	final void setUp() throws Exception {
		tokenizer = getTokenizer();
		tests = getTests();
	}

	@Test
	final void containsEmptyTest() {
		for (T t : tests) {
			if (t.string().isEmpty()) {
				return;
			}
		}

		fail("Test must contain a case with empty string");
	}

	@Test
	final void shouldImplementToString() {
		assertThat(tokenizer, implementsToString());
	}

	@Test
	final void shouldTokenizeToList() {
		if (!supportsTokenizeToList()) {
			assertThrows(UnsupportedOperationException.class, () -> tokenizer.tokenizeToList(""));
		} else {
			for (T t : tests) {
				testTokens(t.string(), t.tokensAsList(),
						tokenizer.tokenizeToList(t.string()));
			}
		}
	}

	@Test
	final void tokenizeToListShouldThrowNullPointerException() {
		if (supportsTokenizeToList()) {
			assertThrows(NullPointerException.class, () -> tokenizer.tokenizeToList(null));
		} else {
			assertThrows(UnsupportedOperationException.class, () -> tokenizer.tokenizeToList(null));
		}
	}

	@Test
	final void shouldTokenizeToSet() {
		if (!supportsTokenizeToSet()) {
			assertThrows(UnsupportedOperationException.class, () -> tokenizer.tokenizeToSet(""));
		} else {
			for (T t : tests) {
				testTokens(t.string(), t.tokensAsSet(), tokenizer.tokenizeToSet(t.string()));
			}
		}
	}

	@Test
	final void tokenizeToSetShouldThrowNullPointerException() {
		if (supportsTokenizeToSet()) {
			assertThrows(NullPointerException.class, () -> tokenizer.tokenizeToSet(null));
		} else {
			assertThrows(UnsupportedOperationException.class, () -> tokenizer.tokenizeToSet(null));
		}
	}


	@Test
	final void shouldTokenizeToMutiset() {
		if (!supportsTokenizeToMultiset()) {
			assertThrows(UnsupportedOperationException.class, () -> tokenizer.tokenizeToMultiset(""));
		} else {
			for (T t : tests) {
				testTokens(t.string(), t.tokensAsMultiset(),
						tokenizer.tokenizeToMultiset(t.string()));
			}
		}
	}

	@Test
	final void tokenizeToMultisetShouldThrowNullPointerException() {
		if (supportsTokenizeToMultiset()) {
			assertThrows(NullPointerException.class, () -> tokenizer.tokenizeToMultiset(null));
		} else {
			assertThrows(UnsupportedOperationException.class, () -> tokenizer.tokenizeToMultiset(null));
		}
	}

}
