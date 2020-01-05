package com.fri.code.subjects.v1.resources;

import com.fri.code.subjects.lib.SubjectMetadata;
import com.fri.code.subjects.services.beans.SubjectMetadataBean;
import com.fri.code.subjects.v1.dtos.ApiError;
import org.eclipse.microprofile.metrics.annotation.Timed;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
@Path("/subjects")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SubjectMetadataResource {

    @Inject
    private SubjectMetadataBean subjectMetadataBean;


    @GET
    @Path("/all")
    @Timed
    public Response getSubjects() {
        List<SubjectMetadata> subjects = subjectMetadataBean.getSubjects();
        return Response.status(Response.Status.OK).entity(subjects).build();
    }

    @GET
    @Path("/{subjectID}")
    public Response getSubjectById(@PathParam("subjectID") Integer subjectID) {
        try {
            SubjectMetadata subject = subjectMetadataBean.getSubjectById(subjectID);
            return Response.status(Response.Status.OK).entity(subject).build();
        } catch (Exception e) {
            ApiError error = new ApiError();
            error.setCode(Response.Status.NOT_FOUND.toString());
            error.setMessage("The subject was not found");
            error.setStatus(Response.Status.NOT_FOUND.getStatusCode());
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
    }

    @PUT
    @Path("/{subjectID}/addUser")
    public Response addUser(@PathParam("subjectID") Integer subjectID, @QueryParam("userID") Integer userID){
        SubjectMetadata subjectMetadata = subjectMetadataBean.addUser(subjectID, userID);
        if (subjectMetadata == null){
            ApiError error = new ApiError();
            error.setCode(Response.Status.BAD_REQUEST.toString());
            error.setMessage("Something is wrong");
            error.setStatus(Response.Status.BAD_REQUEST.getStatusCode());
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
        return Response.status(Response.Status.OK).entity(subjectMetadata).build();
    }

    @POST
    public Response createSubject(SubjectMetadata subjectMetadata) {

        if (subjectMetadata.getName() == null || subjectMetadata.getProgrammingLanguage() == null) {
            ApiError error = new ApiError();
            error.setCode(Response.Status.BAD_REQUEST.toString());
            error.setMessage("Some parameters are missing");
            error.setStatus(Response.Status.BAD_REQUEST.getStatusCode());
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        } else {
            try {
                subjectMetadata = subjectMetadataBean.createSubjectMetadata(subjectMetadata);
                return Response.status(Response.Status.OK).entity(subjectMetadata).build();
            } catch (Exception e) {
                ApiError error = new ApiError();
                error.setCode(Response.Status.BAD_REQUEST.toString());
                error.setMessage("The subject cannot be added");
                error.setStatus(Response.Status.BAD_REQUEST.getStatusCode());
                return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
            }

        }
    }

    @PUT
    @Path("/{subjectID}")
    public Response putSubject(@PathParam("subjectID") Integer subjectID, SubjectMetadata updatedSubject) {
        if (updatedSubject.getName() == null || updatedSubject.getProgrammingLanguage() == null) {
            ApiError error = new ApiError();
            error.setCode(Response.Status.BAD_REQUEST.toString());
            error.setMessage("Some parameters are missing");
            error.setStatus(Response.Status.BAD_REQUEST.getStatusCode());
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        updatedSubject = subjectMetadataBean.putSubjectMetadata(subjectID, updatedSubject);
        if (updatedSubject == null) {
            ApiError error = new ApiError();
            error.setCode(Response.Status.NOT_FOUND.toString());
            error.setMessage("The subject was not found");
            error.setStatus(Response.Status.NOT_FOUND.getStatusCode());
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
        return Response.status(Response.Status.OK).entity(updatedSubject).build();
    }

    @DELETE
    @Path("/{subjectID}")
    public Response deleteSubject(@PathParam("subjectID") Integer subjectID) {
        if (subjectMetadataBean.deleteSubjectMetadata(subjectID)) {
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            ApiError error = new ApiError();
            error.setCode(Response.Status.NOT_FOUND.toString());
            error.setMessage("The subject was not found");
            error.setStatus(Response.Status.NOT_FOUND.getStatusCode());
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
    }

}
