package Code;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class FileCleaner {

	public static void main(String[] args) {
		
		FileCleaner cleaner = new FileCleaner();
		
		final File folder = new File ("/Users/IrisSeaman/Documents/Research/Stemmer/src/StemSampleTalks");
		
		cleaner.ReadFilesInFolder(folder);
		//cleaner.WordFrequencyCounter(folder);
	}
	
	private void ReadFilesInFolder(final File folder) {
		
		List<String>noise = GetNoiseWords();
		
		for(final File fileEntry : folder.listFiles()) {
			
			//Store filename
			String fileName = fileEntry.getName();
			
			// Need to change this back to not include the .DS_Store file
			if (!fileEntry.getName().equals(".DS_Store")){
				//System.out.println(fileEntry.getName());
				
				try {
					BufferedReader reader = new BufferedReader(new FileReader(fileEntry));
					StringBuilder fileTextBuilder = new StringBuilder();
					String currentLine;
					
					while( (currentLine = reader.readLine()) != null) {
						fileTextBuilder.append(currentLine);
						fileTextBuilder.append(" ");
					}
					reader.close();
					
					String fileText = fileTextBuilder.toString();
					fileText = fileText.replaceAll("\\s+", " ").trim();
					String cleanWords = RemoveNoiseSentence(fileText, noise);
					
					// Write out file with stems
					CreateCleanFile(cleanWords, fileName);
					
				} catch (FileNotFoundException e) {
					System.out.println("Error: Count not create BufferReader in ReadFilesInFolder Method");
				}
				catch (IOException e) {
					System.out.println("Error: Cannot readLine on reader in ReadFilesInFolder Method");
				}
				
			}
			
		}
	}
	
	private void CreateCleanFile(String fileText, String fileName) {
		String filePath = "/Users/IrisSeaman/Documents/Research/Stemmer/src/CleanTalks/";
		
		try {
			
			PrintWriter writer = new PrintWriter(filePath + "clean-" + fileName);
			
			writer.println(fileText);
			
			writer.close();
		
		} catch (FileNotFoundException e) {
			System.out.println("Error: PrintWriter cannot be created in CreateStemmedFile Method");
		}
		
	}
	
    private List<String> GetNoiseWords() {
    	List<String> noiseWords = new ArrayList<String>();
    	int count = 0;
    	try {
			Scanner fileScanner = new Scanner(new File("NoisyData.txt"));
			
			while(fileScanner.hasNext()) {
				String nextWord = fileScanner.next();
				noiseWords.add(nextWord);
				System.out.println(nextWord);
				count++;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	System.out.println(count);
    	return noiseWords;
    }
    
    private String RemoveNoiseSentence(String sent, List<String> noise) {
    	
		List<String> cleanWords = new ArrayList<String>();
		sent = sent.replaceAll("\\s+", " ").trim();
		String allWords[] = sent.split(" ");
		for(int i = 0; i < allWords.length; i++) {
			String cleanString = allWords[i];
			if (noise.contains(cleanString)) {
				cleanString = null;
			}
			else {
				cleanWords.add(cleanString);
			}
			
		}
		
		// Change back to a string
		StringBuilder stringToBuild = new StringBuilder();
		
		for(int i = 0; i < cleanWords.size(); i++)
		{
			stringToBuild.append(cleanWords.get(i));
			if (i != cleanWords.size() -1) {
				stringToBuild.append(" ");
			}
		}
		
		return stringToBuild.toString();
	}
    
    
    private void WordFrequencyCounter(final File folder) {
		
    	Map<String, Integer> wordFrequencies = new HashMap<String, Integer>();
		for(final File fileEntry : folder.listFiles()) {
			
			//Store filename
			String fileName = fileEntry.getName();
			
			// Need to change this back to not include the .DS_Store file
			if (!fileEntry.getName().equals(".DS_Store")){
				//System.out.println(fileEntry.getName());
				
				try {
					BufferedReader reader = new BufferedReader(new FileReader(fileEntry));
					StringBuilder fileTextBuilder = new StringBuilder();
					String currentLine;
					
					while( (currentLine = reader.readLine()) != null) {
						fileTextBuilder.append(currentLine);
						fileTextBuilder.append(" ");
					}
					reader.close();
					
					String fileText = fileTextBuilder.toString();
					fileText = fileText.replaceAll("\\s+", " ").trim();
					
					// Ended reading in all text
					
					// Do the word count 
					String allWords[] = fileText.toString().split(" ");
					
					
					for ( int i = 0; i < allWords.length; i++) {
						
						String word = allWords[i];
						if (wordFrequencies.containsKey(word)) {
							int frequency = wordFrequencies.get(word);
							wordFrequencies.put(word, ++frequency);
						} else {
							wordFrequencies.put(word, 1);
						}
					}
					
					// Iterate..and report the word frequencies
					// Print out Results about output
					
				    
				    wordFrequencies = sortByComparator(wordFrequencies, false);
				    //PrintWordFrequenciesToFile(wordFrequencies);
				    PrintWordLOWFrequenciesToFile(wordFrequencies);
					
				} catch (FileNotFoundException e) {
					System.out.println("Error: Count not create BufferReader in ReadFilesInFolder Method");
				}
				catch (IOException e) {
					System.out.println("Error: Cannot readLine on reader in ReadFilesInFolder Method");
				}
				
			}
			
		}
	}
		
	
	private void PrintWordFrequenciesToFile(Map<String, Integer> wordFrequencies) {	
		
	    PrintWriter writer;
		
			try {
				writer = new PrintWriter("WordFrequencies.txt", "UTF-8");
			
			
		        Iterator<Map.Entry<String, Integer>> it = wordFrequencies.entrySet().iterator();
		        
		        while(it.hasNext()) {
		            Map.Entry<String, Integer> entry = it.next();
		            String word = entry.getKey();
		            Integer freq = entry.getValue();
		            
		            writer.println("\n" + word + " " + freq);
		        }
	        
	        writer.close();
	        
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	/* PrintWordLOWFrequenciesToFile
	 * 
	 * Writes out a file 'LOW_WordFrequencies' of all the unique
	 * cleaned words that have low frequencies in all the documents. 
	 * Frequencies must by less than or equal to 29
	 */
	private void PrintWordLOWFrequenciesToFile(Map<String, Integer> wordFrequencies) {	
		
	    PrintWriter writer;
		
			try {
				writer = new PrintWriter("LOW_WordFrequencies.txt", "UTF-8");
			
			
		        Iterator<Map.Entry<String, Integer>> it = wordFrequencies.entrySet().iterator();
		        
		        while(it.hasNext()) {
		            Map.Entry<String, Integer> entry = it.next();
		            String word = entry.getKey();
		            Integer freq = entry.getValue();
		            
		            if (freq <= 29 ) {
		            	writer.println(word);
		            }
		        }
	        
	        writer.close();
	        
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	private Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap, final boolean order)
	{
	
	    List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(unsortMap.entrySet());
	
	    // Sorting the list based on values
	    Collections.sort(list, new Comparator<Entry<String, Integer>>()
	    {
	        public int compare(Entry<String, Integer> o1,
	                Entry<String, Integer> o2)
	        {
	            if (order)
	            {
	                return o1.getValue().compareTo(o2.getValue());
	            }
	            else
	            {
	                return o2.getValue().compareTo(o1.getValue());
	
	            }
	        }
	    });
	
	    // Maintaining insertion order with the help of LinkedList
	    Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
	    for (Entry<String, Integer> entry : list)
	    {
	        sortedMap.put(entry.getKey(), entry.getValue());
	    }
	
	    return sortedMap;
	}

}
