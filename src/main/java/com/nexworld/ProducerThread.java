package com.nexworld;

//lien interressant howtoprogram.xyz/2016/05/29/create-multi-threaded-apache-kafka-consumer/
//schema registry http://docs.confluent.io/1.0/schema-registry/docs/serializer-formatter.html
//partitionner http://howtoprogram.xyz/2016/06/04/write-apache-kafka-custom-partitioner/

import java.util.Properties;


/**
 * Created by aturbillon on 07/04/2017.
 */
public class ProducerThread implements Runnable {

    private final String topic;
    private final String DATA_SOURCE;
    private final String BROKERS;

    public ProducerThread(String brokers, String topic, String data_source) {

        Properties prop = createProducerConfig(brokers);
        this.topic = topic;
        this.DATA_SOURCE = data_source; //we added source because we fake stream and we are ready a file
        this.BROKERS = brokers;
    }

    private static Properties createProducerConfig(String brokers) {
        Properties props = new Properties();
        props.put("bootstrap.servers", brokers);
        props.put("client.id", "client_1");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 10);
        props.put("buffer.memory", 33554432);
        props.put("request.timeout.ms", 2000);//see if work
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return props;
    }

    @Override
    public void run() {
    }

    //we call the function from consumerGroup that calculate the number of partition from a topic + broker list the group id is just here to keep trace from who is doing what in zookeeper
    public int getNbrPartition() {
        return 2;
    }



}
