Compile using javac Tester.java TestThread.java and in collections with javac ConcurrentTrie.java ConcurrentTrieNode.java
Run using java Tester
Tester will run all test sets specified in test.txt (test.txt contains int specifying number of test sets (one per thread) followed by the test set files).
A test set is a txt file comprising of an int specifying the number of commands followed by an int specifying the number of words in the wordBank.
Then comes the words (one per line) followed by command type (ADD, REMOVE, CONTAINS) followed by an associated string (each on own line).