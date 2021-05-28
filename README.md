# spell-checker

*TAKEN DIRECTLY FROM PROJECT PROMPT* 

Part 1
For this assignment, you will write a simple but efficient interactive spell-checking program that reads in a dictionary of thousands of words (provided for you), and then spell checks a user-inputted document of words.  

Requirements/Tips:

1. Your program should first take in a command line parameter of the name of the dictionary file (Dictionary.txt).  

2. After storing the words of the dictionary into a hash table, it should prompt the user for the name of a document file.  Your program should read the document and determine the misspelled words (i.e. the words that do not appear in the dictionary). 
For each misspelled word, it should print the word with its line number and give the user the following options: 
•	(i) Ignore all (subsequent appearances of this word should then be considered correctly spelled).
•	(r) Suggest corrections and allow the user to replace this and all subsequent occurrences of the misspelled word with a suggested word (see the “Suggesting Corrections” section below for more details). Note that if the user chooses to replace the misspelled word, the replacement should occur for this and all subsequent instances of the misspelled word (yes, this is a simplified version of how modern-day spell checkers perform the replace operation – see the extra credit below). If the program does not find any suggestions, then it should report so.
•	(n) Go to the next word (subsequent appearances of this word should be reported as misspelled). 
•	(q) Quit spell-checking this file.

3. As in the previous assignment (File Searcher), you should have a Word object for each misspelled word. Remember to make this class encapsulated. This will allow you to keep track of the user’s choices for how to handle this word. For example, your Word object can keep (at least) the following information:
o	ignore – true if the user chooses to ignore this word
o	replacement – the word (if one exists) that the user has chosen to replace this misspelled word with. 
o	replace – (optional) true if the user chooses to replace this word. You can skip this data field and simply check if replacement is null/not null.

4. Suggesting Corrections:
Your program should generate a list of suggestions for misspelled words. Specifically, it should apply at least two of the techniques below to generate new words and verify that these are valid words in the dictionary.  For each suggestion, it should give the user the option to replace a misspelled word with the suggestion. 
•	Swap: Swap each adjacent pair of characters in the word.
•	Insert: Insert in between each adjacent pair of characters in the word (also before the first character and after the last character), each letter from 'a' through 'z'.
•	Delete: Delete each character from the word.
•	Replace: Replace each character in the word with each letter from 'a' through 'z'.
•	Split: Split the word into a pair of words by adding a space in between each adjacent pair of characters in the word. Note that this should generate a suggestion only if both words in the pair are found in the wordlist.

You are required to implement at least two of these techniques, but you will receive extra credit for each additional technique that you use. Please indicate any additional technique with a comment in your code.

5. The Hash Tables:
You should use a Quadrating Probing Hash Tables to store the dictionary of words and a separate table to store the misspelled words. The source code for a Quadrating Probing Hash Table is provided for you on canvas.  You will also create a hash table-like structure to store the misspelled words. This table should be a length-52 array of ArrayList of Word objects (each index will hold the misspelled Word objects that start with the letter that corresponds to the index). Specifically, the first 26 buckets will be for upper case letters and the next 26 will be for lower case letters.  When you find an upper-case misspelled word, you should hash it to the appropriate upper case bucket, and do a similar thing for lower case misspelled words. (We are using this table instead of another hash table because in Part 2, you will be asked to sort each of the buckets).

6. The Input Files:
As in the previous homework, you will have to parse punctuation marks out of the input files. 

7. Output:
In addition to the interactive output described above, your program should output two files:
(1) A version of the input file with misspelled words corrected according to the user’s choices. To name this output file, add the suffix corrected to the name of the input file. For example, if the input file is smallfile.txt, then the output file should be named smallfile_corrected.txt. To reflect the user’s choice for each misspelled word, your program should write to this file as the interaction with the user takes place (not after). 
(2) A file containing a list of the misspelled words (including the ones that are ignored) with their line numbers in order of appearance.  To name this output file, add the suffix order to the name of the input file. For example, if the input file is smallfile.txt, then the output file should be named smallfile_order.txt. Even if the user decided to quit spell-checking the file early, all the misspelled words should still appear in smallfile_order.txt. 





Part 2
You will now extend your spell-checking program so that it also prints the misspelled words in sorted (i.e. alphabetic) order. 


Requirements/Tips:

1.	Create a new file SpellSorter.java and copy and paste your code from Part 1 and modify this code. 

2.	To display the misspelled words alphabetically, your program should perform a bucket sort. Therefore (in addition to the previous misspelled words table) you will now need to create your own hash table to store the misspelled words. 

To display the misspelled words alphabetically, your program should perform a bucket sort. To perform the bucket sort, you will have to sort each bucket of your misspelled words table (from Par 1) individually. When you print the misspelled words in alphabetical order, you may print ALL of the upper-case words first, followed by ALL of the lower case words. 
You must use quickSort (with a cut-off of 3) to sort each individual bucket of this hash table. When the list is of size 3, just do some simple comparisons to sort the list (this is actually insertion sort). You may NOT use any built-in Java sorting methods to do this.  

Here is some pseudocode you may use to implement the Partition routine of quickSort. Please note that this is pseudocode and not actual code so some of the details are intentionally left out for you to think about and fill in. It may be helpful to review and compare this pseudocode to the pseudocode for Partition that we discussed in class. 

/* Partitions A[first…last] around pivot
Partition(A, first, last, pivot)
1.	 swap pivot and A[last]
2.	 i = first
3.	 j = last - 1
4.	 loop = true
 //move i right and j left until A[i] > pivot and A[j] < pivot
5.	 while(loop)
6.	     while(A[i] <= pivot) i++ 
7.	     while(A[j] >= pivot) j--
8.	     if(i < j) //i and j have not yet crossed
9.	        swap A[i] and A[j]
10.	else //i and j have crossed
11.	   loop = false
12. swap pivot and A[i]
13. return i


3.	To name your output file, add the suffix sorted.txt to the name of the input file. For example, if the input file is smallfile.txt, then the output file should be named smallfile_sorted.txt.

4.	If there are multiple occurrences of the same misspelled word, in the sorted output file, your program should print the misspelled word once along with its line numbers.

5.	Your program should still have all the functionalities from Part 1, but it should now also output a file containing a list of the misspelled words in alphabetic order. Note that the only difference noticeable to the user from this part and Part 1 is that the user will now also get a sorted output file (all user interaction will remain the same).
