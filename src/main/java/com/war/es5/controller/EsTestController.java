/**
 * 
 */
package com.war.es5.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder.Field;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.DefaultEntityMapper;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.DeleteQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.war.es5.entity.EsStructuredInfo;
import com.war.es5.entity.TestEntity;
import com.war.es5.entity.TestEntityHighlight;
import com.war.es5.repository.EsTestRepository;
import com.war.es5.service.EsTemplateService;

/**
 * @author admin
 *
 */
@Controller
@RequestMapping("/estest")
public class EsTestController {

	@Autowired
	private ElasticsearchTemplate esTp;
	@Autowired
	private EsTestRepository repository;
	
	@Autowired
	private EsTemplateService service;
	
	/**
	 * 保存-- ElasticsearchTemplate接口例子 entity 可以没有id
	 * 
	 * @param document
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/createIndex", method = RequestMethod.POST)
	public ResponseEntity<HttpStatus> createIndex(@RequestBody TestEntity document) throws IOException {

		// 插入
		service.createIndex(document);
		return new ResponseEntity<HttpStatus>(HttpStatus.ALREADY_REPORTED, HttpStatus.OK);
	}
	
	/**
	 * 保存-- ElasticsearchTemplate接口例子 entity 可以没有id
	 * 
	 * @param document
	 * @return
	 */
	@RequestMapping(value = "/rmIndex", method = RequestMethod.POST)
	public ResponseEntity<HttpStatus> rmIndex(@RequestBody TestEntity document) {

		// 删除
//		EsStructuredInfo es = new EsStructuredInfo();
		service.deleteIndex(document);
		return new ResponseEntity<HttpStatus>(HttpStatus.ALREADY_REPORTED, HttpStatus.OK);
	}
	

	/**
	 * 保存-- ElasticsearchTemplate接口例子 entity 可以没有id
	 * 
	 * @param document
	 * @return
	 */
	@RequestMapping(value = "/saveT", method = RequestMethod.POST)
	public ResponseEntity<HttpStatus> saveT(@RequestBody TestEntity document) {

		// 插入
		service.save(document);
		return new ResponseEntity<HttpStatus>(HttpStatus.ALREADY_REPORTED, HttpStatus.OK);
	}

	

	/**
	 * 保存-- ElasticsearchTemplate接口例子 entity 可以没有id
	 * 
	 * @param document
	 * @return
	 */
	@RequestMapping(value = "/saveByEsTp", method = RequestMethod.POST)
	public ResponseEntity<HttpStatus> saveByEsTp(@RequestBody TestEntity document) {

		// 插入
		List<IndexQuery> queries = new ArrayList<IndexQuery>();
		IndexQuery indexQuery = new IndexQuery();
		// indexQuery.setIndexName(indexName);
		indexQuery.setObject(document);
		queries.add(indexQuery);
		if (!esTp.indexExists(document.getClass())) {
			esTp.createIndex(document.getClass());
		}
		esTp.bulkIndex(queries);
		return new ResponseEntity<HttpStatus>(HttpStatus.ALREADY_REPORTED, HttpStatus.OK);
	}

	/**
	 * 保存-- Repository接口例子
	 * 
	 * @param document
	 * @return
	 */
	@RequestMapping(value = "/saveByRepository", method = RequestMethod.POST)
	public ResponseEntity<TestEntity> saveByRepository(@RequestBody TestEntity document) {
		TestEntity rel = repository.save(document);
		// repository.saveAll(arg0)
		return new ResponseEntity<TestEntity>(rel, HttpStatus.OK);
	}

	/**
	 * delete-- Repository接口例子
	 * 
	 * @param document
	 * @return
	 */
	@RequestMapping(value = "/deleteByRepository", method = RequestMethod.POST)
	public ResponseEntity<HttpStatus> deleteByRepository(@RequestBody TestEntity document) {
		// 删除
		repository.delete(document);
		return new ResponseEntity<HttpStatus>(HttpStatus.OK, HttpStatus.OK);
	}

	/**
	 * 高亮 ElasticsearchTemplate接口例子
	 * 
	 * @param document
	 * @return
	 */
	@RequestMapping(value = "/searchHightLightByTp", method = RequestMethod.POST)
	public ResponseEntity<List<JSONObject>> searchHightLightByTp(@RequestBody JSONObject document) {
		String[] fliedname = { "text1", "text2" };
		// 精确匹配
		QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(document.getString("key"), fliedname);
		System.out.println("builder.toString()  " + queryBuilder.toString());

		// 高亮字段
		Field[] field = { new Field(fliedname[0]), new Field(fliedname[1]) };
		// 分页
		Pageable pageable = new PageRequest(0, 100);
		SearchQuery searchquery = new NativeSearchQueryBuilder()	
				//查询哪些字段出来
				// .withFields(fliedname) 
				// 哪些字段高亮
				.withHighlightFields(field) 
				// 查询条件
				.withQuery(queryBuilder) 
				.withPageable(pageable)
				.build();

		List<JSONObject> lst = esTp.query(searchquery, new ResultsExtractor<List<JSONObject>>() {
			DefaultEntityMapper defaultEntityMapper = new DefaultEntityMapper();

			@Override
			public List<JSONObject> extract(SearchResponse arg0) {
				// 解析命中结果
				return StreamSupport.stream(arg0.getHits().spliterator(), false).map(this::searchHitToMyClass)
						.collect(Collectors.toList());
			}

			// 解析结果结果集
			private JSONObject searchHitToMyClass(SearchHit searchHit) {
				// 序列化命中的对象
				JSONObject myObject = null;
				try {
					myObject = defaultEntityMapper.mapToObject(searchHit.getSourceAsString(), JSONObject.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
				// id 需要set 如果保存是使用es的_id
				myObject.put("id", searchHit.getId());
				// 高亮结果集.
				Map<String, String> mapHighlight = new HashMap<String, String>();
				
				Map<String, HighlightField> mapHighlights = searchHit.getHighlightFields();
				mapHighlights.forEach((key, value) -> {
					System.out.println("key = " + key);
					System.out.println("value = " + value);
				});
				mapHighlights.values().stream().forEach(highlightField -> {

					System.out.println("name = " + highlightField.getName());
					System.out.println("Fragments = " + highlightField.getFragments().toString());
					Arrays.stream(highlightField.getFragments()).forEach(text -> {
						System.out.println("text = " + text.string());
						mapHighlight.put(highlightField.getName(), text.string());
					});
				});
				myObject.put("highlight", mapHighlight);
				return myObject;
			}

		});

		return new ResponseEntity<List<JSONObject>>(lst, HttpStatus.OK);
	}

	/**
	 * 高亮 ElasticsearchTemplate接口例子
	 * 
	 * @param document
	 * @return
	 */
	@RequestMapping(value = "/searchPageByTp", method = RequestMethod.POST)
	public ResponseEntity<List<TestEntityHighlight>> searchPageByTp(@RequestBody JSONObject document) {
		String[] fliedname = { "text1", "text2" };
		// 精确匹配
		QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(document.getString("key"), fliedname);
		System.out.println("builder.toString()  " + queryBuilder.toString());
		// 高亮字段
		Field[] field = { new Field(fliedname[0]), new Field(fliedname[1]) };
		// 分页
		Pageable pageable = new PageRequest(0, 1);
		SearchQuery searchquery = new NativeSearchQueryBuilder()
				//查询哪些字段出来
				// .withFields(fliedname) 
				// 哪些字段高亮
				.withHighlightFields(field) 
				// 查询条件
				.withQuery(queryBuilder) 
				 //分页
				.withPageable(pageable) 
				.build();
//		SearchResultMapper resultMapper = new DefaultResultMapper().mapResults(response, clazz, pageable);
		//一定要有entity
		List<TestEntityHighlight>  lst = esTp.queryForList(searchquery, TestEntityHighlight.class);
//		AggregatedPage<TestEntityHighlight> pages = esTp.queryForPage(searchquery, TestEntityHighlight.class, new SearchResultMapper() {
//			DefaultEntityMapper defaultEntityMapper = new DefaultEntityMapper();
//			
//			@Override
//			public <T> AggregatedPage<T> mapResults(SearchResponse searchresponse, Class<T> class1, Pageable pageable) {
//				
//				System.out.println("pageable.getPageSize()" + pageable.getPageSize());
//				// 解析命中结果
//				List<TestEntityHighlight> lstOBJ = StreamSupport.stream(searchresponse.getHits().spliterator(), false)
//						.map(this::searchHitToMyClass).collect(Collectors.toList());
////				new AggregatedPageImpl<>(content, pageable, total)
//				//坑噢
//				return new AggregatedPageImpl<T>((List<T>) lstOBJ);
//			}
//
//			// 解析结果结果集
//			private TestEntityHighlight searchHitToMyClass(SearchHit searchHit) {
//				// 序列化命中的对象
//				TestEntityHighlight myObject = null;
//				try {
//					myObject = defaultEntityMapper.mapToObject(searchHit.getSourceAsString(), TestEntityHighlight.class);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				// id 需要set 如果保存是使用es的_id
//				myObject.setId(searchHit.getId());
//				// 高亮结果集.
//				Map<String, String> mapHighlight = new HashMap<String, String>();
//				Map<String, HighlightField> mapHighlights = searchHit.getHighlightFields();
//				mapHighlights.forEach((key, value) -> {
//					System.out.println("key = " + key);
//					System.out.println("value = " + value);
//				});
//				mapHighlights.values().stream().forEach(highlightField -> {
//
//					System.out.println("name = " + highlightField.getName());
//					System.out.println("Fragments = " + highlightField.getFragments().toString());
//					Arrays.stream(highlightField.getFragments()).forEach(text -> {
//						System.out.println("text = " + text.string());
//						mapHighlight.put(highlightField.getName(), text.string());
//					});
//				});
//				myObject.setMapHighlight(mapHighlight);
//				return myObject;
//			}
//
//		});
	
		return new ResponseEntity<List<TestEntityHighlight>>(lst, HttpStatus.OK);
	}

	/**
	 * 查询 Repository接口例子
	 * 
	 * @param document
	 * @return
	 */
	@RequestMapping(value = "/searchByRepository", method = RequestMethod.POST)
	public ResponseEntity<Page<TestEntity>> searchByRepository(@RequestBody JSONObject document) {
		String[] fliedname = { "text1", "text2" };
		// 精确匹配
		QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(document.getString("key"), fliedname);
		System.out.println("builder.toString()  " + queryBuilder.toString());
		// 高亮字段 没效果
		 Field[] field = {new Field(fliedname[0]),new Field(fliedname[1])};
		SearchQuery searchquery = new NativeSearchQueryBuilder()
				//查询哪些字段出来
				 .withFields(fliedname) 
				//哪些字段高亮
				 .withHighlightFields(field) 
				// 查询条件
				.withQuery(queryBuilder) 
				.build();
		Page<TestEntity> lst = repository.search(searchquery);
		return new ResponseEntity<Page<TestEntity>>(lst, HttpStatus.OK);
	}
	
	/**
	 * 查询 Repository接口例子
	 * 
	 * @param document
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<String> delete(@RequestBody JSONObject document) {
		//删除es数据  这里可以修改不同的条件
		QueryBuilder queryBuilder = QueryBuilders.termQuery("text1", 
				"iicd");
		SearchQuery searchquery = new NativeSearchQueryBuilder()
				//查询哪些字段出来
				// .withFields(fliedname) 
				// 哪些字段高亮
//				.withHighlightFields(field) 
				// 查询条件
				.withQuery(queryBuilder) 
				.build();
		List<TestEntity> lst = esTp.queryForList(searchquery, TestEntity.class);
		System.out.println(lst.size());
		//删除
		DeleteQuery del = new DeleteQuery();
		del.setQuery(queryBuilder);
		del.setType(TestEntity.INDEX);
		esTp.delete(del, TestEntity.class);

		lst = esTp.queryForList(searchquery, TestEntity.class);
		System.out.println(lst.size());
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}
}
