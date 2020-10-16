/*-
 * #%L
 * Simmetrics Core
 * %%
 * Copyright (C) 2014 - 2020 Simmetrics Authors
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
package org.simmetrics;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.List;
import java.util.function.Predicate;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public final class PredicateMatcher extends TypeSafeDiagnosingMatcher<Predicate<String>> {

	private final boolean accept;
	private final List<String> values;

	private PredicateMatcher(boolean accept, List<String> values) {
		this.accept = accept;
		this.values = values;
	}

	@Override
	protected boolean matchesSafely(Predicate<String> item, Description mismatchDescription) {

		List<String> filtered;

		if(accept){
			mismatchDescription.appendText("but accepted: ");
			filtered = values.stream().filter(item).collect(toList());
		} else {
			mismatchDescription.appendText("but rejected: ");
			filtered = values.stream().filter(item.negate()).collect(toList());
		}

		mismatchDescription.appendValueList("[", ",", "]", filtered);
		return filtered.equals(values);
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("A predicate ");
		if (accept) {
			description.appendText("accepts: ");
		} else {
			description.appendText("rejects: ");
		}
		description.appendValueList("[", ",", "]", values);
	}

	public static PredicateMatcher accepts(String... values) {
		return new PredicateMatcher(true, asList(values));
	}

	public static PredicateMatcher rejects(String... values) {
		return new PredicateMatcher(false, asList(values));
	}
}
