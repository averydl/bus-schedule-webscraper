import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;
/*
 *
 * @author DerekAvery
 * 
 * The WebScraper class contains a main method that provides basic WebScraping
 * functionality, downloading requested HTML resources from the default  
 * @param url (namely, bus route information) based on user input. The WebScraper
 * class functions operate using Regular Expressions which are formatted to extract
 * relevant content based on the current (as of 2019/01/23) format of the 
 * Community Transit HTML webpage.
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
        
        // reformat bus schedules containing '/' character to use a '-' character
        if(bus.contains("/"))
            bus = bus.substring(0, bus.indexOf("/")) + "-" + bus.substring(bus.indexOf("/")+1, bus.length());
        
        // attempt to download schedule HTML
        try {
            ctWebpage = new URLReader(url + "route/" + bus);
        } catch (Exception e) {
            System.out.println("Unable to download the bus schedule.");
        }
        
        System.out.println(getStops(ctWebpage.getContent()));
        

    }
    
    /* 
     * Returns a String containing all bus routes from a community transit HTML schedule
     * page using Regular Expressions to extract relevant content
     */
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
            
            // append each bus to the current destination to the StringBuilder
            while(r.find()) {
                result.append("Bus Number: ");
                result.append(r.group(1));
                result.append("\n");
            }
            result.append("++++++++++++++++++++++++++++++\n");
        }
        return result.toString();
    }
    
    /* 
     * Returns a String containing all bus stop information for a given bus route's HTML 
     * page using Regular Expressions to extract relevant content
     */
    public static String getStops(String info) {
        // create RegEx's for destinations and bus stops 
        String destination = "<h2>Weekday<small>(.*)</small></h2>((\\s*|.*)*)</thead>";
        String busStops = "<strong.*>(.*)</strong>\\s*</span>\\s*<p>(.*)</p>";
        Pattern dests = Pattern.compile(destination);
        Pattern stops = Pattern.compile(busStops);
        Matcher d = dests.matcher(info);
        
        StringBuilder result = new StringBuilder();
        
        // append each destination name to the StringBuilder
        while(d.find()) {
            result.append("Destination: ");
            result.append(d.group(1));
            result.append("\n");
            
            String current = d.group(2);
            Matcher s = stops.matcher(current);
            
            // append each bus stop and its description to the StringBuilder
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
