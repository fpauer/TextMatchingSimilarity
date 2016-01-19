# Matching two JSON files using Text Similarity

## Challenge

The challenge is to determine when two pieces of information from different data sources are actually talking about the same.

For example,you have to compare two similiar products "Sony_Cyber-shot_DSC-W310" and "Sony DSC-W310 12.1MP Digital Camera ...", and using text similiraty algorithms match them. 

However there are some requirements:

- Precision – do you make (m)any false matches?
- Recall – how many correct matches did you make?
- Efficiency (data structure and algorithm choices)


## First approach

After a while analysing carefully every piece of the problem, I made some reasearches about the academic names quoted in the challenge description. 

Record Linkage and Entity Resolution:
 * https://en.wikipedia.org/wiki/Record_linkage
 * https://www.inf.ed.ac.uk/publications/thesis/online/IM080663.pdf
 * https://en.wikipedia.org/wiki/Approximate_string_matching
 * http://web.archive.org/web/20081224234350/http://www.dcs.shef.ac.uk/~sam/stringmetrics.html

Comparing some of them, I decided to pick up commonly used algorithms and some of their variations. Based on the idea to match string I found rich materials about the pros and cons of each algorithm. Finally, I chose these:

 * https://en.wikipedia.org/wiki/Levenshtein_distance
 * https://en.wikipedia.org/wiki/Wagner-Fischer_algorithm

The Wagner–Fischer algorithm is a better version of Levenshtein Distance algorithm (also called as Edit Distance) using dynamic programing, it calculates the edit distance between two strings. The least distance should be the best match.

After looking deeply into some articles about the both algorithms, the both were strongly recommended to match strings. Beyond that if you made some modifications you should improve the original version to a better one and reduce the time complexity. For this, you just have to use two matrix rows and calculate only the diagonal values as described in the article bellow. 

 * [Wagner–Fischer algorithm time complexity: O(k min (m, n)) time (where k is max distance)](https://en.wikipedia.org/wiki/Wagner%E2%80%93Fischer_algorithm).
  

When I finished the implementation and runned the code, it worked well however the results and the running time were really bad. So, I figured out the idea to compare the distance from each strings in thousands of lines didn't seem to be the best aprroach. 

These algorithms work well for estimating distance between shorter strings that differ largely at character level, they become too computationally expensive and less accurate for larger strings and that was problem, long strings.

Then, I reviewed the problem and the requirements. During this process I though why not match the words instead of letters. Looking to the files you can see strong characteristics in some attributes (manufacturer, model) which should define a product properly. Then, I started to look a best approach.

## Best approach

Again, I started to look at new algorithms to find a more appropriate solution and while I was revising some previous article about String Metrics I found Jaccard Similarity Coefficient.

 * http://web.archive.org/web/20081224234350/http://www.dcs.shef.ac.uk/~sam/stringmetrics.html#jaccard

Jaccard Similarity can be used as the simplest method for computing likeness as the proportion of tokens shared by both
strings. In summary, it is a token based vector used for comparing similarity and differences between sets of data, in that case set of words(strings). 

 * https://en.wikipedia.org/wiki/Jaccard_index
 * http://en.wikipedia.org/wiki/MinHash

## Conclusion

Finally, after changing some codes, I could get better results and reduced the running time in 75% comparing with the first version. 


