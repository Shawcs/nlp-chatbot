package com.nexworld;

/**
 * Created by aturbillon on 29/08/2017.
 */
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.Collections;
import java.util.List;

public class TwitterSearch {

    public static List<Status> search(String keyword,int size) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey("tFlYaKGcMKpySsf1vYuFkUO2Y")
                .setOAuthConsumerSecret("Hn8NqtIgeQeNN1f03EljAVuyofup8te4l1oJBP6ra7piehJyPC")
                .setOAuthAccessToken("736263028329619456-lX93jaiEeNgdvnyFfh5UKRN4VTcczLf")
                .setOAuthAccessTokenSecret("gGOl9MXakZtqC4Kn60qqtZTXJsLEPyHE8Ywi09V7mSxZt")
                .setDebugEnabled(true);

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        Query query = new Query(keyword + " -filter:retweets -filter:links -filter:replies ");
        query.setCount(size);
        query.setLocale("en");
        query.setLang("en");

        try {
            QueryResult queryResult = twitter.search(query);

            //for printing purpose
        /*    for (Status status : queryResult.getTweets()) {

               System.out.println("\n twit from  @" + status.getUser().getScreenName() + " \n say :" + status.getText());
            }*/

            return queryResult.getTweets();
        } catch (TwitterException e) {
            // ignore
            e.printStackTrace();
        }
        return Collections.emptyList();

    }


}