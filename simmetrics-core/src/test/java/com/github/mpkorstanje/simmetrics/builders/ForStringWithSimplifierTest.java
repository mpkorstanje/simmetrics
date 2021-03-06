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
package com.github.mpkorstanje.simmetrics.builders;

import static org.junit.jupiter.api.Assertions.assertSame;

import com.github.mpkorstanje.simmetrics.Metric;
import com.github.mpkorstanje.simmetrics.StringMetricTest;
import com.github.mpkorstanje.simmetrics.builders.StringMetrics.ForStringWithSimplifier;
import com.github.mpkorstanje.simmetrics.metrics.Identity;
import com.github.mpkorstanje.simmetrics.simplifiers.Simplifier;
import com.github.mpkorstanje.simmetrics.simplifiers.Simplifiers;
import org.junit.jupiter.api.Test;

class ForStringWithSimplifierTest extends StringMetricTest {

	private final Metric<String> metric = new Identity<>();
	private final Simplifier simplifier = Simplifiers.toLowerCase();
	
	@Override
	protected ForStringWithSimplifier getMetric() {
		return new ForStringWithSimplifier(metric, simplifier);
	}
	
	@Override
	protected T[] getTests() {
		return new T[]{
				new T(0.0f, "To repeat repeat is to repeat", ""),
				new T(1.0f, "To repeat repeat is to repeat", "to repeat repeat is to repeat"),
		};
	}
	
	@Override
	protected boolean satisfiesCoincidence() {
		return false;
	}
	
	@Override
	protected boolean toStringIncludesSimpleClassName() {
		return false;
	}
	
	@Test
	void shouldReturnMetric(){
		assertSame(metric, getMetric().getMetric());
	}
	
	@Test
	void shouldReturnSimplifier(){
		assertSame(simplifier, getMetric().getSimplifier());
	}
}
