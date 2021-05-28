import java.util.ArrayList;

// I have neither given nor received any unauthorized aid on this assignment. 

public class Word implements Comparable<Word>{

	private String text; // Misspelled word
	private boolean ignored; // Whether the user wants to ignore this word (true if ignored) 
	private boolean replaced; // Whether the user chose to replace this word (true is replaced) 
	private String replacement; // Misspelled word's replacement based on users choice
	private ArrayList<String> instances; // Line numbers where this misspelled word appears 
	
	//-----------------CONSTRUCTORS-------------------
	
	// Empty constructor: called to make an object of type Word 
	public Word() {			
		
	}
		
	// Non-empty constructor: called to make an object of type Word with its text filled 
	public Word(String w) {
		this.text = w; 
		this.ignored = false; 
		this.replaced = false; 
	}
	
	
	//---------------------SETTERS---------------------
	/*
	 * Setter for text 
	 * 
	 * @param w - the misspelled text for Word object 
	 */
	public void setText(String w) {
		this.text = w; 
	}
	
	/*
	 * Setter for ignored 
	 * 
	 * @param i - true if the Word should be ignored
	 * 			- false if the Word should not be ignored 
	 */
	public void setIgnored(boolean i) {
		this.ignored = i; 
	}
	
	/*
	 * Setter for replaced 
	 * 
	 * @param r - true if the Word has been replaced or corrected 
	 * 			- false if the Word has been replaced or corrected 
	 */
	public void setReplaced(boolean r) {
		this.replaced = r; 
	}
	
	/*
	 * Setter for replacement 
	 * 
	 * @param r - the corrected text for misspelled Word object 
	 */
	public void setReplacement(String r) {
		this.replacement = r; 
	}
	
	/*
	 * Setter for instances 
	 * 
	 * @param instances - all the line numbers where this misspelled word is found 
	 */
	public void setInstances(ArrayList<String> instances) {
		this.instances = instances; 
	}
	
	
	//----------------------GETTERS----------------------
	/*
	 * Getter for text
	 */
	public String getText() {
		return this.text; 
	}
	
	/*
	 * Getter for ignored
	 */
	public boolean getIgnored() {
		return this.ignored; 
	}
	
	/*
	 * Getter for replaced 
	 */
	public boolean getReplaced() {
		return this.replaced; 
	}
	
	/*
	 * Getter for replacement 
	 */
	public String getReplacement() {
		return this.replacement; 
	}
	
	/*
	 * Getter for instances 
	 */
	public ArrayList<String> getInstances(){
		return this.instances; 
	}
	
	
	//-------------------OTHER METHODS----------------
	/*
	 * Add a single instance (line number) to the instances of a Word object. 
	 * 
	 * @param instance - line number where the Word object appears in the file 
	 */
	public void addToInstances(String instance) {
		if (instances == null) {
			instances = new ArrayList<String>(); 
		}
		this.instances.add(instance); 
	}
	
	/*
	 * Convert and return the text and instances to a readable single String without brackets 
	 */
	public String getInstancesString() {
		String instancesStr = ""; 
		for (int i = 0; i<this.instances.size(); i++) {
			instancesStr += this.instances.get(i) + " "; 
		}
		return instancesStr; 
	}
	
	/*
	 * Return a String with some information about this word 
	 */
	public String toString() {
		return (this.text); 
	}	
	
	/*
	 * Compare two objects of type Word 
	 */
	public int compareTo(Word other) {
		return this.text.compareTo(other.text); 
	}
	
	
}
