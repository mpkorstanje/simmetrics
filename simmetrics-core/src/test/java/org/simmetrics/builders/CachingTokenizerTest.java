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
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableMultiset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.simmetrics.tokenizers.Tokenizer;
import org.simmetrics.tokenizers.TokenizerTest;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

abstract class CachingTokenizerTest<V> extends TokenizerTest {

    @Mock
    private Tokenizer innerTokenizer;

    @Mock
    private Cache<String, V> brokenCache;

    private final Cache<String, V> cache = CacheBuilder.newBuilder()
            .initialCapacity(2)
            .maximumSize(2)
            .build();

    @Override
    protected final Tokenizer getTokenizer() {
        return getTokenizer(cache, innerTokenizer);
    }

    protected abstract Tokenizer getTokenizer(Cache<String, V> cache, Tokenizer tokenizer);

    @BeforeEach
    public void before() throws Exception {

        lenient()
                .when(brokenCache.get(anyString(), any(Callable.class)))
                .thenThrow(new ExecutionException(new Exception()));
        lenient()
                .when(innerTokenizer.tokenizeToList("ABC"))
                .thenReturn(newArrayList("ABC"));
        lenient()
                .when(innerTokenizer.tokenizeToList("CCC"))
                .thenReturn(newArrayList("CCC"));
        lenient()
                .when(innerTokenizer.tokenizeToList("EEE"))
                .thenReturn(newArrayList("EEE"));
        lenient()
                .when(innerTokenizer.tokenizeToList(""))
                .thenReturn(newArrayList(""));
        lenient()
                .when(innerTokenizer.tokenizeToSet("ABC"))
                .thenReturn(newHashSet("ABC"));
        lenient()
                .when(innerTokenizer.tokenizeToSet("CCC"))
                .thenReturn(newHashSet("CCC"));
        lenient()
                .when(innerTokenizer.tokenizeToSet("EEE"))
                .thenReturn(newHashSet("EEE"));
        lenient()
                .when(innerTokenizer.tokenizeToSet(""))
                .thenReturn(newHashSet(""));
        lenient()
                .when(innerTokenizer.tokenizeToMultiset("ABC"))
                .thenReturn(ImmutableMultiset.of("ABC"));
        lenient()
                .when(innerTokenizer.tokenizeToMultiset("CCC"))
                .thenReturn(ImmutableMultiset.of("CCC"));
        lenient()
                .when(innerTokenizer.tokenizeToMultiset("EEE"))
                .thenReturn(ImmutableMultiset.of("EEE"));
        lenient()
                .when(innerTokenizer.tokenizeToMultiset(""))
                .thenReturn(ImmutableMultiset.of(""));
    }

    @Test
    void tokenizeToSetShouldUseCache() {
        if (!supportsTokenizeToSet()) {
            return;
        }

        for (T t : tests) {
            tokenizer.tokenizeToSet(t.string());
        }

        verify(innerTokenizer, times(1)).tokenizeToSet("ABC");
        verify(innerTokenizer, times(2)).tokenizeToSet("CCC");
    }

    @Test
    void tokenizeToMultisetShouldUseCache() {
        if (!supportsTokenizeToMultiset()) {
            return;
        }

        for (T t : tests) {
            tokenizer.tokenizeToMultiset(t.string());
        }

        verify(innerTokenizer, times(1)).tokenizeToMultiset("ABC");
        verify(innerTokenizer, times(2)).tokenizeToMultiset("CCC");
    }

    @Test
    void tokenizeToListShouldUseCache() {
        if (!supportsTokenizeToList()) {
            return;
        }

        for (T t : tests) {
            tokenizer.tokenizeToList(t.string());
        }

        verify(innerTokenizer, times(1)).tokenizeToList("ABC");
        verify(innerTokenizer, times(2)).tokenizeToList("CCC");
    }

    @Test
    void tokenizeToListShouldThrowIllegalStateException() {
        if (!supportsTokenizeToList()) {
            return;
        }
        assertThrows(
                IllegalStateException.class,
                () -> getTokenizer(brokenCache, innerTokenizer).tokenizeToList("Sheep")
        );
    }

    @Test
    void tokenizeToSetShouldThrowIllegalStateException() {
        if (!supportsTokenizeToSet()) {
            return;
        }
        assertThrows(
                IllegalStateException.class,
                () -> getTokenizer(brokenCache, innerTokenizer).tokenizeToSet("Sheep")
        );
    }

    @Test
    void tokenizeToMultisetShouldThrowIllegalStateException() {
        if (!supportsTokenizeToMultiset()) {
            return;
        }
        assertThrows(
                IllegalStateException.class,
                () -> getTokenizer(brokenCache, innerTokenizer).tokenizeToMultiset("Sheep")
        );
    }

    @Override
    protected final T[] getTests() {

        return new T[] {
                new T("ABC", "ABC"),
                new T("CCC", "CCC"),
                new T("ABC", "ABC"),
                new T("EEE", "EEE"),
                new T("ABC", "ABC"),
                new T("CCC", "CCC"),
                new T("", "")
        };
    }

}
