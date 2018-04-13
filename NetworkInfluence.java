// LEAVE THIS FILE IN THE DEFAULT PACKAGE
//  (i.e., DO NOT add 'package cs311.pa1;' or similar)

// DO NOT MODIFY THE EXISTING METHOD SIGNATURES
//  (you may, however, add member fields and additional methods)

// DO NOT INCLUDE LIBRARIES OUTSIDE OF THE JAVA STANDARD LIBRARY
//  (i.e., you may only include libraries of the form java.*)

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class NetworkInfluence
{
	private String filepath;
	private LinkedList<String> adjList[];
	
	public NetworkInfluence(String graphData)
	{
		filepath = graphData;
		try {
			adjList = makeAdjList();
		} catch (FileNotFoundException e) {
			System.out.println("Filepath was not found");
		}
	}
	
	private LinkedList<String>[] makeAdjList() throws FileNotFoundException {
		
		File file = new File(filepath);
		Scanner scan = new Scanner(file);
		
		int vertices = 0;
		String line;
		line = scan.nextLine();
		try {
			vertices = Integer.parseInt(line);
		} catch (NumberFormatException e) {
			System.out.println("File did not have the number of vertices on the first line");
		}
		
		LinkedList<String> result[] = new LinkedList[vertices];
		for (int i = 0; i < result.length; ++i) {
            result[i] = new LinkedList<>();
        }
		
		int pos = 0;
		while (scan.hasNextLine()) {
			line = scan.nextLine();
			Scanner s = new Scanner(line);
			while (s.hasNext()) {
				String word = s.next();
				for (int i = 0; i < result.length; i++) {
					
					//First word exists already as a vertex, add second word as an edge
					if (word.equals(result[i].get(0))) {
						word = s.next();
						result[i].addFirst(word);
					}
				}
				
				//Otherwise there is no vertex for the first word, make one
				result[pos].addFirst(word);
				pos++;
			}
			
			pos++;
			s.close();
		}
		scan.close();
		return result;
	}

	public int outDegree(String v)
	{
		int total = 0;
		
		//Goes through each vertice in adjList
		for(int i=0;i<adjList.length;i++){
			
			//If the first word is the same as the vertice, add 1 to total
			if(adjList[i].get(0).equals(v))
				total++;
		}
		
		//Send back the total -> the number of times v is the first word in adjList
		return total;
	}

	public ArrayList<String> shortestPath(String u, String v)
	{
		ArrayList<String> visited;
		ArrayList<String> result;
		
		result = shortestPathHelper(u, v, visited);
		
		ArrayList<String> result2;
		
		for(int i=result.size()-1;i>=0;i--)
			result2.add(result.get(i));
		
		return result2;
	}
	
	
	// Helper Method to find shortest path
	public ArrayList<String> shortestPathHelper(String from, String to, ArrayList<String> visited){
		ArrayList<String> out;
		visited.add(from);
		
		//Goes through the adjList to find all connected vertices
		for(int i=0;i<adjList.length;i++){
			if(adjList[i].get(0).equals(from)){
				visited = false;
				
				//Checks if the found edge has already been visited
				for(int j=0;j<visited.size();j++){
					if(adjList[i].get(1)==visited.get(j)){
						visited = true;
						break;
					}
				}
				if(visited == false){
					out.add(adjList[i].get(1));
				}
			}
		}
		
		ArrayList<String> shortArr;
		if(out.size() == 0)
			return shortArr;
		
		for(int n=0;n<out.size();n++){
			if(out.equals(to)){
				shortArr.add(to);
				shortArr.add(from);
				return shortArr;
			}
		}
		
		shortArr = shortestPathHelper(out.get(0), to, visited);
		for(int m=1;m<out.size();m++){
			ArrayList<String> arr2 = shortestPathHelper(out.get(m),to,visited);
			boolean contain = false;
			for(int z=0;z<arr2.size();z++){
				if(arr2.get(z).equals(to))
					contain = true;
			}
			if((arr2.size() < shortArr.size())&&(contain == true)){
				shortArr = arr2;
			}
		}
		
		shortArr.add(from);
		return shortArr;
	}

	public int distance(String u, String v)
	{
		// implementation:

		// replace this:
		return -1;
	}

	public int distance(ArrayList<String> s, String v)     // Goes through every String in s
	{                                                      // to find and return the shortest distance
		int min;
		min = distance(s.get(0),v);
		for(int i=1;i<s.size();i++){
			int nw=distance(s.get(i),v);
			if(nw < min){
				min = nw;
			}
		}
		return min;
	}

	public float influence(String u)
	{
		// implementation

		// replace this:
		return -1f;
	}

	public float influence(ArrayList<String> s)
	{
		// implementation

		// replace this:
		return -1f;
	}

	public ArrayList<String> mostInfluentialDegree(int k)
	{
		// implementation

		// replace this:
		return null;
	}

	public ArrayList<String> mostInfluentialModular(int k)
	{
		// implementation

		// replace this:
		return null;
	}

	public ArrayList<String> mostInfluentialSubModular(int k)
	{
		// implementation

		// replace this:
		return null;
	}
}