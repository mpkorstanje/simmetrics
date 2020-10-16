/*-
 * #%L
 * Simmetrics Core
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
package org.simmetrics.tokenizers;

import org.junit.Test;
import org.simmetrics.tokenizers.Tokenizers.Transform.FilterTransform;

import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.simmetrics.tokenizers.Tokenizers.Filter;
import static org.simmetrics.tokenizers.Tokenizers.Transform;

public class TransformTest extends TokenizerTest {

	private final Tokenizer whitespace = Tokenizers.whitespace();
	private final Function<String, String> makeA = s -> "A";
	private final Function<String, String> replaceAWithB = s -> s.replaceAll("A", "B");
	private final Predicate<String> alwaysTrue = s -> true;

	private static java.util.function.Function<String, String> toUpperCase() {
		return String::toUpperCase;
	}

	@Override
	protected T[] getTests() {
		return new T[]{
				new T("the mouse and cat or dog",
						"THE", "MOUSE", "AND", "CAT", "OR", "DOG"),
				new T("")
		};
	}

	@Override
	protected Tokenizer getTokenizer() {
		return new Transform(whitespace, toUpperCase());
	}

	@Test
	public void shouldCreateCombinedForFilter() {
		Filter filter = new Filter(whitespace, alwaysTrue);
		Tokenizer tokenizer = Transform.createCombined(filter, makeA);

		assertEquals(FilterTransform.class, tokenizer.getClass());

		FilterTransform filterTransform = (FilterTransform) tokenizer;

		assertSame(filter, filterTransform.getTokenizer());
		assertSame(makeA, filterTransform.getFunction());

	}

	@Test
	public void shouldCreateCombinedForFilterTransform() {
		Filter filter = new Filter(whitespace, alwaysTrue);
		FilterTransform t = new FilterTransform(filter, makeA);
		Tokenizer tokenizer = Transform.createCombined(t, replaceAWithB);

		assertEquals(FilterTransform.class, tokenizer.getClass());

		FilterTransform filterTransform = (FilterTransform) tokenizer;

		assertSame(filter, filterTransform.getTokenizer());
		assertEquals("B", filterTransform.getFunction().apply("0"));
	}

	@Test
	public void shouldCreateCombinedForTransform() {
		Transform t = new Transform(whitespace, makeA);
		Tokenizer tokenizer = Transform.createCombined(t, replaceAWithB);

		assertEquals(Transform.class, tokenizer.getClass());

		Transform transform = (Transform) tokenizer;

		assertSame(whitespace, transform.getTokenizer());
		assertEquals("B", transform.getFunction().apply("0"));
	}

}
