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
package com.github.mpkorstanje.simmetrics.simplifiers;

import static java.text.Normalizer.normalize;
import static java.text.Normalizer.Form.NFC;
import static java.text.Normalizer.Form.NFD;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static com.github.mpkorstanje.simmetrics.simplifiers.Simplifiers.chain;
import static com.github.mpkorstanje.simmetrics.simplifiers.Simplifiers.replaceNonWord;
import static com.github.mpkorstanje.simmetrics.simplifiers.Simplifiers.toLowerCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.mpkorstanje.simmetrics.simplifiers.Simplifiers.ChainSimplifier;
import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
class SimplifiersTest {

	static final class Normalize extends SimplifierTest {

		@Override
		protected Simplifier getSimplifier() {
			return Simplifiers.normalize(NFC);
		}

		@Override
		protected T[] getTests() {
			return new T[] { 
					new T("", ""),
					new T(normalize("é", NFD), normalize("é", NFC))
			};
		}
	}
	
	static final class WithChainSimplifier extends SimplifierTest {

		@Override
		protected Simplifier getSimplifier() {
			return Simplifiers.chain(toSheep(), Simplifiers.chain(reverseCapitalized(), toGoat()),
					toUpperCase());
		}

		@Override
		protected T[] getTests() {
			return new T[] { new T("a", "A SHEEP GOAT"),
					new T("A cat", "PEEHS TAC A GOAT"),
					new T("", " SHEEP GOAT") };
		}
		
		public void shouldCopyListOfSimplifier() {
			List<Simplifier> simplifiersList = Arrays.asList(Simplifiers.toLowerCase());
			ChainSimplifier simplifier = new ChainSimplifier(simplifiersList);
			assertThat(simplifier.getSimplifiers(), is(sameInstance(simplifier.getSimplifiers())));
			assertThat(simplifier.getSimplifiers(), is(not(sameInstance(simplifiersList))));
		}

	}

	static final class WithEmpty extends SimplifierTest {

		@Override
		protected Simplifier getSimplifier() {
			return Simplifiers.chain(new ArrayList<>());
		}

		@Override
		protected T[] getTests() {
			return new T[] { new T("a Sheep Goat", "a Sheep Goat"),
					new T("", "") };
		}

	}

	static final class WithTwo extends SimplifierTest {

		@Override
		protected Simplifier getSimplifier() {
			return Simplifiers.chain(toSheep(), toGoat());
		}

		@Override
		protected T[] getTests() {
			return new T[] { new T("a", "a Sheep Goat"),
					new T("", " Sheep Goat") };
		}

	}

	static final class RemoveDiacritics extends SimplifierTest {

		@Override
		protected Simplifier getSimplifier() {
			return Simplifiers.removeDiacritics();
		}

		@Override
		protected T[] getTests() {
			return new T[] {
					new T("Chilpéric II son of Childeric II",
							"Chilperic II son of Childeric II"),
					new T("The 11th Hour", "The 11th Hour"), new T("", ""), };
		}

	}

	static final class ToUpperCase extends SimplifierTest {

		@Override
		protected Simplifier getSimplifier() {
			return Simplifiers.toUpperCase();
		}

		@Override
		protected T[] getTests() {
			return new T[] { new T("A", "A"), new T("a", "A"), new T("", "") };
		}

	}

	static final class ToLowerCase extends SimplifierTest {

		@Override
		protected Simplifier getSimplifier() {
			return Simplifiers.toLowerCase();
		}

		@Override
		protected T[] getTests() {
			return new T[] { new T("A", "a"), new T("a", "a"), new T("", "") };
		}

	}

	static final class ReplaceNonWordCharacters extends SimplifierTest {

		@Override
		protected Simplifier getSimplifier() {
			return Simplifiers.replaceNonWord();
		}

		@Override
		protected T[] getTests() {
			return new T[] { new T("##", "  "),
					new T("The ##th Hour", "The   th Hour"), new T("", "") };
		}

	}
	
	

	static final class ReplaceRegex extends SimplifierTest {

		@Override
		protected Simplifier getSimplifier() {
			return Simplifiers.replaceAll("[a-z]+", "@");
		}

		@Override
		protected T[] getTests() {
			return new T[] { new T("##", "##"),
					new T("The ##th Hour", "T@ ##@ H@"), new T("", "") };
		}

	}

	static final class RemoveRegex extends SimplifierTest {

		@Override
		protected Simplifier getSimplifier() {
			return Simplifiers.removeAll("[a-z]+");
		}

		@Override
		protected T[] getTests() {
			return new T[] { new T("##", "##"),
					new T("The ##th Hour", "T ## H"), new T("", "") };
		}

	}

	static Simplifier reverseCapitalized() {
		return new Simplifier() {

			@Override
			public String simplify(String input) {
				if (!Character.isUpperCase(input.charAt(0))) {
					return input;
				}

				return new StringBuilder(input).reverse().toString();
			}

			@Override
			public String toString() {
				return "reverseCapitalized";
			}
		};
	}

	static Simplifier toGoat() {
		return new Simplifier() {

			@Override
			public String simplify(String input) {
				return input + " Goat";
			}

			@Override
			public String toString() {
				return "Goat";
			}
		};
	}

	static Simplifier toSheep() {
		return new Simplifier() {

			@Override
			public String simplify(String input) {
				return input + " Sheep";
			}

			@Override
			public String toString() {
				return "Sheep";
			}
		};
	}

	static Simplifier toUpperCase() {
		return new Simplifier() {

			@Override
			public String simplify(String input) {
				return input.toUpperCase();
			}

			@Override
			public String toString() {
				return "toUpperCase";
			}
		};

	}

	static final class ShouldThrowFor  {

		@Test
		void chainWithListContainingNull() {
			assertThrows(IllegalArgumentException.class, () -> Simplifiers
                    .chain(Arrays.asList(Simplifiers.toLowerCase(), null, Simplifiers.removeNonWord())));
		}

		@Test
		void chainWithNull() {
			assertThrows(IllegalArgumentException.class, () -> Simplifiers.chain((Simplifier) null));
		}

		@Test
		void chainWithNullInVarArg() {
			assertThrows(IllegalArgumentException.class, () -> Simplifiers
                    .chain(Simplifiers.toLowerCase(), null, Simplifiers.removeNonWord()));
		}

		@Test
		void chainWithSingle() {
			Simplifier lower = Simplifiers.toLowerCase();
			assertSame(lower, Simplifiers.chain(lower));
		}

		@Test
		void chainWithSingletonList() {
			Simplifier lower = Simplifiers.toLowerCase();
			assertSame(lower, Simplifiers.chain(singletonList(lower)));
		}
	}
}
