package com.dss.springboot.blog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dss.springboot.blog.domain.Vote;
import com.dss.springboot.blog.repository.VoteRepository;
import com.dss.springboot.blog.service.VoteService;

@Service
public class VoteServiceImpl implements VoteService {

	@Autowired
	private VoteRepository voteRepository;
	
	@Override
	public Vote getVoteById(Long id) {
		return voteRepository.getOne(id);
	}

	@Override
	public void removeVoteById(Long id) {
		voteRepository.deleteById(id);
	}

}
