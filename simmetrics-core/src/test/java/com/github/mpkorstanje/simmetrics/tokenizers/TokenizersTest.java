/*-
 * #%L
 * Simmetrics - Core
 * %%
 * Copyright (C) 2014 - 2021 Simmetrics Authors
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * #L%
 */
package com.github.mpkorstanje.simmetrics.tokenizers;

import org.junit.jupiter.api.Test;
import com.github.mpkorstanje.simmetrics.tokenizers.Tokenizers.Filter;
import com.github.mpkorstanje.simmetrics.tokenizers.Tokenizers.Filter.TransformFilter;
import com.github.mpkorstanje.simmetrics.tokenizers.Tokenizers.QGram;
import com.github.mpkorstanje.simmetrics.tokenizers.Tokenizers.QGramExtended;
import com.github.mpkorstanje.simmetrics.tokenizers.Tokenizers.Recursive;
import com.github.mpkorstanje.simmetrics.tokenizers.Tokenizers.Split;
import com.github.mpkorstanje.simmetrics.tokenizers.Tokenizers.Transform;
import com.github.mpkorstanje.simmetrics.tokenizers.Tokenizers.Transform.FilterTransform;
import com.github.mpkorstanje.simmetrics.tokenizers.Tokenizers.Whitespace;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static com.github.mpkorstanje.simmetrics.PredicateMatcher.accepts;
import static com.github.mpkorstanje.simmetrics.PredicateMatcher.rejects;
import static com.github.mpkorstanje.simmetrics.tokenizers.Tokenizers.chain;

final class TokenizersTest {

	private final String regex = "\\s+";
	private final Pattern pattern = Pattern.compile(regex);
	private final Tokenizer whitespace = Tokenizers.whitespace();
	private final Function<String, String> identity = s -> "A";
	private final Function<String, String> identity2 = s -> s.replaceAll("A", "B");

	private final Predicate<String> sometimesPassing = s -> asList("sometimes", "passing").contains(s);
	private final Predicate<String> occasionallyPassing = s -> asList("occasionally", "passing").contains(s);

	@Test
	void shouldReturnSplitForPattern() {
		Tokenizer tokenizer = Tokenizers.pattern(pattern);

		assertEquals(Split.class, tokenizer.getClass());

		Split split = (Split) tokenizer;
		assertEquals(pattern, split.getPattern());
	}

	@Test
	void shouldReturnSplitForRegex() {
		Tokenizer tokenizer = Tokenizers.pattern(regex);

		assertEquals(Split.class, tokenizer.getClass());

		Split split = (Split) tokenizer;
		assertEquals(regex, split.getPattern().toString());
	}

	@Test
	void shouldReturnQGram() {
		Tokenizer tokenizer = Tokenizers.qGram(3);

		assertEquals(QGram.class, tokenizer.getClass());

		QGram qGram = (QGram) tokenizer;
		assertEquals(3, qGram.getQ());
	}

	@Test
	void shouldReturnQGramWithFilter() {
		Tokenizer tokenizer = Tokenizers.qGramWithFilter(3);

		assertEquals(QGram.class, tokenizer.getClass());

		QGram qGram = (QGram) tokenizer;
		assertEquals(3, qGram.getQ());
		assertTrue(qGram.isFilter());
	}

	@Test
	void shouldReturnQGramWithPadding() {
		Tokenizer tokenizer = Tokenizers.qGramWithPadding(3);

		assertEquals(QGramExtended.class, tokenizer.getClass());

		QGramExtended qGram = (QGramExtended) tokenizer;
		assertEquals(3, qGram.getQ());
		assertEquals("##", qGram.getStartPadding());
		assertEquals("##", qGram.getEndPadding());
	}

	@Test
	void shouldReturnQGramWithCustomPadding() {
		Tokenizer tokenizer = Tokenizers.qGramWithPadding(3, "@");

		assertEquals(QGramExtended.class, tokenizer.getClass());

		QGramExtended qGram = (QGramExtended) tokenizer;
		assertEquals(3, qGram.getQ());
		assertEquals("@@", qGram.getStartPadding());
		assertEquals("@@", qGram.getEndPadding());
	}

	@Test
	void shouldReturnQGramWithCustomStartAndEndPadding() {
		Tokenizer tokenizer = Tokenizers.qGramWithPadding(3, "^", "$");

		assertEquals(QGramExtended.class, tokenizer.getClass());

		QGramExtended qGram = (QGramExtended) tokenizer;
		assertEquals(3, qGram.getQ());
		assertEquals("^^", qGram.getStartPadding());
		assertEquals("$$", qGram.getEndPadding());
	}

	@Test
	void shouldReturnWhitespace() {
		assertEquals(Whitespace.class,
				Tokenizers.whitespace().getClass());
	}

	@Test
	void shouldReturnTransform() {
		Tokenizer tokenizer = Tokenizers.transform(whitespace, identity);

		assertEquals(Transform.class, tokenizer.getClass());

		Transform transform = (Transform) tokenizer;
		assertSame(whitespace, transform.getTokenizer());
		assertSame(identity, transform.getFunction());
	}

	@Test
	void shouldReturnTransformForTransform() {
		Transform t = new Transform(whitespace, identity);
		Tokenizer tokenizer = Tokenizers.transform(t, identity2);

		assertEquals(Transform.class, tokenizer.getClass());

		Transform transform = (Transform) tokenizer;
		assertSame(whitespace, transform.getTokenizer());
		assertEquals("B", transform.getFunction().apply("0"));
	}

	@Test
	void shouldReturnTransformForFilter() {
		Tokenizers.Filter filter = new Tokenizers.Filter(whitespace, occasionallyPassing);
		Tokenizer tokenizer = Tokenizers.transform(filter, identity);

		assertEquals(FilterTransform.class, tokenizer.getClass());

		FilterTransform transform = (FilterTransform) tokenizer;
		assertSame(filter, transform.getTokenizer());
		assertSame(identity, transform.getFunction());
	}

	@Test
	void shouldChainSingletonList() {
		List<Tokenizer> tokenizers = singletonList(whitespace);
		Tokenizer tokenizer = chain(tokenizers);
		assertSame(whitespace, tokenizer);
	}

	@Test
	void shouldChainPlusOne() {
		Tokenizer tokenizer = chain(whitespace);
		assertSame(whitespace, tokenizer);
	}

	@Test
	void shouldChainList() {
		List<Tokenizer> tokenizers = asList(whitespace, whitespace, whitespace);
		Tokenizer tokenizer = chain(tokenizers);

		assertEquals(Recursive.class, tokenizer.getClass());
		Recursive recursive = (Recursive) tokenizer;
		assertEquals(tokenizers, recursive.getTokenizers());
	}

	@Test
	void shouldChainArrayPlusOne() {
		Tokenizer tokenizer = chain(whitespace, whitespace, whitespace);

		assertEquals(Recursive.class, tokenizer.getClass());
		Recursive recursive = (Recursive) tokenizer;
		assertEquals(asList(whitespace, whitespace, whitespace), recursive.getTokenizers());
	}

	@Test
	void shouldChainRecursiveList() {
		List<Tokenizer> tokenizers = asList(whitespace,
				chain(whitespace, whitespace), whitespace);
		Tokenizer tokenizer = chain(tokenizers);

		assertEquals(Recursive.class, tokenizer.getClass());
		Recursive recursive = (Recursive) tokenizer;
		assertEquals(asList(whitespace, whitespace, whitespace, whitespace),
				recursive.getTokenizers());
	}

	@Test
	void shouldChainRecursiveArrayPlusOne() {
		Tokenizer tokenizer = chain(whitespace,
				chain(whitespace, whitespace), whitespace);

		assertEquals(Recursive.class, tokenizer.getClass());
		Recursive recursive = (Recursive) tokenizer;
		assertEquals(asList(whitespace, whitespace, whitespace, whitespace),
				recursive.getTokenizers());
	}

	@Test
	void shouldReturnFilter() {
		Tokenizer tokenizer = Tokenizers.filter(whitespace, occasionallyPassing);

		assertEquals(Filter.class, tokenizer.getClass());

		Filter filter = (Filter) tokenizer;
		assertSame(whitespace, filter.getTokenizer());
		assertSame(occasionallyPassing, filter.getPredicate());
	}

	@Test
	void shouldReturnFilterForFilter() {
		Filter t = new Filter(whitespace, occasionallyPassing);
		Tokenizer tokenizer = Tokenizers.filter(t, sometimesPassing);

		assertEquals(Filter.class, tokenizer.getClass());

		Filter filter = (Filter) tokenizer;

		assertSame(whitespace, filter.getTokenizer());
		assertThat(filter.getPredicate(), accepts("passing"));
		assertThat(filter.getPredicate(), rejects("sometimes", "occasionally"));
	}

	@Test
	void shouldReturnFilterForTransform() {
		Transform t = new Transform(whitespace, identity);
		Tokenizer tokenizer = Tokenizers.filter(t, sometimesPassing);

		assertEquals(TransformFilter.class, tokenizer.getClass());

		TransformFilter filter = (TransformFilter) tokenizer;

		assertSame(t, filter.getTokenizer());
		assertSame(filter.getPredicate(), sometimesPassing);
	}

}
