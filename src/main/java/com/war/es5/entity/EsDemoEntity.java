
package com.war.es5.entity;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author admin
 * 测试用
 */
@Document(indexName=EsDemoEntity.INDEX)
public class EsDemoEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2335404138062073937L;

	/**
	 * index name
	 */
	public static final String INDEX = "index_es_demo";
	
	/**
	 * 高亮的msg
	 */
	private List<String> highlights;
	
	/**
	 * 唯一性标识
	 */
	@Id
	@Field(type=FieldType.Keyword)
	private String id;
	
	/**
	 * 名称
	 */
	@Field(type=FieldType.Auto)
	private String name;
	
	
	/**
	 * mark
	 */
	@Field(type=FieldType.Auto)
	private String tagline;
	
	/**
	 * 版本号
	 */
	@Field(type=FieldType.Object)
	private Version version;
	
	public class Version {
		
		@Field(type=FieldType.Text)
		private String number;
		
		@Field(type=FieldType.Text)
		private String lucene_version;

		/**
		 * @return the number
		 */
		public String getNumber() {
			return number;
		}

		/**
		 * @param number the number to set
		 */
		public void setNumber(String number) {
			this.number = number;
		}

		/**
		 * @return the lucene_version
		 */
		public String getLucene_version() {
			return lucene_version;
		}

		/**
		 * @param lucene_version the lucene_version to set
		 */
		public void setLucene_version(String lucene_version) {
			this.lucene_version = lucene_version;
		}
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the tagline
	 */
	public String getTagline() {
		return tagline;
	}

	/**
	 * @param tagline the tagline to set
	 */
	public void setTagline(String tagline) {
		this.tagline = tagline;
	}

	/**
	 * @return the version
	 */
	public Version getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(Version version) {
		this.version = version;
	}

	/**
	 * @return the index
	 */
	public static String getIndex() {
		return INDEX;
	}

	/**
	 * @return the highlights
	 */
	public List<String> getHighlights() {
		return highlights;
	}

	/**
	 * @param highlights the highlights to set
	 */
	public void setHighlights(List<String> highlights) {
		this.highlights = highlights;
	};
}
