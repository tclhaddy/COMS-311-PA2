import java.io.*;
import java.net.*;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;

public class WikiCrawler {
	
	//private static final String BASE_URL = "http://web.cs.iastate.edu/~pavan";
	private static final String BASE_URL = "https://en.wikipedia.org";
	private String seedURL;
	private int max;
	private ArrayList<String> topics;
	private String fileName;
	
	public WikiCrawler(String s, int m, ArrayList<String> t, String f) {
		seedURL = s;
		max = m;
		topics = t;
		fileName = f;
	}
	
	public String getSeedURL() {
		return seedURL;
	}
	
	public int getMax() {
		return max;
	}
	
	public ArrayList<String> getTopics() {
		return topics;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void crawl() throws InterruptedException, IOException {
		PrintWriter writer = new PrintWriter(fileName,"UTF-8");
		writer.println(max);
		Queue<SimpleEntry<String,String>> toVisit = new LinkedList<SimpleEntry<String,String>>();
		HashSet<String> visited = new HashSet<String>();
		int pagesWithTopics = 0;
		int counter = 0;
		toVisit.add(new SimpleEntry<String,String>("",seedURL));
		while(!toVisit.isEmpty()&&pagesWithTopics<max){
			SimpleEntry<String,String> toAndFrom = toVisit.poll();
			String curURL = toAndFrom.getValue();
			ArrayList<String> tempTopics = new ArrayList<String>();
			tempTopics.addAll(topics);
			if(!visited.contains(curURL)){
				Scanner scanner = new Scanner((new URL(BASE_URL+curURL)).openStream());
				boolean currentlyInPTag = false;
				LinkedList<SimpleEntry<String,String>> allNextURLs = new LinkedList<SimpleEntry<String,String>>();
				visited.add(curURL);
				while(scanner.hasNextLine()){
					String curLine = scanner.nextLine();
					if(curLine.contains("<p>")) currentlyInPTag = true;
					if(currentlyInPTag&&!tempTopics.isEmpty()){
						for(int i=0; i<tempTopics.size(); i++){
							if(curLine.contains(tempTopics.get(i))) tempTopics.remove(i);
						}
					}
					if(curLine.contains("href=\"/wiki/")){
						int startOfLink = curLine.indexOf("href=\"/wiki/")+6;
						String nextURL = "";
						while(curLine.charAt(startOfLink)!='\"'){
							nextURL+= curLine.charAt(startOfLink);
							startOfLink++;
						}
						//System.out.println(nextURL);
						if(!nextURL.contains(":")&&!nextURL.contains("#"))allNextURLs.add(new SimpleEntry<String,String>(toAndFrom.getValue(),nextURL));
					}
					if(curLine.contains("<\\p>")) currentlyInPTag = false;
				}
				boolean thisPageHasAllTopics = tempTopics.isEmpty();
				if(thisPageHasAllTopics){
					toVisit.addAll(allNextURLs);
					pagesWithTopics++;
					if(!toAndFrom.getKey().equals("")) writer.println(toAndFrom.getKey() + " " + toAndFrom.getValue());
				}
				scanner.close();
				counter++;
				if(counter%25==0) Thread.sleep(3000);
			}
		}
		writer.close();
	}
}
