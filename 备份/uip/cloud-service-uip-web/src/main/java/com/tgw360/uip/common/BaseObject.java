package com.tgw360.uip.common;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 为所有的子类提供了通用的toString，hashCode，eqauls方法。<br>
 * 
 * @author 邹祥
 * @date 2016年11月12日 上午9:33:26
 */
public abstract class BaseObject implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 将对象写成JSON格式的字符串。
	 */
	@Override
	public String toString() {
		return toString(ToStringStyle.JSON_STYLE);
	}

	public String toString(ToStringStyle style) {
		return ToStringBuilder.reflectionToString(this, style);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object other) {
		return EqualsBuilder.reflectionEquals(this, other);
	}
}
