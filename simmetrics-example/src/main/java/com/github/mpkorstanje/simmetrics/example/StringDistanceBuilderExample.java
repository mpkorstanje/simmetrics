/*-
 * #%L
 * Simmetrics Examples
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

import static com.github.mpkorstanje.simmetrics.builders.StringDistanceBuilder.with;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import com.github.mpkorstanje.simmetrics.StringDistance;
import com.github.mpkorstanje.simmetrics.builders.StringDistanceBuilder;
import com.github.mpkorstanje.simmetrics.metrics.EuclideanDistance;
import com.github.mpkorstanje.simmetrics.metrics.Levenshtein;
import com.github.mpkorstanje.simmetrics.simplifiers.Simplifiers;
import com.github.mpkorstanje.simmetrics.tokenizers.Tokenizers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;

/**
 * The string distance builder can be used to compose distance metrics for
 * strings.
 */
final class StringDistanceBuilderExample {

	/**
	 * Simply comparing strings through a metric may not be very effective. By
	 * adding simplifiers, tokenizers and filters and transform the
	 * effectiveness of a metric can be improved.
	 * 
	 * The exact combination is generally domain specific. The
	 * StringDistanceBuilder supports these domain specific customizations. Some
	 * example usages are shown below
	 */
	static float example00() {

		String a = "Chilpéric II son of Childeric II";
		String b = "chilperic ii son of childeric ii";

		StringDistance metric = new Levenshtein();

		return metric.distance(a, b); // 7.0000
	}

	/**
	 * Simplification
	 * 
	 * Simplification increases the effectiveness of a metric by removing noise
	 * and reducing the dimensionality of the problem. The process maps a a
	 * complex string to a simpler format. This allows string from different
	 * sources to be compared in the same form.
	 *
	 * The Simplifiers utility class contains a collection of common, useful
	 * simplifiers. For a custom simplifier you can implement the Simplifier
	 * interface.
	 */
	static float example01() {

		String a = "Chilpéric II son of Childeric II";
		String b = "Chilperic II son of Childeric II";

		StringDistance metric = 
				StringDistanceBuilder.with(new Levenshtein())
				.simplify(Simplifiers.removeDiacritics())
				.build();

		return metric.distance(a, b); // 0.0000
	}

	/**
	 * Simplifiers can also be chained.
	 */
	static float example02() {

		String a = "Chilpéric II son of Childeric II";
		String b = "chilperic ii son of childeric ii";

		StringDistance metric = 
				StringDistanceBuilder.with(new Levenshtein())
				.simplify(Simplifiers.removeDiacritics())
				.simplify(Simplifiers.toLowerCase())
				.build();

		return metric.distance(a, b); // 0.0000
	}

	/**
	 * Tokenization
	 * 
	 * A metric can be used to measure the distance between strings. However
	 * not all metrics can operate on strings directly. Some operate on lists,
	 * sets or multisets. To compare strings with a metric that works on a
	 * collection a tokenizer is required. Tokenization cuts up a string into
	 * parts.
	 * 
	 * Example:
	 * 
	 * `chilperic ii son of childeric ii`
	 * 
	 * By splitting on whitespace is tokenized into:
	 * 
	 * `[chilperic,ii,son,of,childeric,ii]`
	 * 
	 * The choice of the tokenizer can influence the effectiveness of a metric.
	 * For example when comparing individual words a q-gram tokenizer will be
	 * more effective while a whitespace tokenizer will be more effective when
	 * comparing documents.
	 * 
	 * The Tokenizers utility class contains a collection of common, useful
	 * tokenizers. For a custom tokenizer you can implement the Tokenizer
	 * interface.
	 */
	static float example03() {

		String a = "A quirky thing it is. This is a sentence.";
		String b = "This sentence is similar; a quirky thing it is.";

		StringDistance metric = 
				StringDistanceBuilder.with(new EuclideanDistance<>())
				.tokenize(Tokenizers.whitespace())
				.build();

		return metric.distance(a, b); // 2.0000
	}

	/**
	 * Tokenizers can also be chained.
	 *
	 * `chilperic ii son of childeric ii`
	 *
	 * By splitting on whitespace is tokenized into:
	 *
	 * `[chilperic,ii,son,of,childeric,ii]`
	 *
	 * Then after using a q-gram with a q of 2:
	 *
	 * `[ch,hi,il,il,lp,pe,er,ri,ic,ii,so,on,of,ch,hi,il,ld,de,er,ri,ic,ii]`
	 *
	 */
	static float example04() {

		String a = "A quirky thing it is. This is a sentence.";
		String b = "This sentence is similar; a quirky thing it is.";

		StringDistance metric = 
				StringDistanceBuilder.with(new EuclideanDistance<>())
				.tokenize(Tokenizers.whitespace())
				.tokenize(Tokenizers.qGram(3))
				.build();

		return metric.distance(a, b); // 2.8284
	}

	/**
	 * Tokens can be filtered to avoid comparing strings on common but otherwise
	 * low information words. Tokens can be filtered after any tokenization step
	 * and filters can be applied repeatedly.
	 * 
	 * A filter can be implemented by implementing a the {@link Predicate}
	 * interface. By chaining predicates more complicated filters can be build.
	 * */
	static float example05() {
		Set<String> commonWords = Sets.newHashSet("it", "is");
		Set<String> otherCommonWords = Sets.newHashSet("a");

		String a = "A quirky thing it is. This is a sentence.";
		String b = "This sentence is similar; a quirky thing it is.";

		StringDistance metric = 
				StringDistanceBuilder.with(new EuclideanDistance<>())
				.simplify(Simplifiers.toLowerCase())
				.simplify(Simplifiers.removeNonWord())
				.tokenize(Tokenizers.whitespace())
				.filter(s -> !commonWords.contains(s))
				.filter(s -> !otherCommonWords.contains(s))
				.tokenize(Tokenizers.qGram(3))
				.build();

		return metric.distance(a, b); // 4.6904
	}

	/**
	 * Tokens can be transformed to a simpler form. This may be used to reduce
	 * the possible token space. Tokens can be transformed after any
	 * tokenization step and the transformation can be applied repeatedly.
	 * 
	 * A transformation can be implemented by implementing a the Function
	 * interface.
	 */
	static float example06() {
		
		Function<String, String> reverse = input -> new StringBuilder(input).reverse().toString();

		String a = "A quirky thing it is. This is a sentence.";
		String b = "This sentence is similar; a quirky thing it is.";

		StringDistance metric = 
				StringDistanceBuilder.with(new EuclideanDistance<>())
				.simplify(Simplifiers.toLowerCase())
				.simplify(Simplifiers.removeNonWord())
				.tokenize(Tokenizers.whitespace())
				.transform(reverse)
				.tokenize(Tokenizers.qGram(3))
				.build();

		return metric.distance(a, b); // 4.6904
	}

	/**
	 * Tokenization and simplification can be expensive operations. To avoid
	 * executing expensive operations repeatedly, intermediate results can be
	 * cached. Note that Caching itself also has a non-trivial cost. Base your
	 * decision on metrics!
	 */
	static float example07() {

		String a = "A quirky thing it is. This is a sentence.";
		String b = "This sentence is similar; a quirky thing it is.";

		Cache<String,String> stringCache = 
				CacheBuilder.newBuilder()
				.maximumSize(2)
				.build();
		
		Cache<String,Multiset<String>> tokenCache = 
				CacheBuilder.newBuilder()
				.maximumSize(2)
				.build();	
		
		StringDistance metric = 
				StringDistanceBuilder.with(new EuclideanDistance<>())
				.simplify(Simplifiers.toLowerCase())
				.simplify(Simplifiers.removeNonWord())
				.cacheStrings(stringCache)
				.tokenize(Tokenizers.qGram(3))
				.cacheTokens(tokenCache)
				.build();

		return metric.distance(a, b); // 4.6904
	}

}
