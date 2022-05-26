package com.issuemoa.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableBatchProcessing // 배치 활성화
@EnableJpaAuditing // Audit은 감시하다라는 뜻으로 Spring Data JPA에서 시간에 대해서 자동으로 값을 넣어주는 기능
@SpringBootApplication
public class IssuemoaBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(IssuemoaBatchApplication.class, args);
	}

}
