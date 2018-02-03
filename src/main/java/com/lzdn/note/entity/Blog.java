package com.lzdn.note.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.lzdn.note.common.entity.BaseEntity;

@Entity
@Table(name = "t_blog")
@JSONType(orders = {"blogId", "blogName", "blogImg", "introduction", "content", "time", "browse", "praise", "blogType", "reprintedUrl"})
public class Blog extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue
    @Column(name = "blog_id")
	private Integer blogId; // 主键ID

	@Column(name = "blog_name", length = 32, nullable = false)
	private String blogName; // 博客名称

	@Column(name = "blog_img", length = 50)
	private String blogImg; // 博客图片

	@Column(name = "description", length = 128)
	private String introduction; // 博客介绍

	@Column(name = "content", length = 1000)
	private String content; // 博客内容

	@Column(name = "push_time")
    @Temporal(TemporalType.TIMESTAMP)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss.SSS")
	private Date pushTime; // 发布时间

	@Column(name = "browse", length = 16)
	private Integer browse; // 浏览数量

	@Column(name = "praise", length = 16)
	private Integer praise; // 赞的数量

	@Column(name = "blog_type", length = 3)
	private Integer blogType; // 博客类型

	@Column(name = "reprinted_url", length = 150)
	private String reprintedUrl; // 转载Url

	public Integer getBlogId() {
		return blogId;
	}

	public void setBlogId(Integer blogId) {
		this.blogId = blogId;
	}

	public String getBlogName() {
		return blogName;
	}

	public void setBlogName(String blogName) {
		this.blogName = blogName;
	}

	public String getBlogImg() {
		return blogImg;
	}

	public void setBlogImg(String blogImg) {
		this.blogImg = blogImg;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getPushTime() {
		return pushTime;
	}

	public void setPushTime(Date pushTime) {
		this.pushTime = pushTime;
	}

	public Integer getBrowse() {
		return browse;
	}

	public void setBrowse(Integer browse) {
		this.browse = browse;
	}

	public Integer getPraise() {
		return praise;
	}

	public void setPraise(Integer praise) {
		this.praise = praise;
	}

	public Integer getBlogType() {
		return blogType;
	}

	public void setBlogType(Integer blogType) {
		this.blogType = blogType;
	}

	public String getReprintedUrl() {
		return reprintedUrl;
	}

	public void setReprintedUrl(String reprintedUrl) {
		this.reprintedUrl = reprintedUrl;
	}

	@Override
	public String toString() {
		return "Blog [blogId=" + blogId + ", blogName=" + blogName + ", blogImg=" + blogImg + ", introduction="
				+ introduction + ", content=" + content + ", pushTime=" + pushTime + ", browse=" + browse + ", praise="
				+ praise + ", blogType=" + blogType + ", reprintedUrl=" + reprintedUrl + "]";
	}
 

}
