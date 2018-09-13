package com.dss.springboot.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dss.springboot.blog.domain.Vote;

public interface VoteRepository extends JpaRepository<Vote, Long> {

}
