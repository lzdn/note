package com.lzdn.note;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.lzdn.note.common.repository.BaseRepositoryFactoryBean;

@SpringBootApplication
@EnableJpaRepositories(/*basePackages = {"org.konghao"},*/repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class) 
@ComponentScan(value = "com.lzdn.note")
public class NoteApplication {

	public static void main(String[] args) {
		SpringApplication.run(NoteApplication.class, args);
	}

}
