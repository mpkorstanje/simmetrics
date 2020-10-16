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

import static java.util.Arrays.asList;

import org.simmetrics.tokenizers.Tokenizers.Transform;
import org.simmetrics.tokenizers.Tokenizers.Filter.TransformFilter;
import org.simmetrics.tokenizers.Tokenizers.Transform.FilterTransform;

import java.util.function.Function;

import static org.simmetrics.tokenizers.Tokenizers.whitespace;

public class FilterTransformTest extends TokenizerTest {

	private java.util.function.Function<String, String> toUpperCase = String::toUpperCase;

	private static java.util.function.Predicate<String> theAndOr = s -> !asList("eht", "dna", "ro").contains(s);

	private static Function<String, String> reverse = input -> new StringBuilder(input).reverse().toString();

	@Override
	protected T[] getTests() {
		return new T[]{
				new T("the mouse and cat or dog", "ESUOM", "TAC", "GOD"),
				new T("")
		};
	}

	@Override
	protected Tokenizer getTokenizer() {
		final Transform transform = new Transform(whitespace(), reverse);
		final TransformFilter transformFilter = new TransformFilter(transform, theAndOr);
		return new FilterTransform(transformFilter, toUpperCase);

	}


}
