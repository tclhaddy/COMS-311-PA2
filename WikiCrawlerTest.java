import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

public class WikiCrawlerTest {

	@Test
	public void testCrawlNoTopics() throws InterruptedException, IOException {
		ArrayList<String> topics = new ArrayList<String>();
		WikiCrawler w = new WikiCrawler("/wiki/Iowa_State_University",100,topics,"WikiISU.txt");
		w.crawl();
	}

	@Test
	public void testCrawlWithTopics() throws InterruptedException, IOException {
		ArrayList<String> topics = new ArrayList<String>();
		topics.add("Iowa State");
		topics.add("Cyclones");
		WikiCrawler w = new WikiCrawler("/wiki/Iowa_State_University",100,topics,"WikiISU.txt");
		w.crawl();
	}
}
