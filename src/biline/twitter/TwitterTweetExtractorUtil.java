package biline.twitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TwitterTweetExtractorUtil {
	
	public HashMap<String, String> tweetElements; 
	public static String tweetText;
	
	public TwitterTweetExtractorUtil(String tweet) {
		//Constructor's Stub
		if(tweet.equals(""))
			tweetText = "Halo @facebook check http://facebook.com dude! Your site is down! By @yehgevni #facebook #social";
		else
			tweetText = "";
		
		//System.out.println("Original tweetText is: " + tweetText);	
	}
	
	//public static void main(String args[]){
	public ArrayList<String> parseTweetForHashtags(String tweetText){
		
	    ArrayList<String> tags = new ArrayList<String>();
	    String patternStr 	   = "(?:\\s|\\A)[##]+([A-Za-z0-9-_]+)";
	    Pattern pattern 	   = Pattern.compile(patternStr);
	    Matcher matcher 	   = pattern.matcher(tweetText);
	    String resultTags 	   = "";
	    while (matcher.find()) {
	         resultTags = matcher.group();
	         resultTags = resultTags.replace(" ", "");
	         resultTags = resultTags.replace("#", "");
	         tags.add(resultTags);
	     }

        //System.out.println(tags);
	    
		return tags;
	}
	
	//public static void main(String args[]){
	public void parseTweetModification(){
			String tweetText = "halo @facebook check http://facebook.com dude! Your site is down! By @yehgevni #facebook #social"; 
			
			String patternStr = "(?:\\s|\\A)[##]+([A-Za-z0-9-_]+)";
		    Pattern pattern   = Pattern.compile(patternStr);
		    Matcher matcher   = pattern.matcher(tweetText);
		    
		    //Looking for URL
		    int indexOfHttp = tweetText.indexOf("http:");
	        int endPoint    = (tweetText.indexOf(' ', indexOfHttp) != -1) ? tweetText.indexOf(' ', indexOfHttp) : tweetText.length();
		    String url      = tweetText.substring(indexOfHttp, endPoint);
		    
		    System.out.println(url);
		    
		    //Looking for Hashtags
		    HashMap<String, String> tagsMap = new HashMap<String, String>();
		    Integer i = 0;
		    String resultTags = "";
		    while (matcher.find()) {
		         resultTags = matcher.group();
		         resultTags = resultTags.replace(" ", "");
		         resultTags = resultTags.replace("#", "");
		         tagsMap.put(i.toString(), resultTags);
		         i++;
		         System.out.println(tagsMap);
		     }
		    
		    //Looking for mentioned Users
		    patternStr = "(?:\\s|\\A)[@]+([A-Za-z0-9-_]+)";
		    pattern    = Pattern.compile(patternStr);
		    matcher    = pattern.matcher(tweetText);
		    HashMap<String, String> usersMap = new HashMap<String, String>();
		    i = 0;
		    String resultUsers = "";
		    while (matcher.find()) {
		    	 resultUsers = matcher.group();
		    	 resultUsers = resultUsers.replace(" ", "");
		    	 resultUsers = resultUsers.replace("@", "");
		    	 usersMap.put(i.toString(), resultUsers);
		    	 i++;
		    	 System.out.println(usersMap);
		    }
	}
	
	public String parseTweetOriginal(String tweetText) {
		 
	     // Search for URLs
	     if (!tweetText.equals("") && tweetText.contains("http:")) {
	         int indexOfHttp      = tweetText.indexOf("http:");
	         int endPoint    	  = (tweetText.indexOf(' ', indexOfHttp) != -1) ? tweetText.indexOf(' ', indexOfHttp) : tweetText.length();
	         String url           = tweetText.substring(indexOfHttp, endPoint);
	         String targetUrlHtml =  "<a href='${url}' target='_blank'>${url}</a>";
	         tweetText            = tweetText.replace(url,targetUrlHtml );
	     }
	 
	     String patternStr = "(?:\\s|\\A)[##]+([A-Za-z0-9-_]+)";
	     Pattern pattern   = Pattern.compile(patternStr);
	     Matcher matcher   = pattern.matcher(tweetText);
	     String result     = "";
	 
	     // Search for Hashtags
	     while (matcher.find()) {
	         result 		   = matcher.group();
	         result 		   = result.replace(" ", "");
	         String search     = result.replace("#", "");
	         String searchHTML ="<a href='http://search.twitter.com/search?q=" + search + "'>" + result + "</a>";
	         tweetText         = tweetText.replace(result,searchHTML);
	     }
	 
	     // Search for Users
	     patternStr = "(?:\\s|\\A)[@]+([A-Za-z0-9-_]+)";
	     pattern    = Pattern.compile(patternStr);
	     matcher    = pattern.matcher(tweetText);
	     while (matcher.find()) {
	         result = matcher.group();
	         result = result.replace(" ", "");
	         String rawName  = result.replace("@", "");
	         String userHTML ="<a href='http://twitter.com/${rawName}'>" + result + "</a>";
	         tweetText = tweetText.replace(result,userHTML);
	     }
	     
	     return tweetText;
	 }
	
	public void usingHashMapMultiDimension(){
		HashMap<String, HashMap<String, String>> map = new HashMap<String, HashMap<String, String>>();
		map.put("username", new HashMap<String, String>());
		map.get("username").put("tag","one");
		map.get("username").put("tag","two");
		
		map.put("hashtags", new HashMap<String, String>());
		
		HashMap<String, String> anotherMap = new HashMap<String, String>();
		
		anotherMap.put("username", "@fahmivanhero");
		anotherMap.put("hashtags", "#tagarone");
		anotherMap.put("hashtags", "#tagartwo");
		String s = anotherMap.get("hashtags");

		System.out.println(s);
	}

}
