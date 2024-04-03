package org.elnar.crudapp.view;

import lombok.RequiredArgsConstructor;
import org.elnar.crudapp.controller.PostController;
import org.elnar.crudapp.controller.WriterController;
import org.elnar.crudapp.enums.PostStatus;
import org.elnar.crudapp.enums.WriterStatus;
import org.elnar.crudapp.model.Post;
import org.elnar.crudapp.model.Writer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

@RequiredArgsConstructor
public class WriterView {
	private final WriterController writerController;
	private final PostController postController;
	
	private final Scanner scanner = new Scanner(System.in);
	
	public void run(){
		boolean running = true;
		
		while (running){
			System.out.println("1. Создать писателя");
			System.out.println("2. Получить писателя по ID");
			System.out.println("3. Получить всех писателей");
			System.out.println("4. Обновить информацию о писателе");
			System.out.println("5. Удалить писателя");
			System.out.println("0. Выйти");
			System.out.print("Выберите опцию: ");
			
			int option = scanner.nextInt();
			scanner.nextLine();
			
			switch (option) {
				case 1 -> createWriter();
				case 2 -> getWriterById();
				case 3 -> getAllWriters();
				case 4 -> updateWriter();
				case 5 -> deleteWriter();
				case 0 -> running = false;
				default -> System.out.println("Неверная опция. Пожалуйста, попробуйте еще раз.");
			}
		}
	}
	
	private void createWriter(){
		System.out.println("Введите имя: ");
		String firstname = scanner.nextLine();
		
		System.out.println("Введите фамилию: ");
		String lastname = scanner.nextLine();
		
		Writer createWriter = Writer.builder()
				.firstname(firstname)
				.lastname(lastname)
				.writerStatus(WriterStatus.ACTIVE)
				.posts(new ArrayList<>())
				.build();
		
		writerController.saveWriter(createWriter);
		System.out.println("Писатель создан");
		
		addPostsForWriter(createWriter);
	}
	
	private void getWriterById(){
		System.out.println("Введите ID писателя: ");
		Long id = scanner.nextLong();
		Writer writer = writerController.getWriterById(id);
		System.out.println("Найден писатель: " + writer);
	}
	
	private void getAllWriters(){
		List<Writer> writers = writerController.getAllWriters();
		for(Writer writer : writers){
			System.out.println(writer);
		}
	}
	
	private void updateWriter(){
		System.out.println("Введите ID писателя для обновления: ");
		Long id = scanner.nextLong();
		scanner.nextLine();
		
		System.out.println("Введите новое имя: ");
		String firstname = scanner.nextLine();
		
		System.out.println("Введите новую фамилию: ");
		String lastname = scanner.nextLine();
		
		Writer updatedWriter = Writer.builder()
				.id(id)
				.firstname(firstname)
				.lastname(lastname)
				.writerStatus(WriterStatus.ACTIVE)
				.posts(new ArrayList<>())
				.build();
		
		writerController.updateWriter(updatedWriter);
		System.out.println("Писатель обновлен");
	}
	
	private void deleteWriter(){
		System.out.print("Введите ID писателя для удаления: ");
		Long id = scanner.nextLong();
		writerController.deleteWriterById(id);
		System.out.println("Писатель удален.");
	}
	
	
	/////////////////////////////////////////////////////////////////////////////
	
	private void addPostsForWriter(Writer writer) {
		System.out.println("Хотите добавить посты для писателя? (да/нет)");
		String answer = scanner.nextLine();
		while ("да".equalsIgnoreCase(answer)) {
			Post post = createPostForWriter(writer);
			postController.createdPost(post);
			
			System.out.println("Хотите добавить еще записи для писателя? (да/нет)");
			answer = scanner.nextLine();
		}
	}
	
	private Post createPostForWriter(Writer writer) {
		System.out.println("Введите содержание поста: ");
		String content = scanner.nextLine();
		
		return Post.builder()
				.content(content)
				.created(new Date())
				.updated(new Date())
				.postStatus(PostStatus.ACTIVE)
				.writer(writer)
				.build();
	}
}