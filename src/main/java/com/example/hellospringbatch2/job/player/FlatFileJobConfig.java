package com.example.hellospringbatch2.job.player;

import com.example.hellospringbatch2.core.dto.PlayerDto;
import com.example.hellospringbatch2.core.dto.PlayerSalaryDto;
import com.example.hellospringbatch2.core.service.PlayerSalaryService;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.util.List;

@Configuration
@AllArgsConstructor
public class FlatFileJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job flatFileJob(Step flatFileStep) {
        return jobBuilderFactory.get("flatFileJob")
                .incrementer(new RunIdIncrementer())
                .start(flatFileStep)
                .build();
    }

    @JobScope
    @Bean
    public Step flatFileStep(FlatFileItemReader<PlayerDto> playerFileItemReader
            , ItemProcessor<PlayerDto, PlayerSalaryDto> playerSalaryItemProcessor) {
        return stepBuilderFactory.get("flatFileStep")
                .<PlayerDto, PlayerSalaryDto>chunk(3)
                .reader(playerFileItemReader)
                .processor(playerSalaryItemProcessor)
                .writer(new ItemWriter<>() {
                    @Override
                    public void write(List<? extends PlayerSalaryDto> items) throws Exception {
                        items.forEach(System.out::println);
                    }
                })
                .build();
    }


    @StepScope
    @Bean
    public FlatFileItemReader<PlayerDto> playerFileItemReader() {
        return new FlatFileItemReaderBuilder<PlayerDto>()
                .name("playerFileItemReader")
                .lineTokenizer(new DelimitedLineTokenizer())  // , 로 구분
                .linesToSkip(1) // 가장 위에서 부터 1줄 건너뜀
                .fieldSetMapper(new PlayerFieldSetMapper()) // 라인을 어떻게 객체로 매핑할껀지
                .resource(new FileSystemResource("player-list.txt"))  // 읽을 파일
                .build();

    }


    @StepScope
    @Bean
    public ItemProcessor<PlayerDto, PlayerSalaryDto> playerSalaryItemProcessor(PlayerSalaryService playerSalaryService) {
        return item -> playerSalaryService.calSalary(item);
    }

//    @StepScope
//    @Bean
//    public FlatFileItemWriter<PlayerSalaryDto> playerFileItemWriter(){
//
//        return new FlatFileItemWriterBuilder<PlayerSalaryDto>()
//                .name("playerFileItemWriter")
//                .resource()
//                .lineAggregator()
//                .build();
//
//    }

}