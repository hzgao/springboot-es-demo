/**
 * 
 */
package com.war.es5.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.war.es5.entity.EsDemoEntity;

/**
 * @author admin
 * @param <T>
 *	esdemo 操作接口 ;可以扩展接口
 */
@Repository
public interface EsDemoRepository extends ElasticsearchRepository<EsDemoEntity, String> {

}
