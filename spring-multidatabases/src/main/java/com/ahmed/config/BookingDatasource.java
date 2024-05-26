package com.ahmed.config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "bookingEntityManagerFactory", transactionManagerRef = "bookingTransactionManager", basePackages = "com.ahmed.repo.booking")
public class BookingDatasource {

	@Primary
	@Bean(name = "bookingDataSource")
	@ConfigurationProperties(prefix = "spring.booking.datasource")
	public DataSource userDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Primary
	@Bean(name = "bookingEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("bookingDataSource") DataSource dataSource) {
		HashMap<String, Object> properties = new HashMap<>();
//		properties.put("hibernate.hbm2ddl.auto", "update");
//		properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
		return builder.dataSource(dataSource).properties(properties).packages("com.ahmed.model.booking")
				.persistenceUnit("Booking").build();
	}

	@Primary
	@Bean(name = "bookingTransactionManager")
	public PlatformTransactionManager transactionManager(
			@Qualifier("bookingEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}

}
