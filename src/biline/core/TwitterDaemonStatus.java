package biline.core;

import biline.config.*;
import biline.db.*;
import biline.twitter.*;
import twitter4j.AsyncTwitter;
import twitter4j.AsyncTwitterFactory;
import twitter4j.DirectMessage;
import twitter4j.FilterQuery;
import twitter4j.RateLimitStatus;
import twitter4j.RateLimitStatusEvent;
import twitter4j.RateLimitStatusListener;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserStreamListener;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ListIterator;
import java.util.Map;


public class TwitterDaemonStatus {
	
	private static final String twitterUser    = "biline_dev";
	//bni46 //bni46 //bni46_cs //fahmivanhero //biline_dev //dev_amartha
	private static final String twitterAccount = "biline_dev";
	//BNI //BNI //BNICustomerCare //fahmivanhero //biline_dev //dev_amartha
	
	private static String latestStatus;
	private static Status status;
	private static String savedDirectory;
	
	private static TwitterStream twitterStream;
	private static TwitterStream twitterStreamDM;
	private static TwitterLogging twitterLog;
	
	private static String recipientId;
	private static String directMsg;
	
	private static Twitter twitter;
	private static Twitter twitterDM;
	private static AsyncTwitter asyncTwitter;
	private static AsyncTwitter asyncTwitterDM;

	private static Connection con;
	private static Statement stm;
	private static ResultSet rs;
	private static ResultSet rs2;
	private static ResultSet rs3;
	private static String command;
	private static MysqlConnect db_object;
	
	private static SimpleDateFormat dateFormat;
	private static String today;
	
	private static ArrayList<String> hashtags;
	private static ArrayList<String> menus;
	private static ArrayList<String> aliasmenus;
	private static ArrayList<String> directMessagesPromoAndServices;
	private static ArrayList<String> directMessagesForMentions;
	private static TwitterTweetExtractorUtil tagExtractor;
	private static TwitterStatisticsUtil statLogger;
	
	public TwitterDaemonStatus() {	
		//Constructor
		setLatestStatus("default");
		setSaveDirectory();
	}

	public static void main(String[] args) throws TwitterException {
		// *Listeners' shared (data structured) objects
		hashtags         = new ArrayList<String>();
		menus 		     = new ArrayList<String>();
		aliasmenus 		 = new ArrayList<String>();
		
		tagExtractor     = new TwitterTweetExtractorUtil("");
		statLogger		 = new TwitterStatisticsUtil();
		twitterLog   	 = new TwitterLogging();
		
		directMessagesForMentions       = new ArrayList<String>();
		directMessagesPromoAndServices  = new ArrayList<String>();
		
		dateFormat       =  new SimpleDateFormat("yyyy-MM-dd");
		
		// *Connecting to DB & Instantiate Accessing DB Object
		stm = null; rs = null; rs2 = null; rs3 = null;
		try{
			db_object = new MysqlConnect();
			con 	  = db_object.getConnection(); 
		} catch(ClassNotFoundException cnfe){
			cnfe.printStackTrace();
		} catch(SQLException sqle){
			sqle.printStackTrace();
		}
		
		   /* Using Stream API */
		   // [INIT]
		   // Connects to the Streaming API - with OAUTH2 Key n Token
	       TwitterStreamBuilderUtil twitterStreamBuilder = new TwitterStreamBuilderUtil(twitterUser);
		   twitterStream   = twitterStreamBuilder.getStream();
		   asyncTwitter    = AsyncTwitterFactory.getSingleton();
           twitter         = TwitterFactory.getSingleton();
           
		   ArrayList<String> trackMention   = new ArrayList<String>();
		   String listOfTagsQuery           = "SELECT * FROM tbl_hashtags_mention"; 
		   
		   try {
			    if(con == null){
					  db_object.openConnection();
					  con = db_object.getConnection();
		        }   
				stm = con.createStatement();
				rs  = stm.executeQuery(listOfTagsQuery);
				System.out.print("\n[KEYWORDS] Trackings: ");
				int n = 0;
				while (rs.next()){
					if(n % 10 == 0)
						System.out.print("\n");
					System.out.print("#" + rs.getString("hashtag_term") + " ");
					trackMention.add("@" + twitterUser + " #" + rs.getString("hashtag_term"));
					n++;
		   		}
				System.out.print("\n[KEYWORDS] " + n + " keywords being tracked for handler @" + twitterUser + "\n");
		   } catch (SQLException e) {
			   	e.printStackTrace();
		   } finally{
			    if(con != null){
					  try {
						db_object.closeConnection();
					  } catch (SQLException e) {
						e.printStackTrace();
					  } finally{
						  con = null;
					  }
		        }
		   }
		   
		   FilterQuery fq    = new FilterQuery();
	       String keywords[] = { "@biline_dev #taplusmuda", "@biline_dev #twitbank", "@biline_dev #bni" };
		   //String keywords[] = { "#microfinance", "#life" };
		   //String keywords[]   = trackMention.toArray(new String[trackMention.size()]);
	       //String[] trackArray = trackMention.toArray(new String[trackMention.size()]);
	       fq.track(keywords);
		   
	       // Sets stream listener(s) to track events from the Stream: 
	       // (1) User stream & (2) Stream's rate limit. 
	       twitterStream.addListener(userStreamlistener);
	       twitterStream.addRateLimitStatusListener(rateLimitStatusListener);

	       // *TO LISTEN TO STATUS MENTIONS
	       //twitterStream.filter(filter);
	       //twitterStream.filter("@dev_amartha #microfinance, @dev_amartha #life");
	       //twitterStream.filter(fq);
	       twitterStream.user();
	   
	}
	
	// *Implement a stream listener to track from the Stream prior to being assigned to stream. 
    // *A stream listener has unimplemented multiple methods to respond to multiple events in streams accordingly.
	private static final UserStreamListener userStreamlistener = new UserStreamListener() {
	      
		   @Override
	        public void onStatus(Status status) {
			    //System.out.println("onStatus @" + status.getUser().getScreenName() + " - " + status.getText());
			   	String hashtag_string = "";
			    //Extract hashtags from status
	            hashtags = tagExtractor.parseTweetForHashtags(status.getText());
	            
	            for (String tag : hashtags) { hashtag_string += tag.toLowerCase() + " "; }
	            

	            System.out.println("onStatus @" + status.getUser().getScreenName() + " - " + status.getText());
	            System.out.println("HashTags String: " + hashtag_string + "\n\n");
	            
	            /*
	            // *We compose DM per hashTAGS
	            String promotionsQuery = "";
	            stm = null; rs = null;
	            
	            for (String tag : hashtags) {
	            	Date now = new Date();
	            	today    = dateFormat.format(now);
	            	promotionsQuery = "SELECT * FROM tbl_promotions LEFT JOIN tbl_hashtags " 
	            					+ "ON tbl_hashtags.hashtag_id = tbl_promotions.promotion_hashtag "
	            					+ "WHERE tbl_hashtags.hashtag_term = '" + tag + "' "
	            					+ "AND tbl_promotions.promotion_enddate >= '" + today + "' AND tbl_promotions.promotion_deleted = '0'";
	            	//System.out.println(promotionsQuery);
	            	
	     		   	try {
			     		if(con == null){
			     			db_object.openConnection();
			  				con = db_object.getConnection();
			  	        }
		     			stm = con.createStatement();
		     			rs  = stm.executeQuery(promotionsQuery);
		     			while (rs.next()){
		     				directMessagesForMentions.add(rs.getString("promotion_content"));
		     				//System.out.println("DM with: " + tag);
		     	   		   }
	     		   	} catch (SQLException e) {
	     		   		e.printStackTrace();
	     		   	} finally{
		     		   	if(con != null){
		  				  try {
							db_object.closeConnection();
		  				  } catch (SQLException e) {
							e.printStackTrace();
		  				  } finally{
		  				  		con = null;
		     		   		}
		     		   	}
	     		   	}
	     		   	
	     		   	// *Log for Statistics
		            statLogger.eventsLog("1", status.getUser().getScreenName(), tag);
				}
	            //System.out.println("DMes are: "); 
	            //System.out.println(directMessagesForMentions);
	            
	            // *We then send out all possible DM per hashTAGS
	            int i = 0;
	            for (String msg : directMessagesForMentions) {
		            recipientId = status.getUser().getScreenName();
		            directMsg = msg;
		            try {
		            	if(!recipientId.equalsIgnoreCase(twitterAccount) && !directMsg.equalsIgnoreCase("")){
	     					DirectMessage message = twitterDM.sendDirectMessage(recipientId, directMsg);
	     				}
						//System.out.println("Sent: " + message.getText() + " to @" + status.getUser().getScreenName());
			            //asyncTwitter.sendDirectMessage(recipientId, directMsg);
		    		    //System.out.println("Sent: " + directMsg + " to @" + status.getUser().getScreenName());
					} catch (TwitterException e) {
						e.printStackTrace();
					}
		            
		            // *Log for Statistics
		            if(i <hashtags.size()){
		            	twitterLog.saveOutwardDM(status.getUser().getScreenName(), msg, "Mention Reply", hashtags.get(i));
		            }
		            i++;
	            }
	            
	            // *Freeing up memory   
	            hashtags.clear(); 
	            directMessagesForMentions.clear();
	            */
	        }

	        @Override
	        public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
	            System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
	        }

	        @Override
	        public void onDeletionNotice(long directMessageId, long userId) {
	            System.out.println("Got a direct message deletion notice id:" + directMessageId);
	        }

	        @Override
	        public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
	            System.out.println("Got a track limitation notice:" + numberOfLimitedStatuses);
	        }

	        @Override
	        public void onScrubGeo(long userId, long upToStatusId) {
	            System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
	        }

	        @Override
	        public void onStallWarning(StallWarning warning) {
	            System.out.println("Got stall warning:" + warning);
	        }

	        @Override
	        public void onFriendList(long[] friendIds) {
	            System.out.print("onFriendList");
	            for (long friendId : friendIds) {
	                System.out.print(" " + friendId);
	            }
	            System.out.println();
	        }

	        @Override
	        public void onFavorite(User source, User target, Status favoritedStatus) {
	            System.out.println("onFavorite source:@"
	                + source.getScreenName() + " target:@"
	                + target.getScreenName() + " @"
	                + favoritedStatus.getUser().getScreenName() + " - "
	                + favoritedStatus.getText());
	        }

	        @Override
	        public void onUnfavorite(User source, User target, Status unfavoritedStatus) {
	            System.out.println("onUnFavorite source:@"
	                + source.getScreenName() + " target:@"
	                + target.getScreenName() + " @"
	                + unfavoritedStatus.getUser().getScreenName()
	                + " - " + unfavoritedStatus.getText());
	        }

	        @Override
	        public void onFollow(User source, User followedUser) {
	            System.out.println("onFollow fromFilterStream source:@"
	                + source.getScreenName() + " target:@"
	                + followedUser.getScreenName());
	        }

	        @Override
	        public void onUnfollow(User source, User followedUser) {
	            System.out.println("onFollow source:@"
	                + source.getScreenName() + " target:@"
	                + followedUser.getScreenName());
	        }

	        @Override
	        public void onDirectMessage(DirectMessage directMessage) {
	            System.out.println("onDirectMessage text:"
	                + directMessage.getText());
	        }

	        @Override
	        public void onUserListMemberAddition(User addedMember, User listOwner, UserList list) {
	            System.out.println("onUserListMemberAddition added member:@"
	                + addedMember.getScreenName()
	                + " listOwner:@" + listOwner.getScreenName()
	                + " list:" + list.getName());
	        }

	        @Override
	        public void onUserListMemberDeletion(User deletedMember, User listOwner, UserList list) {
	            System.out.println("onUserListMemberDeleted deleted member:@"
	                + deletedMember.getScreenName()
	                + " listOwner:@" + listOwner.getScreenName()
	                + " list:" + list.getName());
	        }

	        @Override
	        public void onUserListSubscription(User subscriber, User listOwner, UserList list) {
	            System.out.println("onUserListSubscribed subscriber:@"
	                + subscriber.getScreenName()
	                + " listOwner:@" + listOwner.getScreenName()
	                + " list:" + list.getName());
	        }

	        @Override
	        public void onUserListUnsubscription(User subscriber, User listOwner, UserList list) {
	            System.out.println("onUserListUnsubscribed subscriber:@"
	                + subscriber.getScreenName()
	                + " listOwner:@" + listOwner.getScreenName()
	                + " list:" + list.getName());
	        }

	        @Override
	        public void onUserListCreation(User listOwner, UserList list) {
	            System.out.println("onUserListCreated  listOwner:@"
	                + listOwner.getScreenName()
	                + " list:" + list.getName());
	        }

	        @Override
	        public void onUserListUpdate(User listOwner, UserList list) {
	            System.out.println("onUserListUpdated  listOwner:@"
	                + listOwner.getScreenName()
	                + " list:" + list.getName());
	        }

	        @Override
	        public void onUserListDeletion(User listOwner, UserList list) {
	            System.out.println("onUserListDestroyed  listOwner:@"
	                + listOwner.getScreenName()
	                + " list:" + list.getName());
	        }

	        @Override
	        public void onUserProfileUpdate(User updatedUser) {
	            System.out.println("onUserProfileUpdated user:@" + updatedUser.getScreenName());
	        }

	        @Override
	        public void onUserDeletion(long deletedUser) {
	            System.out.println("onUserDeletion user:@" + deletedUser);
	        }

	        @Override
	        public void onUserSuspension(long suspendedUser) {
	            System.out.println("onUserSuspension user:@" + suspendedUser);
	        }

	        @Override
	        public void onBlock(User source, User blockedUser) {
	            System.out.println("onBlock source:@" + source.getScreenName()
	                + " target:@" + blockedUser.getScreenName());
	        }

	        @Override
	        public void onUnblock(User source, User unblockedUser) {
	            System.out.println("onUnblock source:@" + source.getScreenName()
	                + " target:@" + unblockedUser.getScreenName());
	        }

	        @Override
	        public void onRetweetedRetweet(User source, User target, Status retweetedStatus) {
	            System.out.println("onRetweetedRetweet source:@" + source.getScreenName()
	                + " target:@" + target.getScreenName()
	                + retweetedStatus.getUser().getScreenName()
	                + " - " + retweetedStatus.getText());
	        }

	        @Override
	        public void onFavoritedRetweet(User source, User target, Status favoritedRetweet) {
	            System.out.println("onFavroitedRetweet source:@" + source.getScreenName()
	                + " target:@" + target.getScreenName()
	                + favoritedRetweet.getUser().getScreenName()
	                + " - " + favoritedRetweet.getText());
	        }

	        @Override
	        public void onQuotedTweet(User source, User target, Status quotingTweet) {
	            System.out.println("onQuotedTweet" + source.getScreenName()
	                + " target:@" + target.getScreenName()
	                + quotingTweet.getUser().getScreenName()
	                + " - " + quotingTweet.getText());
	        }

	        @Override
	        public void onException(Exception ex) {
	            ex.printStackTrace();
	            System.out.println("onException filterListener:" + ex.getMessage());
	        }

	};


	// *Implement a Stream's rate limit listener to track from the Stream if limitation occurs esp. in Rate Limit Response 402. 
    // *A Stream's rate limit listener has two unimplemented methods to respond to Rate Limit Response 402 events in streams accordingly.
	private static final RateLimitStatusListener rateLimitStatusListener = new RateLimitStatusListener() {
		
		@Override
		public void onRateLimitStatus(RateLimitStatusEvent event) {
			System.out.println("onRateLimitStatus: " + event.toString());
			System.out.println("Limit["+event.getRateLimitStatus().getLimit() + "], Remaining[" +event.getRateLimitStatus().getRemaining()+"]");
			int remaining = event.getRateLimitStatus().getRemaining();
			int resetInterval = event.getRateLimitStatus().getSecondsUntilReset() * 1000;
			if(remaining < 2)
			{
				try {
					Thread.sleep(resetInterval+5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		@Override
		public void onRateLimitReached(RateLimitStatusEvent event) {
			System.out.println("onRateLimitReached: " + event.toString());
			System.out.println("Limit["+event.getRateLimitStatus().getLimit() + "], Remaining[" +event.getRateLimitStatus().getRemaining()+"]");
			try {
				Thread.sleep(900000+5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	};
	
	public static void setSaveDirectory(){
		TwitterDaemonStatus.savedDirectory = "save";
	}
	
	public static String getSavedDirectory(){
		return savedDirectory;
	}
	
	public static void setLatestStatus(String status){
		if(status.equals("default"))
			TwitterDaemonStatus.latestStatus = "Tweet status posting " + new Date().toString() +" .";
		else
			TwitterDaemonStatus.latestStatus = status;
	}
	
	public static String getLatestStatus(){
		return latestStatus;
	}

}
