// LEAVE THIS FILE IN THE DEFAULT PACKAGE
//  (i.e., DO NOT add 'package cs311.pa1;' or similar)

// DO NOT MODIFY THE EXISTING METHOD SIGNATURES
//  (you may, however, add member fields and additional methods)

// DO NOT INCLUDE LIBRARIES OUTSIDE OF THE JAVA STANDARD LIBRARY
//  (i.e., you may only include libraries of the form java.*)

import java.util.ArrayList;

public class NetworkInfluence
{
	// NOTE: graphData is an absolute file path that contains graph data, NOT the raw graph data itself
	public NetworkInfluence(String graphData)
	{
		// implementation
		//I suck at coding
	}

	public int outDegree(String v)
	{
		// implementation

		// replace this:
		return -1;
	}

	public ArrayList<String> shortestPath(String u, String v)
	{
		// implementation

		// replace this:
		return null;
	}

	public int distance(String u, String v)
	{
		// implementation:

		// replace this:
		return -1;
	}

<<<<<<< HEAD
	public int distance(ArrayList<String> s, String v)
	{
		int min=distance(s.get(0),v);
		for(int i=1;i<s.size();i++){
			int curr=distance(s.get(i),v);
			if(curr<min)
				min=curr;
=======
	public int distance(ArrayList<String> s, String v)     // Goes through every String in s
	{                                                      // to find and return the shortest distance
		int min;
		min = distance(s.get(0),v);
		for(int i=1;i<s.size();i++){
			int nw=distance(s.get(i),v);
			if(nw < min){
				min = nw;
			}
>>>>>>> Joey_T
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