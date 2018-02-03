package com.lzdn.note.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONType;
import com.lzdn.note.common.entity.BaseEntity;

@Entity
@Table(name = "t_dict")
@JSONType(orders = { "id", "dictName", "dictType", "dictValue", "orderBy" })
public class Dict extends BaseEntity {

	private static final long serialVersionUID = 1L;
	// t_dict

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "dict_name", length = 32, nullable = false)
	private String dictName;
	
	@Column(name = "dict_type", length = 32, nullable = false)
	private String dictType;
	
	@Column(name = "dict_value", length = 32)
	private String dictValue;
	
	@Column(name = "order_by", length = 3)
	private Integer orderBy;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	public String getDictType() {
		return dictType;
	}

	public void setDictType(String dictType) {
		this.dictType = dictType;
	}

	public String getDictValue() {
		return dictValue;
	}

	public void setDictValue(String dictValue) {
		this.dictValue = dictValue;
	}

	public Integer getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Integer orderBy) {
		this.orderBy = orderBy;
	}

	@Override
	public String toString() {
		return "Dict [id=" + id + ", dictName=" + dictName + ", dictType=" + dictType + ", dictValue=" + dictValue
				+ ", orderBy=" + orderBy + "]";
	}
	

}
