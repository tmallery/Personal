# Programming Portfolio
<<<<<<< HEAD
=======
This repository contains personal, mini projects, which serves as my programming portfolio.
>>>>>>> 37b65b525f43fe12eb5a03f7a1fec207d615c0a1

## Code Examples

### Term Dictionary

A simple dictionary of terms. The dictionary exists as a tree map where each node in the tree is a word from the input string. This itself isn't very interesting and just using a HashMap would be easier. The power comes from being able to take a string of tokens and use the dictionary to find the longest match.

[Term Dictionary Source Package](https://github.com/tmallery/Personal/tree/master/Java/src/main/java/tgm/dictionary)
<<<<<<< HEAD


### Game of Life

A Java implementation of [Conway's Game of Life](https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life).

[Source Package](https://github.com/tmallery/Personal/tree/master/Java/src/main/java/tgm)

### Permutation Tree

This was created as a way to represent the input states for mining the Game of Life. Note a 6x6 grid of binary values has over 67 million combinations. Using a String to represent the input and a recursive function to generate an array of all possible combinations, would take up over 65 GB and my computer doesn't have that much system memory.

I could have kept the input on disk, but even then, there will be a point when the amount of space required will exceed a reasonable sized hard drive.

I realized I could represent the permutations as path down the branch of a tree. When traversing the tree, at any given node, there's at most one active child node. Past nodes can be deleted [nulled out for garbage collection], and future nodes can be created as needed.

Switching from generating all inputs at once to this method produced an incredible savings in memory usage by the JVM.

[PermutationTree Source Package](https://github.com/tmallery/Personal/tree/master/Java/src/main/java/tgm/permutationtree/)

### MineGameOfLife

### MultiThreadedMiner
=======
>>>>>>> 37b65b525f43fe12eb5a03f7a1fec207d615c0a1
