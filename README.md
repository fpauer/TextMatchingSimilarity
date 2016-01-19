

1 - reviewed the problem and the requirements
2 - made researches about Record Linkage, Entity Resolution, Reference Reconciliation,
 * https://en.wikipedia.org/wiki/Record_linkage
 
3 - made researches about string matching algorithms

4 - I pickup some algorithm based on the kind of I had 
 * https://en.wikipedia.org/wiki/Levenshtein_distance
 * https://en.wikipedia.org/wiki/Wagner-Fischer_algorithm

5 - reviewed the problem and the requirements
6 - CHecked the results and found some false positives and the matching took a long running time even using a better implementation.
It works well for estimating distance between shorter strings that differ largely at character level, they become too computationally 
expensive and less accurate for larger
strings

So, how I could improve it ? So I decide to look at words matching , 
because there are some words with strong definitions like words about the product model and manufacturer
So started to look at new algorithm to match my necessities and I started to thing about it.

xx - revise the article about String Metrics
 * http://web.archive.org/web/20081224234350/http://www.dcs.shef.ac.uk/~sam/stringmetrics.html

So I found Jaccard Similarity coefficient it is a token based vector space similarity measure.

Jaccard similarity can then be used as the simplest method for computing likeness as the proportion of tokens shared by both
strings.
 
So it was possible to reduce the running time overall 25% from the first aprroach
