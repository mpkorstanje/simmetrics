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

import com.google.common.cache.Cache;
import com.google.common.collect.Multiset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.simmetrics.ListDistance;
import org.simmetrics.MultisetDistance;
import org.simmetrics.SetDistance;
import org.simmetrics.StringDistance;
import org.simmetrics.builders.StringDistanceBuilder.CachingListTokenizer;
import org.simmetrics.builders.StringDistanceBuilder.CachingMultisetTokenizer;
import org.simmetrics.builders.StringDistanceBuilder.CachingSetTokenizer;
import org.simmetrics.builders.StringDistanceBuilder.CachingSimplifier;
import org.simmetrics.simplifiers.Simplifier;
import org.simmetrics.tokenizers.Tokenizer;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.simmetrics.builders.StringDistanceBuilder.with;

final class StringDistanceBuilderTest {

	@ExtendWith(MockitoExtension.class)
	public static class StringDistanceBuilderChainTest {

		@Mock
		private StringDistance stringDistance;
		@Mock
		private ListDistance<String> listDistance;
		@Mock
		private SetDistance<String> setDistance;
		@Mock
		private MultisetDistance<String> multisetDistance;	
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
		void testStringDistance01() {
			with(stringDistance)
					.build();
		}
	
		@Test
		void testStringDistanceWithSimplifier01() {
			with(stringDistance)
					.simplify(simplifier)
					.build();
		}
		
		@Test
		void testStringDistanceWithSimplifier02() {
			with(stringDistance)
					.simplify(simplifier)
					.simplify(simplifier)
					.build();
		}
		@Test
		void testStringDistanceWithSimplifier01WithCache() {
			with(stringDistance)
					.simplify(simplifier)
					.cacheStrings(stringCache)
					.build();
		}
		@Test
		void testStringDistanceWithSimplifer02WithCache() {
			with(stringDistance)
					.simplify(simplifier)
					.simplify(simplifier)
					.cacheStrings(stringCache)
					.build();
		}
		
		@Test
		void testListDistance() {
			with(listDistance)
					.tokenize(tokenizer)
					.build();
		}
	
		@Test
		void testListDistanceWithFilter() {
			with(listDistance)
					.tokenize(tokenizer)
					.filter(predicate)
					.build();
		}
		@Test
		void testListDistanceWithTransform() {
			with(listDistance)
					.tokenize(tokenizer)
					.transform(function)
					.build();
		}
		
		
		@Test
		void testListDistanceWithFilterAndTransform01() {
			with(listDistance)
					.tokenize(tokenizer)
					.filter(predicate)
					.transform(function)
					.build();
		}
	
		@Test
		void testListDistanceWithFilterAndTransform02() {
			with(listDistance)
					.tokenize(tokenizer)
					.transform(function)
					.filter(predicate)
					.build();
		}
	
		@Test
		void testListDistanceWithSimplifier01() {
			with(listDistance)
					.simplify(simplifier)
					.tokenize(tokenizer)
					.build();
		}
	
		@Test
		void testListDistanceWithSimplifier02() {
			with(listDistance)
					.simplify(simplifier)
					.simplify(simplifier)
					.tokenize(tokenizer)
					.build();
		}
		
		@Test
		void testListDistanceWithSimplifier01WithCache() {
			with(listDistance)
					.simplify(simplifier)
					.cacheStrings(stringCache)
					.tokenize(tokenizer)
					.build();
		}
	
		@Test
		void testListDistanceWithSimplifier02WithCache() {
			with(listDistance)
					.simplify(simplifier)
					.simplify(simplifier)
					.cacheStrings(stringCache)
					.tokenize(tokenizer)
					.build();
		}
		
		@Test
		void testListDistance02() {
			with(listDistance)
					.tokenize(tokenizer)
					.tokenize(tokenizer)
					.build();
		}
		
		@Test
		void testListDistance01WithCache() {
			with(listDistance)
					.tokenize(tokenizer)
					.cacheTokens(listCache)
					.build();
		}
		
		@Test
		void testListDistance02WithCache() {
			with(listDistance)
					.tokenize(tokenizer)
					.tokenize(tokenizer)
					.cacheTokens(listCache)
					.build();
		}

		@Test
		void testSetDistance() {
			with(setDistance)
					.tokenize(tokenizer)
					.build();
		}
	
		@Test
		void testSetDistanceWithFilter() {
			with(setDistance)
					.tokenize(tokenizer)
					.filter(predicate)
					.build();
		}
		
		@Test
		void testSetDistanceWithTransform() {
			with(setDistance)
					.tokenize(tokenizer)
					.transform(function)
					.build();
		}
		
		
		@Test
		void testSetDistanceWithFilterAndTransform01() {
			with(setDistance)
					.tokenize(tokenizer)
					.filter(predicate)
					.transform(function)
					.build();
		}
	
		@Test
		void testSetDistanceWithFilterAndTransform02() {
			with(setDistance)
					.tokenize(tokenizer)
					.transform(function)
					.filter(predicate)
					.build();
		}
	
		@Test
		void testSetDistanceWithSimplifier01() {
			with(setDistance)
					.simplify(simplifier)
					.tokenize(tokenizer)
					.build();
		}
	
		@Test
		void testSetDistanceWithSimplifier02() {
			with(setDistance)
					.simplify(simplifier)
					.simplify(simplifier)
					.tokenize(tokenizer)
					.build();
		}
	
		@Test
		void testSetDistanceWithSimplifier01WithCache() {
			with(setDistance)
					.simplify(simplifier)
					.cacheStrings(stringCache)
					.tokenize(tokenizer)
					.build();
		}
	
		@Test
		void testSetDistanceWithSimplifier02WithCache() {
			with(setDistance)
					.simplify(simplifier)
					.simplify(simplifier)
					.cacheStrings(stringCache)
					.tokenize(tokenizer)
					.build();
		}
		@Test
		void testSetDistance02() {
			with(setDistance)
					.tokenize(tokenizer)
					.tokenize(tokenizer)
					.build();
		}
	
		@Test
		void testSetDistance01WithCache() {
			with(setDistance)
					.tokenize(tokenizer)
					.cacheTokens(setCache)
					.build();
		}
		
		@Test
		void testSetDistance02WithCache() {
			with(setDistance)
					.tokenize(tokenizer)
					.tokenize(tokenizer)
					.cacheTokens(setCache)
					.build();
		}

		@Test
		void testMultisetDistance() {
			with(multisetDistance)
					.tokenize(tokenizer)
					.build();
		}
	
		@Test
		void testMultisetDistanceWithFilter() {
			with(multisetDistance)
					.tokenize(tokenizer)
					.filter(predicate)
					.build();
		}
		
		@Test
		void testMultisetDistanceWithTransform() {
			with(multisetDistance)
					.tokenize(tokenizer)
					.transform(function)
					.build();
		}
		
		
		@Test
		void testMultisetDistanceWithFilterAndTransform01() {
			with(multisetDistance)
					.tokenize(tokenizer)
					.filter(predicate)
					.transform(function)
					.build();
		}
	
		@Test
		void testMultisetDistanceWithFilterAndTransform02() {
			with(multisetDistance)
					.tokenize(tokenizer)
					.transform(function)
					.filter(predicate)
					.build();
		}
	
		@Test
		void testMultisetDistanceWithSimplifier01() {
			with(multisetDistance)
					.simplify(simplifier)
					.tokenize(tokenizer)
					.build();
		}
	
		@Test
		void testMultisetDistanceWithSimplifier02() {
			with(multisetDistance)
					.simplify(simplifier)
					.simplify(simplifier)
					.tokenize(tokenizer)
					.build();
		}
		@Test
		void testMultisetDistanceWithSimplifier01WithCache() {
			with(multisetDistance)
					.simplify(simplifier)
					.cacheStrings(stringCache)
					.tokenize(tokenizer)
					.build();
		}
	
		@Test
		void testMultisetDistanceWithSimplifier02WithCache() {
			with(multisetDistance)
					.simplify(simplifier)
					.simplify(simplifier)
					.cacheStrings(stringCache)
					.tokenize(tokenizer)
					.build();
		}
		@Test
		void testMultisetDistance02() {
			with(multisetDistance)
					.tokenize(tokenizer)
					.tokenize(tokenizer)
					.build();
		}
	
		@Test
		void testMultisetDistance01WithCache() {
			with(multisetDistance)
					.tokenize(tokenizer)
					.cacheTokens(multisetCache)
					.build();
		}
		
		@Test
		void testMultisetDistance02WithCache() {
			with(multisetDistance)
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
