// LEAVE THIS FILE IN THE DEFAULT PACKAGE
//  (i.e., DO NOT add 'package cs311.pa1;' or similar)

// DO NOT MODIFY THE EXISTING METHOD SIGNATURES
//  (you may, however, add member fields and additional methods)

// DO NOT INCLUDE LIBRARIES OUTSIDE OF THE JAVA STANDARD LIBRARY
//  (i.e., you may only include libraries of the form java.*)

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;

public class NetworkInfluence
{
	
	/**
	 *  Private class that represents a graph to be used by NetworkInfluence
	 */
	private class Graph {
		
		private LinkedList<String> adjList[];
		private HashMap<String,Integer> getLoc;
		
		private Graph(String graphData) throws FileNotFoundException {
			getLoc = new HashMap<String,Integer>();
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
			
			int nextEmpty = 0;
			while (scan.hasNextLine()) {
				line = scan.nextLine();
				Scanner s = new Scanner(line);
				while (s.hasNext()) {
					String v = s.next();
					String e = s.next();
					nextEmpty = addEdge(result, v, e, nextEmpty);
				}	
				s.close();
			}
			scan.close();
			return result;
		}
		
		private int addEdge(LinkedList<String> adjacencyList[], String vertex, String toAdd, int nextEmpty) {
			
			if(!getLoc.containsKey(vertex)){
				getLoc.put(vertex,nextEmpty);
				adjacencyList[getLoc.get(vertex)].addFirst(vertex);
				nextEmpty++;
			}
			adjacencyList[getLoc.get(vertex)].add(toAdd);
			return nextEmpty;
	    }
		
		private String toStr() {
			
			String result = "";
			for (int i = 0; i < adjList.length; i++) {
				for (int j = 0; j < adjList[i].size(); j++) {
					if (j == 0)
						result += "Vertex:{" + adjList[i].get(j) + "} --> "; 
					else if (j == adjList[i].size()-1)
						result += "[" + adjList[i].get(j) + "]";
					else
						result += "[" + adjList[i].get(j) + "] --> ";
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
		float total = 1;
		HashSet<String> visited = new HashSet<String>();
		Queue<SimpleEntry<String,Integer>> toVisit = new LinkedList<SimpleEntry<String,Integer>>();
		toVisit.add(new SimpleEntry<String,Integer>(u,0));
		while(!toVisit.isEmpty()){
			SimpleEntry<String,Integer> curNode = toVisit.poll();
			if(!visited.contains(curNode.getKey())){
				if(curNode.getValue()!=0){
					total+=1/(Math.pow(2,curNode.getValue()));
				}
				visited.add(curNode.getKey());
				ListIterator<String> curList = graph.adjList[graph.getLoc.get(curNode.getKey())].listIterator();
				while(curList.hasNext()){
					toVisit.add(new SimpleEntry<String,Integer>(curList.next(),curNode.getValue()+1));
				}
			}
		}
		return total;
	}

	public float influence(ArrayList<String> s)
	{
		float total = s.size();
		HashSet<String> visited = new HashSet<String>();
		Queue<SimpleEntry<String,Integer>> toVisit = new LinkedList<SimpleEntry<String,Integer>>();
		for(int i=0; i<s.size(); i++){
			toVisit.add(new SimpleEntry<String,Integer>(s.get(i),0));
		}
		while(!toVisit.isEmpty()){
			SimpleEntry<String,Integer> curNode = toVisit.poll();
			if(!visited.contains(curNode.getKey())&&!s.contains(curNode.getKey())){
				if(curNode.getValue()!=0){
					total+=1/(Math.pow(2,curNode.getValue()));
				}
				visited.add(curNode.getKey());
				ListIterator<String> curList = graph.adjList[graph.getLoc.get(curNode.getKey())].listIterator();
				while(curList.hasNext()){
					toVisit.add(new SimpleEntry<String,Integer>(curList.next(),curNode.getValue()+1));
				}
			}
		}
		return total;
	}

	public ArrayList<String> mostInfluentialDegree(int k)
	{
		ArrayList<String> all_nodes = new ArrayList<String>();
		for(int i=0;i<graph.adjList.length;i++){
			boolean visited_one = false;
			boolean visited_two = false;
			for(int j=0;j<all_nodes.size();j++){
				if(graph.adjList[i].get(0).equals(all_nodes.get(j)))
					visited_one = true;
				if(graph.adjList[i].get(1).equals(all_nodes.get(j)))
					visited_two = true;
			}
			if(!visited_one)
				all_nodes.add(graph.adjList[i].get(0));
			if(!visited_two)
				all_nodes.add(graph.adjList[i].get(1));
		}
		
		String[] most_influencial = new String[k];
		float[] influencial_val = new float[k];
		int index = 0;
		for(int i=0;i<all_nodes.size();i++){
			float a = influence(all_nodes.get(i));
			if(index < k){
				most_influencial[index] = all_nodes.get(i);
				influencial_val[index] = a;
				index++;
			}else{
				float min = influencial_val[0];
				int ind = 0;
				for(int j=1;j<influencial_val.length;j++){
					if(influencial_val[j]<min){
						min = influencial_val[j];
						ind = j;
					}
				}
				if(a > min){
					influencial_val[ind] = a;
					most_influencial[ind] = all_nodes.get(i);
				}
			}
		}
		
		ArrayList<String> influence = new ArrayList<String>();
		for(int n=0;n<most_influencial.length;n++){
			influence.add(most_influencial[n]);
		}
		
		return influence;
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
		NetworkInfluence nw = new NetworkInfluence("C:\\Users\\Thomas\\Documents\\java_isu\\workspace\\PA2\\directed_graph.txt");
		System.out.println(nw.graph.toStr());
	}
}