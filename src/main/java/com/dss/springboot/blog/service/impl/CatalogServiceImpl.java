package com.dss.springboot.blog.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dss.springboot.blog.domain.Catalog;
import com.dss.springboot.blog.domain.User;
import com.dss.springboot.blog.repository.CatalogRepository;
import com.dss.springboot.blog.service.CatalogService;

@Service
public class CatalogServiceImpl implements CatalogService {

	@Autowired
	private CatalogRepository catalogRepository;
	
	@Override
	public Catalog saveCatalog(Catalog catalog) {
		
		//判断这个catalog在这个user下是否已经存在
		List<Catalog> list = catalogRepository.findByUserAndName(catalog.getUser(), catalog.getName());
		if(list != null && list.size() > 0) {
			throw new IllegalArgumentException("该分类已经存在了！！！");
		}
		
		return catalogRepository.save(catalog);
	}

	@Override
	public void removeCatalog(Long id) {
		catalogRepository.deleteById(id);
	}

	@Override
	public Catalog getCatalogById(Long id) {
		return catalogRepository.getOne(id);
	}

	@Override
	public List<Catalog> getCatalogsByUser(User user) {
		return catalogRepository.findByUser(user);
	}

}
