package org.elnar.crudapp;

import org.elnar.crudapp.enums.PostStatus;
import org.elnar.crudapp.model.Post;
import org.elnar.crudapp.repository.PostRepository;
import org.elnar.crudapp.service.PostService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class PostServiceTest {
	
	private static PostRepository postRepository;
	private static PostService postService;
	private static Post testPost;
	
	@BeforeAll
	static void setUp() {
		postRepository = Mockito.mock(PostRepository.class);
		postService = new PostService(postRepository);
		
		testPost = Post.builder()
				.id(1L)
				.content("Test content")
				.created(new Date())
				.updated(new Date())
				.postStatus(PostStatus.ACTIVE)
				.build();
	}
	
	@Test
	void testGetById(){
		when(postRepository.getById(anyLong())).thenReturn(testPost);
		
		Post post = postService.getPostById(1L);
		
		assertNotNull(post);
		assertEquals("Test content", post.getContent());
		assertEquals(PostStatus.ACTIVE, post.getPostStatus());
	}
	
	@Test
	void testGetAllPosts() {
		List<Post> posts = new ArrayList<>();
		posts.add(testPost);
		
		when(postRepository.getAll()).thenReturn(posts);
		
		List<Post> result = postService.getAllPosts();
		
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals("Test content", result.get(0).getContent());
		assertEquals(PostStatus.ACTIVE, result.get(0).getPostStatus());
	}
	
	@Test
	void testSavePost() {
		when(postRepository.save(any(Post.class))).thenReturn(testPost);
		
		Post post = postService.savePost(testPost);
		
		assertNotNull(post);
		assertEquals("Test content", post.getContent());
		assertEquals(PostStatus.ACTIVE, post.getPostStatus());
		
		verify(postRepository, times(1)).save(testPost);
	}
	
	@Test
	void testUpdatePost() {
		Post updatedPost = Post.builder()
				.id(1L)
				.content("Test content updated")
				.updated(new Date())
				.postStatus(PostStatus.ACTIVE)
				.build();
		
		when(postRepository.update(any(Post.class))).thenReturn(updatedPost);
		
		Post post = postService.updatePost(updatedPost);
		
		assertNotNull(post);
		assertEquals("Test content updated", post.getContent());
		assertEquals(PostStatus.ACTIVE, post.getPostStatus());
		
		verify(postRepository, times(1)).update(updatedPost);
	}
	
	@Test
	void testDeletePost() {
		postService.deletePost(1L);
		verify(postRepository, times(1)).deleteById(1L);
	}
}