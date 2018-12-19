package com.tianlangcloud.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tianlangcloud.common.Utils;
import com.tianlangcloud.dao.ESKnowDao;
import com.tianlangcloud.dao.MSKnowDao;
import com.tianlangcloud.model.Know;
import com.tianlangcloud.model.PageSerch;

@Service
@SuppressWarnings("unused")
public class KnowServiceImpl implements KnowService{

	@Autowired
	private ESKnowDao esKnowDao;
	@Autowired
	private MSKnowDao msKnowDao;
	
	/**
	 * 根据关键字进行全文查询
	 * @param keyWord
	 * @return
	 */
	@Override
	public List<Know> searchKeyWord(PageSerch pageSerch) {
		List<Know> list = new ArrayList<>();
		
		//分页加关键字全文搜索
		Pageable pageable = new PageRequest(pageSerch.getPageNumber(), pageSerch.getPageSize());
		QueryStringQueryBuilder builder = new QueryStringQueryBuilder(pageSerch.getKeyWord());
		SearchQuery searchPage = new NativeSearchQueryBuilder()
											.withPageable(pageable).withQuery(builder).build();
				
		Page<Know> iterable = esKnowDao.search(searchPage);
		
		//封装到作用域
		int i = iterable.getTotalPages();
		pageSerch.getRequest().setAttribute("total", i);
		pageSerch.getRequest().setAttribute("current", pageSerch.getPageNumber() + 1);
		long count = iterable.getTotalElements();
		pageSerch.getRequest().setAttribute("count", count);
		
		Iterator<Know> iterator = iterable.iterator();
		while (iterator.hasNext()) {
			Know know = iterator.next();
			//清空html格式
			String know_title = know.getTitle();
			String title = Utils.filterString(know_title);
			String know_content = know.getContent();
			String content = Utils.filterString(know_content);
			
			StringBuffer titleBuffer = new StringBuffer(title);
			if (content.length() >= 150) {
				content = content.substring(0,150);
			}
			
			//高亮关键字
			title = Utils.highLightKeyWords(pageSerch.getKeyWord(), title);
			content = Utils.highLightKeyWords(pageSerch.getKeyWord(), content);
			
			//封装
			know.setTitle(title);
			know.setContent(content);
			list.add(know);
		}
		
		return list;
	}
	
	/**
	 * 根据ID查询mysql数据库中的知道详情
	 * @param id
	 * @return
	 */
	@Override
	public Know msSelectKnowById(Integer id) {
		Map<String, Object> knowById = msKnowDao.selectKnowById(id);
		Know know = new Know();
		know.setId(knowById.get("id").toString());
		know.setUid(knowById.get("uid").toString());
		know.setTitle(Utils.filterString(knowById.get("title").toString()));
		know.setContent(knowById.get("content").toString());;
		know.setCreateTime(knowById.get("create_time").toString());
		know.setUpdateTime(knowById.get("update_time").toString());
		return know;
	}
	
	/**
	 * 查询ES库中所有的“知道”根据修改时间排序
	 * @return
	 */
	@Override
	public List<Know> esSelectKnowAllSortByTime(PageSerch pageSerch) {
		List<Know> list = new ArrayList<>();
		//分页加关键字全文搜索
		Pageable pageable = new PageRequest(pageSerch.getPageNumber(), pageSerch.getPageSize(),new Sort("updateTime"));
				
		Page<Know> iterable = esKnowDao.findAll(pageable);
		Iterator<Know> iterator = iterable.iterator();
		
		//封装到作用域
		int i = iterable.getTotalPages();
		pageSerch.getRequest().setAttribute("total", i);
		pageSerch.getRequest().setAttribute("current", pageSerch.getPageNumber() + 1);
		long count = iterable.getTotalElements();
		pageSerch.getRequest().setAttribute("count", count);
		
		while (iterator.hasNext()) {
			Know know = iterator.next();
			//清空html格式
			String know_title = know.getTitle();
			String title = Utils.filterString(know_title);
			String know_content = know.getContent();
			String content = Utils.filterString(know_content);
			
			if (content.length() >= 150) {
				content = content.substring(0,150);
			}
			
			//封装
			know.setTitle(title);
			know.setContent(content);
			list.add(know);
		}
		return list;
	}
	
	/**
	 * 查询ES库中所有的“知道”
	 * @return
	 */
	@Override
	public List<Know> esSelectKnowAll() {
		List<Know> list = new ArrayList<>();
		Iterable<Know> iterable = esKnowDao.findAll(new Sort("id"));
		Iterator<Know> iterator = iterable.iterator();
		while (iterator.hasNext()) {
			Know know = (Know) iterator.next();
			list.add(know);
		}
		return list;
	}

	/**
	 * 查询MS库中所有的“知道”
	 * @return
	 */
	@Override
	public List<Know> msSelectKnowAll() {
		List<Map<String, Object>> list = msKnowDao.selectKnowAll();
		List<Know> msList = new ArrayList<>();
		 
		for (Map<String, Object> map : list) {
			Know know = new Know();
			know.setId(map.get("id").toString());
			know.setUid(map.get("uid").toString());
			know.setTitle(map.get("title").toString());
			know.setContent(map.get("content").toString());
			know.setCreateTime(map.get("createTime").toString());
			know.setUpdateTime(map.get("updateTime").toString());
			msList.add(know);
		}
		return msList;
	}
	
	/**
	 * 同步添加知道
	 * @param know
	 * @return
	 */
	@Override
	@Transactional
	public void emInsertKnow(Know know) {
		String title = know.getTitle();
		know.setTitle(Utils.filterString(title));
		int kid = msKnowDao.insertKnow(know);
		Map<String, Object> map = msKnowDao.selectKnowById(kid);
		
		String create_time = map.get("create_time").toString();
		String update_time = map.get("update_time").toString();
		
		know.setId(String.valueOf(kid));
		know.setUid("1");
		know.setCreateTime(create_time);
		know.setUpdateTime(update_time);
		
		esKnowDao.save(know);
	}

	/**
	 * 根据ID同步删除知道
	 * @param know
	 * @return
	 */
	@Override
	@Transactional
	public void emDeleteKnowById(String id) {
		esKnowDao.delete(id);
		msKnowDao.deleteKnowById(id);
	}
	
}
