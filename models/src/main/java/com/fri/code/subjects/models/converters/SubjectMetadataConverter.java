package com.fri.code.subjects.models.converters;

import com.fri.code.subjects.lib.SubjectMetadata;
import com.fri.code.subjects.models.entities.SubjectMetadataEntity;

public class SubjectMetadataConverter {

    public static SubjectMetadata toDTO(SubjectMetadataEntity entity) {
        SubjectMetadata subjectMetadata = new SubjectMetadata();

        subjectMetadata.setID(entity.getID());
        subjectMetadata.setName(entity.getName());
        subjectMetadata.setProgrammingLanguage(entity.getProgrammingLanguage());
        subjectMetadata.setUsers(entity.getUsers());

        return subjectMetadata;
    }

    public static SubjectMetadataEntity toEntity(SubjectMetadata subjectMetadata){
        SubjectMetadataEntity entity = new SubjectMetadataEntity();

        entity.setID(subjectMetadata.getID());
        entity.setName(subjectMetadata.getName());
        entity.setProgrammingLanguage(subjectMetadata.getProgrammingLanguage());
        entity.setUsers(subjectMetadata.getUsers());
        return entity;
    }

}
