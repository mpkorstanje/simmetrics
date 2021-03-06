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

package com.github.mpkorstanje.simmetrics.metrics;

import org.junit.jupiter.api.Test;
import com.github.mpkorstanje.simmetrics.Metric;
import com.github.mpkorstanje.simmetrics.StringMetricTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

final class StringMetricsTest {

	public static class CreateCosineSimilarity extends StringMetricTest {

		@Override
		protected Metric<String> getMetric() {
			return StringMetrics.cosineSimilarity();
		}

		@Override
		protected boolean toStringIncludesSimpleClassName() {
			return false;
		}

		@Override
		protected T[] getTests() {
			return new T[]{
					new T(0.5000f, "test string1", "test string2"),
					new T(0.5000f, "test string1", "test string2"),
					new T(0.7071f, "test", "test string2"),
					new T(0.0000f, "", "test string2"),
					new T(0.7500f, "aaa bbb ccc ddd", "aaa bbb ccc eee"),
					new T(0.7500f, "a b c d", "a b c e"),};
		}

	}

	public static class CreateDiceSimlarity extends StringMetricTest {

		@Override
		protected Metric<String> getMetric() {
			return StringMetrics.dice();
		}

		@Override
		protected boolean toStringIncludesSimpleClassName() {
			return false;
		}

		@Override
		protected T[] getTests() {
			return new T[]{
					new T(0.5000f, "test string1", "test string2"),
					new T(0.6666f, "test", "test string2"),
					new T(0.0000f, "", "test string2"),
					new T(0.7500f, "aaa bbb ccc ddd", "aaa bbb ccc eee"),
					new T(0.7500f, "a b c d", "a b c e"),};
		}

	}

	public static class CreateEuclideanMetric extends StringMetricTest {

		@Override
		protected Metric<String> getMetric() {
			return StringMetrics.euclideanDistance();
		}

		@Override
		protected boolean toStringIncludesSimpleClassName() {
			return false;
		}

		@Override
		protected T[] getTests() {
			return new T[]{
					new T(0.5000f, "test string1", "test string2"),
					new T(0.5527f, "test", "test string2"),
					new T(0.2928f, "", "test string2"),
					new T(0.7500f, "aaa bbb ccc ddd", "aaa bbb ccc eee"),
					new T(0.7500f, "a b c d", "a b c e"),};
		}

	}

	public static class CreateJaccard extends StringMetricTest {

		@Override
		protected Metric<String> getMetric() {
			return StringMetrics.jaccard();
		}

		@Override
		protected boolean toStringIncludesSimpleClassName() {
			return false;
		}

		@Override
		protected T[] getTests() {
			return new T[]{
					new T(0.3333f, "test string1", "test string2"),
					new T(0.5000f, "test", "test string2"),
					new T(0.0000f, "", "test string2"),
					new T(0.6000f, "aaa bbb ccc ddd", "aaa bbb ccc eee"),
					new T(0.6000f, "a b c d", "a b c e"),};
		}

	}

	public static class CreateGeneralizedJaccard extends StringMetricTest {

		@Override
		protected Metric<String> getMetric() {
			return StringMetrics.generalizedJaccard();
		}

		@Override
		protected boolean toStringIncludesSimpleClassName() {
			return false;
		}

		@Override
		protected T[] getTests() {
			return new T[]{
					new T(0.3333f, "test string1", "test string2"),
					new T(0.5000f, "test", "test string2"),
					new T(0.0000f, "", "test string2"),
					new T(0.6000f, "aaa bbb ccc ddd", "aaa bbb ccc eee"),
					new T(0.6000f, "a b c d", "a b c e"),};
		}

	}

	public static class CreateLevel2 extends StringMetricTest {

		@Override
		protected Metric<String> getMetric() {
			return StringMetrics.level2SmithWatermanGotoh();
		}

		@Override
		protected boolean toStringIncludesSimpleClassName() {
			return false;
		}

		@Override
		protected T[] getTests() {
			return new T[]{
					new T(0.9286f, "test string1", "test string2"),
					new T(0.8660f, "test", "test string2"),
					new T(0.0000f, "", "test string2"),
					new T(0.7500f, "aaa bbb ccc ddd", "aaa bbb ccc eee"),
					new T(0.7500f, "a b c d", "a b c e"),};
		}

		@Override
		protected boolean satisfiesSubadditivity() {
			return false;
		}

	}

	public static class CreateOverlapCoefficient extends StringMetricTest {
		@Override
		protected Metric<String> getMetric() {
			return StringMetrics.overlapCoefficient();
		}

		@Override
		protected boolean toStringIncludesSimpleClassName() {
			return false;
		}

		@Override
		protected T[] getTests() {
			return new T[]{
					new T(0.5000f, "test string1", "test string2"),
					new T(1.0000f, "test", "test string2"),
					new T(0.0000f, "", "test string2"),
					new T(0.7500f, "aaa bbb ccc ddd", "aaa bbb ccc eee"),
					new T(0.7500f, "a b c d", "a b c e"),};
		}

		@Override
		protected boolean satisfiesCoincidence() {
			return false;
		}

	}

	public static class CreateQGramsMetric extends StringMetricTest {

		@Override
		protected Metric<String> getMetric() {
			return StringMetrics.qGramsDistance();
		}

		@Override
		protected boolean toStringIncludesSimpleClassName() {
			return false;
		}

		@Override
		protected T[] getTests() {
			return new T[]{
					new T(0.7857f, "test string1", "test string2"),
					new T(0.3999f, "test", "test string2"),
					new T(0.0000f, "", "test string2"),
					new T(0.7058f, "aaa bbb ccc ddd", "aaa bbb ccc eee"),
					new T(0.6666f, "a b c d", "a b c e"),};
		}

	}

	public static class CreateSimonWhite extends StringMetricTest {

		@Override
		protected Metric<String> getMetric() {
			return StringMetrics.simonWhite();
		}

		@Override
		protected boolean toStringIncludesSimpleClassName() {
			return false;
		}

		@Override
		protected T[] getTests() {
			return new T[]{
					new T(0.8889f, "test string1", "test string2"),
					new T(0.5000f, "test", "test string2"),
					new T(0.0000f, "", "test string2"),
					new T(0.7500f, "aaa bbb ccc ddd", "aaa bbb ccc eee")};
		}

	}

	public static class CreateIdentity extends StringMetricTest {

		@Override
		protected Metric<String> getMetric() {
			return StringMetrics.identity();
		}

		@Override
		protected T[] getTests() {
			return new T[]{
					new T(1.0000f, "test string1", "test string1"),
					new T(0.0000f, "test string1", "test string2"),
					new T(0.0000f, "test", "test string2"),
					new T(0.0000f, "", "test string2"),
					new T(0.0000f, "aaa bbb ccc ddd", "aaa bbb ccc eee"),
					new T(0.0000f, "a b c d", "a b c e"),};
		}

	}

	public static class Utilities {
		//TODO: Test
		@Test
		void blockDistance() {
			assertNotNull(StringMetrics.blockDistance());
		}

	}

	public static class CreateStringMetrics {
		@Test
		void damerauLevenshtein() {
			assertNotNull(StringMetrics.damerauLevenshtein());
		}

		@Test
		void jaro() {
			assertNotNull(StringMetrics.jaro());
		}

		@Test
		void jaroWinkler() {
			assertNotNull(StringMetrics.jaroWinkler());
		}

		@Test
		void levenshtein() {
			assertNotNull(StringMetrics.levenshtein());
		}

		@Test
		void needlemanWunch() {
			assertNotNull(StringMetrics.needlemanWunch());
		}

		@Test
		void smithWaterman() {
			assertNotNull(StringMetrics.smithWaterman());

		}

		@Test
		void smithWatermanGotoh() {
			assertNotNull(StringMetrics.smithWatermanGotoh());
		}

		@Test
		void longestCommonSubsequence() {
			assertNotNull(StringMetrics.longestCommonSubSequence());
		}

		@Test
		void longestCommonSubstring() {
			assertNotNull(StringMetrics.longestCommonSubstring());
		}
	}

}
