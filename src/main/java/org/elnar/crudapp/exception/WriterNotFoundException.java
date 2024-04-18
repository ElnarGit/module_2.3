package org.elnar.crudapp.exception;

public class WriterNotFoundException extends NotFoundException {
	
	public WriterNotFoundException(Long id) {
		super("Писатель с идентификатором " + id + " не найден.");
	}
}
