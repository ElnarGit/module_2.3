package org.elnar.crudapp.repository.hibernate;

import org.elnar.crudapp.enums.LabelStatus;
import org.elnar.crudapp.exception.HibernateRepositoryException;
import org.elnar.crudapp.exception.NotFoundException;
import org.elnar.crudapp.model.Label;
import org.elnar.crudapp.repository.LabelRepository;
import org.elnar.crudapp.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class HibernateLabelRepositoryImpl implements LabelRepository {
	
	@Override
	public Label getById(Long id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			
			Label label = session.get(Label.class, id);
			
			if(label == null){
				throw new NotFoundException("Метка с идентификатором " + id + " не найдена.");
			}
			
			return label;
		}catch (HibernateRepositoryException e) {
			throw new HibernateRepositoryException("Ошибка при получении метки по идентификатору", e);
		}
	}
	
	@Override
	public List<Label> getAll() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			
			return session.createQuery("FROM Label ", Label.class).getResultList();
			
		}catch (HibernateRepositoryException e) {
			throw new HibernateRepositoryException("Ошибка при получении всех меток", e);
		}
	}
	
	@Override
	public Label save(Label label) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			
			session.persist(label);
			
			session.getTransaction().commit();
			
			return label;
		}catch (HibernateRepositoryException e) {
			throw new HibernateRepositoryException("Ошибка при сохранении метки", e);
		}
	}
	
	@Override
	public Label update(Label label) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			
			session.merge(label);
			
			session.getTransaction().commit();
			
			return label;
		}catch (HibernateRepositoryException e) {
			throw new HibernateRepositoryException("Ошибка при обновление метки", e);
		}
	}
	
	@Override
	public void deleteById(Long id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			
			Label label = session.get(Label.class, id);
			
			if(label == null){
				throw new NotFoundException("Метка с идентификатором " + id + " не найдена.");
			}
			
			label.setLabelStatus(LabelStatus.DELETED);
			session.merge(label);
			
			session.getTransaction().commit();
		}catch (HibernateRepositoryException e) {
			throw new HibernateRepositoryException("Ошибка при удаление метки", e);
		}
	}
}
