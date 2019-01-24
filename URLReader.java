package Assignment1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;

/* 
 * Original code obtained from Professor Sara Farag
 * at Bellevue College, 2019/01/14 and modified
 * by Derek Avery for this implementation
 */
public class URLReader {
    private String content;

    /*
     * Instantiate the URLReader class by passing a valid URL String
     * and the URLReader class will attempt to connect to the desired
     * webpage and download the content, which will be stored in the
     * @param content String
     */
    public URLReader(String url) throws Exception {
        // try to open connection to the desired resource
        URLConnection conn = new URL(url).openConnection();
        conn.setRequestProperty("user-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

        // read all content from the webpage and store it in @param content
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine = "";
        content = "";
        while ((inputLine = in.readLine()) != null) {
            content += inputLine + "\n";

        }
        in.close();
    }
    
    public String getContent() {
        return content;
    }
}
