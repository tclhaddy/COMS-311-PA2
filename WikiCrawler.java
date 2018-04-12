import java.io.*;
import java.net.*;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;

public class WikiCrawler {
	static final String BASE_URL = "https://en.wikipedia.org";
	String seedURL;
	int max;
	ArrayList<String> topics;
	String fileName;
	public WikiCrawler(String s, int m, ArrayList<String> t, String f){
		seedURL = s;
		max = m;
		topics = t;
		fileName = f;
	}
	public void crawl() throws InterruptedException, IOException{
		PrintWriter writer = new PrintWriter(fileName,"UTF-8");
		writer.println(max);
		Queue<SimpleEntry<String,String>> toVisit = new LinkedList<SimpleEntry<String,String>>();
		HashSet<String> visited = new HashSet<String>();
		int counter = 0;
		toVisit.add(new SimpleEntry<String,String>("",seedURL));
		while(!toVisit.isEmpty()&&visited.size()<=max){
			SimpleEntry<String,String> toAndFrom = toVisit.poll();
			String curStr = BASE_URL+toAndFrom.getValue();
			if(!visited.contains(curStr)){
				visited.add(curStr);
				URL curURL = new URL(curStr);
				InputStream is = curURL.openStream();
				Scanner scanner = new Scanner(is);
				HashSet<String> allTextWords = new HashSet<String>();
				while(scanner.hasNext()){
					String curWord = scanner.next();
					//System.out.println(curWord);
					if(curWord.length()>12&&curWord.substring(0,12).equals("href=\"/wiki/")){
						String nextURL = "";
						for(int i=6; i<curWord.length()-1; i++){
							nextURL+= curWord.charAt(i);
						}
						System.out.println(nextURL);
						if(!nextURL.contains(":")&&!nextURL.contains("#"))toVisit.add(new SimpleEntry<String,String>(toAndFrom.getValue(),nextURL));
					}
				}
				if(!toAndFrom.getKey().equals("")) writer.println(toAndFrom.getKey() + " " + toAndFrom.getValue());
				scanner.close();
				is.close();
				counter++;
				if(counter%25==0) Thread.sleep(3000);
			}
		}
		writer.close();
	}
	public static void main(String[] args) throws InterruptedException, IOException{
		ArrayList<String> topics = new ArrayList<String>();
		WikiCrawler w = new WikiCrawler("/wiki/Iowa_State_University",100,topics,"WikiISU.txt");
		w.crawl();
	}
}
