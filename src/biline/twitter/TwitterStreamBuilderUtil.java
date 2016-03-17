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
			this.CONSUMER_KEY		  = "TEDkbbVcGS37C0G1Lf9BjHA0w";
			this.CONSUMER_SECRET 	  = "V2YcsaiIYw2kO5k6ya8lQaWVU1nGjm0UlIOnf3A2dmvxlBo1qX";
			this.ACCESS_TOKEN		  = "69868781-v9VfpKq1n7j0nu2dWVOG9O0FS8Rl6CwqQW8NU7VA9";
			this.ACCESS_TOKEN_SECRET  = "GcORBA5esOJ99kgPhUJyXhibj6qXhBzGjsdj521tqd2NO";
		}
		else if(twitterHandler.equals("bni46")){ //2nd Apps
			this.CONSUMER_KEY		  = "mpmBSPcnCj0djPkIn4oQCtuvH";
			this.CONSUMER_SECRET 	  = "QHVg6gTN9VqWPWJyknRwFdlHddqui4hSBYJzL1jQVSDPnIZS73";
			this.ACCESS_TOKEN		  = "62455266-06Os7Be9lQD42tMfV3CwKXgD1LEhyKeoJzN0a8yWU";
			this.ACCESS_TOKEN_SECRET  = "mWRVxhwh1ZPXtCSyrCL0cbor4UiBoTrw9YuHLksrcd34p";
		}
		else if(twitterHandler.equals("bni46_cs")){
			this.CONSUMER_KEY		  = "2eAZTNPCwA4H4YAyy5gr76XKZ";
			this.CONSUMER_SECRET 	  = "jvGCwWPjELtNdN8fnGhQ09LpU7e32uGdeSl7kAb4FjxwzaME0o";
			this.ACCESS_TOKEN		  = "3272444328-8epr3Bfy8tXs5TS59sM3tbmjn9xYPc24deiqLT6";
			this.ACCESS_TOKEN_SECRET  = "nO6mDSloPoeqW86A6UQwy7lJmoKji0ZpNmK5sqe0UVBvI";
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
