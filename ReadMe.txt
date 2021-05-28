I have neither given nor received unauthorized aid on this assignment. - Katie Macalintal 


Part 1
Obvious methods due to the provided pseudo code, sample output, or need to complete simple tasks: readDictionary(), getUsersFile(), createOrderFile(), getMisWord(), getFileContents(), addToMisTable(), misTableContains(), getMisTableIndex(), removePunctuation(), suggestReplace(), suggestInsert(), suggestDelete(), suggestSplit()


main(): Based on the sample output provided, a majority of the code in the main method seems obvious, expect for the code when the user to decides to interact with the misspelled words (eg. the user picks ‘p’). When the user picks ‘p’, their order file is written right away, to ensure that every misspelled word is included in this output file. In part two, their sorted file is also written at the same time for the same reason. When the user is spell checking, in order to show the appropriate misspelled word, I decided to read each line from the order output file. I did this because the order file has the misspelled words written in the order that they appear, which is also the order that the user will be spell checking their file. When doing this, I just extracted the Word object associated with the misspelled word present in that line and allowed the user to interact with that Word object if it has not been ignored or replaced. When the user has either spell checked every single word or quit, the corrected file is then written. 


checkFileWords(): This method checks which words in the file are misspelled. It writes the misspelled words to the output file with the line number it appeared on. It also adds the line number to the object’s instance data field. The parameter it takes is an array of Strings. This entire array can be seen as all the contents in the file. Each entry in this array can be seen as a single line in that file. I found that passing in the contents of the file in this way allowed me to better track which line number a misspelled word appeared on. 


interactWithMisWords(): This method allows the user to interact with and correct their misspelled words. Based on the sample output provided, I think that the code in this method is obvious, but it is not obvious why it returns a boolean. It returns a boolean because the main method’s inner loop relies on this method, which gets the information on whether or not the user wants to keep correcting their misspelled words. 


writeCorrectedFile(): Due to the way I had formatted my program, writing the corrected file by reading through every word in the original file seemed most logical and programmable to me. For every word in the file, I store its original form (eg. the word with potential punctuation) into a variable (originalWord). Then I store a modified version of the word without any punctuation and check if this edited word is an entry in the dictionary. If it is an entry in the dictionary, then I just write the original entry that contains its punctuation to the file. If it is not an entry in the dictionary and it has not been replaced, then that means it would be written to the corrected file the same way it was written in the original file. Thus, I also write the original word to the file. If it is not an entry in the dictionary, but it has been replaced, then punctuation will need to be added. I try to account for the punctuation by comparing the length of the modified word to that of the original word. If the modified word is shorter than the original, then that means punctuation was removed. In order to store the removed punctuation, I just read the last character of the original word, since it seems like punctuation is only ever tacked onto the end of the word. However, this means that the program will not accurately gather the punctuation if it is present in the middle or beginning of a word. The stored punctuation will just be added to the replacement of the misspelled word when written to the corrected output file. 


Part 2
Obvious methods due to pseudo code provided and discussed in class: QuickSort(), InsertionSort(), medianOf3(), Partition()


fillSortedMisTable(): This method is called when the user decides to interact with the misspelled words (eg. the user picks ‘p’). It fills a new HashTable with all the misspelled words in alphabetical order and writes all the misspelled words to the outfile. It first creates the sorted file and then reads through the unsorted HashTable with the misspelled words (misTable). The program only then works with the arrayLists in the misTable that actually have misspelled words. Then because my QuickSort takes an array as its parameter, I convert the unsorted arrayList of words to an array, quick sort them, and then store them in the corresponding index in the sortedMisTable. To ensure that the outfile writes everything alphabetically, I then go through the entries I just sorted and write them to the sorted outfile. 


Bugs 
* The program will crash if it cannot find the file the user entered and the user selects p. 
* When writing the corrected file, this program assumes that it will only be removing punctuation from the end of the word. The program will not be able to accurately account for punctuation that is tacked onto the beginning or stuck in the middle of a word when writing the corrected file. 
* When the user is interacting with the misspelled words in the program (eg. deciding whether they want to ignore all, replace all, move onto the next misspelled word, or quit), the program will move onto the next instance of a misspelled word if any character other than i, r, or q is entered. It will crash if nothing is entered because the program reads the first character of the response. 
* If the user quits the file and then decides to print all the words again, the program will pick off at the word the user did not replace or ignore. 
* When a user is trying to replace a word and they enter a number that does not correspond to any of the suggestions given, the program will print out a message and not mark the Word as replaced. However, they will not be prompted to pick another suggestion right away.