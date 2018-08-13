/**
 * 
 */
package com.war.es5.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.DeleteQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.war.es5.service.EsTemplateService;

/**
 * @author admin es业务组件
 */
@Service
public class EsTemplateServiceImpl implements EsTemplateService {
	
	/**
	 * 日志信息
	 */
	public Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ElasticsearchTemplate esTp;
	
	/**
	 * 打印docment info
	 * @param t
	 */
	protected <T> void docmentInfo(T t) {

		Class<? extends Object> clazz = t.getClass();
		if (clazz.isAnnotationPresent(Document.class)) {
			Document document = (Document) clazz.getAnnotation(Document.class);
			Assert.hasText(document.indexName(),
					" Unknown indexName. Make sure the indexName is defined. e.g @Document(indexName=\"foo\")");
			String indexName = document.indexName();
			System.out.println(indexName);
			String indexType = StringUtils.hasText(document.type()) ? document.type()
					: clazz.getSimpleName().toLowerCase(Locale.ENGLISH);
			System.out.println(indexType);
			boolean useServerConfiguration = document.useServerConfiguration();
			System.out.println(useServerConfiguration);
			short shards = document.shards();
			System.out.println(shards);
			short replicas = document.replicas();
			System.out.println(replicas);
			String refreshInterval = document.refreshInterval();
			System.out.println(refreshInterval);
			String indexStoreType = document.indexStoreType();
			System.out.println(indexStoreType);
			boolean createIndexAndMapping = document.createIndex();
			System.out.println(createIndexAndMapping);
		}
	
	}

	/**
	 * entity 保存
	 */
	@Override
	public <T> void save(T t) {
		Class<? extends Object> clazz = t.getClass();
		if (clazz.isAnnotationPresent(Document.class)) {
			Document document = (Document) clazz.getAnnotation(Document.class);
			Assert.hasText(document.indexName(),
					" Unknown indexName. Make sure the indexName is defined. e.g @Document(indexName=\"foo\")");
			String indexName = document.indexName();
			String indexType = StringUtils.hasText(document.type()) ? document.type()
					: clazz.getSimpleName().toLowerCase(Locale.ENGLISH);
			// 插入
			IndexQuery indexQuery = new IndexQuery();
			indexQuery.setIndexName(indexName);
			indexQuery.setType(indexType);
			indexQuery.setObject(t);
			//ID TODO 
			List<IndexQuery> queries = new ArrayList<IndexQuery>();
			queries.add(indexQuery);
			esTp.bulkIndex(queries);
		}
	}

	
	@Override
	public <T> void update(T t) {
		// TODO Auto-generated method stub
	}

	@Override
	public <T> void deleteIndex(T t) {
		Class<? extends Object> clazz = t.getClass();
		if (clazz.isAnnotationPresent(Document.class)) {
			Document document = (Document) clazz.getAnnotation(Document.class);
			Assert.hasText(document.indexName(),
					" Unknown indexName. Make sure the indexName is defined. e.g @Document(indexName=\"foo\")");
			String indexName = document.indexName();
//			String indexType = StringUtils.hasText(document.type()) ? document.type()
//					: clazz.getSimpleName().toLowerCase(Locale.ENGLISH);
			// 删除索引
			esTp.deleteIndex(indexName);
		}
	}

	@Override
	public <T> void createIndex(T t) throws IOException {
		Class<? extends Object> clazz = t.getClass();
		if (clazz.isAnnotationPresent(Document.class)) {
			Document document = (Document) clazz.getAnnotation(Document.class);
			Assert.hasText(document.indexName(),
					" Unknown indexName. Make sure the indexName is defined. e.g @Document(indexName=\"foo\")");
			String indexName = document.indexName();
			String indexType = StringUtils.hasText(document.type()) ? document.type()
					: clazz.getSimpleName().toLowerCase(Locale.ENGLISH);
			
			//ik 分词器ik_max_word  ik_smart
			XContentBuilder setting = XContentFactory.jsonBuilder()
					.startObject()
					.startObject("analysis")
					.startObject("analyzer")
					.startObject("ik")
					.field("tokenizer", "ik_max_word")
					.endObject()
					.endObject()
					.endObject()
					.endObject();
			setting.prettyPrint();
			logger.info(String.format("setting:%s", setting.string()));
			
			//mapping 映射
			XContentBuilder mapping = XContentFactory.jsonBuilder()
					.startObject()
					.startObject("properties");
			//属性获取
			java.lang.reflect.Field[] atris = clazz.getDeclaredFields();
			for (java.lang.reflect.Field item : atris) {
				logger.info(String.format("flied:%s", item.getName()));
				org.springframework.data.elasticsearch.annotations.Field[] esFields = item.getDeclaredAnnotationsByType(org.springframework.data.elasticsearch.annotations.Field.class);
				for (org.springframework.data.elasticsearch.annotations.Field fliedType : esFields) {
					logger.info(String.format("fliedType:%s", fliedType.toString()));
					mapping.startObject(item.getName())
					.field("type",fliedType.type().toString().toLowerCase())
					.field("analyzer",fliedType.analyzer().toString().toLowerCase())
					.field("search_analyzer",fliedType.searchAnalyzer().toString().toLowerCase())
					.endObject();
				}

			}
			mapping
			.endObject()
			.endObject();
			mapping.prettyPrint();
			logger.info(String.format("mapping:%s", mapping.string()));
			//创建索引
			esTp.deleteIndex(indexName);
			esTp.createIndex(indexName,setting);
			esTp.putMapping(indexName, indexType, mapping);
			esTp.refresh(indexName);
		}
	}

	@Override
	public <T> void delete(T t) {
		Class<? extends Object> clazz = t.getClass();
		if (clazz.isAnnotationPresent(Document.class)) {
			Document document = (Document) clazz.getAnnotation(Document.class);
			Assert.hasText(document.indexName(),
					" Unknown indexName. Make sure the indexName is defined. e.g @Document(indexName=\"foo\")");
			String indexName = document.indexName();
			String indexType = StringUtils.hasText(document.type()) ? document.type()
					: clazz.getSimpleName().toLowerCase(Locale.ENGLISH);
			//删除es数据
			BoolQueryBuilder queryBuilder=QueryBuilders.boolQuery();
			//对象转成map
			JSONObject obj = JSONObject.parseObject(JSON.toJSONString(t));
			for(Map.Entry<String, Object> entry : obj.entrySet()) {
				//绝对匹配
				queryBuilder.must(QueryBuilders.termQuery(entry.getKey(), 
						entry.getValue()));
			}
			
			//删除
			DeleteQuery del = new DeleteQuery();
			del.setIndex(indexName);
			del.setType(indexType);
			del.setQuery(queryBuilder);
			
			esTp.delete(del);
		}
	}

}
