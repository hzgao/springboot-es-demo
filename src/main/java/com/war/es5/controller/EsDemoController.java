/**
 * 
 */
package com.war.es5.controller;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.war.es5.entity.EsDemoEntity;
import com.war.es5.entity.EsDemoEntity.Version;
import com.war.es5.repository.EsDemoRepository;


/**
 * @author admin
 *
 */
@Controller
@RequestMapping("/esdemo")
public class EsDemoController {
	
	@Autowired
	private EsDemoRepository repository;
	@Autowired
	private ElasticsearchTemplate esTp;
	
	/**
	 * ElasticsearchTemplate接口例子
	 * @param document 
	 * @return
	 */
	@RequestMapping(value = "/searchByEsTp", method = RequestMethod.POST)
    public ResponseEntity<List<EsDemoEntity>> searchByEsTp(@RequestBody EsDemoEntity document) {
		//查询条件 某字段按字符串模糊查询
		QueryBuilder builder = QueryBuilders.matchQuery("name", document.getName());
//				QueryBuilders.moreLikeThisQuery(new String[] {document.getName()});
				//QueryBuilders.queryStringQuery(document.getName());
//				QueryBuilders.matchQuery("name", document.getName());
//				QueryBuilders.multiMatchQuery(document.getName(), "name");
		System.out.println("builder.toString()  " +  builder.toString());
		//构建查询
		SearchQuery query = new NativeSearchQueryBuilder().withQuery(builder).build();
//		System.out.println("query.toString()  " +  query.getFilter().toString());
		
		List<EsDemoEntity> lst = esTp.queryForList(query, EsDemoEntity.class);

        return new ResponseEntity<List<EsDemoEntity>>(lst, HttpStatus.OK);
    }
	
	/**
	 * ElasticsearchTemplate接口例子
	 * @param document 
	 * @return
	 */
	@RequestMapping(value = "/saveByEsTp", method = RequestMethod.POST)
    public ResponseEntity<EsDemoEntity> saveByEsTp(@RequestBody EsDemoEntity document) {
		document.setId(UUID.randomUUID().toString());
		document.setTagline("You Know, for Search");
		Version version = document.new Version();
		version.setNumber("5.4.3");
		version.setLucene_version("6.5.1");
		document.setVersion(version);
		//插入
		List<IndexQuery> queries = new ArrayList<IndexQuery>();
		IndexQuery indexQuery = new IndexQuery();
		//不需要index  
		indexQuery.setObject(document);
		queries.add(indexQuery);
		esTp.bulkIndex(queries);
		
        return new ResponseEntity<EsDemoEntity>(document, HttpStatus.OK);
    }
	
	
	/**
	 * ElasticserachRepository接口例子
	 * @param document 
	 * @return
	 */
	@RequestMapping(value = "/searchByRepository", method = RequestMethod.POST)
    public ResponseEntity<Iterable<EsDemoEntity>> searchByRepository(@RequestBody EsDemoEntity document) {
		//精确匹配
		QueryBuilder builder = QueryBuilders.multiMatchQuery(document.getName(), "name");
		System.out.println("builder.toString()  " +  builder.toString());
		Iterable<EsDemoEntity> lstEs = repository.search(builder);
//				.findAll();
        return new ResponseEntity<Iterable<EsDemoEntity>>(lstEs, HttpStatus.OK);
    }
	
	/**
	 * ElasticserachRepository接口例子
	 * @param document 
	 * @return
	 */
	@RequestMapping(value = "/searchHightLight", method = RequestMethod.POST)
    public ResponseEntity<List<EsDemoEntity>> searchHightLight(@RequestBody EsDemoEntity document) {
		String[] fliedname = {"name"};
		//精确匹配
		QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(document.getName(), fliedname);
		System.out.println("builder.toString()  " +  queryBuilder.toString());
		//高亮字段
		Field[] field = {new Field(fliedname[0])};
		SearchQuery searchquery = new NativeSearchQueryBuilder()
				.withHighlightFields(field)
				.withQuery(queryBuilder)
				.build();
		
//		Page<EsDemoEntity> lstEs = repository.search(searchquery);
		List<EsDemoEntity> lst = esTp.query(searchquery, new CustomResultExtractor());
		
//				.findAll();
        return new ResponseEntity<List<EsDemoEntity>>(lst, HttpStatus.OK);
    }

	/**
	 * ElasticserachRepository接口例子
	 * @param document
	 * @return
	 */
	@RequestMapping(value = "/saveByRepository", method = RequestMethod.POST)
    public ResponseEntity<EsDemoEntity> saveByRepository(@RequestBody EsDemoEntity document) {
		document.setId(UUID.randomUUID().toString());
		document.setTagline("You Know, for Search");
		Version version = document.new Version();
		version.setNumber("5.4.3");
		version.setLucene_version("6.5.1");
		document.setVersion(version);
		EsDemoEntity rel = repository.save(document);
        return new ResponseEntity<EsDemoEntity>(rel, HttpStatus.OK);
    }

}
