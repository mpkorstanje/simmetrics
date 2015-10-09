/*
 * #%L
 * Simmetrics Core
 * %%
 * Copyright (C) 2014 - 2015 Simmetrics Authors
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

package org.simmetrics;

import java.util.Set;

import org.junit.Test;
import org.simmetrics.tokenizers.Tokenizer;
import org.simmetrics.tokenizers.TokenizerTest;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import static com.google.common.collect.Sets.newHashSet;
import static org.mockito.Mockito.*;

@SuppressWarnings("javadoc")
public class CachingSetTokenizerTest extends TokenizerTest {

	private Tokenizer innerTokenizer;
	private Cache<String, Set<String>> cache;
	
	@Override
	protected final boolean supportsTokenizeToList() {
		return false;
	}

	@Override
	protected final Tokenizer getTokenizer() {
		
		innerTokenizer = mock(Tokenizer.class);
	
		when(innerTokenizer.tokenizeToSet("ABC")).thenReturn(newHashSet("ABC"));
		when(innerTokenizer.tokenizeToSet("CCC")).thenReturn(newHashSet("CCC"));
		when(innerTokenizer.tokenizeToSet("EEE")).thenReturn(newHashSet("EEE"));
		when(innerTokenizer.tokenizeToSet("")).thenReturn(newHashSet(""));
	
		cache = CacheBuilder.newBuilder().initialCapacity(2).maximumSize(2).build();

		
		return new StringMetricBuilder.CachingSetTokenizer(cache,innerTokenizer);
	}

	@Override
	protected final T[] getTests() {

		return new T[] { new T("ABC", "ABC")
				, new T("CCC", "CCC"),
				new T("ABC", "ABC"), 
				new T("EEE", "EEE"), 
				new T("ABC", "ABC"),
				new T("CCC", "CCC"),
				new T("","") 

		};
	}

	@Test
	public final void tokenizeToSetShouldUseCache() {
		for (T t : tests) {
			tokenizer.tokenizeToSet(t.string());
		}

		 verify(innerTokenizer, times(1)).tokenizeToSet("ABC");
		 verify(innerTokenizer, times(2)).tokenizeToSet("CCC");
	}
	
}