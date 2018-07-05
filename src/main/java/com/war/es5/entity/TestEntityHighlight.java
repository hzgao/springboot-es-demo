/**
 * 
 */
package com.war.es5.entity;

import java.util.Map;

/**
 * @author admin
 *
 */
public class TestEntityHighlight extends TestEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6539323853658659290L;
	
	/**
	 * 高亮字段
	 */
	private Map<String,String> mapHighlight;

	/**
	 * @return the mapHighlight
	 */
	public Map<String, String> getMapHighlight() {
		return mapHighlight;
	}

	/**
	 * @param mapHighlight the mapHighlight to set
	 */
	public void setMapHighlight(Map<String, String> mapHighlight) {
		this.mapHighlight = mapHighlight;
	}

}
