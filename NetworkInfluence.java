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
	            result[i] = new LinkedList<String>();
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
		
		private int addEdge(LinkedList<String> adjacencyList[], String vertex, String toAdd, int nextEmpty){
			if(!getLoc.containsKey(vertex)){
				adjacencyList[nextEmpty].add(vertex);
				getLoc.put(vertex,nextEmpty);
				nextEmpty++;
			}
			if(!getLoc.containsKey(toAdd)){
				adjacencyList[nextEmpty].add(toAdd);
				getLoc.put(toAdd,nextEmpty);
				nextEmpty++;
			}
			adjacencyList[getLoc.get(vertex)].add(toAdd);
			return nextEmpty;
	    }
		
		private String toStr() {
			String result = "";
			for (int i = 0; i < adjList.length; i++) {
				for (int j = 0; j < adjList[i].size(); j++) {
					if (j == 0){
						result += "Vertex: " + "[" + adjList[i].get(j) + "]--> "; 
					}
					else if (j == adjList[i].size()-1){
						result += "[" + adjList[i].get(j) + "]";
					}
					else{
						result += "[" + adjList[i].get(j) + "]--> ";
					}
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
		return graph.adjList[graph.getLoc.get(v)].size()-1;
	}

	public ArrayList<String> shortestPath(String u, String v)
	{
		HashSet<String> visited = new HashSet<String>();
		ArrayList<String> path = new ArrayList<String>();
		if(graph.getLoc.get(u)==null||graph.getLoc.get(v)==null) return path;
		Queue<SimpleEntry<String,ArrayList<String>>> toVisit = new LinkedList<SimpleEntry<String,ArrayList<String>>>();
		toVisit.add(new SimpleEntry<String,ArrayList<String>>(u, new ArrayList<String>()));
		while(!toVisit.isEmpty()){
			SimpleEntry<String,ArrayList<String>> curNode = toVisit.poll();
			if(v.equals(curNode.getKey())){
				path.addAll(curNode.getValue());
				path.add(curNode.getKey());
				if(u.equals(v)) path.add(curNode.getKey());
				break;
			}
			else if(!visited.contains(curNode.getKey())){
				visited.add(curNode.getKey());
				ListIterator<String> curList = graph.adjList[graph.getLoc.get(curNode.getKey())].listIterator();
				curList.next();
				while(curList.hasNext()){
					ArrayList<String> nextPath = new ArrayList<String>();
					nextPath.addAll(curNode.getValue());
					nextPath.add(curNode.getKey());
					toVisit.add(new SimpleEntry<String,ArrayList<String>>(curList.next(),nextPath));
				}
			}
		}
		return path;
	}

	public int distance(String u, String v)
	{
		if(u.equals(v))
			return 0;
		ArrayList<String> shortestPath;
		shortestPath = shortestPath(u,v);
		return shortestPath.size()-1;
	}

	public int distance(ArrayList<String> s, String v)     // Goes through every String in s
	{                                                      // to find and return the shortest distance
		int min;
		min = Integer.MAX_VALUE;
		for(int i=0;i<s.size();i++){
			int nw=distance(s.get(i),v);
			if((nw < min)&&(nw!=-1)){
				min = nw;
			}
		}
		if(min==Integer.MAX_VALUE)
			return -1;
		return min;
	}

	public float influence(String u)
	{
		float total = 0;
		HashSet<String> visited = new HashSet<String>();
		Queue<SimpleEntry<String,Integer>> toVisit = new LinkedList<SimpleEntry<String,Integer>>();
		toVisit.add(new SimpleEntry<String,Integer>(u,0));
		while(!toVisit.isEmpty()){
			SimpleEntry<String,Integer> curNode = toVisit.poll();
			if(!visited.contains(curNode.getKey())){
				total+=1/(Math.pow(2,curNode.getValue()));
				visited.add(curNode.getKey());
				ListIterator<String> curList = graph.adjList[graph.getLoc.get(curNode.getKey())].listIterator();
				curList.next();
				while(curList.hasNext()){
					toVisit.add(new SimpleEntry<String,Integer>(curList.next(),curNode.getValue()+1));
				}
			}
		}
		return total;
	}

	public float influence(ArrayList<String> s)
	{
		float total = 0;
		HashSet<String> visited = new HashSet<String>();
		Queue<SimpleEntry<String,Integer>> toVisit = new LinkedList<SimpleEntry<String,Integer>>();
		for(int i=0; i<s.size(); i++){
			toVisit.add(new SimpleEntry<String,Integer>(s.get(i),0));
		}
		while(!toVisit.isEmpty()){
			SimpleEntry<String,Integer> curNode = toVisit.poll();
			if(!visited.contains(curNode.getKey())){
				total+=1/(Math.pow(2,curNode.getValue()));
				visited.add(curNode.getKey());
				ListIterator<String> curList = graph.adjList[graph.getLoc.get(curNode.getKey())].listIterator();
				curList.next();
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
			boolean visited = false;
			for(int j=0;j<all_nodes.size();j++){
				if(graph.adjList[i].get(0).equals(all_nodes.get(j)))
					visited = true;
			}
			if(!visited)
				all_nodes.add(graph.adjList[i].get(0));
		}
		
		String[] most_influencial = new String[k];
		float[] influencial_val = new float[k];
		int index = 0;
		for(int i=0;i<all_nodes.size();i++){
			float a = outDegree(all_nodes.get(i));
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
		ArrayList<String> result = new ArrayList<>();
		float[] infVal = new float[graph.adjList.length];
		HashMap<String,Float> influ = new HashMap<String,Float>();
		for (int i = 0; i < graph.adjList.length; i++) {
			infVal[i] = influence(graph.adjList[i].get(0));
			influ.put(graph.adjList[i].get(0),infVal[i]);
		}
		Arrays.sort(infVal);
		ArrayList<Float> topK = new ArrayList<Float>();
		for(int i=infVal.length-k; i<infVal.length; i++){
			topK.add(infVal[i]);
		}
		for (int i = 0; i<graph.adjList.length; i++){
			if(topK.contains(influ.get(graph.adjList[i].get(0)))){
				result.add(graph.adjList[i].get(0));
			}
			if(result.size()==k) break;
		}
		return result;
	}

	public ArrayList<String> mostInfluentialSubModular(int k)
	{
		ArrayList<String> result = new ArrayList<>();
		ArrayList<SimpleEntry<String,Float>> allInf = new ArrayList<SimpleEntry<String,Float>>();
		for(int i=0; i<graph.adjList.length; i++){
			allInf.add(new SimpleEntry<String,Float>(graph.adjList[i].get(0),influence(graph.adjList[i].get(0))));
		}
		float smallestInK = Float.MAX_VALUE;
		for(int i=1; i<=k; i++){
			SimpleEntry<String,Float> cur = new SimpleEntry<String,Float>("init",0f);
			int indexToRemove = 0;
			for(int j=0; j<allInf.size(); j++){
				if(allInf.get(j).getValue()<=smallestInK&&allInf.get(j).getValue()>cur.getValue()){
					cur = allInf.get(j);
					indexToRemove = j;
				}
			}
			smallestInK = cur.getValue();
			result.add(cur.getKey());
			allInf.remove(indexToRemove);
		}
		return result;
	}
	
	//Delete this later
	public static void main(String[] args) {
		ArrayList<String> set = new ArrayList<String>();
		set.add("/wiki/Computer_Science");
		set.add("/wiki/Science");
		set.add("/wiki/Formal_science");
		set.add("/wiki/Astrophysics");
		set.add("/wiki/List_of_life_sciences");
		set.add("/wiki/Continuum_mechanics");
		set.add("/wiki/Chemistry");
		set.add("/wiki/Earth_science");
		set.add("/wiki/Physics");
		set.add("/wiki/Geophysics");
		set.add("/wiki/Theoretical_physics");
		set.add("/wiki/Biophysics");
		set.add("/wiki/Quantum_mechanics");
		set.add("/wiki/Mechanics");
		set.add("/wiki/Modern_physics");
		NetworkInfluence nw = new NetworkInfluence("C:\\Users\\Dustin\\workspace\\cs311pa2\\wikiCS.txt");
		//System.out.println(nw.graph.toStr());
		for(int i=0; i<set.size(); i++){
			System.out.println(set.get(i)+" "+nw.influence(set.get(i)));
		}
		System.out.println(nw.mostInfluentialDegree(10).toString());
		System.out.println(nw.mostInfluentialModular(10).toString());
		System.out.println(nw.mostInfluentialSubModular(10).toString());
	}
}