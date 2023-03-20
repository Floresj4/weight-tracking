package com.flores.dev.batch.weight.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import com.mysql.jdbc.Driver;

@Configuration
public class DataSourceConfig {

	
	@Bean
	@Primary
	public DataSource hsql() {
        final SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriver(new org.hsqldb.jdbcDriver());
        dataSource.setUrl("jdbc:hsqldb:mem:mydb");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
	}
	
	@Bean
	public SimpleDriverDataSource mysql() throws SQLException {
		
		try {
	        final SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
	        dataSource.setDriver(new com.mysql.cj.jdbc.Driver());
	        dataSource.setUrl("jdbc:mysql://localhost:3306/weight_tracking");
	        dataSource.setUsername("root");
	        dataSource.setPassword("1234");

	        return dataSource;
		}
		catch (SQLException e) {
			throw e;
		}
		
	}
}
