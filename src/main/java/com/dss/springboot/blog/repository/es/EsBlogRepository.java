package com.dss.springboot.blog.repository.es;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.dss.springboot.blog.domain.es.EsBlog;

public interface EsBlogRepository extends ElasticsearchRepository<EsBlog, String> {

	/**
	 * 模糊查询(去重)，通过title，summary，content，tag
	 * @param title
	 * @param Summary
	 * @param content
	 * @param tags
	 * @param pageable
	 * @return
	 */
	Page<EsBlog> findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContainingOrTagsContaining(String title,String Summary,String content,String tags,Pageable pageable);
	
	/**
	 * 通过blog的id查找EsBlog对象
	 * @param blogId
	 * @return
	 */
	EsBlog findByBlogId(Long blogId);

}
