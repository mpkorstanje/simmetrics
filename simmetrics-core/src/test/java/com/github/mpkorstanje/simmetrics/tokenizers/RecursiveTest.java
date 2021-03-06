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
package com.github.mpkorstanje.simmetrics.tokenizers;

import com.github.mpkorstanje.simmetrics.tokenizers.Tokenizers.Recursive;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

class RecursiveTest extends TokenizerTest {
	
	@Override
	protected T[] getTests() {
		return new T[] {
					new T("Mouse", 
							"Mo", "ou", "ou", "us", "ou", "us", "us","se"), 
					new T("")
				};
	}

	@Override
	protected Tokenizer getTokenizer() {
		return new Recursive(asList(Tokenizers.qGram(5), Tokenizers.qGram(4), Tokenizers.qGram(3), Tokenizers.qGram(2)));
	}
	
	@Test
	void shouldThrowForListContainingNull() {
		assertThrows(NullPointerException.class, () -> new Recursive(asList(Tokenizers.whitespace(), null)));
	}
	
	public void shouldCopyListOfTokenizers() {
		List<Tokenizer> tokenizerList = Arrays.asList(Tokenizers.whitespace());
		Recursive tokenizer = new Recursive(tokenizerList);
		assertThat(tokenizer.getTokenizers(), is(sameInstance(tokenizer.getTokenizers())));
		assertThat(tokenizer.getTokenizers(), is(not(sameInstance(tokenizerList))));
	}
	

}
