/*-
 * #%L
 * Simmetrics - Core
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

package com.github.mpkorstanje.simmetrics.builders;

import com.google.common.cache.Cache;
import com.google.common.collect.Multiset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.github.mpkorstanje.simmetrics.ListMetric;
import com.github.mpkorstanje.simmetrics.MultisetMetric;
import com.github.mpkorstanje.simmetrics.SetMetric;
import com.github.mpkorstanje.simmetrics.StringMetric;
import com.github.mpkorstanje.simmetrics.builders.StringMetricBuilder.CachingListTokenizer;
import com.github.mpkorstanje.simmetrics.builders.StringMetricBuilder.CachingMultisetTokenizer;
import com.github.mpkorstanje.simmetrics.builders.StringMetricBuilder.CachingSetTokenizer;
import com.github.mpkorstanje.simmetrics.builders.StringMetricBuilder.CachingSimplifier;
import com.github.mpkorstanje.simmetrics.simplifiers.Simplifier;
import com.github.mpkorstanje.simmetrics.tokenizers.Tokenizer;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.github.mpkorstanje.simmetrics.builders.StringMetricBuilder.with;

final class StringMetricBuilderTest {

	@ExtendWith(MockitoExtension.class)
	public static class StringMetricBuilderChainTest {

		@Mock
		private StringMetric stringMetric;
		@Mock
		private ListMetric<String> listMetric;
		@Mock
		private SetMetric<String> setMetric;
		@Mock
		private MultisetMetric<String> multisetMetric;	
		@Mock
		private Simplifier simplifier;
		@Mock
		private Tokenizer tokenizer;
	
		@Mock
		private Predicate<String> predicate;
		@Mock
		private Function<String,String> function;
		@Mock
		private Cache<String, String> stringCache;
		@Mock
		private Cache<String, List<String>> listCache;
		@Mock
		private Cache<String, Set<String>> setCache;
		@Mock
		private Cache<String, Multiset<String>> multisetCache;
		
		@Test
		void testStringMetric01() {
			with(stringMetric)
					.build();
		}
	
		@Test
		void testStringMetricWithSimplifier01() {
			with(stringMetric)
					.simplify(simplifier)
					.build();
		}
		
		@Test
		void testStringMetricWithSimplifier02() {
			with(stringMetric)
					.simplify(simplifier)
					.simplify(simplifier)
					.build();
		}
		@Test
		void testStringMetricWithSimplifier01WithCache() {
			with(stringMetric)
					.simplify(simplifier)
					.cacheStrings(stringCache)
					.build();
		}
		@Test
		void testStringMetricWithSimplifer02WithCache() {
			with(stringMetric)
					.simplify(simplifier)
					.simplify(simplifier)
					.cacheStrings(stringCache)
					.build();
		}
		
		@Test
		void testListMetric() {
			with(listMetric)
					.tokenize(tokenizer)
					.build();
		}
	
		@Test
		void testListMetricWithFilter() {
			with(listMetric)
					.tokenize(tokenizer)
					.filter(predicate)
					.build();
		}
		@Test
		void testListMetricWithTransform() {
			with(listMetric)
					.tokenize(tokenizer)
					.transform(function)
					.build();
		}
		
		
		@Test
		void testListMetricWithFilterAndTransform01() {
			with(listMetric)
					.tokenize(tokenizer)
					.filter(predicate)
					.transform(function)
					.build();
		}
	
		@Test
		void testListMetricWithFilterAndTransform02() {
			with(listMetric)
					.tokenize(tokenizer)
					.transform(function)
					.filter(predicate)
					.build();
		}
	
		@Test
		void testListMetricWithSimplifier01() {
			with(listMetric)
					.simplify(simplifier)
					.tokenize(tokenizer)
					.build();
		}
	
		@Test
		void testListMetricWithSimplifier02() {
			with(listMetric)
					.simplify(simplifier)
					.simplify(simplifier)
					.tokenize(tokenizer)
					.build();
		}
		
		@Test
		void testListMetricWithSimplifier01WithCache() {
			with(listMetric)
					.simplify(simplifier)
					.cacheStrings(stringCache)
					.tokenize(tokenizer)
					.build();
		}
	
		@Test
		void testListMetricWithSimplifier02WithCache() {
			with(listMetric)
					.simplify(simplifier)
					.simplify(simplifier)
					.cacheStrings(stringCache)
					.tokenize(tokenizer)
					.build();
		}
		
		@Test
		void testListMetric02() {
			with(listMetric)
					.tokenize(tokenizer)
					.tokenize(tokenizer)
					.build();
		}
		
		@Test
		void testListMetric01WithCache() {
			with(listMetric)
					.tokenize(tokenizer)
					.cacheTokens(listCache)
					.build();
		}
		
		@Test
		void testListMetric02WithCache() {
			with(listMetric)
					.tokenize(tokenizer)
					.tokenize(tokenizer)
					.cacheTokens(listCache)
					.build();
		}

		@Test
		void testSetMetric() {
			with(setMetric)
					.tokenize(tokenizer)
					.build();
		}
	
		@Test
		void testSetMetricWithFilter() {
			with(setMetric)
					.tokenize(tokenizer)
					.filter(predicate)
					.build();
		}
		
		@Test
		void testSetMetricWithTransform() {
			with(setMetric)
					.tokenize(tokenizer)
					.transform(function)
					.build();
		}
		
		
		@Test
		void testSetMetricWithFilterAndTransform01() {
			with(setMetric)
					.tokenize(tokenizer)
					.filter(predicate)
					.transform(function)
					.build();
		}
	
		@Test
		void testSetMetricWithFilterAndTransform02() {
			with(setMetric)
					.tokenize(tokenizer)
					.transform(function)
					.filter(predicate)
					.build();
		}
	
		@Test
		void testSetMetricWithSimplifier01() {
			with(setMetric)
					.simplify(simplifier)
					.tokenize(tokenizer)
					.build();
		}
	
		@Test
		void testSetMetricWithSimplifier02() {
			with(setMetric)
					.simplify(simplifier)
					.simplify(simplifier)
					.tokenize(tokenizer)
					.build();
		}
	
		@Test
		void testSetMetricWithSimplifier01WithCache() {
			with(setMetric)
					.simplify(simplifier)
					.cacheStrings(stringCache)
					.tokenize(tokenizer)
					.build();
		}
	
		@Test
		void testSetMetricWithSimplifier02WithCache() {
			with(setMetric)
					.simplify(simplifier)
					.simplify(simplifier)
					.cacheStrings(stringCache)
					.tokenize(tokenizer)
					.build();
		}
		@Test
		void testSetMetric02() {
			with(setMetric)
					.tokenize(tokenizer)
					.tokenize(tokenizer)
					.build();
		}
	
		@Test
		void testSetMetric01WithCache() {
			with(setMetric)
					.tokenize(tokenizer)
					.cacheTokens(setCache)
					.build();
		}
		
		@Test
		void testSetMetric02WithCache() {
			with(setMetric)
					.tokenize(tokenizer)
					.tokenize(tokenizer)
					.cacheTokens(setCache)
					.build();
		}

		@Test
		void testMultisetMetric() {
			with(multisetMetric)
					.tokenize(tokenizer)
					.build();
		}
	
		@Test
		void testMultisetMetricWithFilter() {
			with(multisetMetric)
					.tokenize(tokenizer)
					.filter(predicate)
					.build();
		}
		
		@Test
		void testMultisetMetricWithTransform() {
			with(multisetMetric)
					.tokenize(tokenizer)
					.transform(function)
					.build();
		}
		
		
		@Test
		void testMultisetMetricWithFilterAndTransform01() {
			with(multisetMetric)
					.tokenize(tokenizer)
					.filter(predicate)
					.transform(function)
					.build();
		}
	
		@Test
		void testMultisetMetricWithFilterAndTransform02() {
			with(multisetMetric)
					.tokenize(tokenizer)
					.transform(function)
					.filter(predicate)
					.build();
		}
	
		@Test
		void testMultisetMetricWithSimplifier01() {
			with(multisetMetric)
					.simplify(simplifier)
					.tokenize(tokenizer)
					.build();
		}
	
		@Test
		void testMultisetMetricWithSimplifier02() {
			with(multisetMetric)
					.simplify(simplifier)
					.simplify(simplifier)
					.tokenize(tokenizer)
					.build();
		}
		@Test
		void testMultisetMetricWithSimplifier01WithCache() {
			with(multisetMetric)
					.simplify(simplifier)
					.cacheStrings(stringCache)
					.tokenize(tokenizer)
					.build();
		}
	
		@Test
		void testMultisetMetricWithSimplifier02WithCache() {
			with(multisetMetric)
					.simplify(simplifier)
					.simplify(simplifier)
					.cacheStrings(stringCache)
					.tokenize(tokenizer)
					.build();
		}
		@Test
		void testMultisetMetric02() {
			with(multisetMetric)
					.tokenize(tokenizer)
					.tokenize(tokenizer)
					.build();
		}
	
		@Test
		void testMultisetMetric01WithCache() {
			with(multisetMetric)
					.tokenize(tokenizer)
					.cacheTokens(multisetCache)
					.build();
		}
		
		@Test
		void testMultisetMetric02WithCache() {
			with(multisetMetric)
					.tokenize(tokenizer)
					.tokenize(tokenizer)
					.cacheTokens(multisetCache)
					.build();
		}
	}
	public static class CachingListTokenizerTest extends CachingTokenizerTest<List<String>> {

		@Override
		protected boolean supportsTokenizeToSet() {
			return false;
		}

		@Override
		protected boolean supportsTokenizeToMultiset() {
			return false;
		}

		@Override
		public Tokenizer getTokenizer(Cache<String, List<String>> cache, Tokenizer tokenizer) {
			return new CachingListTokenizer(cache, tokenizer);
		}
	}
	
	public static class CachingMultisetTokenizerTest extends CachingTokenizerTest<Multiset<String>> {

		@Override
		protected final boolean supportsTokenizeToList() {
			return false;
		}
		
		@Override
		protected boolean supportsTokenizeToSet() {
			return false;
		}

		@Override
		public Tokenizer getTokenizer(Cache<String, Multiset<String>> cache, Tokenizer tokenizer) {
			return new CachingMultisetTokenizer(cache, tokenizer);
		}
	}
	
	public static class CachingSetTokenizerTest extends CachingTokenizerTest<Set<String>> {

		@Override
		protected final boolean supportsTokenizeToList() {
			return false;
		}
		
		@Override
		protected boolean supportsTokenizeToMultiset() {
			return false;
		}

		@Override
		public Tokenizer getTokenizer(Cache<String, Set<String>> cache, Tokenizer tokenizer) {
			return new CachingSetTokenizer(cache,tokenizer);
		}
	}
	
	public static class StringDistanceCachingSimplifierTest extends CachingSimplifierTest {

		@Override
		protected Simplifier getCachingSimplifier(Cache<String, String> cache,
				Simplifier innerSimplifier) {
			return new CachingSimplifier(cache, innerSimplifier);
		}
	}
}
