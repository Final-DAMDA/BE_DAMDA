package com.damda.back.config.csv;

import com.damda.back.domain.area.Area;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.support.builder.CompositeItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.persistence.EntityManagerFactory;

@Configuration
@RequiredArgsConstructor
public class AreaReaderJobConfig {
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final EntityManagerFactory entityManagerFactory;
	@Bean
	public Job saveAreaJob() throws Exception {
		return this.jobBuilderFactory.get("saveAreaJob")
				.incrementer(new RunIdIncrementer())
				.start(saveAreaStep())
				.build();
	}

	@Bean
	public Step saveAreaStep() throws Exception {
		return this.stepBuilderFactory.get("saveAreaStep")
				.<Area, Area>chunk(10)
				.reader(itemReader())
				.writer(itemWriter())
				.build();
	}

	private ItemReader<? extends Area> itemReader() throws Exception {
		DefaultLineMapper lineMapper = new DefaultLineMapper();
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

		lineTokenizer.setNames("city","district");
		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(fieldSet -> new Area(
				fieldSet.readString(0),
				fieldSet.readString(1)));

		FlatFileItemReader<Area> itemReader = new FlatFileItemReaderBuilder<Area>()
				.name("saveAreaItemReader")
				.encoding("UTF-8")
				.linesToSkip(1)
				.resource((new ClassPathResource("/csv/Area.csv")))
				.lineMapper(lineMapper)
				.build();

		itemReader.afterPropertiesSet();

		return itemReader;
	}
	private ItemWriter<? super Area> itemWriter() throws Exception {
		JpaItemWriter<Area> jpaItemWriter = new JpaItemWriterBuilder<Area>()
				.entityManagerFactory(entityManagerFactory)
				.build();


		CompositeItemWriter<Area> itemWriter = new CompositeItemWriterBuilder<Area>()
				.delegates(jpaItemWriter)
				.build();

		itemWriter.afterPropertiesSet();
		return itemWriter;
	}

}
