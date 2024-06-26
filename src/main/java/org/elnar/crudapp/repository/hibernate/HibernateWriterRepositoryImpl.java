package org.elnar.crudapp.repository.hibernate;


import org.elnar.crudapp.enums.WriterStatus;
import org.elnar.crudapp.exception.HibernateRepositoryException;
import org.elnar.crudapp.exception.WriterNotFoundException;
import org.elnar.crudapp.model.Writer;
import org.elnar.crudapp.repository.WriterRepository;
import org.elnar.crudapp.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;


public class HibernateWriterRepositoryImpl implements WriterRepository {
	
	@Override
	public Writer getById(Long id) {
		try (Session session =  HibernateUtil.openSession()) {
			
			Writer writer = session.createQuery(
					"FROM Writer w LEFT JOIN FETCH  w.posts where w.id = :writerId", Writer.class)
					.setParameter("writerId", id)
					.uniqueResult();
			
			if (writer == null) {
				throw new WriterNotFoundException(id);
			}
			
			return writer;
		}catch (HibernateRepositoryException e) {
			throw new HibernateRepositoryException("Ошибка при получении писателя по идентификатору", e);
		}
	}
	
	@Override
	public List<Writer> getAll() {
		try (Session session =  HibernateUtil.openSession()) {
			
			return session.createQuery("FROM Writer", Writer.class).getResultList();
		
		}catch (HibernateRepositoryException e) {
			throw new HibernateRepositoryException("Ошибка при получении всех писателей", e);
		}
	}
	
	@Override
	public Writer save(Writer writer) {
		try (Session session =  HibernateUtil.openSession()) {
			session.beginTransaction();
			
			session.persist(writer);
			
			session.getTransaction().commit();
			
			return writer;
		}catch (HibernateRepositoryException e) {
			throw new HibernateRepositoryException("Ошибка при сохранении писателя", e);
		}
	}
	
	@Override
	public Writer update(Writer writer) {
		try (Session session =  HibernateUtil.openSession()) {
			session.beginTransaction();
			
			session.merge(writer);
			
			session.getTransaction().commit();
			
			return writer;
		}catch (HibernateRepositoryException e) {
			throw new HibernateRepositoryException("Ошибка при обновление писателя", e);
		}
	}
	
	@Override
	public void deleteById(Long id) {
		try (Session session =  HibernateUtil.openSession()) {
			session.beginTransaction();
			
			Writer writer = session.get(Writer.class, id);
			
			if (writer == null) {
				throw new WriterNotFoundException(id);
			}
			
			writer.setWriterStatus(WriterStatus.DELETED);
			session.merge(writer);
			
			session.getTransaction().commit();
		}catch (HibernateRepositoryException e) {
			throw new HibernateRepositoryException("Ошибка при удаление писателя", e);
		}
	}
}
