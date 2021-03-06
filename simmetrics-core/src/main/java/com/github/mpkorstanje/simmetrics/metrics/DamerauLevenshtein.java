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

package com.github.mpkorstanje.simmetrics.metrics;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Math.max;
import static com.github.mpkorstanje.simmetrics.metrics.Math.max;
import static com.github.mpkorstanje.simmetrics.metrics.Math.min;
import static com.github.mpkorstanje.simmetrics.metrics.Unicode.codePointLength;

import com.github.mpkorstanje.simmetrics.StringDistance;
import com.github.mpkorstanje.simmetrics.StringMetric;

/**
 * Calculates the Damerau-Levenshtein similarity and distance measure between
 * two strings.
 * <p>
 * Insert/delete, substitute and transpose operations can be weighted. When the
 * cost for substitution and/or transposition are zero Damerau-Levenshtein does
 * not satisfy the coincidence property.
 * <p>
 * This class is immutable and thread-safe.
 *
 * @see <a
 * href="https://en.wikipedia.org/wiki/Damerau%E2%80%93Levenshtein_distance">Wikipedia
 * - Damerau-Levenshtein distance</a>
 * @see Levenshtein
 */
public final class DamerauLevenshtein implements StringMetric, StringDistance {

	private final float maxCost;
	private final float insertDelete;
	private final float substitute;
	private final float transpose;

	/**
	 * Constructs a new Damerau-Levenshtein metric.
	 */
	public DamerauLevenshtein() {
		this(1.0f, 1.0f, 1.0f);
	}

	/**
	 * Constructs a new weighted Damerau-Levenshtein metric. When the cost for
	 * substitution and/or transposition are zero Damerau-Levenshtein does not
	 * satisfy the coincidence property.
	 *
	 * @param insertDelete positive non-zero cost of an insert or deletion operation
	 * @param substitute   positive cost of a substitute operation
	 * @param transpose    positive cost of a transpose operation
	 */
	public DamerauLevenshtein(float insertDelete, float substitute, float transpose) {
		checkArgument(insertDelete > 0);
		checkArgument(substitute >= 0);
		checkArgument(transpose >= 0);

		this.maxCost = max(insertDelete, substitute, transpose);
		this.insertDelete = insertDelete;
		this.substitute = substitute;
		this.transpose = transpose;
	}

	@Override
	public float compare(final String a, final String b) {
		if (a.isEmpty() && b.isEmpty()) {
			return 1.0f;
		}

		return 1.0f - (distance(a, b) / (maxCost * max(codePointLength(a), codePointLength(b))));
	}

	@Override
	public float distance(final String s, final String t) {

		if (s.isEmpty())
			return codePointLength(t) * insertDelete;
		if (t.isEmpty())
			return codePointLength(s) * insertDelete;
		if (s.equals(t))
			return 0;

		final int tLength = codePointLength(t);
		final int sLength = codePointLength(s);

		float[] swap;
		float[] v0 = new float[tLength + 1];
		float[] v1 = new float[tLength + 1];
		float[] v2 = new float[tLength + 1];

		// initialize v1 (the previous row of distances)
		// this row is A[0][i]: edit distance for an empty s
		// the distance is just the number of characters to delete from t
		for (int i = 0; i < v1.length; i++) {
			v1[i] = i * insertDelete;
		}

		for (int i = 0; i < sLength; i++) {

			// first element of v2 is A[i+1][0]
			// edit distance is delete (i+1) chars from s to match empty t
			v2[0] = (i + 1) * insertDelete;

			for (int j = 0; j < tLength; j++) {
				final boolean match =
						j > 0 && i > 0
								&& s.codePointAt(i - 1) == t.codePointAt(j)
								&& s.codePointAt(i) == t.codePointAt(j - 1);
				if (match) {
					v2[j + 1] = min(
							v2[j] + insertDelete,
							v1[j + 1] + insertDelete,
							v1[j] + (s.codePointAt(i) == t.codePointAt(j) ? 0.0f : substitute),
							v0[j - 1] + transpose
					);
				} else {
					v2[j + 1] = min(
							v2[j] + insertDelete,
							v1[j + 1] + insertDelete,
							v1[j] + (s.codePointAt(i) == t.codePointAt(j) ? 0.0f : substitute)
					);
				}
			}

			swap = v0;
			v0 = v1;
			v1 = v2;
			v2 = swap;
		}

		// latest results was in v2 which was swapped to v1
		return v1[tLength];
	}

	@Override
	public String toString() {
		return "DamerauLevenshtein [insertDelete=" + insertDelete
				+ ", substitute=" + substitute + ", transpose=" + transpose
				+ "]";
	}

}
