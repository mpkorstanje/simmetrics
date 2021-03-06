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

package com.github.mpkorstanje.simmetrics.metrics.costfunctions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static com.github.mpkorstanje.simmetrics.matchers.ImplementsToString.implementsToString;
import static com.github.mpkorstanje.simmetrics.matchers.ToStringContainsSimpleClassName.toStringContainsSimpleClassName;

import com.github.mpkorstanje.simmetrics.metrics.functions.Gap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public abstract class GapCostTest {

	protected static class T {
		protected final float cost;
		protected final String string;
		protected final int index1;
		protected final int index2;

		public T(float cost, String string, int index1, int index2) {
			super();
			this.cost = cost;
			this.string = string;
			this.index1 = index1;
			this.index2 = index2;
		}
	}

	private static final float DEFAULT_DELTA = 0.0001f;
	private float delta;

	protected Gap cost;

	protected abstract Gap getCost();

	protected abstract T[] getTests();

	@BeforeEach
	public void setUp() throws Exception {
		cost = getCost();
		delta = getDelta();
	}

	protected float getDelta() {
		return DEFAULT_DELTA;
	}

	@Test
	void value() {
		for (T t : getTests()) {

			float actuall = cost.value(t.index1, t.index2);

			String costMessage = "Cost %.3f must fall within [%.3f - %.3f] range";
			costMessage = String.format(costMessage, actuall, cost.min(),
					cost.max());
			assertTrue(cost.min() <= actuall
					&& actuall <= cost.max(), costMessage);

			String message = String.format("\"%s\" vs \"%s\"",
					t.string.charAt(t.index1), t.string.charAt(t.index2));
			assertEquals(t.cost, actuall, delta, message);
		}
	}

	public void generateTest() {
		for (T t : getTests()) {
			float actuall = cost.value(t.index1, t.index2);

			String message = String.format("new T(%.4ff, testString, %s, %s),",
					actuall, t.index1, t.index2);
			System.out.println(message);
		}
	}

	@Test
	final void shouldImplementToString() {
		assertThat(cost, implementsToString());
		assertThat(cost, toStringContainsSimpleClassName());
	}

	protected static void assertToStringContains(Gap metric,
			String content) {
		String string = metric.toString();
		String message = String.format("%s must contain %s ", string, content);

		assertTrue(message.contains(content), message);
	}

}
