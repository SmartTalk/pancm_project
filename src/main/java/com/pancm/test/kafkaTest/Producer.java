package com.pancm.test.kafkaTest;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class Producer extends Thread {

	private final KafkaProducer<String, String> producer;
	private final String topic;
   
	/**
	 * 
	 * @param kafkaStr   kafka地址
	 * @param topic      消息名称
	 * @param
	 */
	public Producer(String kafkaStr, String topic) {
		Properties props = new Properties();
		props.put("bootstrap.servers", kafkaStr);
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 16384);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		props.put("key.serializer", StringSerializer.class.getName());
		props.put("value.serializer", StringSerializer.class.getName());
		this.producer = new KafkaProducer<String, String>(props);
		this.topic = topic;
	}

	@Override
	public void run() {
		int messageNo = 1;
		try {
			while (true) {
				String messageStr = "Message_" + messageNo;
				if(messageNo%100==0){
					System.out.println("Send:" + messageStr);
				}
				producer.send(new ProducerRecord<String, String>(topic, "Message", messageStr));
				messageNo++;
				sleep(20);
				if(messageNo%1000==0){
					break;
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			producer.close();
		}
	}

}
