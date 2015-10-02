package biline.twitter;

/**
 * TwitterStreamBuilderUtil
 *
 * Class untuk menginisiasi konfigurasi objek yg akan mengakses Twitter API
 *
 * @package		biline.config
 * @author		Developer Team
 * @copyright   Copyright (c) 2015, PT. Biline Aplikasi Digital for PT Bank Negara Indonesia Tbk,,
 */
/*
 * ------------------------------------------------------
 *  Memuat package dan library
 * ------------------------------------------------------
 */
import twitter4j.conf.ConfigurationBuilder;

import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

//Usage// 
//TwitterStreamBuilderUtil twitSBU = new TwitterStreamBuilderUtil("amartha");
public class TwitterStreamBuilderUtil {

	private String CONSUMER_KEY;
	private String CONSUMER_SECRET;
	private String ACCESS_TOKEN;
	private String ACCESS_TOKEN_SECRET;
	
	public TwitterStreamBuilderUtil(String twitterHandler) {
		// Constructor
		if(twitterHandler.equals("amartha")){
			this.CONSUMER_KEY 		 = "NtbORNZdwwCaeLelSiUJll14z";
			this.CONSUMER_SECRET 	 = "CuGeg1yDQJGj6WFCivYqZltj5PvRWDRWadsEaX9a11agNUzaci";
			this.ACCESS_TOKEN 		 = "3825555556-EzMlPf990ZrL0U0HnAR1OSyhSBR3xkgSrOkDSF3";
			this.ACCESS_TOKEN_SECRET = "RenSLFtVJ931XxniN75Xx2wR7qWjWE1G8p0Qsn2pO9ysA";
		}
		else if(twitterHandler.equals("bni46")){
			this.CONSUMER_KEY		  = "ieQr4cnkzw8vQHaamX3LcZOWL";
			this.CONSUMER_SECRET 	  = "sAxJbBxZee39nXJSgKHhHbU4oRiSlfSZ6MBNddvaOsWKIFAGNk";
			this.ACCESS_TOKEN		  = "62455266-yu62JxW5IiZmGICRNKItxzHlwAKD3vwpujkYPtt74";
			this.ACCESS_TOKEN_SECRET  = "UEmx3nkIuSp8PfOHgxqbNvBZbEigJp7yRuaXd11uWPSLP";
		}
	}
	
	public TwitterStream getStream() {

		  ConfigurationBuilder cb = new ConfigurationBuilder();
		  cb.setDebugEnabled(true);
		  cb.setOAuthConsumerKey(this.CONSUMER_KEY);
		  cb.setOAuthConsumerSecret(this.CONSUMER_SECRET);
		  cb.setOAuthAccessToken(this.ACCESS_TOKEN);
		  cb.setOAuthAccessTokenSecret(this.ACCESS_TOKEN_SECRET);

		  return new TwitterStreamFactory(cb.build()).getInstance();
	}

}
