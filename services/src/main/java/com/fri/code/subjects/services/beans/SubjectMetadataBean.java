package com.fri.code.subjects.services.beans;

import com.fri.code.subjects.lib.SubjectMetadata;
import com.fri.code.subjects.models.converters.SubjectMetadataConverter;
import com.fri.code.subjects.models.entities.SubjectMetadataEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class SubjectMetadataBean {

    @Inject
    EntityManager em;

    public List<SubjectMetadata> getSubjects(){
        TypedQuery<SubjectMetadataEntity> query = em.createNamedQuery("SubjectMetadataEntity.getAll", SubjectMetadataEntity.class);
        return query.getResultList().stream().map(SubjectMetadataConverter::toDTO).collect(Collectors.toList());
    }

    public SubjectMetadata getSubjectById(Integer subjectID){
        TypedQuery<SubjectMetadataEntity> query = em.createNamedQuery("SubjectMetadataEntity.getSubjectById", SubjectMetadataEntity.class);
        return SubjectMetadataConverter.toDTO(query.setParameter(1, subjectID).getSingleResult());
    }


    public SubjectMetadata createSubjectMetadata(SubjectMetadata subjectMetadata){
        SubjectMetadataEntity subjectMetadataEntity = SubjectMetadataConverter.toEntity(subjectMetadata);

        try {
            beginTx();
            em.persist(subjectMetadataEntity);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        if (subjectMetadataEntity.getID() == null) {
            throw new RuntimeException("The input was not saved");
        }

        return SubjectMetadataConverter.toDTO(subjectMetadataEntity);
    }

    public SubjectMetadata putSubjectMetadata(Integer ID, SubjectMetadata subjectMetadata){

        SubjectMetadataEntity entity = em.find(SubjectMetadataEntity.class, ID);

        if (entity == null){
            return null;
        }


        SubjectMetadataEntity updatedEntity = SubjectMetadataConverter.toEntity(subjectMetadata);


        try {
            beginTx();
            updatedEntity.setID(entity.getID());
            updatedEntity = em.merge(updatedEntity);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return SubjectMetadataConverter.toDTO(updatedEntity);
    }

    public boolean deleteSubjectMetadata(Integer subjectID){

        SubjectMetadataEntity entity = em.find(SubjectMetadataEntity.class, subjectID);
        if (entity != null) {
            try {
                beginTx();
                em.remove(entity);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
            }
        }
        else return false;
        return true;
    }


    private void beginTx() {
        if (!em.getTransaction().isActive())
            em.getTransaction().begin();
    }

    private void commitTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().commit();
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().rollback();
    }
}
