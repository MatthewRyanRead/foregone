# Foregone

My solution to [the Codejam 2019 problem](https://codingcompetitions.withgoogle.com/codejam/round/0000000000051705/0000000000088231).

Unlike the solutions proposed on the Analysis section of that page, it solves the problem with a custom "Big Integer" type implementation I call a Fourless Number and performing a search not all that dissimilar from binary search.

It first splits the number into two (as close to equal halves as possible), and searches by first increasing one by the amount need to make it "fourless", and storing it as the minimum difference.  That minimum difference is used to decrease the second number, adding to it along the way to make the second number "fourless" as well.  The amount added on top of the initial difference is used to as the new minimum difference, and applied to the first number in the same way.  This process goes back and forth until the difference to be applied is zero; at this point, both numbers are "fourless" and sum to the expected total.  They are also necessarily the pair of Fourless Numbers closest to their average.
