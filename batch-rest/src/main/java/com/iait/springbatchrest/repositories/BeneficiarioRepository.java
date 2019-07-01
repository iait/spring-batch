package com.iait.springbatchrest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.iait.springbatchrest.entities.BeneficiarioEntity;

public interface BeneficiarioRepository extends JpaRepository<BeneficiarioEntity, Long>, 
        QuerydslPredicateExecutor<BeneficiarioEntity> {

}
