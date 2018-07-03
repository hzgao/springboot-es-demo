/**
 * 
 */
package com.war.es5.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.springframework.data.elasticsearch.ElasticsearchException;
import org.springframework.data.elasticsearch.core.DefaultEntityMapper;
import org.springframework.data.elasticsearch.core.ResultsExtractor;

import com.war.es5.entity.EsDemoEntity;

/**
 * @author admin
 * @param <T>
 * 结果处理
 */
public class CustomResultExtractor implements ResultsExtractor<List<EsDemoEntity>> {

	
	private final DefaultEntityMapper defaultEntityMapper;

	public CustomResultExtractor() {
	    defaultEntityMapper = new DefaultEntityMapper();
	}
	
	@Override
	public List<EsDemoEntity> extract(SearchResponse searchresponse) {
		return StreamSupport.stream(searchresponse.getHits().spliterator(), false) 
		        .map(this::searchHitToMyClass) 
		        .collect(Collectors.toList());
	}

	private EsDemoEntity searchHitToMyClass(SearchHit searchHit) {
		EsDemoEntity myObject;
	    try {
	        myObject = defaultEntityMapper.mapToObject(searchHit.getSourceAsString(), EsDemoEntity.class);
	    } catch (IOException e) {
	        throw new ElasticsearchException("failed to map source [ " + searchHit.getSourceAsString() + "] to class " + EsDemoEntity.class.getSimpleName(), e);
	    }
	    List<String> highlights = searchHit.getHighlightFields().values()
	        .stream() 
	        .flatMap(highlightField -> Arrays.stream(highlightField.fragments())) 
	        .map(Text::string) 
	        .collect(Collectors.toList());
	    // Or whatever you want to do with the highlights。
	    myObject.setHighlights(highlights);
	    return myObject;
//	    		new EsDemoEntity(myObject, highlights);
	}
}
