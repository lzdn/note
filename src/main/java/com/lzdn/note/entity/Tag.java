package com.lzdn.note.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONType;
import com.lzdn.note.common.entity.BaseEntity;

@Entity
@Table(name = "t_tag")
@JSONType(orders = { "tagId", "tagType", "tagName", "tagValue", "order_by" })
public class Tag extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "tag_id")
	private Integer tagId;

	@Column(name = "tag_type", length = 32, nullable = false)
	private String tagType;

	@Column(name = "tag_name", length = 32, nullable = false)
	private String tagName;

	@Column(name = "tag_value", length = 32)
	private String tagValue;

	@Column(name = "order_by", length = 3)
	private Integer orderBy;

	public Integer getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Integer orderBy) {
		this.orderBy = orderBy;
	}

	public Integer getTagId() {
		return tagId;
	}

	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}

	public String getTagType() {
		return tagType;
	}

	public void setTagType(String tagType) {
		this.tagType = tagType;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getTagValue() {
		return tagValue;
	}

	public void setTagValue(String tagValue) {
		this.tagValue = tagValue;
	}

	@Override
	public String toString() {
		return "Tag [tagId=" + tagId + ", tagType=" + tagType + ", tagName=" + tagName + ", tagValue=" + tagValue + "]";
	}

}
