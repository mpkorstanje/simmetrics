/*-
 * #%L
 * Simmetrics Core
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

package com.github.mpkorstanje.simmetrics.metrics;

import com.github.mpkorstanje.simmetrics.builders.StringMetricBuilder;
import com.github.mpkorstanje.simmetrics.tokenizers.Tokenizers;
import com.github.mpkorstanje.simmetrics.StringMetric;

import static com.github.mpkorstanje.simmetrics.builders.StringMetricBuilder.with;
import static com.github.mpkorstanje.simmetrics.tokenizers.Tokenizers.qGramWithPadding;

/**
 * Utility class for string similarity metrics.
 * <p>
 * Consists of well known string similarity metrics and methods to create string
 * similarity metrics from list- or set metrics. All metrics are setup with
 * sensible defaults, to customize metrics use {@link StringMetricBuilder}.
 * <p>
 * The created similarity metrics are immutable and thread-safe provided all their
 * components are also immutable and thread-safe.
 */
public final class StringMetrics {

	/**
	 * Returns a cosine similarity metric over tokens in a string. The tokens
	 * are created by splitting the string on whitespace.
	 * 
	 * @return a cosine similarity metric
	 * 
	 * @see CosineSimilarity
	 */
	public static StringMetric cosineSimilarity() {
		return with(new CosineSimilarity<>())
			.tokenize(Tokenizers.whitespace())
			.build();
	}

	/**
	 * Returns a block distance similarity metric over tokens in a string. The
	 * tokens are created by splitting the string on whitespace.
	 * 
	 * @return a block distance metric
	 * 
	 * @see BlockDistance
	 */
	public static StringMetric blockDistance() {
		return with(new BlockDistance<>()).tokenize(Tokenizers.whitespace()).build();
	}

	/**
	 * Returns a Damerau-Levenshtein similarity metric over tokens in a string.
	 * The tokens are created by splitting the string on whitespace.
	 * 
	 * @return a Damerau-Levenshtein metric
	 * 
	 * @see DamerauLevenshtein
	 */
	public static StringMetric damerauLevenshtein() {
		return new DamerauLevenshtein();
	}

	/**
	 * Returns a Dice similarity metric over tokens in a string. The tokens are
	 * created by splitting the string on whitespace.
	 * 
	 * @return a dice metric
	 * 
	 * @see Dice
	 */
	public static StringMetric dice() {
		return with(new Dice<>()).tokenize(Tokenizers.whitespace()).build();
	}

	/**
	 * Returns an Euclidean distance similarity metric over tokens in a string.
	 * The tokens are created by splitting the string on whitespace.
	 * 
	 * @return a Euclidean distance similarity metric
	 * 
	 * @see EuclideanDistance
	 */
	public static StringMetric euclideanDistance() {
		return with(new EuclideanDistance<>()).tokenize(Tokenizers.whitespace())
				.build();
	}

	/**
	 * Returns a generalized Jaccard similarity metric over tokens in a string.
	 * The tokens are created by splitting the string on whitespace.
	 * 
	 * @return a generalized Jaccard index metric
	 * 
	 * @see GeneralizedJaccard
	 */
	public static StringMetric generalizedJaccard() {
		return with(new GeneralizedJaccard<>()).tokenize(Tokenizers.whitespace())
				.build();
	}

	/**
	 * Returns an identity string similarity metric. The metric returns 1.0 when
	 * the inputs are equals, and 0.0 when they're not.
	 * 
	 * @return an identity similarity metric
	 * 
	 * @see Identity
	 */
	public static StringMetric identity() {
		return new StringMetric() {

			private final Identity<String> metric = new Identity<>();

			@Override
			public float compare(String a, String b) {
				return metric.compare(a, b);
			}

			@Override
			public String toString() {
				return metric.toString();
			}
		};
	}

	/**
	 * Returns a Jaccard similarity metric over tokens in a string. The
	 * tokens are created by splitting the string on whitespace.
	 * 
	 * @return a Jaccard similarity metric
	 * 
	 * @see Jaccard
	 */
	public static StringMetric jaccard() {
		return with(new Jaccard<>()).tokenize(Tokenizers.whitespace()).build();
	}

	/**
	 * Returns a Jaro string similarity metric.
	 * 
	 * @return a Jaro string similarity metric
	 * 
	 * @see Jaro
	 */
	public static StringMetric jaro() {
		return new Jaro();
	}

	/**
	 * Returns a Jaro-Winkler string similarity metric.
	 * 
	 * @return a Jaro-Winkler string similarity metric
	 * 
	 * @see JaroWinkler
	 */
	public static StringMetric jaroWinkler() {
		return new JaroWinkler();
	}

	/**
	 * Returns a Levenshtein string similarity metric.
	 * 
	 * @return a Levenshtein string similarity metric
	 * 
	 * @see Levenshtein
	 */
	public static StringMetric levenshtein() {
		return new Levenshtein();
	}

	/**
	 * Returns a normalized Level 2 metric over tokens in a string. The
	 * tokens are created by splitting the string on whitespace. The metric
	 * applies Smith-Waterman-Gotoh internally.
	 * 
	 * @return a normalized Monge-Elkan metric
	 * 
	 * @see Level2
	 */
	public static StringMetric level2SmithWatermanGotoh() {
		return with(new Level2(new SmithWatermanGotoh())).tokenize(
				Tokenizers.whitespace()).build();
	}

	/**
	 * Returns a Needleman-Wunch string similarity metric.
	 * 
	 * @return a Needleman-Wunch string similarity metric
	 * 
	 * @see NeedlemanWunch
	 */
	public static StringMetric needlemanWunch() {
		return new NeedlemanWunch();
	}

	/**
	 * Returns an overlap coefficient similarity metric over tokens in a string.
	 * The tokens are created by splitting the string on whitespace.
	 * 
	 * @return an overlap coefficient metric
	 * 
	 * @see OverlapCoefficient
	 */
	public static StringMetric overlapCoefficient() {
		return with(new OverlapCoefficient<>()).tokenize(Tokenizers.whitespace())
				.build();
	}

	/**
	 * Returns a q-grams distance similarity metric. Q-grams distance applies a
	 * block distance similarity similarity metric over all tri-grams in a
	 * string.
	 * 
	 * @return a q-grams distance similarity metric
	 * 
	 * @see BlockDistance
	 */
	public static StringMetric qGramsDistance() {
		return with(new BlockDistance<>()).tokenize(Tokenizers.qGramWithPadding(3))
				.build();
	}

	/**
	 * Returns a Simon White similarity metric. Simon White applies the
	 * quantitative version Dice similarity over tokens in a string. The tokens
	 * are created by splitting the string on whitespace and taking bi-grams of
	 * the created tokens.
	 * <p>
	 * Implementation based on the ideas as outlined in <a
	 * href="http://www.catalysoft.com/articles/StrikeAMatch.html">How to Strike
	 * a Match</a> by <cite>Simon White</cite>.
	 * 
	 * @return a Simon White similarity metric
	 * 
	 * @see SimonWhite
	 */
	public static StringMetric simonWhite() {
		return with(new SimonWhite<>()).tokenize(Tokenizers.whitespace())
				.tokenize(Tokenizers.qGram(2)).build();
	}

	/**
	 * Returns a Smith-Waterman string similarity metric.
	 * 
	 * @return a Smith-Waterman string similarity metric
	 * 
	 * @see SmithWaterman
	 */
	public static StringMetric smithWaterman() {
		return new SmithWaterman();
	}

	/**
	 * Returns a Smith-Waterman-Gotoh string similarity metric.
	 * 
	 * @return a Smith-Waterman-Gotoh string similarity metric
	 * 
	 * @see SmithWatermanGotoh
	 */
	public static StringMetric smithWatermanGotoh() {
		return new SmithWatermanGotoh();
	}

	/**
	 * Returns a string similarity metric that uses the
	 * {@link LongestCommonSubSequence} metric.
	 * 
	 * @return a longest common sub sequence metric
	 */
	public static StringMetric longestCommonSubSequence() {
		return new LongestCommonSubSequence();
	}

	/**
	 * Returns a similarity metric that uses the {@link LongestCommonSubstring}
	 * metric.
	 * 
	 * @return a longest common substring metric
	 */
	public static StringMetric longestCommonSubstring() {
		return new LongestCommonSubstring();
	}


	private StringMetrics() {
		// Utility class.
	}

}
