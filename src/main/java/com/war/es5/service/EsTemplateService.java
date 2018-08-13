/**
 * 
 */
package com.war.es5.service;

import java.io.IOException;

/**
 * @author admin
 *
 */
public interface EsTemplateService {

	/**
	 * 保存
	 * @param t
	 */
	<T> void save(T t);
	
	
	/**
	 * 删除
	 * 条件key=value
	 */
	<T> void delete(T t);
	
	/**
	 * 更新 不建议用
	 */
	<T> void update(T t);
	
	/**
	 * 删除索引
	 */
	<T> void deleteIndex(T t);

	
	/**
	 * 创建setting  mapping的索引
	 * @param t
	 * @throws IOException
	 */
	<T> void createIndex(T t) throws IOException;
}
