package com.nexworld;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.io.IOException;
import java.util.*;

/**
 * A simple corenlp example ripped directly from the Stanford CoreNLP website using text from wikinews.
 */
public class MainTwitter {

    /**
     * @param args
     * @throws IOException
     * @throws TwitterException
     */
    public static void main(String[] args) throws IOException, TwitterException {


        String subject = "Trump";
        Map<String, String> twits = sentiments(subject);


        //System.out.println("\n\n end");

      /*  List<String> values= new ArrayList<String>();
        values.add("dota");*/
        //   stream(values);
        List<String> values = new ArrayList<String>();

        for (Map.Entry<String, String> entry : twits.entrySet()) {
            // System.out.println(entry.getKey() + "/" + entry.getValue());
            values.add(entry.getValue());
        }

        System.out.println("sentiment moyen des tweets concernant " + subject + " = " + meanSentiment(values));
    }

    public static Map<String, String> sentiments(String searchKeywords) {

        Properties pipelineProps = new Properties();
        Properties tokenizerProps = new Properties();
        pipelineProps.setProperty("annotators", "parse, sentiment");
        pipelineProps.setProperty("parse.binaryTrees", "true");
        pipelineProps.setProperty("enforceRequirements", "false");
        tokenizerProps.setProperty("annotators", "tokenize ssplit");
        StanfordCoreNLP tokenizer = new StanfordCoreNLP(tokenizerProps);
        StanfordCoreNLP pipeline = new StanfordCoreNLP(pipelineProps);


        Map<String, String> results = new HashMap<>();
        if (searchKeywords == null || searchKeywords.length() == 0) {
            return results;
        }
        Set<String> keywords = new HashSet<>();
        for (String keyword : searchKeywords.split(",")) {
            keywords.add(keyword.trim().toLowerCase());
        }
        if (keywords.size() > 3) {
            keywords = new HashSet<>(new ArrayList<>(keywords).subList(0, 3));
        }

        String line = "i'm done with this game i don't like it";


        for (String keyword : keywords) {
            List<Status> statuses = TwitterSearch.search(keyword,20);

            System.out.println("Found  " + statuses.size() + " tweet ...");
            for (Status status : statuses) {

                Annotation annotation = tokenizer.process(status.getText());
                pipeline.annotate(annotation);
                // normal output
                String sentiment = null;
                for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                    sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
                    //   System.out.println(output);
                }

                //  findSentiment(status.getText());

                System.out.println("tweet = " + status.getText() + " //// sentiment " + sentiment + " \n");
                results.put(status.getText().toString(), sentiment.toString());
            }

        }

        return results;
    }

    public static String meanSentiment(List<String> sentiment) {

        List<Integer> list = new ArrayList<Integer>();
        for (String value : sentiment) {

            int toint = convertToint(value);
            list.add(toint);
        }

        int total = 0;
        for (int value : list) {
            total += value;
        }
        float moyenne = (float) total / list.size();
        int res =  Math.round(moyenne);
        String sentimentMoyen = convertTosentiment(res);
        System.out.println(moyenne+ " => " +sentimentMoyen +" => "+res);



        return sentimentMoyen;
    }

    public static int convertToint(String sentiment) {
        if (sentiment.equals("Very Negative")) {
            return 0;
        }
        if (sentiment.equals("Negative")) {
            return 1;
        }
        if (sentiment.equals("Neutral")) {
            return 2;
        }
        if (sentiment.equals("Positive")) {
            return 3;
        }
        if (sentiment.equals("Very positive")) {
            return 4;
        } else {
            return 5;
        }
    }

    public static String convertTosentiment(int sentiment) {
        if (sentiment == 0) {
            return "Very Negative";
        }
        if (sentiment == 1) {
            return "Negative";
        }
        if (sentiment == 2) {
            return "Neutral";
        }
        if (sentiment == 3) {
            return "Positive";
        }
        if (sentiment == 4) {
            return "Very positive";
        } else {
            return "unknow";
        }
    }
}