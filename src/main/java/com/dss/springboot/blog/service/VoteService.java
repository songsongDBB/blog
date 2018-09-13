package com.dss.springboot.blog.service;

import com.dss.springboot.blog.domain.Vote;

public interface VoteService {

	/**
	 * 根据id获取点赞信息，这里能获取这个点赞是那个用户的
	 * @param id
	 * @return
	 */
	Vote getVoteById(Long id);
	
	/**
	 * 根据id删除评论
	 * @param id
	 */
	void removeVoteById(Long id);
}
