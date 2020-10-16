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

package org.simmetrics.builders;

import com.google.common.collect.Multiset;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.simmetrics.Metric;
import org.simmetrics.StringMetric;
import org.simmetrics.StringMetricTest;
import org.simmetrics.builders.StringMetrics.ForList;
import org.simmetrics.builders.StringMetrics.ForListWithSimplifier;
import org.simmetrics.builders.StringMetrics.ForMultiset;
import org.simmetrics.builders.StringMetrics.ForMultisetWithSimplifier;
import org.simmetrics.builders.StringMetrics.ForSet;
import org.simmetrics.builders.StringMetrics.ForSetWithSimplifier;
import org.simmetrics.builders.StringMetrics.ForString;
import org.simmetrics.builders.StringMetrics.ForStringWithSimplifier;
import org.simmetrics.metrics.Identity;
import org.simmetrics.simplifiers.Simplifier;
import org.simmetrics.simplifiers.Simplifiers;
import org.simmetrics.tokenizers.Tokenizer;
import org.simmetrics.tokenizers.Tokenizers;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.simmetrics.simplifiers.Simplifiers.toLowerCase;
import static org.simmetrics.simplifiers.SimplifiersMatcher.chain;
import static org.simmetrics.tokenizers.Tokenizers.whitespace;

@SuppressWarnings({ "javadoc", "deprecation" })
@RunWith(Enclosed.class)
public class StringMetricsTest {

	public static class Create {

		private final Metric<String> metric = new Identity<>();
		private final Metric<List<String>> listMetric = new Identity<>();
		private final Metric<Set<String>> setMetric = new Identity<>();

		private final Simplifier simplifier = Simplifiers.toLowerCase();
		private final Simplifier simplifier2 = Simplifiers.removeNonWord();
		private final Tokenizer tokenizer = Tokenizers.whitespace();

		@Test
		public void shouldReturnSame() {
			StringMetric s = new ForString(metric);
			assertSame(s, StringMetrics.create(s));
		}

		@Test
		public void shouldReturnForString() {
			StringMetric wrapped = StringMetrics.create(metric);
			assertEquals(ForString.class, wrapped.getClass());
			ForString forString = (ForString) wrapped;
			assertSame(metric, forString.getMetric());
		}

		@Test
		public void shouldReturnForStringWithSimplifier() {
			ForString forString = new ForString(metric);
			StringMetric wrapped = StringMetrics.create(forString, simplifier);

			assertEquals(ForStringWithSimplifier.class, wrapped.getClass());
			ForStringWithSimplifier fsws = (ForStringWithSimplifier) wrapped;
			assertSame(metric, fsws.getMetric());
			assertSame(simplifier, fsws.getSimplifier());
		}

		@Test
		public void shouldReturnForStringWithChainedSimplifiers() {
			ForStringWithSimplifier forString = new ForStringWithSimplifier(metric, simplifier);
			StringMetric wrapped = StringMetrics.create(forString, simplifier2);

			assertEquals(ForStringWithSimplifier.class, wrapped.getClass());
			ForStringWithSimplifier fsws = (ForStringWithSimplifier) wrapped;
			assertSame(metric, fsws.getMetric());
		}

		@Test
		public void shouldReturnForListWithSimplifier() {
			ForList forList = new ForList(listMetric, tokenizer);
			StringMetric wrapped = StringMetrics.create(forList, simplifier);

			assertEquals(ForListWithSimplifier.class, wrapped.getClass());
			ForListWithSimplifier flws = (ForListWithSimplifier) wrapped;
			assertSame(listMetric, flws.getMetric());
			assertEquals(simplifier, flws.getSimplifier());
			assertSame(tokenizer, flws.getTokenizer());
		}

		@Test
		public void shouldReturnForListWithChainedSimplifiers() {
			ForListWithSimplifier forList = new ForListWithSimplifier(listMetric, simplifier, tokenizer);
			StringMetric wrapped = StringMetrics.create(forList, simplifier2);

			assertEquals(ForListWithSimplifier.class, wrapped.getClass());
			ForListWithSimplifier flws = (ForListWithSimplifier) wrapped;
			assertSame(listMetric, flws.getMetric());
			assertThat(flws.getSimplifier(), chain(simplifier2, simplifier));
			assertSame(tokenizer, flws.getTokenizer());

		}

		@Test
		public void shouldReturnForSetWithSimplifier() {
			ForSet forSet = new ForSet(setMetric, tokenizer);
			StringMetric wrapped = StringMetrics.create(forSet, simplifier);

			assertEquals(ForSetWithSimplifier.class, wrapped.getClass());
			ForSetWithSimplifier fsws = (ForSetWithSimplifier) wrapped;
			assertSame(setMetric, fsws.getMetric());
			assertSame(simplifier, fsws.getSimplifier());
			assertSame(tokenizer, fsws.getTokenizer());

		}

		@Test
		public void shouldReturnForSetWithChainedSimplifiers() {
			ForSetWithSimplifier forSet = new ForSetWithSimplifier(setMetric, simplifier, tokenizer);
			StringMetric wrapped = StringMetrics.create(forSet, simplifier2);

			assertEquals(ForSetWithSimplifier.class, wrapped.getClass());
			ForSetWithSimplifier fsws = (ForSetWithSimplifier) wrapped;
			assertSame(setMetric, fsws.getMetric());
			assertThat(fsws.getSimplifier(), chain(simplifier2, simplifier));
			assertSame(tokenizer, fsws.getTokenizer());
		}

	}

	public static class CreateForList {

		private Metric<List<String>> metric = new Identity<>();
		private Tokenizer tokenizer = Tokenizers.whitespace();
		private Simplifier simplifier = Simplifiers.toLowerCase();

		@Test
		public void shouldReturnForList() {

			StringMetric wrapped = StringMetrics.createForListMetric(metric, tokenizer);
			assertEquals(ForList.class, wrapped.getClass());
			ForList forList = (ForList) wrapped;
			assertSame(metric, forList.getMetric());
			assertSame(tokenizer, forList.getTokenizer());
		}

		@Test
		public void shouldReturnForListWithSimplifier() {

			StringMetric wrapped = StringMetrics.createForListMetric(metric, simplifier, tokenizer);
			assertEquals(ForListWithSimplifier.class, wrapped.getClass());
			ForListWithSimplifier forList = (ForListWithSimplifier) wrapped;
			assertSame(metric, forList.getMetric());
			assertSame(tokenizer, forList.getTokenizer());
			assertSame(simplifier, forList.getSimplifier());
		}

	}

	public static class CreateForSet {

		private Metric<Set<String>> metric = new Identity<>();
		private Tokenizer tokenizer = Tokenizers.whitespace();
		private Simplifier simplifier = Simplifiers.toLowerCase();

		@Test
		public void shouldReturnForSet() {

			StringMetric wrapped = StringMetrics.createForSetMetric(metric, tokenizer);
			assertEquals(ForSet.class, wrapped.getClass());
			ForSet forSet = (ForSet) wrapped;
			assertSame(metric, forSet.getMetric());
			assertSame(tokenizer, forSet.getTokenizer());
		}

		@Test
		public void shouldReturnForSetWithSimplifier() {

			StringMetric wrapped = StringMetrics.createForSetMetric(metric, simplifier, tokenizer);
			assertEquals(ForSetWithSimplifier.class, wrapped.getClass());
			ForSetWithSimplifier forSet = (ForSetWithSimplifier) wrapped;
			assertSame(metric, forSet.getMetric());
			assertSame(tokenizer, forSet.getTokenizer());
			assertSame(simplifier, forSet.getSimplifier());
		}

	}

	public static class CreateForMultiset {

		private Metric<Multiset<String>> metric = new Identity<>();
		private Tokenizer tokenizer = Tokenizers.whitespace();
		private Simplifier simplifier = Simplifiers.toLowerCase();

		@Test
		public void shouldReturnForSet() {

			StringMetric wrapped = StringMetrics.createForMultisetMetric(metric, tokenizer);
			assertEquals(ForMultiset.class, wrapped.getClass());
			ForMultiset forSet = (ForMultiset) wrapped;
			assertSame(metric, forSet.getMetric());
			assertSame(tokenizer, forSet.getTokenizer());
		}

		@Test
		public void shouldReturnForSetWithSimplifier() {

			StringMetric wrapped = StringMetrics.createForMultisetMetric(metric, simplifier, tokenizer);
			assertEquals(ForMultisetWithSimplifier.class, wrapped.getClass());
			ForMultisetWithSimplifier forSet = (ForMultisetWithSimplifier) wrapped;
			assertSame(metric, forSet.getMetric());
			assertSame(tokenizer, forSet.getTokenizer());
			assertSame(simplifier, forSet.getSimplifier());
		}

	}

	public static class ForListTest extends StringMetricTest {

		@Override
		protected boolean toStringIncludesSimpleClassName() {
			return false;
		}

		@Override
		protected StringMetric getMetric() {
			Metric<List<String>> identity = new Identity<>();
			return new ForList(identity, whitespace());
		}

		@Override
		protected T[] getTests() {
			return new T[]{
					new T(1.0f, "a b c","a b c"),
					new T(0.0f, "a b c","a b c d"),
					new T(0.0f, "","a b c")
			};
		}
	}

	public static class ForListWithSimplifierTest extends StringMetricTest {

		@Override
		protected boolean toStringIncludesSimpleClassName() {
			return false;
		}

		@Override
		protected boolean satisfiesCoincidence() {
			return false;
		}

		@Override
		protected StringMetric getMetric() {
			Metric<List<String>> identity = new Identity<>();
			return new ForListWithSimplifier(identity, toLowerCase(), whitespace());
		}

		@Override
		protected T[] getTests() {
			return new T[]{
					new T(1.0f, "A B C","a b c"),
					new T(0.0f, "a b c","a b c d"),
					new T(0.0f, "","a b c")
			};
		}
	}

	public static class ForSetTest extends StringMetricTest {

		@Override
		protected boolean toStringIncludesSimpleClassName() {
			return false;
		}

		@Override
		protected StringMetric getMetric() {
			Metric<Set<String>> identity = new Identity<>();
			return new StringMetrics.ForSet(identity, whitespace());
		}

		@Override
		protected T[] getTests() {
			return new T[]{
					new T(1.0f, "a b c","a b c"),
					new T(0.0f, "a b c","a b c d"),
					new T(0.0f, "","a b c")
			};
		}
	}

	public static class ForSetWithSimplifierTest extends StringMetricTest {

		@Override
		protected boolean satisfiesCoincidence() {
			return false;
		}

		@Override
		protected boolean toStringIncludesSimpleClassName() {
			return false;
		}

		@Override
		protected StringMetric getMetric() {
			Metric<Set<String>> identity = new Identity<>();
			return new ForSetWithSimplifier(identity, toLowerCase(), whitespace());
		}

		@Override
		protected T[] getTests() {
			return new T[]{
					new T(1.0f, "A B C","a b c"),
					new T(0.0f, "a b c","a b c d"),
					new T(0.0f, "","a b c")
			};
		}
	}

	public static class ForMultisetTest extends StringMetricTest {

		@Override
		protected boolean toStringIncludesSimpleClassName() {
			return false;
		}

		@Override
		protected StringMetric getMetric() {
			Metric<Multiset<String>> identity = new Identity<>();
			return new StringMetrics.ForMultiset(identity, whitespace());
		}

		@Override
		protected T[] getTests() {
			return new T[]{
					new T(1.0f, "a b c","a b c"),
					new T(0.0f, "a b c","a b c d"),
					new T(0.0f, "","a b c")
			};
		}
	}

	public static class ForMultisetWithSimplifierTest extends StringMetricTest {

		@Override
		protected boolean satisfiesCoincidence() {
			return false;
		}

		@Override
		protected boolean toStringIncludesSimpleClassName() {
			return false;
		}

		@Override
		protected StringMetric getMetric() {
			Metric<Multiset<String>> identity = new Identity<>();
			return new StringMetrics.ForMultisetWithSimplifier(identity, toLowerCase(), whitespace());
		}

		@Override
		protected T[] getTests() {
			return new T[]{
					new T(1.0f, "A B C","a b c"),
					new T(0.0f, "a b c","a b c d"),
					new T(0.0f, "","a b c")
			};
		}
	}

	public static class ForStringTest extends StringMetricTest {

		@Override
		protected boolean toStringIncludesSimpleClassName() {
			return false;
		}

		@Override
		protected StringMetric getMetric() {
			Metric<String> identity = new Identity<>();
			return new ForString(identity);
		}

		@Override
		protected T[] getTests() {
			return new T[]{
					new T(1.0f, "a b c","a b c"),
					new T(0.0f, "a b c","a b c d"),
					new T(0.0f, "","a b c")
			};
		}
	}

	public static class ForStringWithSimplifierTest extends StringMetricTest {

		@Override
		protected boolean satisfiesCoincidence() {
			return false;
		}

		@Override
		protected boolean toStringIncludesSimpleClassName() {
			return false;
		}

		@Override
		protected StringMetric getMetric() {
			Metric<String> identity = new Identity<>();
			return new ForStringWithSimplifier(identity, toLowerCase());
		}

		@Override
		protected T[] getTests() {
			return new T[]{
					new T(1.0f, "A B C","a b c"),
					new T(0.0f, "a b c","a b c d"),
					new T(0.0f, "","a b c")
			};
		}
	}
}
