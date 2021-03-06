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

import java.util.List;
import java.util.Set;

import com.github.mpkorstanje.simmetrics.Metric;
import com.github.mpkorstanje.simmetrics.StringMetric;
import com.github.mpkorstanje.simmetrics.simplifiers.Simplifier;
import com.github.mpkorstanje.simmetrics.tokenizers.Tokenizer;

import com.google.common.collect.Multiset;

import static java.util.Objects.requireNonNull;
import static com.github.mpkorstanje.simmetrics.simplifiers.Simplifiers.chain;

final class StringMetrics {

	public static StringMetric create(Metric<String> metric) {
		if (metric instanceof StringMetric) {
			return (StringMetric) metric;
		}

		return new ForString(metric);
	}


	public static StringMetric create(Metric<String> metric, Simplifier simplifier) {
		if (metric instanceof ForString) {
			ForString forString = (ForString) metric;
			return new ForStringWithSimplifier(forString.getMetric(),
					simplifier);
		} else if (metric instanceof ForStringWithSimplifier) {
			ForStringWithSimplifier fsws = (ForStringWithSimplifier) metric;
			return new ForStringWithSimplifier(fsws.getMetric(),
					chain(simplifier, fsws.getSimplifier()));
		} else if (metric instanceof ForList) {
			ForList fl = (ForList) metric;
			return new ForListWithSimplifier(fl.getMetric(), simplifier, fl.getTokenizer());
		} else if (metric instanceof ForListWithSimplifier) {
			ForListWithSimplifier fl = (ForListWithSimplifier) metric;
			return new ForListWithSimplifier(fl.getMetric(),
					chain(simplifier, fl.getSimplifier()), fl.getTokenizer());
		} else if (metric instanceof ForSet) {
			ForSet fl = (ForSet) metric;
			return createForSetMetric(fl.getMetric(), simplifier,
					fl.getTokenizer());
		} else if (metric instanceof ForSetWithSimplifier) {
			ForSetWithSimplifier fl = (ForSetWithSimplifier) metric;
			return createForSetMetric(fl.getMetric(),
					chain(simplifier, fl.getSimplifier()), fl.getTokenizer());
		}

		return new ForStringWithSimplifier(metric, simplifier);
	}


	public static StringMetric createForListMetric(Metric<List<String>> metric, Simplifier simplifier,
	                                               Tokenizer tokenizer) {
		return new ForListWithSimplifier(metric, simplifier, tokenizer);

	}


	public static StringMetric createForListMetric(Metric<List<String>> metric, Tokenizer tokenizer) {
		return new ForList(metric, tokenizer);
	}


	public static StringMetric createForSetMetric(Metric<Set<String>> metric, Simplifier simplifier,
	                                              Tokenizer tokenizer) {
		return new ForSetWithSimplifier(metric, simplifier, tokenizer);
	}


	public static StringMetric createForSetMetric(Metric<Set<String>> metric, Tokenizer tokenizer) {
		return new ForSet(metric, tokenizer);
	}


	public static StringMetric createForMultisetMetric(Metric<Multiset<String>> metric, Simplifier simplifier,
	                                                   Tokenizer tokenizer) {
		return new ForMultisetWithSimplifier(metric, simplifier, tokenizer);
	}


	public static StringMetric createForMultisetMetric(Metric<Multiset<String>> metric, Tokenizer tokenizer) {
		return new ForMultiset(metric, tokenizer);
	}


	private StringMetrics() {
		// Utility class.
	}


	static final class ForList implements StringMetric {
		private final Metric<List<String>> metric;
		private final Tokenizer tokenizer;

		ForList(Metric<List<String>> metric, Tokenizer tokenizer) {

			requireNonNull(metric);
			requireNonNull(tokenizer);

			this.metric = metric;
			this.tokenizer = tokenizer;
		}

		@Override
		public float compare(String a, String b) {
			return metric.compare(tokenizer.tokenizeToList(a),
					tokenizer.tokenizeToList(b));
		}

		Metric<List<String>> getMetric() {
			return metric;
		}

		Tokenizer getTokenizer() {
			return tokenizer;
		}

		@Override
		public String toString() {
			return metric + " [" + tokenizer + "]";
		}
	}

	static final class ForListWithSimplifier implements StringMetric {
		private final Metric<List<String>> metric;
		private final Simplifier simplifier;
		private final Tokenizer tokenizer;

		ForListWithSimplifier(Metric<List<String>> metric,
		                      Simplifier simplifier, Tokenizer tokenizer) {

			requireNonNull(metric);
			requireNonNull(simplifier);
			requireNonNull(tokenizer);

			this.metric = metric;
			this.simplifier = simplifier;
			this.tokenizer = tokenizer;
		}

		@Override
		public float compare(String a, String b) {
			return metric.compare(
					tokenizer.tokenizeToList(simplifier.simplify(a)),
					tokenizer.tokenizeToList(simplifier.simplify(b)));
		}

		Metric<List<String>> getMetric() {
			return metric;
		}

		Simplifier getSimplifier() {
			return simplifier;
		}

		Tokenizer getTokenizer() {
			return tokenizer;
		}

		@Override
		public String toString() {
			return metric + " [" + simplifier + " -> " + tokenizer + "]";
		}
	}

	static final class ForSet implements StringMetric {

		private final Metric<Set<String>> metric;
		private final Tokenizer tokenizer;

		ForSet(Metric<Set<String>> metric, Tokenizer tokenizer) {
			requireNonNull(metric);
			requireNonNull(tokenizer);

			this.metric = metric;
			this.tokenizer = tokenizer;
		}

		@Override
		public float compare(String a, String b) {
			return metric.compare(tokenizer.tokenizeToSet(a),
					tokenizer.tokenizeToSet(b));
		}

		Metric<Set<String>> getMetric() {
			return metric;
		}

		Tokenizer getTokenizer() {
			return tokenizer;
		}

		@Override
		public String toString() {
			return metric + " [" + tokenizer + "]";
		}

	}

	static final class ForSetWithSimplifier implements StringMetric {

		private final Metric<Set<String>> metric;
		private final Simplifier simplifier;
		private final Tokenizer tokenizer;

		ForSetWithSimplifier(Metric<Set<String>> metric, Simplifier simplifier,
		                     Tokenizer tokenizer) {
			requireNonNull(metric);
			requireNonNull(simplifier);
			requireNonNull(tokenizer);

			this.metric = metric;
			this.simplifier = simplifier;
			this.tokenizer = tokenizer;
		}

		@Override
		public float compare(String a, String b) {
			return metric.compare(
					tokenizer.tokenizeToSet(simplifier.simplify(a)),
					tokenizer.tokenizeToSet(simplifier.simplify(b)));
		}

		Metric<Set<String>> getMetric() {
			return metric;
		}

		Simplifier getSimplifier() {
			return simplifier;
		}

		Tokenizer getTokenizer() {
			return tokenizer;
		}

		@Override
		public String toString() {
			return metric + " [" + simplifier + " -> " + tokenizer + "]";
		}

	}

	static final class ForMultiset implements StringMetric {

		private final Metric<Multiset<String>> metric;
		private final Tokenizer tokenizer;

		ForMultiset(Metric<Multiset<String>> metric, Tokenizer tokenizer) {
			requireNonNull(metric);
			requireNonNull(tokenizer);

			this.metric = metric;
			this.tokenizer = tokenizer;
		}

		@Override
		public float compare(String a, String b) {
			return metric.compare(tokenizer.tokenizeToMultiset(a),
					tokenizer.tokenizeToMultiset(b));
		}

		Metric<Multiset<String>> getMetric() {
			return metric;
		}

		Tokenizer getTokenizer() {
			return tokenizer;
		}

		@Override
		public String toString() {
			return metric + " [" + tokenizer + "]";
		}

	}

	static final class ForMultisetWithSimplifier implements StringMetric {

		private final Metric<Multiset<String>> metric;
		private final Simplifier simplifier;
		private final Tokenizer tokenizer;

		ForMultisetWithSimplifier(Metric<Multiset<String>> metric,
		                          Simplifier simplifier, Tokenizer tokenizer) {
			requireNonNull(metric);
			requireNonNull(simplifier);
			requireNonNull(tokenizer);

			this.metric = metric;
			this.simplifier = simplifier;
			this.tokenizer = tokenizer;
		}

		@Override
		public float compare(String a, String b) {
			return metric.compare(
					tokenizer.tokenizeToMultiset(simplifier.simplify(a)),
					tokenizer.tokenizeToMultiset(simplifier.simplify(b)));
		}

		Metric<Multiset<String>> getMetric() {
			return metric;
		}

		Simplifier getSimplifier() {
			return simplifier;
		}

		Tokenizer getTokenizer() {
			return tokenizer;
		}

		@Override
		public String toString() {
			return metric + " [" + simplifier + " -> " + tokenizer + "]";
		}

	}

	static final class ForString implements StringMetric {
		private final Metric<String> metric;

		ForString(Metric<String> metric) {
			this.metric = metric;
		}

		@Override
		public float compare(String a, String b) {
			return metric.compare(a, b);
		}

		@Override
		public String toString() {
			return metric.toString();
		}

		Metric<String> getMetric() {
			return metric;
		}

	}

	static final class ForStringWithSimplifier implements StringMetric {

		private final Metric<String> metric;

		private final Simplifier simplifier;

		ForStringWithSimplifier(Metric<String> metric, Simplifier simplifier) {
			requireNonNull(metric);
			requireNonNull(simplifier);

			this.metric = metric;
			this.simplifier = simplifier;
		}

		@Override
		public float compare(String a, String b) {
			return metric.compare(simplifier.simplify(a),
					simplifier.simplify(b));
		}

		Metric<String> getMetric() {
			return metric;
		}

		Simplifier getSimplifier() {
			return simplifier;
		}

		@Override
		public String toString() {
			return metric + " [" + simplifier + "]";
		}

	}

}
