package biline.core;

import biline.config.*;
import biline.db.*;
import biline.twitter.*;
//import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;


public class TwitterDaemon {
	
	private static String latestStatus;
	private static Status status;
	private static String savedDirectory;
	private static TwitterStream twitterStream;
	private static String recipientId;
	private static String directMsg;
	private static Twitter twitter;
	private static AsyncTwitter asyncTwitter;
	
	public TwitterDaemon() {	
		//Constructor
		setLatestStatus("default");
		setSaveDirectory();
	    //System.out.println("Saving Stream API Result into Destination File: ");
	}

	public static void main(String[] args) throws TwitterException {
		/* Using RESTful API to Update Status */
		// [INIT] 
		// Create (min.) a requesting RESTful API instance
		// If we want to have multiple RESTful API instances
		   //Twitter twitter_one = new TwitterFactory().getInstance(new AccessToken("XXX","XXX"));
		   //Twitter twitter_two = new TwitterFactory().getInstance(new AccessToken("YYY","YYY"));
		// Else, we can use singleton to have only one instance, shared across application life-shelf
		   //Twitter twitter = TwitterFactory.getSingleton();
        
		// [1] Posting non-Async 'New Status' to User's Twitter Account
		
			//try {
			//	setLatestStatus("default");
			//	TwitterDaemon.status = twitter.updateStatus(latestStatus);
			//	System.out.println("Successfully updated the status to [" + TwitterDaemon.status.getText() + "].");
			//} catch (TwitterException e) {
			//	e.printStackTrace();
			//}
		
		// [2] Sending non-Async 'DM' to User's Twitter Account
		
			//try {
			//String msg = "You got DM from @dev_amartha. Thanks for tweeting our hashtags!";
			//DirectMessage message = twitter.sendDirectMessage(recipientId, msg);
		    //System.out.println("Sent: " + message.getText() + " to @" + message.getRecipientScreenName());
			//} catch (TwitterException e) {
			//	e.printStackTrace();
			//}
		   
		/* Using Stream API */
		// [INIT]
		// Connects to the Streaming API - with Amartha OAUTH2 Key n Token
	       TwitterStreamBuilderUtil twitterStreamAmartha = new TwitterStreamBuilderUtil("fahmivanhero");
		   twitterStream = twitterStreamAmartha.getStream();
		   //twitterStream = new TwitterStreamFactory().getInstance();
		
		// [1] User Stream API - Stream Tweets of a Twitter User 
		// Sets (a) the followed users id/handler name (optional) [+] (b) what keywords 
		// to be tracked from the Stream.
		   
		   //args[0] = [1145,1426,2456]
	       //args[1] = [#microfinance,#amartha,#life]
		   
		   //ArrayList<Long>   follow  = new ArrayList<Long>();
	       //ArrayList<String> track   = new ArrayList<String>();
		   ArrayList<String> track   = new ArrayList<String>();
		   track.add("@fahmivanhero #microfinance"); 
		   track.add("@fahmivanhero #amartha");
		   
		   //for (String arg : args) {
	       //   if (isNumericalArgument(arg)) {
	       //     	  for (String id : arg.split(",")) {
	       //       	follow.add(Long.parseLong(id));
	       // 		  }
           //   } else {
	       //     track.addAll(Arrays.asList(arg.split(",")));
	       //   }
	       //}
	       //long[] followArray = new long[follow.size()];
	       //for (int i = 0; i < follow.size(); i++) {
	       //      followArray[i] = follow.get(i);
	       //}
		   
		   //String[] trackArray = track.toArray(new String[track.size()]);
		   
		// Sets stream listener(s) to track events from the Stream: 
		// (1) User stream & (2) Stream's rate limit. 
	       twitterStream.addListener(userStreamlistener);
	       twitterStream.addRateLimitStatusListener(rateLimitStatusListener);
	       
	       twitterStream.filter(new FilterQuery( track.toArray( new String[track.size()] ) ) );
	       
	       //FilterQuery fq = new FilterQuery();
		   //String keywords[] = { "#microfinance", "#life" };
		   //fq.track(keywords);
	       //twitterStream.filter(fq);
	       //twitterStream.filter("@dev_amartha #microfinance,@dev_amartha #life");
	       //twitterStream.user();
	    // Methods: user() & filter() internally create threads respectively, manipulating TwitterStream; e.g. user() simply gets all tweets from its following users.
	    // Methods: user() & filter() then call the appropriate listener methods according to each stream events (such as status, favorite, RT, DM, etc) continuously.
	       
	}
	
	// *Implement a stream listener to track from the Stream prior to being assigned to stream. 
    // *A stream listener has unimplemented multiple methods to respond to multiple events in streams accordingly.
	private static final UserStreamListener userStreamlistener = new UserStreamListener() {
	      
		   @Override
	        public void onStatus(Status status) {
	            System.out.println("onStatus @" + status.getUser().getScreenName() + " - " + status.getText());
	            //try {
	    			//String msg = "You got DM from @dev_amartha. Thanks for tweeting our hashtags!";
	    			//DirectMessage message = twitter.sendDirectMessage(status.getUser().getScreenName(), msg);
	    			//System.out.println("Sent: " + message.getText() + " to @" + message.getRecipientScreenName());
	    		//} catch (TwitterException e) {
	    		//	e.printStackTrace();
	    		//}
	            //AsyncTwitterFactory factory = new AsyncTwitterFactory();
	            //asyncTwitter = factory.getInstance();
	            // OR //
	            //asyncTwitter = AsyncTwitterFactory.getSingleton();
	            twitter      = new TwitterFactory().getInstance();
	            
	            recipientId = status.getUser().getScreenName();
	            directMsg   = "You got DM from @fahmivanhero. Thanks for tweeting our hashtags!";
    			
	            //asyncTwitter.sendDirectMessage(recipientId, directMsg);
	            try {
					twitter.sendDirectMessage(recipientId, directMsg);
				} catch (TwitterException e) {
					e.printStackTrace();
				}
    		    System.out.println("Sent: " + directMsg + " to @" + status.getUser().getScreenName());
	    		
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
	            System.out.println("onFollow source:@"
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
	            System.out.println("onException:" + ex.getMessage());
	        }

	};
	
	// *Implement a Stream's rate limit listener to track from the Stream if limitation occurs esp. in Rate Limit Response 402. 
    // *A Stream's rate limit listener has two unimplemented methods to respond to Rate Limit Response 402 events in streams accordingly.
	private static final RateLimitStatusListener rateLimitStatusListener = new RateLimitStatusListener() {
		
		@Override
		public void onRateLimitStatus(RateLimitStatusEvent event) {
			System.out.println("onRateLimitStatus: " + event.toString());
			System.out.println("Limit["+event.getRateLimitStatus().getLimit() + "], Remaining[" +event.getRateLimitStatus().getRemaining()+"]");
		}
		
		@Override
		public void onRateLimitReached(RateLimitStatusEvent event) {
			System.out.println("onRateLimitReached: " + event.toString());
			System.out.println("Limit["+event.getRateLimitStatus().getLimit() + "], Remaining[" +event.getRateLimitStatus().getRemaining()+"]");
		}
		
	};
	
	public static void setSaveDirectory(){
		TwitterDaemon.savedDirectory = "save";
	}
	
	public static String getSavedDirectory(){
		return savedDirectory;
	}
	
	public static void setLatestStatus(String status){
		if(status.equals("default"))
			TwitterDaemon.latestStatus = "Tweet status posting " + new Date().toString() +" .";
		else
			TwitterDaemon.latestStatus = status;
	}
	
	public static String getLatestStatus(){
		return latestStatus;
	}

}