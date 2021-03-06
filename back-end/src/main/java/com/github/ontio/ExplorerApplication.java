/*
 * Copyright (C) 2018 The ontology Authors
 * This file is part of The ontology library.
 *
 * The ontology is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The ontology is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with The ontology.  If not, see <http://www.gnu.org/licenses/>.
 */


package com.github.ontio;

import com.github.ontio.utils.ConfigParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

@SpringBootApplication
@EnableAsync
@EnableTransactionManagement
public class ExplorerApplication {

	private static final Logger logger = LoggerFactory.getLogger(ExplorerApplication.class);


	@Autowired
	private ConfigParam configParam;

	public static void main(String[] args) {
		SpringApplication.run(ExplorerApplication.class, args);
	}


	@Bean
	public AsyncTaskExecutor taskExecutor() {
		logger.info("########taskExecutor#########");
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setMaxPoolSize(configParam.THREADPOOLSIZE_MAX);
		executor.setCorePoolSize(configParam.THREADPOOLSIZE_CORE);
		executor.setQueueCapacity(configParam.THREADPOOLSIZE_QUEUE);
		executor.setThreadNamePrefix("TxnHandlerThread--");
		executor.setKeepAliveSeconds(configParam.THREADPOOLSIZE_KEEPALIVE_SECOND);
		// Rejection policies
		executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
			@Override
			public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
				logger.error("###########reject thread....");
				// .....
			}
		});
		// executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.initialize();

		return executor;
	}


}
