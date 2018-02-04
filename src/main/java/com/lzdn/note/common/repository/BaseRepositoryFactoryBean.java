package com.lzdn.note.common.repository;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

public class BaseRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable> extends JpaRepositoryFactoryBean<T, S, ID> {
	
	public BaseRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
		super(repositoryInterface);
	}
	@SuppressWarnings("rawtypes")
	@Override
	protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
		return new CustomerRepositoryFactory(entityManager);
	}
	private static class CustomerRepositoryFactory<T, I extends Serializable> extends JpaRepositoryFactory {
		private EntityManager entityManager;
		public CustomerRepositoryFactory(EntityManager entityManager) {
			super(entityManager);
			this.entityManager = entityManager;
		}
		@SuppressWarnings({ "unused", "unchecked" })
		protected Object getTargetRepository(RepositoryMetadata metadata) {
			return new BaseRepositoryImpl<T, I>((Class<T>) metadata.getDomainType(), entityManager);
		}
		protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
			return BaseRepository.class;
		}
	}
}
