package Code;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Stemmer {

	public static void main(String[] args) {
		
		final File folder = new File ("/Users/IrisSeaman/Documents/Research/Stemmer/src/SampleTalks");
		Stemmer stemmer = new Stemmer();
		PorterStemmer porterStemmer = new PorterStemmer();
		StopWords stopWords = new StopWords();
		
		stemmer.ReadFilesInFolder(folder, porterStemmer, stopWords);
		
		//String description = "A priesthood choir from the Provo Missionary Training Center sings a medley of “I Hope They Call Me on a Mission,” “I Will Be Valiant,” “We'll Bring the World His Truth,” and “Called to Serve.”";
		//String description = "LDS - Apostasy and Restoration - The Church of Jesus Christ of Latter-day Saints has many beliefs in common with other Christian churches. But we have differences, and those differences explain why we send missionaries to other Christians, why we build temples in addition to churches, and why our beliefs";
		//description = description.replaceAll("[^a-zA-Z ]", " ").toLowerCase();
		//description = description.replaceAll("\\s+", " ").trim();
		//String[] words = description.split(" ");
		
		//for (int i = 0; i < words.length; i++) {
		//	words[i] = porterStemmer.stem(words[i]);
		//	System.out.print(words[i] + " ");
		//}
	}
	
	private void ReadFilesInFolder(final File folder, PorterStemmer porterStemmer, StopWords stopWords) {
		
		for(final File fileEntry : folder.listFiles()) {
			
			//Store filename
			String fileName = fileEntry.getName();
			
			// Need to change this back to not include the .DS_Store file
			if (!fileEntry.getName().equals(".DS_Store")){
				System.out.println(fileEntry.getName());
				
				try {
					BufferedReader reader = new BufferedReader(new FileReader(fileEntry));
					StringBuilder fileTextBuilder = new StringBuilder();
					String currentLine;
					
					while( (currentLine = reader.readLine()) != null) {
						fileTextBuilder.append(currentLine);
					}
					reader.close();
					
					//Clean Text - Remove all non characters, lower case, and remove excessive spacing
					String fileText = fileTextBuilder.toString();
					fileText = fileText.replaceAll("[^a-zA-Z ]", " ").toLowerCase();
					fileText = fileText.replaceAll("\\s+", " ").trim();
					//System.out.println(fileText);
					
					//Create a string array of words
					String[] words = fileText.split(" ");
					
					//Create a string array for stems
					List<String> wordStems = new ArrayList<String>();
					
					// Iterate through each word and stem
					for(int i = 0; i < words.length; i++) {
						
						String word = words[i];
						String stem;
						
						if(!stopWords.contains(word)) {
							//For Letters that are by themselves (i.e. The 'S' in 'Thomas S Monson')
							if (word.length() == 1 && !word.equals("i") && !word.equals("a")) {
								stem = word;
							}
							else {
								stem = porterStemmer.stem(word);
							}
							
							//Add stem into the array of wordStems
							wordStems.add(stem);
							
							//Print the original word and its stem version
							System.out.println(word + "->" + stem);
						}
					}
					
					// Write out file with stems
					CreateStemmedFile(wordStems, fileName);
					
				} catch (FileNotFoundException e) {
					System.out.println("Error: Count not create BufferReader in ReadFilesInFolder Method");
				}
				catch (IOException e) {
					System.out.println("Error: Cannot readLine on reader in ReadFilesInFolder Method");
				}
				
			}
			
		}
	}
	
	private void CreateStemmedFile(List<String> stems, String fileName) {
		String filePath = "/Users/IrisSeaman/Documents/Research/Stemmer/src/StemSampleTalks/";
		
		try {
			
			PrintWriter writer = new PrintWriter(filePath + "stemmed-" + fileName);
			
			for(int i = 0; i < stems.size(); i++) {
				writer.println(stems.get(i));
			}
			
			writer.close();
		
		} catch (FileNotFoundException e) {
			System.out.println("Error: PrintWriter cannot be created in CreateStemmedFile Method");
		}
		
	}

}
