package com.flores.dev.batch.weight.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.flores.dev.batch.weight.model.WeightEntry;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Autowired
	private JobBuilderFactory jobBuilder;
	
	@Autowired
	private StepBuilderFactory stepBuilder;

	@Autowired
	@Qualifier("mysql")
	private DataSource mysql;
	
	@Bean
	@StepScope
	public FlatFileItemReader<WeightEntry> getCsvReader(@Value("#{jobParameters['inputFile']}") String inputFile) {
		log.trace("Initializing reader...");

		Resource resource = new FileSystemResource(inputFile);
		
		String[] fieldNames = {"Date", "Weight"};

		return new FlatFileItemReaderBuilder<WeightEntry>()
				.name("weightCsvReader")
				.resource(resource)
				.linesToSkip(1)
				.delimited()
				.names(fieldNames)
				.targetType(WeightEntry.class)
				.build();
	}

	@Bean
	@StepScope
	public ItemWriter<WeightEntry> getWriter() throws Exception {
		log.trace("Initializing writer...");

		return new JdbcBatchItemWriterBuilder<WeightEntry>()
				.itemSqlParameterSourceProvider(
						new BeanPropertyItemSqlParameterSourceProvider<>())
				.sql("insert into weight_entries(entry_date, entry_value) values (:date, :weight)")
				.dataSource(mysql)
				.build();
	}

	@Bean
	public Step step() throws Exception {
		return stepBuilder.get("weightCsvReadStep")
				.<WeightEntry, WeightEntry>chunk(25)
				.reader(getCsvReader(null))
				.writer(getWriter())
				.build();
	}
	
	@Bean
	public Job job() throws Exception {
		return jobBuilder.get("batchLoadDatabase")
				.flow(step())
				.end()
				.build();
	}
}
