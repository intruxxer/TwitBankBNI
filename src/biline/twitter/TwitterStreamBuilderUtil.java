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
//TwitterStreamBuilderUtil twitBuilder = new TwitterStreamBuilderUtil("amartha/fahmivanhero/bni46");
public class TwitterStreamBuilderUtil {

	private String CONSUMER_KEY;
	private String CONSUMER_SECRET;
	private String ACCESS_TOKEN;
	private String ACCESS_TOKEN_SECRET;
	
	public TwitterStreamBuilderUtil(String twitterHandler) {
		// Constructor
		if(twitterHandler.equals("dev_amartha")){
			this.CONSUMER_KEY 		 = "NtbORNZdwwCaeLelSiUJll14z";
			this.CONSUMER_SECRET 	 = "CuGeg1yDQJGj6WFCivYqZltj5PvRWDRWadsEaX9a11agNUzaci";
			this.ACCESS_TOKEN 		 = "3825555556-0yPXCBEvlqWqmKVmfYWSqU1hDLUjKcNoZLFvAWb";
			this.ACCESS_TOKEN_SECRET = "oB7vp6SCnhBdBa6gI4YvmZeQAVOtgMjD3BqPEo84TXjiR";
		}
		else if(twitterHandler.equals("fahmivanhero")){
			this.CONSUMER_KEY		  = "Q2V6Nx3160lSm8KAVgP67lpp9";
			this.CONSUMER_SECRET 	  = "YimS1y7oDNF8Ni3nMRV9QxtOgDzD4PZ32qTqsI21NFQoeqWwCr";
			this.ACCESS_TOKEN		  = "69868781-pCFlQfzOW5y182tcLAvkkSpwz9EEx7W3OuD5f2eXb";
			this.ACCESS_TOKEN_SECRET  = "6rtdaoMZJ6gJPwUcuKt8yHDkJKoQ8M5etXmzZK6jLxrBA";
		}
		else if(twitterHandler.equals("bni46")){
			this.CONSUMER_KEY		  = "ieQr4cnkzw8vQHaamX3LcZOWL";
			this.CONSUMER_SECRET 	  = "sAxJbBxZee39nXJSgKHhHbU4oRiSlfSZ6MBNddvaOsWKIFAGNk";
			this.ACCESS_TOKEN		  = "62455266-TbRhx8jNzSKNdU5eSKh9q4lnNCgtWSqdKXKNytCc2";
			this.ACCESS_TOKEN_SECRET  = "aj5Ffh3xfvgBHtz3ahxE1T1M6jCiDCGNW7swo3WNgJV6x";
		}
		else {
			this.CONSUMER_KEY		  = "";
			this.CONSUMER_SECRET 	  = "";
			this.ACCESS_TOKEN		  = "";
			this.ACCESS_TOKEN_SECRET  = "";
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
