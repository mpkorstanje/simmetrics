/*-
 * #%L
 * Simmetrics Examples
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
package com.github.mpkorstanje.simmetrics.example;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.github.mpkorstanje.simmetrics.SetMetric;
import com.github.mpkorstanje.simmetrics.StringDistance;
import com.github.mpkorstanje.simmetrics.StringMetric;
import com.github.mpkorstanje.simmetrics.builders.StringDistanceBuilder;
import com.github.mpkorstanje.simmetrics.builders.StringMetricBuilder;
import com.github.mpkorstanje.simmetrics.metrics.CosineSimilarity;
import com.github.mpkorstanje.simmetrics.metrics.EuclideanDistance;
import com.github.mpkorstanje.simmetrics.metrics.OverlapCoefficient;
import com.github.mpkorstanje.simmetrics.metrics.StringMetrics;
import com.github.mpkorstanje.simmetrics.simplifiers.Simplifiers;
import com.github.mpkorstanje.simmetrics.tokenizers.Tokenizer;
import com.github.mpkorstanje.simmetrics.tokenizers.Tokenizers;

/**
 * Examples from README.md
 */
final class ReadMeExample {

	static float example01() {

		String str1 = "This is a sentence. It is made of words";
		String str2 = "This sentence is similar. It has almost the same words";

		StringMetric metric = StringMetrics.cosineSimilarity();

		float result = metric.compare(str1, str2); // 0.4767

		return result;
	}

	static float example02() {

		String str1 = "This is a sentence. It is made of words";
		String str2 = "This sentence is similar. It has almost the same words";

		StringMetric metric = StringMetricBuilder
				.with(new CosineSimilarity<>())
				.simplify(Simplifiers.toLowerCase(Locale.ENGLISH))
				.simplify(Simplifiers.replaceNonWord())
				.tokenize(Tokenizers.whitespace()).build();

		float result = metric.compare(str1, str2); // 0.5720

		return result;
	}

	static float example03() {

		String str1 = "This is a sentence. It is made of words";
		String str2 = "This sentence is similar. It has almost the same words";

		StringDistance metric = StringDistanceBuilder
				.with(new EuclideanDistance<>())
				.simplify(Simplifiers.toLowerCase(Locale.ENGLISH))
				.simplify(Simplifiers.replaceNonWord())
				.tokenize(Tokenizers.whitespace()).build();

		float result = metric.distance(str1, str2); // 3.0000

		return result;
	}

	static float example04() {

		Set<Integer> scores1 = new HashSet<>(asList(1, 1, 2, 3, 5, 8, 11, 19));
		Set<Integer> scores2 = new HashSet<>(asList(1, 2, 4, 8, 16, 32, 64));

		SetMetric<Integer> metric = new OverlapCoefficient<>();

		float result = metric.compare(scores1, scores2); // 0.4285

		return result;
	}


	static List<String> example05() {
		String str1 = "𐇑𐇛𐇜𐇐𐇡";

		Tokenizer tokenizer = input -> {
			List<String> tokens = new ArrayList<>();
			for (int start = 0; start < input.length(); start = input.offsetByCodePoints(start, 1)){
				int end = input.offsetByCodePoints(start, 1);
				tokens.add(input.substring(start, end));
			}
			return tokens;
		};

		List<String> result = tokenizer.tokenizeToList(str1);  // [ 𐇑, 𐇛, 𐇜, 𐇐, 𐇡 ]

		return result;
	}

}
