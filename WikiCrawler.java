import java.io.*;
import java.net.*;
import java.util.*;

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
	//eyy
	public void crawl() throws InterruptedException, IOException{
		PriorityQueue<String> toVisit = new PriorityQueue<String>();
		HashSet<String> visited = new HashSet<String>();
		int counter = 0;
		toVisit.add(seedURL);
		while(!toVisit.isEmpty()&&visited.size()<=max){
			String curStr = BASE_URL+toVisit.poll();
			if(!visited.contains(curStr)){
				visited.add(curStr);
				URL curURL = new URL(curStr);
				InputStream is = curURL.openStream();
				Scanner scanner = new Scanner(is);
				HashSet<String> allTextWords = new HashSet<String>();
				while(scanner.hasNextLine()){
					System.out.println(scanner.nextLine()+counter);
				}
				scanner.close();
				is.close();
				counter++;
				if(counter%25==0) Thread.sleep(3000);
			}
		}
	}
	public static void main(String[] args) throws InterruptedException, IOException{
		ArrayList<String> topics = new ArrayList<String>();
		WikiCrawler w = new WikiCrawler("/wiki/Iowa_State_University",100,topics,"WikiISU.txt");
		w.crawl();
	}
}
