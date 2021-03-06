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

import com.github.mpkorstanje.simmetrics.tokenizers.Tokenizers.Filter;
import com.github.mpkorstanje.simmetrics.tokenizers.Tokenizers.Filter.TransformFilter;
import com.github.mpkorstanje.simmetrics.tokenizers.Tokenizers.Transform.FilterTransform;

import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Arrays.asList;
import static com.github.mpkorstanje.simmetrics.tokenizers.Tokenizers.whitespace;

final class TransformFilterTest extends TokenizerTest {

	private static Predicate<String> theAndOr = s -> !asList("THE", "AND", "OR").contains(s);
	private static Predicate<String> dog = s -> !"dog".equals(s);

	private static Function<String, String> toUpperCase = String::toUpperCase;
	

	@Override
	protected T[] getTests() {
		return new T[] {
				new T("the mouse and cat or dog", "MOUSE", "CAT"),
				new T("")
		};
	}

	@Override
	protected Tokenizer getTokenizer() {
		final Filter filter = new Filter(whitespace(), dog);
		final FilterTransform transform = new FilterTransform(filter,toUpperCase);
		return new TransformFilter(transform, theAndOr);
		
	}

}
