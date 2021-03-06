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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class StringMetricBuilderExampleTest {
	
	private static final float DELTA = 0.0001f;
	
	@Test
	void example00(){
		assertEquals(0.7812f, StringMetricBuilderExample.example00(), DELTA);
	}
	
	@Test
	void example01(){
		assertEquals(1.0000f, StringMetricBuilderExample.example01(), DELTA);
	}
	
	@Test
	void example02(){
		assertEquals(1.0000f, StringMetricBuilderExample.example02(), DELTA);
	}
	
	@Test
	void example03(){
		assertEquals(0.7777f, StringMetricBuilderExample.example03(), DELTA);
	}
	
	@Test
	void example04(){
		assertEquals(0.8292f, StringMetricBuilderExample.example04(), DELTA);
	}
	
	@Test
	void example05(){
		assertEquals(0.6902f, StringMetricBuilderExample.example05(), DELTA);
	}
	
	@Test
	void example06(){
		assertEquals(0.6902f, StringMetricBuilderExample.example06(), DELTA);
	}
	
	@Test
	void example07(){
		assertEquals(0.6902f, StringMetricBuilderExample.example07(), DELTA);
	}
}
