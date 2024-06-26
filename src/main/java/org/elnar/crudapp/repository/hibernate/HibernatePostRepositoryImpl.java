package org.elnar.crudapp.repository.hibernate;

import org.elnar.crudapp.enums.PostStatus;
import org.elnar.crudapp.exception.HibernateRepositoryException;
import org.elnar.crudapp.exception.PostNotFoundException;
import org.elnar.crudapp.model.Post;
import org.elnar.crudapp.repository.PostRepository;
import org.elnar.crudapp.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class HibernatePostRepositoryImpl implements PostRepository {
	
	@Override
	public Post getById(Long id) {
		try (Session session = HibernateUtil.openSession()) {
			
			//позволяет загрузить пост вместе со всеми связанными с ним метками
			Post post = session.createQuery(
							"FROM Post p LEFT JOIN FETCH p.labels WHERE p.id = :postId", Post.class)
					.setParameter("postId", id)
					.uniqueResult();
			
			if (post == null) {
				throw new PostNotFoundException(id);
			}
			
			return post;
		} catch (HibernateRepositoryException e) {
			throw new HibernateRepositoryException("Ошибка при получении поста по идентификатору", e);
		}
	}
	
	@Override
	public List<Post> getAll() {
		try (Session session =  HibernateUtil.openSession()) {
			
			return session.createQuery("FROM Post", Post.class).getResultList();
			
		}catch (HibernateRepositoryException e) {
			throw new HibernateRepositoryException("Ошибка при получении всех постов", e);
		}
	}
	
	@Override
	public Post save(Post post) {
		try (Session session = HibernateUtil.openSession()) {
			session.beginTransaction();
			
			session.persist(post);
			
			session.getTransaction().commit();
			
			return post;
		}catch (HibernateRepositoryException e) {
			throw new HibernateRepositoryException("Ошибка при сохранении поста", e);
		}
	}
	
	@Override
	public Post update(Post post) {
		try (Session session =  HibernateUtil.openSession()) {
			session.beginTransaction();
			
			session.merge(post);
			
			session.getTransaction().commit();
			
			return post;
		}catch (HibernateRepositoryException e) {
			throw new HibernateRepositoryException("Ошибка при обновление поста", e);
		}
	}
	
	@Override
	public void deleteById(Long id) {
		try (Session session =  HibernateUtil.openSession()) {
			session.beginTransaction();
			
			Post post = session.get(Post.class, id);
			
			if(post == null){
				throw new PostNotFoundException(id);
			}
			
			post.setPostStatus(PostStatus.DELETED);
			session.merge(post);
			
			session.getTransaction().commit();
		}catch (HibernateRepositoryException e) {
			throw new HibernateRepositoryException("Ошибка при удаление поста", e);
		}
	}
}
