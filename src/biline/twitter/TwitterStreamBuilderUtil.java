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
		else if(twitterHandler.equals("bni46")){
			this.CONSUMER_KEY		  = "zTHyBDaZzDBNpKQx98rNDWEtR";
			this.CONSUMER_SECRET 	  = "mM2mwNfL2Vwga0DuteG9dDXuNOCNmPZzLZ1mZMlnKJPJkNf2TW";
			this.ACCESS_TOKEN		  = "62455266-TbRhx8jNzSKNdU5eSKh9q4lnNCgtWSqdKXKNytCc2";
			this.ACCESS_TOKEN_SECRET  = "aj5Ffh3xfvgBHtz3ahxE1T1M6jCiDCGNW7swo3WNgJV6x";
		}
		else if(twitterHandler.equals("bni46_cs")){
			this.CONSUMER_KEY		  = "n9BLTunGxilyRKFmdnVTKx6d3";
			this.CONSUMER_SECRET 	  = "rVsQwwhDIMp2Qmaa0uvTKKOgzp3BgYH8fXjgYfcj8k8EQWM0im";
			this.ACCESS_TOKEN		  = "3272444328-mD2oKkeDdb9wZd4yXhC9lrwHSkG249Rtmz0NXxb";
			this.ACCESS_TOKEN_SECRET  = "7O78fyKyTy9V3ytZdFSSdFiVSTLgqEaBbISbzPTIXeL6n";
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
