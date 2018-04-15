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
	
	/**
	 *  Private class that represents a graph to be used by NetworkInfluence
	 */
	private class Graph {
		
		private LinkedList<String> adjList[];
		
		private Graph(String graphData) throws FileNotFoundException {
			adjList = makeAdjList(graphData);
		}
		
		private LinkedList<String>[] makeAdjList(String path) throws FileNotFoundException, NumberFormatException {
			
			File file = new File(path);
			Scanner scan = new Scanner(file);
			
			int vertices = 0;
			String line;
			line = scan.nextLine();
			vertices = Integer.parseInt(line);
			
			LinkedList<String> result[] = new LinkedList[vertices];
			for (int i = 0; i < vertices; ++i) {
	            result[i] = new LinkedList<>();
	        }
			
			while (scan.hasNextLine()) {
				line = scan.nextLine();
				Scanner s = new Scanner(line);
				while (s.hasNext()) {
					String v = s.next();
					String e = s.next();
					addEdge(result, v, e);
				}	
				s.close();
			}
			scan.close();
			return result;
		}
		
		private void addEdge(LinkedList<String> adjacencyList[], String vertex, String toAdd) {
	        boolean added = false;
	        int pos = 0;
			while (!adjacencyList[pos].isEmpty() && !added) {
	        	if (vertex.equals(adjacencyList[pos].get(0))) {
	        		adjacencyList[pos].addLast(toAdd);
	        		added = true;
	        	}
	        	else {
	        		pos++;
	        	}
	        }
			
			//No vertex found, toAdd becomes a vertex
			if (!added) {
				adjacencyList[pos].addFirst(vertex);
				adjacencyList[pos].add(toAdd);
			}
	    }
		
		private String toStr() {
			String result = "";
			for (int i = 0; i < adjList.length; i++) {
				for (int j = 0; j < adjList[i].size(); j++) {
					if (j == 0)
						result += "Vertex: [" + adjList[i].get(j) + "]--> "; 
					else if (j == adjList[i].size()-1)
						result += "[" + adjList[i].get(j) + "]";
					else
						result += "[" + adjList[i].get(j) + "]--> ";
				}
				result += "\n";
			}
			return result;
		}
	} //End Graph class
	
	private String filepath;
	private Graph graph;
	
	public NetworkInfluence(String graphData)
	{
		filepath = graphData;
		try {
			graph = new Graph(filepath);
		} catch (FileNotFoundException e) {
			System.out.println("File to contruct the graph was not found");
		}
	}
	
	public int outDegree(String v)
	{
		int total = 0;
		
		//Goes through each vertex in adjList
		for(int i=0;i<graph.adjList.length;i++){
			
			//If the first word is the same as the vertice, add 1 to total
			if(graph.adjList[i].get(0).equals(v))
				total++;
		}
		
		//Send back the total -> the number of times v is the first word in adjList
		return total;
	}

	public ArrayList<String> shortestPath(String u, String v)
	{
		ArrayList<String> visited = new ArrayList<String>();
		ArrayList<String> result;
		
		result = shortestPathHelper(u, v, visited);
		
		ArrayList<String> result2 = new ArrayList<String>();
		
		for(int i=result.size()-1;i>=0;i--)
			result2.add(result.get(i));
		
		return result2;
	}
	
	// Helper Method to find shortest path
	private ArrayList<String> shortestPathHelper(String from, String to, ArrayList<String> visited){
		ArrayList<String> out = new ArrayList<String>();
		visited.add(from);
		
		//Goes through the adjList to find all connected vertices
		for(int i=0;i<graph.adjList.length;i++){
			if(graph.adjList[i].get(0).equals(from)){
				boolean hasVisited = false;
				
				//Checks if the found edge has already been visited
				for(int j=0;j<visited.size();j++){
					if(graph.adjList[i].get(1)==visited.get(j)){
						hasVisited = true;
						break;
					}
				}
				if(hasVisited == false){
					out.add(graph.adjList[i].get(1));
				}
			}
		}
		
		ArrayList<String> shortArr = new ArrayList<String>();
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
		ArrayList<String> shortestPath;
		shortestPath = shortestPath(u,v);
		return shortestPath.size();
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
	
	//Delete this later
	public static void main(String[] args) {
		NetworkInfluence nw = new NetworkInfluence("C:\\Users\\Thomas\\Desktop\\test.txt");
		System.out.println(nw.graph.toStr());
	}
}