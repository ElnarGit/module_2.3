package org.elnar.crudapp.view;

import lombok.RequiredArgsConstructor;
import org.elnar.crudapp.controller.LabelController;
import org.elnar.crudapp.controller.PostController;
import org.elnar.crudapp.controller.WriterController;
import org.elnar.crudapp.enums.LabelStatus;
import org.elnar.crudapp.enums.PostStatus;
import org.elnar.crudapp.model.Label;
import org.elnar.crudapp.model.Post;
import org.elnar.crudapp.model.Writer;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PostView {
	private final PostController postController;
	private final WriterController writerController;
	private final LabelController labelController;
	
	private final Scanner scanner = new Scanner(System.in);
	
	public void run() {
		boolean running = true;
		
		while (running) {
			System.out.println("1. Создать пост");
			System.out.println("2. Получить пост по ID");
			System.out.println("3. Получить все посты");
			System.out.println("4. Обновить пост");
			System.out.println("5. Удалить пост");
			System.out.println("0. Выйти");
			System.out.print("Выберите опцию: ");
			
			int option = scanner.nextInt();
			scanner.nextLine();
			
			switch (option) {
				case 1 -> createPost();
				case 2 -> getPostById();
				case 3 -> getAllPosts();
				case 4 -> updatePost();
				case 5 -> deletePost();
				case 0 -> running = false;
				default -> System.out.println("Неверная опция. Пожалуйста, попробуйте еще раз.");
			}
		}
	}
	
	private void createPost() {
		Writer writer = chooseWriter();
		
		if (writer != null) {
			System.out.println("Введите содержание поста: ");
			String content = scanner.nextLine();
			
			List<Label> labels = getLabels();
			
			for (Label label : labels) {
				labelController.createLabel(label);
			}
			
			Post post = Post.builder()
					.content(content)
					.created(new Date())
					.updated(new Date())
					.postStatus(PostStatus.ACTIVE)
					.writer(writer)
					.labels(labels)
					.build();
			
			postController.createdPost(post);
			System.out.println("Пост сохранен.");
		}
	}
	
	private void getPostById() {
		System.out.print("Введите ID поста: ");
		Long postId = scanner.nextLong();
		scanner.nextLine();
		
		Post post = postController.getPostById(postId);
		System.out.println("Найден пост: " + post);
	}
	
	private void getAllPosts() {
		List<Post> posts = postController.getAllPosts();
		for (Post post : posts) {
			System.out.println(post);
		}
	}
	
	private void updatePost() {
		System.out.print("Введите ID поста для обновления: ");
		Long postId = scanner.nextLong();
		scanner.nextLine();
		
		Post existingPost = postController.getPostById(postId);
		
		System.out.println("Введите обновленное содержание поста: ");
		String content = scanner.nextLine();
		
		List<Label> newLabels = getLabels();
		
		/*Эта строка кода преобразует список меток existingPost.getLabels() в карту existingLabelMap,
		 где ключом является идентификатор метки, а значением - объект метки.*/
		Map<Long, Label> existingLabelMap = existingPost.getLabels().stream()
				.collect(Collectors.toMap(Label::getId, label -> label));
		
		
		
		System.out.println("Существующие метки:");
		for (Label label : existingPost.getLabels()) {
			System.out.println(label.getId() + ". " + label.getName());
		}
		
		List<Label> labelsToRemove = getLabelsToRemove(existingPost.getLabels());
		
		for (Label label : labelsToRemove) {
			labelController.deleteLabel(label.getId());
		}
		
		for (Label label : newLabels) {
			if (label.getId() == null) {
				labelController.createLabel(label);
			}
		}
		
		// удаляет из списка меток поста все метки, которые содержатся в списке labelsToRemove
		existingPost.getLabels().removeIf(labelsToRemove::contains);
		
		//оставляет в списке меток поста только те метки, которые содержатся в значениях карты existingLabelMap
		existingPost.getLabels().retainAll(existingLabelMap.values());
		
		//добавляет в список меток поста все новые метки из списка newLabels
		existingPost.getLabels().addAll(newLabels);
		
		existingPost.setContent(content);
		existingPost.setUpdated(new Date());
		existingPost.setPostStatus(PostStatus.ACTIVE);
		
		postController.updatePost(existingPost);
		System.out.println("Пост обновлен.");
	}
	
	private void deletePost() {
		System.out.print("Введите ID поста для удаления: ");
		Long postId = scanner.nextLong();
		scanner.nextLine();
		
		postController.deletePost(postId);
		System.out.println("Пост удален.");
	}
	
	
	//////////////////////////////////////////////////////////////////////
	
	private List<Label> getLabels() {
		List<Label> labels = new ArrayList<>();
		
		boolean addLabels = true;
		
		while (addLabels) {
			System.out.println("Хотите добавить метку? (да/нет)");
			String addLabelInput = scanner.nextLine();
			
			if (addLabelInput.equalsIgnoreCase("да")) {
				System.out.println("Введите название метки: ");
				String labelName = scanner.nextLine();
				
				Label label = Label.builder()
						.name(labelName)
						.labelStatus(LabelStatus.ACTIVE)
						.build();
				
				labels.add(label);
			} else {
				addLabels = false;
			}
		}
		return labels;
	}
	
	private Writer chooseWriter() {
		System.out.println("Выберите писателя по ID: ");
		Long writerId = scanner.nextLong();
		scanner.nextLine();
		
		Writer writer = writerController.getWriterById(writerId);
		if (writer != null) {
			System.out.println("Выбранный писатель: " + writer.getFirstname() + " " + writer.getLastname());
		}
		return writer;
	}
	
	private List<Label> getLabelsToRemove(List<Label> existingLabels) {
		List<Label> labelsToRemove = new ArrayList<>();
		
		boolean removeMoreLabels = true;
		
		while (removeMoreLabels) {
			System.out.println("Хотите удалить какие-либо метки? (да/нет)");
			if ("да".equalsIgnoreCase(scanner.nextLine())) {
				System.out.println("Введите ID метки, которую хотите удалить:");
				Long labelIdToRemove = scanner.nextLong();
				scanner.nextLine();
				
				Label labelToRemove = existingLabels.stream()
						.filter(label -> label.getId().equals(labelIdToRemove))
						.findFirst()
						.orElse(null);
				
				if (labelToRemove != null) {
					labelsToRemove.add(labelToRemove);
				} else {
					System.out.println("Метка с ID " + labelIdToRemove + " не найдена.");
				}
			} else {
				removeMoreLabels = false;
			}
		}
		return labelsToRemove;
	}
}