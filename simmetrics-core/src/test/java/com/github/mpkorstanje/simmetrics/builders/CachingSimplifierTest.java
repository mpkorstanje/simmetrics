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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static com.github.mpkorstanje.simmetrics.simplifiers.Simplifiers.toLowerCase;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Test;
import com.github.mpkorstanje.simmetrics.simplifiers.Simplifier;
import com.github.mpkorstanje.simmetrics.simplifiers.SimplifierTest;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

abstract class CachingSimplifierTest extends SimplifierTest {

	private Simplifier innerSimplifier;

	@Override
	protected final Simplifier getSimplifier() {

		innerSimplifier = mock(Simplifier.class);

		when(innerSimplifier.simplify("ABC"))
		.thenReturn("abc");
		when(innerSimplifier.simplify("CCC"))
		.thenReturn("ccc");
		when(innerSimplifier.simplify("EEE"))
		.thenReturn("eee");
		when(innerSimplifier.simplify(""))
		.thenReturn("");

		Cache<String, String> cache = CacheBuilder.newBuilder()
				.initialCapacity(2)
				.maximumSize(2)
				.build();

		return getCachingSimplifier(cache, innerSimplifier);
	}
	
	protected abstract Simplifier getCachingSimplifier(Cache<String,String> cache, Simplifier innerSimplifier);

	@Override
	protected final T[] getTests() {

		return new T[] { 
				new T("ABC", "abc"), 
				new T("CCC", "ccc"),
				new T("ABC", "abc"), 
				new T("EEE", "eee"), 
				new T("ABC", "abc"),
				new T("CCC", "ccc"), 
				new T("", "")
		};
	}

	@Test
	final void simplifyShouldUseCache() {
		for (T t : tests) {
			simplifier.simplify(t.string());
		}

		verify(innerSimplifier, times(1)).simplify("ABC");
		verify(innerSimplifier, times(2)).simplify("CCC");
	}
	
	@Test
	void shouldThrowIllegalStateException() throws ExecutionException{
		Cache<String, String> cache = mock(Cache.class);
		when(cache.get(anyString(), any(Callable.class))).thenThrow(new ExecutionException(new Exception()));
		Simplifier cachingSimplifier = getCachingSimplifier(cache, toLowerCase());
		assertThrows(IllegalStateException.class, () -> cachingSimplifier.simplify("Sheep"));
	}
}
