package com.iait.springbatchrest.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iait.springbatchrest.dtos.BeneficiarioDto;
import com.iait.springbatchrest.entities.BeneficiarioEntity;
import com.iait.springbatchrest.entities.QBeneficiarioEntity;
import com.iait.springbatchrest.repositories.BeneficiarioRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Service
public class BeneficiarioService {

    @Autowired
    private BeneficiarioRepository beneficiarioRepository;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private EntityManagerFactory emf;

    @Transactional(readOnly = true)
    public Optional<BeneficiarioEntity> buscarPorId(Long id) {
        return beneficiarioRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<BeneficiarioEntity> buscarTodos() {
        return beneficiarioRepository.findAll();
    }

    @Transactional
    public BeneficiarioEntity alta(BeneficiarioDto dto) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QBeneficiarioEntity q = QBeneficiarioEntity.beneficiarioEntity;
        Long maxId = queryFactory.select(q.id.max()).from(q).fetchFirst();
        if (maxId == null) {
            maxId = 0L;
        }
        BeneficiarioEntity entity = new BeneficiarioEntity();
        entity.setId(maxId + 1);
        entity.setNombre(dto.getNombre());
        entity.setApellido(dto.getApellido());
        entity.setCuil(dto.getCuil());
        entity.setFechaNacimiento(dto.getFechaNacimiento());
        return beneficiarioRepository.save(entity);
    }

    @Transactional
    public void alta(List<? extends BeneficiarioEntity> entities) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QBeneficiarioEntity q = QBeneficiarioEntity.beneficiarioEntity;
        for (BeneficiarioEntity entity : entities) {
            Long maxId = queryFactory.select(q.id.max()).from(q).fetchFirst();
            if (maxId == null) {
                maxId = 0L;
            }
            entity.setId(maxId + 1);
            em.persist(entity);
        }
        em.getTransaction().commit();
    }
}
