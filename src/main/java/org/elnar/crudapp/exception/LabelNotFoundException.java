package org.elnar.crudapp.exception;

public class LabelNotFoundException extends NotFoundException {
	
	public LabelNotFoundException(Long id) {
		super("Метка с идентификатором " + id + " не найдена.");
	}
}
