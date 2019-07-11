package com.iait.springbatchrest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.iait.springbatchrest.entities.BatchErrorEntity;

public interface BatchErrorRepository extends JpaRepository<BatchErrorEntity, Long>, 
        QuerydslPredicateExecutor<BatchErrorEntity> {

}
