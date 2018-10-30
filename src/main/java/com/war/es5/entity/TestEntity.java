/**
 * 
 */
package com.war.es5.entity;

import java.io.Serializable;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author admin
 *
 */
@Document(indexName=TestEntity.INDEX,type=TestEntity.INDEX)
public class TestEntity implements Serializable   {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7721132426599275297L;
	
	/**
	 * index name
	 */
	public static final String INDEX = "index_es_test";
	
	/**
	 * 唯一性标识 
	 */
	@Id
	private String id;
	@Field(type=FieldType.Text, analyzer="ik_max_word")
	private String text1;
	@Field(type=FieldType.Text, analyzer="ik_max_word")
	private String text2;
	
	private Map<String,Object> highlight;

	/**
	 * @return the text1
	 */
	public String getText1() {
		return text1;
	}
	/**
	 * @param text1 the text1 to set
	 */
	public void setText1(String text1) {
		this.text1 = text1;
	}
	/**
	 * @return the text2
	 */
	public String getText2() {
		return text2;
	}
	/**
	 * @param text2 the text2 to set
	 */
	public void setText2(String text2) {
		this.text2 = text2;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the highlight
	 */
	public Map<String, Object> getHighlight() {
		return highlight;
	}
	/**
	 * @param highlight the highlight to set
	 */
	public void setHighlight(Map<String, Object> highlight) {
		this.highlight = highlight;
	}

}
