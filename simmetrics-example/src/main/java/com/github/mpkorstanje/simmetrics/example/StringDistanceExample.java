/*-
 * #%L
 * Simmetrics - Examples
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

package com.github.mpkorstanje.simmetrics.example;

import static com.github.mpkorstanje.simmetrics.builders.StringDistanceBuilder.with;

import com.github.mpkorstanje.simmetrics.StringDistance;
import com.github.mpkorstanje.simmetrics.builders.StringDistanceBuilder;
import com.github.mpkorstanje.simmetrics.metrics.EuclideanDistance;
import com.github.mpkorstanje.simmetrics.metrics.StringDistances;
import com.github.mpkorstanje.simmetrics.tokenizers.Tokenizers;

/**
 * The StringDistances utility class contains a predefined list of well
 * known distance metrics for strings.
 */
final class StringDistanceExample {

	/**
	 * Two strings can be compared using a predefined distance metric.
	 */
	static float example01() {

		String str1 = "This is a sentence. It is made of words";
		String str2 = "This sentence is similar. It has almost the same words";

		StringDistance metric = StringDistances.levenshtein();

		return metric.distance(str1, str2); // 30.0000
	}

	/**
	 * A tokenizer is included when the metric is a set or list metric. For the
	 * euclidean distance, it is a whitespace tokenizer.
	 * 
	 * Note that most predefined metrics are setup with a whitespace tokenizer.
	 */
	static float example02() {

		String str1 = "A quirky thing it is. This is a sentence.";
		String str2 = "This sentence is similar. A quirky thing it is.";

		StringDistance metric = StringDistances.euclideanDistance();

		return metric.distance(str1, str2); // 2.0000
	}

	/**
	 * Using the string distance builder distance metrics can be customized.
	 * Instead of a whitespace tokenizer a q-gram tokenizer is used.
	 *
	 * For more examples see StringDistanceBuilderExample.
	 */
	static float example03() {

		String str1 = "A quirky thing it is. This is a sentence.";
		String str2 = "This sentence is similar. A quirky thing it is.";

		StringDistance metric = 
				StringDistanceBuilder.with(new EuclideanDistance<>())
				.tokenize(Tokenizers.qGram(3))
				.build();

		return metric.distance(str1, str2); // 4.8989
	}

}
