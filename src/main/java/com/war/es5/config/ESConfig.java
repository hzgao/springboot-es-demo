/**
 * 
 */
package com.war.es5.config;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.util.CollectionUtils;

/**
 * @author admin es 配置
 */
@Configuration
public class ESConfig {

	private static final Logger logger = LoggerFactory.getLogger(ESConfig.class);

	/**
	 * es 集群名称
	 */
	@Value("${spring.data.elasticsearch.clustername}")
	private String clusterName;

	/**
	 * es 链接端口
	 */
	@Value("${spring.data.elasticsearch.port}")
	private int port;

	@Value("#{'${spring.data.elasticsearch.hosts}'.split(',')}")
	private List<String> hosts = new ArrayList<>();

	/**
	 * es 配置
	 * 
	 * @return
	 */
	private Settings settings() {

		Settings settings = Settings.builder().put("cluster.name", clusterName).put("client.transport.sniff", true)
				.build();
		settings.keySet().stream().forEach(h -> {
			logger.debug(String.format("key:%s----value:%s", h, settings.get(h)));
		});
		// for(String key : settings.keySet()) {
		// logger.debug(String.format("key:%s----value:%s", key,settings.get(key)));
		// }
		return settings;
	}

	@Bean
	protected Client buildClient() {
		TransportClient preBuiltTransportClient = new PreBuiltTransportClient(settings());

		if (!CollectionUtils.isEmpty(hosts)) {
			hosts.stream().forEach(h -> {
				try {
					preBuiltTransportClient
							.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(h), port));
				} catch (UnknownHostException e) {
					logger.error("Error addTransportAddress,with host:{}.", h);
				}
			});
		}
		return preBuiltTransportClient;
	}

	@Bean
	public ElasticsearchTemplate elasticsearchTemplate() {
		Client client = buildClient();
		ElasticsearchTemplate esTmeplate = new ElasticsearchTemplate(client);
		// esTmeplate.getClient().threadPool().builders().forEach(h->{
		// logger.debug(String.format("key:%s", h));
		// h.getRegisteredSettings().forEach(h1->{
		// logger.debug(String.format("key2:%s", h1));
		// });
		// });
		return esTmeplate;
	}
}
