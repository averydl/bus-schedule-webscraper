package Assignment1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;
/**
 *
 * @author DerekAvery
 */
public class WebScraper {
    public static final String BUS_REGEX = "";
    public static final String url = "https://www.communitytransit.org/busservice/schedules/";
    public static final Scanner input = new Scanner(System.in);
    public static URLReader ctWebpage;
    
    public static void main(String[] args) {
        
        // download main route webpage and scrape the user-specified route info
        try {
            ctWebpage = new URLReader(url);
        } catch (Exception e) {
            System.out.println("Unable to download the webpage.");
        }
        
        System.out.print("Please enter the first letter of your destination: ");
        String firstChar = input.next().toUpperCase();
        System.out.println(getRoutes(firstChar, ctWebpage.getContent()));

        
        // download the user-specified route info page and scrape for stop/destination info
        System.out.println("Please enter the route ID: ");
        String bus = input.next();
        
        try {
            ctWebpage = new URLReader(url + "route/" + bus);
        } catch (Exception e) {
            System.out.println("Unable to download the bus schedule.");
        }
        
        System.out.println(getStops(ctWebpage.getContent()));
        

    }
    
    public static String getRoutes(String firstChar, String info) {
        // create patterns for destinations/route numbers
        // which are consistent with the @firstChar prefix
        String destinations = String.format("<h3>(%s.*)</h3>((\\s*<div.*>\\s*<div.*>\\s*<strong><a.*>(.*)</a></strong>\\s*</div.*>\\s*<div.*>\\s*</div.*>)*)", firstChar);
        String numbers = "<a.*>(.*)</a>";
        
        Pattern dests = Pattern.compile(destinations);
        Pattern buses = Pattern.compile(numbers);
        
        StringBuilder result = new StringBuilder();
    	Matcher d = dests.matcher(info);
        
        // append destinations to the StringBuilder
    	while(d.find()) {
            String destination = d.group(2);
            Matcher r = buses.matcher(destination);
            result.append("Destination: ");
            result.append(d.group(1));
            result.append("\n");
            while(r.find()) {
                result.append("Bus Number: ");
                result.append(r.group(1));
                result.append("\n");
            }
            result.append("++++++++++++++++++++++++++++++\n");
        }
        return result.toString();
    }
    
    public static String getStops(String info) {
        String destination = "<h2>Weekday<small>(.*)</small></h2>((\\s*|.*)*)</thead>";
        String busStops = "<strong.*>(.*)</strong>\\s*</span>\\s*<p>(.*)</p>";
        Pattern dests = Pattern.compile(destination);
        Pattern stops = Pattern.compile(busStops);
        Matcher d = dests.matcher(info);
        
        StringBuilder result = new StringBuilder();
        
        while(d.find()) {
            result.append("Destination: ");
            result.append(d.group(1));
            result.append("\n");
            
            String current = d.group(2);
            Matcher s = stops.matcher(current);
            
            while(s.find()) {
                result.append("Stop ");
                result.append(s.group(1));
                result.append(": ");
                result.append(s.group(2));
                result.append("\n");
            }
            result.append("++++++++++++++++++++++++++++++\n");
        }
        return result.toString();
        
    }
    
 
    
}
