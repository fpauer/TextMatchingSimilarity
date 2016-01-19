# Matching two JSON files using Text Similarity

## Challenge

The challenge was to determine when two pieces of information from different data sources are actually talking about the same.

For example, You have to compare two similiar products "Sony_Cyber-shot_DSC-W310" and "Sony DSC-W310 12.1MP Digital Camera ...", and using text similiraty algorithms match the both. 

However there are some requirements:

Precision – do you make (m)any false matches?
Recall – how many correct matches did you make?
Efficiency (data structure and algorithm choices)


## First approach

Afert a while analysing carefully every piece of the problem, I made some reasearchs about the academic names quoted in the challenge description. 

Starting with Record Linkage and then immediately Entity Resolution.
 * https://en.wikipedia.org/wiki/Record_linkage
 * https://www.inf.ed.ac.uk/publications/thesis/online/IM080663.pdf
 * https://en.wikipedia.org/wiki/Approximate_string_matching
 * http://web.archive.org/web/20081224234350/http://www.dcs.shef.ac.uk/~sam/stringmetrics.html

So, I decided to pick up common algorithns and some of variations of them. Based on the idea to match string I found rich materials talking about the pros and cons of each algorithm. In that time I chose these:

 * https://en.wikipedia.org/wiki/Levenshtein_distance
 * https://en.wikipedia.org/wiki/Wagner-Fischer_algorithm

The Wagner–Fischer algorithm is a better version of Levenshtein Distance algorithm (also called as Edit Distance) using dynamic programing, it calculates the edit distance between two strings. The least edit distance should be the best match.

After looking deep on some articles about the both algorithms it was strongly recommended to match strings. Beyond that if you made some modifications you should improve the original version to a better one and reduce time complexity. For that you just have to use two matrix rows and calculate only the diagonal values.

 * O(k min (m, n)) time (where k is max distance)

When I finished the implementation and runned the code, it worked however the results and the running time was really bad. So, I figured out the idea to compare the distance from each strings in a thousands of lines didn't seem to be the best aprroach. 

These algorithms work well for estimating distance between shorter strings that differ largely at character level, they become too computationally expensive and less accurate for larger strings and that was problem.

Then, I reviewed the problem and got some requirements and though why not match the words intead of letters. Looking to the files you can see strong characteristics in some attributes which should define a product properly. 

## Best approach

Again, I started to look at new algorithms to match the problem necessities and while revising some previous article about String Metrics I found Jaccard Similarity algorithm.

 * http://web.archive.org/web/20081224234350/http://www.dcs.shef.ac.uk/~sam/stringmetrics.html#jaccard

Jaccard similarity can be used as the simplest method for computing likeness as the proportion of tokens shared by both
strings. In summary, it is a token based vector used for comparing similarity and differences between sets, in that case set of words(string). 

 * https://en.wikipedia.org/wiki/Jaccard_index
 * http://en.wikipedia.org/wiki/MinHash

## Conclusion

Finally, after changing some codes, I could get better results and reduced the running time in 75% comparing with the first version. 


