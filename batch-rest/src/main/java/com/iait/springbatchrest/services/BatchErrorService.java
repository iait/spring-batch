package com.iait.springbatchrest.services;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iait.springbatchrest.entities.BatchErrorEntity;
import com.iait.springbatchrest.entities.QBatchErrorEntity;
import com.iait.springbatchrest.enums.EtapaBatch;
import com.iait.springbatchrest.repositories.BatchErrorRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Service
public class BatchErrorService {

    private static Logger LOG = LoggerFactory.getLogger(BatchErrorService.class);

    @Autowired
    private BatchErrorRepository batchErrorRepository;

    @Autowired
    private ObjectMapper om;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private EntityManagerFactory emf;

    @Transactional(readOnly = true)
    public Optional<BatchErrorEntity> buscarPorId(Long id) {
        return batchErrorRepository.findById(id);
    }

    @Transactional
    public BatchErrorEntity registrarError(Long jobExecutionId, EtapaBatch etapa, 
            Integer elemento, Object item, Throwable t) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QBatchErrorEntity q = QBatchErrorEntity.batchErrorEntity;

        Integer maxId = queryFactory.select(q.errorId.max()).from(q)
                .where(q.jobExecutionId.eq(jobExecutionId))
                .fetchFirst();
        if (maxId == null) {
            maxId = 0;
        }
        Integer errorId = maxId + 1;
        BatchErrorEntity batchErrorEnt = new BatchErrorEntity();
        batchErrorEnt.setJobExecutionId(jobExecutionId);
        batchErrorEnt.setErrorId(errorId);

        batchErrorEnt.setEtapa(etapa);
        batchErrorEnt.setElemento(elemento);

        if (item != null) {
            try {
                batchErrorEnt.setEntidad(om.writeValueAsString(item));
            } catch (JsonProcessingException e) {
                LOG.error("No se pudo serializar el item fallido del job {} error {}", 
                        jobExecutionId, errorId);
            }
        }

        // manejo excepción
        String descripcion;
        if (t instanceof FlatFileParseException) {
            FlatFileParseException ex = (FlatFileParseException) t;
            batchErrorEnt.setLinea(ex.getLineNumber());
            batchErrorEnt.setInput(ex.getInput());
            Throwable originalCause = t;
            while (originalCause.getCause() != null) {
                originalCause = originalCause.getCause();
            }
            descripcion = originalCause.getLocalizedMessage();
        } else {
            descripcion = t.getLocalizedMessage();
        }
        if (descripcion == null) {
            descripcion = t.getClass().getSimpleName();
        }
        batchErrorEnt.setDescripcion(descripcion);

        Writer sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        batchErrorEnt.setStackTrace(sw.toString());

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(batchErrorEnt);
        em.getTransaction().commit();

        return batchErrorEnt;
    }
}
