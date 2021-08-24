package com.araki.payroll.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.araki.payroll.entities.Payment;
import com.araki.payroll.entities.Worker;
import com.araki.payroll.feignclients.WorkerFeignClient;

@Service
public class PaymentService {

	Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private WorkerFeignClient workerFeignClient;
	
	public Payment getPayment(long workerId, int days) {
		Worker worker = null;
		try{
			worker = workerFeignClient.findById(workerId).getBody();
			return new Payment(worker.getName(), worker.getDailyIncome(), days);

		}catch (Exception ex){
			LOGGER.error(ex.getMessage());
			ex.printStackTrace();
		}

		throw new RuntimeException();
	}
}
