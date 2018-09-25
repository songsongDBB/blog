package com.dss.springboot.blog.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import com.dss.springboot.blog.domain.User;
import com.dss.springboot.blog.domain.es.EsBlog;
import com.dss.springboot.blog.repository.es.EsBlogRepository;
import com.dss.springboot.blog.service.EsBlogService;
import com.dss.springboot.blog.service.UserService;
import com.dss.springboot.blog.vo.TagVO;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;

@Service
public class EsBlogServiceImpl implements EsBlogService {

	@Autowired
	private EsBlogRepository esBlogRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;

	private static final Pageable TOP_5_PAGEABLE = new PageRequest(0, 5); // 查询top 5的一个pageable常量对象
	private static final String EMPTY_KEYWORD = "";

	@Override
	public void removeEsBlog(String id) {
		esBlogRepository.deleteById(id);
	}

	@Override
	public EsBlog updateEsBlog(EsBlog esBlog) {
		return esBlogRepository.save(esBlog);
	}

	@Override
	public EsBlog getEsBlogByBlogId(Long blodId) {
		return esBlogRepository.findByBlogId(blodId);
	}

	@Override
	public Page<EsBlog> listNewestEsBlogs(String keyword, Pageable pageable) {
		Page<EsBlog> pages = null;
		Sort sort = new Sort(Direction.DESC, "createTime"); // 通过createTime字段进行降序排序
		if (pageable.getSort() == null) {
			pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
		}

		pages = esBlogRepository
				.findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContainingOrTagsContaining(keyword,
						keyword, keyword, keyword, pageable);

		return pages;
	}

	@Override
	public Page<EsBlog> listHotestEsBlogs(String keyword, Pageable pageable) {
		Sort sort = new Sort(Direction.DESC, "readSize", "commentSize", "voteSize", "createTime");
		if (pageable.getSort() == null) {
			pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
		}
		
		return  esBlogRepository
				.findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContainingOrTagsContaining(keyword,
						keyword, keyword, keyword, pageable);
	}

	@Override
	public Page<EsBlog> listEsBlogs(Pageable pageable) {
		return esBlogRepository.findAll(pageable);
	}

	@Override
	public List<EsBlog> listTop5HotestEsBlogs() {
		Page<EsBlog> page = this.listHotestEsBlogs(EMPTY_KEYWORD, TOP_5_PAGEABLE);
		return page.getContent();
	}
	
	@Override
	public List<EsBlog> listTop5NewestEsBlogs() {
		Page<EsBlog> page = this.listNewestEsBlogs(EMPTY_KEYWORD, TOP_5_PAGEABLE);
		return page.getContent();
	}

	/**
	 * 获取所有blog中使用的最多的30个tag，并且统计标签使用的次数
	 */
	@Override
	public List<TagVO> listTop30Tags() {
		
		List<TagVO> list = new ArrayList<TagVO>();
		// given
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(matchAllQuery())
				.withSearchType(SearchType.QUERY_THEN_FETCH)
				.withIndices("blog").withTypes("blog")
				.addAggregation(terms("tags").field("tags").order(Terms.Order.count(false)).size(30))
				.build();

		// when
		Aggregations aggregations = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
			@Override
			public Aggregations extract(SearchResponse response) {
				return response.getAggregations();
			}
		});

		StringTerms modelTerms =  (StringTerms)aggregations.asMap().get("tags"); 
        
        Iterator<Bucket> modelBucketIt = modelTerms.getBuckets().iterator();
        while (modelBucketIt.hasNext()) {
            Bucket actiontypeBucket = (Bucket) modelBucketIt.next();
 
            list.add(new TagVO(actiontypeBucket.getKey().toString(),
                    actiontypeBucket.getDocCount()));
        }
		return list;
	}

	/**
	 * 获取发表blog最多的前12名user
	 */
	@Override
	public List<User> listTop12Users() {
		List<String> usernamelist = new ArrayList<>();
		// given
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(matchAllQuery())
				.withSearchType(SearchType.QUERY_THEN_FETCH)
				.withIndices("blog").withTypes("blog")
				.addAggregation(terms("users").field("username").order(Terms.Order.count(false)).size(12))
				.build();
		// when
		Aggregations aggregations = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
			@Override
			public Aggregations extract(SearchResponse response) {
				return response.getAggregations();
			}
		});

		StringTerms modelTerms =  (StringTerms)aggregations.asMap().get("users"); 
        
        Iterator<Bucket> modelBucketIt = modelTerms.getBuckets().iterator();
        while (modelBucketIt.hasNext()) {
            Bucket actiontypeBucket = (Bucket) modelBucketIt.next();
            String username = actiontypeBucket.getKey().toString();
            usernamelist.add(username);
        }
        List<User> list = userService.listUsersByUsernames(usernamelist);
        
		return list;

	}

}
