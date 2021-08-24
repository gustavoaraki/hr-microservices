package com.araki.payroll.feignclients;

import com.araki.payroll.config.FeignClientConfig;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.araki.payroll.entities.Worker;

@Component
@FeignClient(name = "hr-worker", path = "/workers", configuration = FeignClientConfig.class)
public interface WorkerFeignClient {
	
	@LoadBalanced
	@GetMapping(value = "/{id}")
	ResponseEntity<Worker> findById(@PathVariable Long id);
}
