/*-
 * #%L
 * Simmetrics - Core
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

import com.github.mpkorstanje.simmetrics.ListMetric;
import com.github.mpkorstanje.simmetrics.StringMetric;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Math.max;
import static java.lang.Math.sqrt;

/**
 * Calculates the normalized Level2 distance (similarity) over two strings.
 * The normalized Level2 distance is used because the the unnormalized
 * distance is not symmetric.
 * <p>
 * <code>
 * similarity(a,b) = sqrt(level2(a,b) * level2(b,a))
 * monge-elkan(a,b) = average( for s in a | max( for q in b | metric(s,q))
 * </code>
 * <p>
 *
 * @see <a href="https://www.aaai.org/Papers/KDD/1996/KDD96-044.pdf">The field
 * Matching problem; Algorithms and applications</a>
 * <p>
 * This class is immutable and thread-safe.
 */
public final class Level2 implements ListMetric<String> {

	private final StringMetric metric;

	/**
	 * Constructs a Level 2 metric with metric.
	 *
	 * @param metric metric to use
	 */
	public Level2(final StringMetric metric) {
		this.metric = metric;
	}

	@Override
	public float compare(List<String> a, List<String> b) {
		checkArgument(!a.contains(null), "a may not not contain null");
		checkArgument(!b.contains(null), "b may not not contain null");

		if (a.isEmpty() && b.isEmpty()) {
			return 1.0f;
		}

		if (a.isEmpty() || b.isEmpty()) {
			return 0.0f;
		}

		// calculates normalized_similarity(a,b)
		return (float) sqrt(level2(a, b) * level2(b, a));
	}

	private float level2(List<String> a, List<String> b) {
		// calculates average( for s in a | max( for q in b | metric(s,q))
		float sum = 0.0f;

		for (String s : a) {
			float max = 0.0f;
			for (String q : b) {
				max = max(max, metric.compare(s, q));
			}
			sum += max;
		}
		return sum / a.size();
	}

	@Override
	public String toString() {
		return "Level2 [metric=" + metric + "]";
	}

}
