package com.araki.hrworker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.araki.hrworker.entities.Worker;

public interface WorkerRepository extends JpaRepository<Worker, Long>{

}
