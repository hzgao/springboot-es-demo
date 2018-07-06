package com.war.es5.config;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.TransportClientFactoryBean;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

@Configuration
public class ElasticSearchConfig {
	private static final Logger logger = LoggerFactory.getLogger(ElasticSearchConfig.class);

	/**
	 * es 集群名称
	 */
	@Value("${spring.data.elasticsearch.clustername}")
	private String clusterName;

	@Value("${spring.data.elasticsearch.hosts}")
	private String hosts;

	@Bean
	protected Client buildClient() {
		logger.debug(String.format("host:[%s]", hosts));
		TransportClientFactoryBean factory = new TransportClientFactoryBean();
		TransportClient client = null;
		try {
			factory.setClientTransportSniff(true);
			factory.setClusterName(clusterName);
			factory.setClusterNodes(hosts);
			factory.afterPropertiesSet();
			client = factory.getObject();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return client;
	}
	
	@Bean
	public ElasticsearchTemplate elasticsearchTemplate() {
		Client client = buildClient();
		ElasticsearchTemplate esTmeplate = new ElasticsearchTemplate(client);
		return esTmeplate;
	}
}
