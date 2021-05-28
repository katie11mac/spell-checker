// I have neither given nor received any unauthorized aid on this assignment. 

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class SpellSorter {
	
	public static ArrayList<Word>[] sortedMisTable = new ArrayList[52]; // Unique misspelled words in the given file 
	
	// --------- PART 1 --------- 
	public static QuadraticProbingHashTable<String> dictionary = new QuadraticProbingHashTable<String>(); 
	public static ArrayList<Word>[] misTable = new ArrayList[52]; // Unique misspelled words in the given file 
	
	public static Scanner scan = new Scanner(System.in); // User's input 
	
	public static String fileName = ""; // Name of file given by user (used for naming output files) 
	
	public static BufferedWriter outO; // Writer for -order output file of all misspelled words 
	public static File orderFile; // File where all these misspelled words will be stored (will be accessed by multiple methods) 
	
	/*
	 * Prompt the user for a file to spell check and prompt them to interact with their misspelled words if desired. 
	 */
	public static void main(String[] args) throws IOException{
		
		// Hashing the dictionary 
		System.out.println("Reading in Dictionary...");
		readDictionary(args[0]); 
		System.out.println("Dictionary Read."); 
		
		// Getting the user's choice of file 
		getUsersFile(); 
		
		// Start user interaction with general program 
		boolean running = true; 
		
		while (running) {
			
			System.out.println();
			System.out.println("Print words (p), enter new file (f), or quit (q)?");
			char generalChoice = scan.nextLine().strip().toLowerCase().charAt(0); 
		
			// User picked print words (p): user will interact with all the misspelled words in the file 
			if (generalChoice == 'p') { 
				
				boolean printingMS = true; // Represents whether user is still interacting with the misspelled words 
				
				createOrderFile(); // Write every misspelled word to the order file 
	
				// ADDED FOR PART 2 ------------------
				fillSortedMisTable(); // Write the misspelled words to the sorted file in alphabetical order 
				// -----------------------------------
				
				// File reader for the order file with all the misspelled words; assists in going through all the misspelled words in the file 
				Scanner fr = new Scanner(orderFile); 
				
				// while there are more misspelled words to check and the user has not quit yet 
				while (fr.hasNextLine() && printingMS == true) {
				
					String readingLine = fr.nextLine(); // Misspelled word (Each line in the order file contains a misspelled word w/ their line number) 
					
					String misWord = readingLine.substring(0, readingLine.indexOf(" ")); // Just the misspelled word without its line number  
					
					Word w = getMisWord(misWord); // Word object of our misspelled word 
					
					// Have the user interact with the misspelled word only if it has not been ignored or replaced 
					if (w.getIgnored() == false && w.getReplaced() == false) {
						System.out.println("--" + readingLine);  
						printingMS = interactWithMisWords(w); 
					}
					
				} 
				
				if (!fr.hasNextLine()) // User has interacted with every misspelled word in the file 
					System.out.println("Spell check complete!"); 
				else // User quit early 
					System.out.println("You quit, but did not spell check every word."); 
				
				// Write their corrected version of the file
				writeCorrectedFile(); 
				
				fr.close(); 
				
			// User picked enter new file (f): user will enter the name of a new file and be prompted the general prompt 
			} else if (generalChoice == 'f') 
				getUsersFile();
			
			// User picked quit (q): the outer general while loop will stop running 
			else if (generalChoice == 'q') 
				running = false; 
			
			// User entered a different response
			else 
				System.out.println("Please pick a valid option."); 
		}
		
		System.out.println("Goodbye!"); 
	
			
	}
	
	//------------------------- PART 2 ------------------------------
	
	/*
	 * Fill the sorted misTable, so that each ArrayList contains the misspelled words in alphabetical order. 
	 * Write each of these arrayLists to the output file corrected. 
	 */
	public static void fillSortedMisTable() throws IOException{
		
		// Creating the sorted file 
		String sortedFileName = fileName.substring(0, fileName.indexOf('.')) + "_sorted.txt"; 
		File sortedFile = new File(sortedFileName); 
		BufferedWriter outS = new BufferedWriter(new FileWriter (sortedFile));
		
		//For every ArrayList in original unsorted misTable
		for (int i = 0; i < misTable.length; i++) {
			
			// Only work with the ArrayLists if they are not empty (have misspelled words) 
			if (misTable[i] != null) {
				
				// Converting the ArrayList in misTable we are currently on to an array, so that we can QuickSort it 
				Word[] misWordsInI = new Word[misTable[i].size()]; 
				misWordsInI = misTable[i].toArray(misWordsInI); 
				
				ArrayList<Word> sortedMisWords = new ArrayList<Word>(); // Will hold the the misspelled words in alphabetical order
				
				// Add the misspelled words from the misTable to its appropriate sortedMisTable entry in alphabetical order 
				Collections.addAll(sortedMisWords, QuickSort(misWordsInI, 0, misWordsInI.length-1)); 
				sortedMisTable[i] = sortedMisWords; 
				
				// For the array list we are currently on, write every misspelled word in the sortedMisTable to the file with it's line numbers 
				for (int j = 0; j < sortedMisTable[i].size(); j++) {
					outS.write(sortedMisTable[i].get(j) + " " + sortedMisTable[i].get(j).getInstancesString() + "\n");
				}
			} 
		}
		
		outS.close(); 
	}
	
	/*
	 * Sort an array of Words through the QuickSort method. 
	 * 
	 * @param Word[] A - array of Word objects that should be sorted 
	 * @param int first - index of the first element in the array 
	 * @param int last - index of the last element in the array 
	 * 
	 * @return Word[] - sorted Array of Word objects 
	 */
	public static Word[] QuickSort(Word[] A, int first, int last) {
		// Cut off is 3 
		if (last-first < 3) {
			InsertionSort(A); 
		} else {
			// Recursively call and sort the left and right hand side of the array 
			Word pivot = medianOf3(A, first, last);  
			int splitPoint = Partition(A, first, last, pivot); 
			QuickSort(A, first, splitPoint-1); 
			QuickSort(A, splitPoint+1, last); 
		}
		return A; 
	}
	
	/*
	 * Sort an array of Words through the InsertionSort method. 
	 * 
	 * @param Word[] A - array of Word objects that should be sorted 
	 */
	public static void InsertionSort(Word[] A) {
		for (int i = 1; i < A.length; i++) {
			// Move the element i left until is is greater than all the previous elements 
			for (int j = 0; j < i; j++) {
				if (A[i].compareTo(A[j]) < 0) {
					Word iElement = A[i]; 
					Word jElement = A[j]; 
					
					A[i] = jElement; 
					A[j] = iElement; 
				}
			}
		}
	}
	
	
	/*
	 * Pick the median of three elements. 
	 * 
	 * @param Word[] A - array of Word objects that should be sorted 
	 * @param int first - index of the first element in the array 
	 * @param int last - index of the last element in the array 
	 * 
	 * @return Word[] 
	 */
	public static Word medianOf3(Word[] A, int first, int last) {
		Word median; 
		int middle = A.length / 2;  // Index of the middle element 
		
		// First element is the median 
		if ((A[first].compareTo(A[last]) > 0 && A[first].compareTo(A[middle]) < 0) || (A[first].compareTo(A[last]) < 0 && A[first].compareTo(A[middle]) > 0))
			median = A[first];
		// Last element is the median 
		else if ((A[last].compareTo(A[first]) > 0 && A[last].compareTo(A[middle]) < 0) || (A[last].compareTo(A[first]) < 0 && A[last].compareTo(A[middle]) > 0))
			median = A[last]; 
		// Middle element is the median 
		else 
			median = A[middle]; 
		
		return median; 
	}
	
	/*
	 * Partitions A from index first to index last around the pivot. 
	 * 
	 * @param Word[] A - array of Word objects that should be sorted 
	 * @param int first - index of the first element in the array 
	 * @param int last - index of the last element in the array 
	 * @param Word pivot - Word object to pivot or sort around 
	 * 
	 * @return int - pivots final position 
	 */
	public static int Partition(Word[] A, int first, int last, Word pivot) {
		
		// Swap the first and the last element 
		Word lastWord = A[last]; 
		int pivotIndex = 0; 
		
		//Get the pivots index 
		for (int i = 0; i < A.length; i++) {
			if (A[i].getText().equals(pivot.getText())) {
				pivotIndex = i;  
			}
		}
		
		A[last] = pivot; 
		A[pivotIndex] = lastWord; 
		
		
		int i = first; // left pointer 
		int j = last - 1; // right pointer (minus one bc the last index is the pivot) 
		
		boolean loop = true; 
		while(loop) {
			
			// Keep on moving i and j until an element being checked 
			// on the left side is not less than the pivot 
			// or an element being checked on the right side is not greater than the pivot 
			while ( A[i].compareTo(pivot) < 0) {
				i++ ; 
			} 
			
			while (A[j].compareTo(pivot) > 0) {
				j--; 
			}
			
			if(i < j) { // i and j have not yet crossed 
				// Swap A[i] and A[j] 
				Word iElement = A[i]; 
				Word jElement = A[j]; 
				
				A[i] = jElement; 
				A[j] = iElement; 
				
			} else { // i and j have crossed (everything is now on the right side) 
				loop = false; 
			}
		}
		
		// Swap the pivot with the remaining element out of place 
		Word AiElement = A[i]; 
		A[i] = pivot; 
		A[last] = AiElement;
		
		// Return the index of the pivot 
		return i; 
	}
	
	
	
	
	//---------------------------- PART 1 ------------------------------------------------
	
	/*
	 * Read the dictionary file and hash all the words to the dictionary hash table. 
	 * 
	 * @param String file - file name of the dictionary 
	 */
	public static void readDictionary(String file) throws IOException{
		Scanner fr = new Scanner(new File(file)); // Create file object and scanner at the same time
		
		// Reading through every line in the file 
		while(fr.hasNextLine()) { 
			String line = fr.nextLine(); 
			dictionary.insert(line); // Insert/Hash each word into the dictionary 
		} 
	}
	
	/*
	 * Prompt the user to enter the name of the file they would like to spell check 
	 */
	public static void getUsersFile() throws IOException{
		// Getting the desired file
		System.out.print("Please enter a file to spellcheck >> "); 
		fileName = scan.nextLine();  
	}
	
	/*
	 * Write all the misspelled words in the file to the order output file 
	 */
	public static void createOrderFile() throws IOException{
		String contents = getFileContents(fileName); // Store the entire file as a String 
		
		// Creating the order file 
		String orderFileName = fileName.substring(0, fileName.indexOf('.')) + "_order.txt"; 
		orderFile = new File(orderFileName); 
		outO = new BufferedWriter(new FileWriter (orderFile));
		
		// Read the contents in the file and write its misspelled words to the order file 
		checkFileWords(contents.split("\n")); // Read through the file contents, add any unique misspelled words to misTable, and write to order file
		outO.close(); 
	}

	/*
	 * Get the Word object of a misspelled word from the misTable, which is 
	 * the data structure with all unique misspelled words in the given file. 
	 * 
	 * @param String misWord - String of the misspelled Word 
	 * @return Word - the Word object of the misspelled word in the misTable 
	 */
	public static Word getMisWord(String misWord) {
		
		Word w = new Word(misWord); 
		
		int wIndex = getMisTableIndex(misWord); // Represents the location of the ArrayList that holds the misspelled word 
		ArrayList<Word> misWords = misTable[wIndex]; //misTable[wIndex] is the array list of all the misspelled words that begin with the same letter 

		
		for (int i = 0; i < misWords.size(); i++) {
			// Found the corresponding Word object to the String misWord 
			if (misWords.get(i).getText().equals(w.getText()))
				return misWords.get(i); 
		}
		
		return null; 
	}

	/*
	 * Store all the contents in the file as a single String 
	 * 
	 * @param String file - name of file to read 
	 * @return String - all the contents in the file in the same format 
	 */
	public static String getFileContents(String file) throws IOException{
		Scanner fr = new Scanner(new File(file)); 
		String fileContents = ""; 
		
		// Store every line and word in the file as a single String 
		while (fr.hasNextLine()) {
			fileContents += fr.nextLine();
			fileContents += "\n"; 
		}
		
		fr.close(); 
		
		return(fileContents);  
	}
	
	/*
	 * Check which words in the file are misspelled, and complete the required actions if it is. 
	 * 
	 * If the word is spelled incorrectly, write the word and line number to the order file.
	 * Also add that misspelled word to the misTable, if it is not already there. 
	 * 
	 * @param String[] contents - array of Strings, which each string represents a line in the file that the user inputed 
	 */
	public static void checkFileWords(String[] contents) throws IOException{
		// Parse through every line (each element in the array) 
		for (int i = 0; i < contents.length; i++) {
			int lineNum = i+1; 
			
			String[] words = contents[i].split(" "); // All the words on one line 
			
			// Parse through every words on that single line 
			for (int j = 0; j < words.length; j++) { 
				
				// Remove it's punctuation 
				String modifiedWord = removePunctuation(words[j]); 
				
				// Word is spelled incorrectly 
				if (!dictionary.contains(modifiedWord)) { 
					outO.write(modifiedWord + " \t" + lineNum + "\n"); // Write to the order file 
					addToMisTable(modifiedWord, lineNum); // Add the word to the misTable and update which lines the word appears on
				} 
			}
		}
	}
	
	/*
	 * Add a misspelled word to the misTable, if it is not already there. 
	 * Update the line numbers that the words appears on. 
	 * 
	 * @param String misWord - String (text) of a the misspelled word 
	 * @param int lineNum - line number the misspelled word appears on 
	 */
	public static void addToMisTable(String misWord, int lineNum) throws IOException{
		
		Word w = new Word(misWord); // Create a word object out of the misspelled text 

		
		int wIndex = getMisTableIndex(w.getText()); // Represents the location of the ArrayList that holds the misspelled word 
		
		
		//misTable[wIndex] is the array list of all the words that begin with the same letter 
		if (misTable[wIndex] == null) {
			misTable[wIndex] = new ArrayList<Word>(); 
		}
		
		if (! misTableContains(misTable[wIndex], w)){ // Check if our misTable already contains this instance of the misspelled text 
			misTable[wIndex].add(w);
		} 
		
		getMisWord(misWord).addToInstances(lineNum + ""); // Add the line number to the Word object 
	}
	
	/*
	 * Check if the misTable contains a Word object with the same text. 
	 * Created this method because the ArrayList method was comparing the memory addresses of two objects, and not the text data field. 
	 * 
	 * @param ArrayList<Word> misWords - misspelled Word objects to parse through 
	 * @param Word w - the Word object to find in ArrayList<Word> misWords
	 * 
	 * @return boolean - true if a Word object in misWords has the same text data field as Word w
	 * 				   - false if no Word  object in misWords has the same text data field as Word w
	 */
	public static boolean misTableContains(ArrayList<Word> misWords, Word w) {
		// Go through every Word in misWords 
		for (int i = 0; i < misWords.size(); i++) {
			if (misWords.get(i).getText().equals(w.getText())) // Compare text data fields 
				return true; 
		}
		return false; 	
	}
	
	/*
	 * Return the index of the ArrayList that will hold a misspelled word in the misTable 
	 * 
	 * @param String text - text of the misspelled word (String) 
	 * @return int - index of the ArrayList that will hold a misspelled word in the misTable
	 */
	public static int getMisTableIndex(String text) {
		char firstL = text.charAt(0); 
		int intFirstL = (int)firstL; // ASCII code of the first letter 
		
		// Slightly alter the ASCII code of the first letter, so that appropriate indices can be found 
		if (Character.isUpperCase(firstL)) { // First letter is upper cased 
			int index = intFirstL - 65; 
			return index; 
		} else { // First letter is lower cased 
			int index = intFirstL - 71; 
			return index; 
		}
	}
	
	/*
	 * Remove all the punctuation from a String
	 * 
	 * @param String word - text of the misspelled word (String) 
	 * @return String - new String of the word without the punctuation 
	 */
	public static String removePunctuation(String word) {
		String modifiedWord = ""; 
		for (int i = 0; i < word.length(); i++ ) {
			char currentChar = word.charAt(i); 
			// Only add the character if it is a space, digit, or letter 
			if (Character.isSpaceChar(currentChar)) 
				modifiedWord += currentChar; 
			else if (Character.isDigit(currentChar))
				modifiedWord += currentChar; 
			else if (Character.isLetter(currentChar))
				modifiedWord += currentChar; 
		}
		return modifiedWord; 
	}
	
	
	/*
	 * Suggesting Corrections: Technique 1/2 
	 * 
	 * Give suggestions for a misspelled word by replacing each character in the word 
	 * with each letter from 'a' to 'z'
	 * 
	 * @param Word w - misspelled Word object 
	 * @return ArrayList<String> - all suggestions and valid words in the dictionary based on this technique 
	 */
	public static ArrayList<String> suggestReplace(Word w){
		
		char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		ArrayList<String> suggestions = new ArrayList<String>(); 
		String text = w.getText();
		
		
		for (int i = 0; i < text.length(); i++) { // For every character in text field of the given Word   
			
			for (int j = 0; j < alphabet.length; j++) { // Replace it with a letter in the alphabet 
				
				String replacement; // Newly created word (potential suggestion) 
				
				if (i == 0) { // Replacing the first letter of the word 
					replacement = alphabet[j] + text.substring(1); 
					
				} else if (i == text.length()) { // Replacing the last letter of the word 
					replacement = text.substring(0, i+1) + alphabet[j];
					
				} else { // Replacing any letter that is not the first or letter of the word 
					replacement = text.substring(0, i) + alphabet[j] + text.substring(i+1); 
				}				
				
				// Check if the created word is a word in the dictionary 
				if (dictionary.contains(replacement)) 
					suggestions.add(replacement); 
			}
		}
		
		return(suggestions); 
	}
	
	/*
	 * Suggesting Corrections: Technique 2/2 
	 * 
	 * Give suggestions for a misspelled word by inserting between each adjacent pair of characters in the word 
	 * (and before the first character and after the last character), each letter from 'a' through 'z'
	 * 
	 * @param Word w - misspelled Word object 
	 * @return ArrayList<String> - all suggestions and valid words in the dictionary based on this technique 
	 */
	public static ArrayList<String> suggestInsert(Word w) {
		
		char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		ArrayList<String> suggestions = new ArrayList<String>(); 
		String text = w.getText();
		
		
		for (int i = 0; i < text.length() + 1; i++) { // For every character in text field of the given Word 
			
			for (int j = 0; j < alphabet.length; j++) { // Replace it with a letter in the alphabet 
				
				String replacement; // Newly created word (potential suggestion) 
				
				if (i == 0) { // Inserting letter at the front of the word 
					replacement = alphabet[j] + text; 
				} else if (i == text.length()) { // Inserting letter at the end of the word 
					replacement = text + alphabet[j];
				} else { // Inserting letter in between a pair of letters 
					replacement = text.substring(0, i) + alphabet[j] + text.substring(i); 
				}			

				// Check if the created word is a word in the dictionary 
				if (dictionary.contains(replacement)) 
					suggestions.add(replacement); 
			}
		}
		
		return(suggestions); 
	}
	
	/*
	 * EXTRA CREDIT 
	 * Suggesting Corrections: Technique 3/2 
	 * 
	 * Give suggestions for a misspelled word by deleting each character from the word. 
	 * 
	 * @param Word w - misspelled Word object 
	 * @return ArrayList<String> - all suggestions and valid words in the dictionary based on this technique 
	 */
	public static ArrayList<String> suggestDelete(Word w){
		
		ArrayList<String> suggestions = new ArrayList<String>(); 
		String text = w.getText();
		
		for (int i = 0; i < text.length(); i++) { // For every character in text field of the given Word   
			String replacement; // Newly created word (potential suggestion) 
				
			if (i == 0) { // Replacing the first letter of the word 
				replacement = text.substring(1); 
					
			} else if (i == text.length()) { // Replacing the last letter of the word 
				replacement = text.substring(0, i) ;
				
			} else { // Replacing any letter that is not the first or letter of the word 
				replacement = text.substring(0, i)  + text.substring(i+1); 
			}				
			
				//Check if the created word is a word in the dictionary 
				if (dictionary.contains(replacement)) 
					suggestions.add(replacement); 
		}
		
		return(suggestions); 
	}
	
	/*
	 * EXTRA CREDIT 
	 * Suggesting Corrections: Technique 4/2 
	 * 
	 * Give suggestions for a misspelled word by adding a space in between each adjacent pair of 
	 * characters in the word. Only adds them as suggestions if both words in the pair are found in the dictionary. 
	 * 
	 * @param Word w - misspelled Word object 
	 * @return ArrayList<String> - all suggestions and valid words in the dictionary based on this technique 
	 */
	public static ArrayList<String> suggestSplit(Word w){
		
		ArrayList<String> suggestions = new ArrayList<String>(); 
		String text = w.getText();
		
		for (int i = 0; i < text.length(); i++) { // For every character in text field of the given Word   
				
				String replacement = ""; // Newly created word (potential suggestion) 
				
				if (i != text.length() - 1) { // For every character except the last 
					// Add a space between each pair of characters 
					replacement = text.substring(0, i+1) + " " + text.substring(i+1); 
					
					// Check if the two words created are both words in the dictionary 
					// Can hard-code this bc adding 1 space will only create two words each time
					String[] splitWords = new String[2]; 
					splitWords = replacement.split(" "); 
					
					// Only add to the suggestions list if both created suggestions are words in the dictionary 
					if (dictionary.contains(splitWords[0]) && dictionary.contains(splitWords[1]))
						suggestions.add(replacement); 
				}
		}
		return(suggestions); 
	}
	
	
	/*
	 * Have the user interact with all the misspelled words in the file they provided 
	 * until they decide to quit. 
	 * 
	 * Through these interactions, the user can decide to ignore, replace, move on to the next 
	 * misspelled word, or quit interacting with them. 
	 * 
	 * @return boolean - true if the user wants to continue interacting with the misspelled words 
	 * 				   - false if the user has quit  
	 */
	public static boolean interactWithMisWords(Word w) {
		
		// Prompt the user and get their choice 
		System.out.println("ignore all (i), replace all (r), next (n), or quit (q)?"); 
		char choice = scan.nextLine().strip().toLowerCase().charAt(0); 
		
		// Run the appropriate situation based on their choice 
		
		// User picked i: ignore all instances of their misspelled word  
		if (choice == 'i') 
			w.setIgnored(true);
		//User picked r: give suggestions and replace the misspelled word if desired 
		else if (choice == 'r') {
			ArrayList<String> suggestions = new ArrayList<String>(); 
			
			String printingInfo = "Replace with   "; // String that hold all of the suggestions and necessary information 
			
			// Add all of the suggestions to the suggestions 
			// Do not have to account for duplicates because the techniques for my suggestions 
			// do not return any words of the same length  
			suggestions.addAll(suggestReplace(w)); 
			suggestions.addAll(suggestInsert(w)); 
			suggestions.addAll(suggestDelete(w)); 
			suggestions.addAll(suggestSplit(w)); 
			
			// Add each suggestion to the printingInfo String 
			for (int i = 0; i < suggestions.size(); i++) {
				printingInfo += " (" + (i+1) + ")" + suggestions.get(i) + ""; 
			}
			
			printingInfo += ", or next (n)?"; 
			
			// Print the appropriate information based on whether there were suggestions or not 
			if (suggestions.isEmpty()) 
				System.out.println("There are no words to replace it with. \n -- Would you like to: ignore all (i), next (n), or quit (q)?"); 
			else 
				System.out.println(printingInfo); 
			
			String replaceChoice = scan.nextLine(); // Get user's choice (can either be an int or a char) 
			
			// If the first character of the user's input is a digit, then they want to replace the word 
			if (Character.isDigit(replaceChoice.charAt(0))) {
				
				// Get the location (index) of the word they want to replace the misspelled word with 
				int replaceIndex = Integer.parseInt(replaceChoice) - 1;  

				if (replaceIndex < suggestions.size()) {
					// Set the replacement to their choice 
					w.setReplacement(suggestions.get(replaceIndex));
					w.setReplaced(true);
				} else 
					System.out.println("Sorry, the number you entered does not correspond to one of the replacements."); 
				
			// If the first character of the user's input is not a digit, then that means it was a character
			} else {
				// User picked i: ignore all instances of their misspelled word  
				if (Character.toLowerCase(replaceChoice.charAt(0)) == 'i')
					w.setIgnored(true);
				// User picked q: stop interacting with the misspelled words 
				else if (Character.toLowerCase(replaceChoice.charAt(0)) == 'q')
					return false; 
				// If the user picks n (or any other character), the program will naturally move onto the next non-ignored misspelled word 
			}

		// User picked q: stop interacting with the misspelled words 
		}  else if (choice == 'q'){
			return false; 
		} 
		
		// If the user picks n (or any other character), the program will naturally move onto the next non-ignored misspelled word 
		
		return true; 
	}
	
	/*
	 * Write the corrected version of the file based on how the user interacted with the misspelled words. 
	 * This method writes the entire file at once. 
	 */
	public static void writeCorrectedFile() throws IOException{
		
		// Creating the corrected file 
		String correctedFileName = fileName.substring(0, fileName.indexOf('.')) + "_corrected.txt"; 
		File correctedFile = new File(correctedFileName); 
		
		// Creating the writer for the corrected file 
		BufferedWriter outC = new BufferedWriter(new FileWriter (correctedFile));
		
		// Read the original file and store each line in an array of Strings 
		String[] contents = getFileContents(fileName).split("\n"); 
		
		// Parse through every line in the original file (each element in the array) 
		for (int i = 0; i < contents.length; i++) {
			String[] words = contents[i].split(" "); // All the words on one line 
			
			// Parse through every word on that single line 
			for (int j = 0; j < words.length; j++) {
				
				String originalWord = words[j]; // Store the original word in case it has punctuation 
				String modifiedWord = removePunctuation(words[j]); // Remove its punctuation to see if it is word in the dictionary 
				
				// Store any punctuation lost 
				String addPunct = ""; // Will hold any potential punctuation removed from the original word 

				// Get the punctuation from the original word 
				// NOTE: This will only account for when there is only 1 punctuation attached to the end of the word 
				if (modifiedWord.length() < originalWord.length()) {
					addPunct = Character.toString(originalWord.charAt(modifiedWord.length())); 
				}
				
				// Write the words in the file to the corrected file with any attached punctuation 
				if (dictionary.contains(modifiedWord)) { // Word is spelled correctly 
					outC.write(originalWord + " "); 
				} else { // Word is spelled incorrectly 
					Word w = getMisWord(modifiedWord); 
					if (w.getReplaced()) 
						outC.write(w.getReplacement() + addPunct + " "); 
					else 
						outC.write(originalWord + " ");
				}
			}
			outC.write("\n");
		}
		outC.close(); 
	}
}
