package com.flores.dev.batch.weight.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import com.flores.dev.batch.weight.model.WeightEntry;

@EnableBatchProcessing
public class Config {

	@Autowired
	private JobBuilderFactory jobBuilder;
	
	@Autowired
	private StepBuilderFactory stepBuilder;

	@Bean
	@StepScope
	public FlatFileItemReader<WeightEntry> getCsvReader() {
		
		String[] fieldNames = {"Date", "Weight"};

		return new FlatFileItemReaderBuilder<WeightEntry>()
				.resource(null)
				.delimited()
				.names(fieldNames)
				.targetType(WeightEntry.class)
				.build();
	}
	
	@Bean
	@StepScope
	public ItemWriter<WeightEntry> getWriter() {
		return (c) -> {
			c.forEach(System.out::println);
		};
	}
	
	@Bean
	public Step step() {
		return stepBuilder.get("weightCsvReadStep")
				.<WeightEntry, WeightEntry>chunk(25)
				.reader(getCsvReader())
				.writer(getWriter())
				.build();
	}
	
	@Bean
	public Job job() {
		return jobBuilder.get("batchLoadDatabase")
				.flow(step())
				.end()
				.build();
	}
}
