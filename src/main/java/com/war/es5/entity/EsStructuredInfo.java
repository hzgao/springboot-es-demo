/**	
 * <br>
 * Copyright 2011 IFlyTek. All rights reserved.<br>
 * <br>			 
 * Package: com.iflytek.EsEntity <br>
 * FileName: EsStructuredInfo.java <br>
 * <br>
 * @version
 * @author pwxu2@iflytek.com
 * @created 2018年7月10日
 * @last Modified 
 * @history
 */

package com.war.es5.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * {ES_引擎处理后的结构化信息}
 * 
 * @author pwxu2@iflytek.com
 * @created 2018年7月10日 下午2:31:20
 * @lastModified
 * @history
 */
@Document(indexName = "dmt", type = "")
public class EsStructuredInfo implements Serializable {
	/**
	 * Comment for <code>serialVersionUID</code>,｛该处说明该变量的含义及作用｝
	 */
	private static final long serialVersionUID = -61099207500956246L;
	/**
	 * 唯一id
	 */
	@Id
	private String id;
	/**
	 * 源id
	 */
	private String originId;
	/**
	 * 文件名
	 */
	@Field(type=FieldType.Text,searchAnalyzer="ik_smart", analyzer="ik_max_word")
	private String fileName;
	/**
	 * 样本id
	 */
	private String sampleId;
	/**
	 * 样本类型编码
	 */
	private String sampleTypeCode;
	/**
	 * 语种
	 */
	private String language;
	/**
	 * 转写结果
	 */
	@Field(type=FieldType.Text,searchAnalyzer="ik_smart", analyzer="ik_max_word")
	private String decodeReesult;
	
	/**
	 * 关键词识别结果
	 */
	@Field(type=FieldType.Text,searchAnalyzer="ik_smart", analyzer="ik_max_word")
	private String keywordResult;
	/**
	 * ocr结果
	 */
	@Field(type=FieldType.Text,searchAnalyzer="ik_smart", analyzer="ik_max_word")
	private String ocrResult;
	/**
	 * 物体检测结果
	 */
	@Field(type=FieldType.Text,searchAnalyzer="ik_smart", analyzer="ik_max_word")
	private String detectResult;
	/**
	 * 场景分类结果
	 */
	@Field(type=FieldType.Text,searchAnalyzer="ik_smart", analyzer="ik_max_word")
	private String sceneResult;
	/**
	 * 台标识别结果
	 */
	@Field(type=FieldType.Text,searchAnalyzer="ik_smart", analyzer="ik_max_word")
	private String logoResult;
	/**
	 * 人脸识别结果
	 */
	@Field(type=FieldType.Text,searchAnalyzer="ik_smart", analyzer="ik_max_word")
	private String faceResult;
	/**
	 * 创建时间
	 */
	private long createTime;
	/**
	 * 更新时间
	 */
	private long updateTime;



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOriginId() {
		return originId;
	}

	public void setOriginId(String originId) {
		this.originId = originId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getSampleId() {
		return sampleId;
	}

	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}

	public String getSampleTypeCode() {
		return sampleTypeCode;
	}

	public void setSampleTypeCode(String sampleTypeCode) {
		this.sampleTypeCode = sampleTypeCode;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getDecodeReesult() {
		return decodeReesult;
	}

	public void setDecodeReesult(String decodeReesult) {
		this.decodeReesult = decodeReesult;
	}

	public String getKeywordResult() {
		return keywordResult;
	}

	public void setKeywordResult(String keywordResult) {
		this.keywordResult = keywordResult;
	}

	public String getOcrResult() {
		return ocrResult;
	}

	public void setOcrResult(String ocrResult) {
		this.ocrResult = ocrResult;
	}

	public String getDetectResult() {
		return detectResult;
	}

	public void setDetectResult(String detectResult) {
		this.detectResult = detectResult;
	}

	public String getSceneResult() {
		return sceneResult;
	}

	public void setSceneResult(String sceneResult) {
		this.sceneResult = sceneResult;
	}

	public String getLogoResult() {
		return logoResult;
	}

	public void setLogoResult(String logoResult) {
		this.logoResult = logoResult;
	}

	public String getFaceResult() {
		return faceResult;
	}

	public void setFaceResult(String faceResult) {
		this.faceResult = faceResult;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

}
