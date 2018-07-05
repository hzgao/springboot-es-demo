/**
 * 
 */
package com.war.es5.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

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
	
	private String text1;
	private String text2;

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

}
