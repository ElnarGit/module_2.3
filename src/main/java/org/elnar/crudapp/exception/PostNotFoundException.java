package org.elnar.crudapp.exception;

public class PostNotFoundException extends NotFoundException {
	
	public PostNotFoundException(Long id) {
		super("Пост с идентификатором " + id + " не найден.");
	}
}
