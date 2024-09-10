package com.blog.services;

import java.util.List;

import com.blog.entities.Post;
import com.blog.payloads.PostDto;
import com.blog.payloads.PostResponse;

public interface PostService {
	//create
	PostDto createPost(PostDto postDto,Integer userId,Integer categoryId);
	//update
	PostDto updatePost(PostDto postDto, Integer postId);
	
	//delete
	void deletePost(Integer postId);
	
	//get all post without paging
	//List<PostDto>getAllPost();
	
	//get all post with paging
	//List<PostDto>getAllPost(Integer pageNumber,Integer pageSize);
	
	//get post with paging customize response
	PostResponse getAllPost(Integer pageNumber,Integer pageSize, String sortBy,String sortDirection);
	
	
	
	//get Single Post
	PostDto getPostById(Integer postId);
	
	//get all posts by category
	List<PostDto>getPostByCategory(Integer categoryId);
	
	//get all posts by user
	List<PostDto>getPostByUser(Integer userId);
	
	//search post
	List<PostDto> searchPosts(String keyword);
}
