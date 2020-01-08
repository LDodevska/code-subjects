package com.fri.code.subjects.v1.resources;

import com.fri.code.subjects.lib.SubjectMetadata;
import com.fri.code.subjects.lib.UserMetadata;
import com.fri.code.subjects.services.beans.SubjectMetadataBean;
import com.fri.code.subjects.v1.dtos.ApiError;
import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.logs.cdi.Log;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.eclipse.microprofile.metrics.annotation.Timed;


import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Log
@ApplicationScoped
@Path("/subjects")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SubjectMetadataResource {

    @Inject
    private SubjectMetadataBean subjectMetadataBean;

    @Inject
    @DiscoverService(value = "code-users")
    private Optional<String> usersPath;

    Client httpClient;

    @PostConstruct
    private void init() {
        httpClient = ClientBuilder.newClient();
    }


    @GET
    @Operation(summary = "Get all subjects", description = "Returns details for the subjects.")
    @ApiResponses({
            @ApiResponse(description = "Subjects' details", responseCode = "200",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = SubjectMetadata.class))))
    })
    @Path("/all")
    @Timed
    public Response getSubjects() {
        List<SubjectMetadata> subjects = subjectMetadataBean.getSubjects();
        return Response.status(Response.Status.OK).entity(subjects).build();
    }

    @GET
    @Operation(summary = "Get subject", description = "Returns details for the specific subject.")
    @ApiResponses({
            @ApiResponse(description = "Subject details", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = SubjectMetadata.class))),
            @ApiResponse(description = "The subject cannot be found", responseCode = "404")
    })
    @Path("/{subjectID}")
    public Response getSubjectById(@PathParam("subjectID") Integer subjectID) {
        try {
            SubjectMetadata subject = subjectMetadataBean.getSubjectById(subjectID);
            return Response.status(Response.Status.OK).entity(subject).build();
        } catch (Exception e) {
            ApiError error = createApiError("The subject was not found", Response.Status.NOT_FOUND);
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
    }

    @PUT
    @Operation(summary = "Add user ID for specific subject", description = "Returns subject with updated user IDs.")
    @ApiResponses({
            @ApiResponse(description = "Add user ID for subject", responseCode = "200",
                    content = @Content(schema = @Schema(implementation =
                    SubjectMetadata.class))),
            @ApiResponse(description = "The user ID cannot be added", responseCode = "400")
    })
    @Path("/{subjectID}/addUser")
    public Response addUser(@PathParam("subjectID") Integer subjectID, @QueryParam("userID") Integer userID){
        SubjectMetadata subjectMetadata = subjectMetadataBean.addUser(subjectID, userID);
        if (subjectMetadata == null){
            ApiError error = createApiError("The user cannot be added", Response.Status.BAD_REQUEST);
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
        return Response.status(Response.Status.OK).entity(subjectMetadata).build();
    }

    @PUT
    @Operation(summary = "Remove user ID for specific subject", description = "Returns subject with updated user IDs.")
    @ApiResponses({
            @ApiResponse(description = "Remove user ID for subject", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = SubjectMetadata.class))),
            @ApiResponse(description = "The user ID cannot be removed", responseCode = "400")
    })
    @Path("/{subjectID}/removeUser")
    public Response removeUser(@PathParam("subjectID") Integer subjectID, @QueryParam("userID") Integer userID){
        SubjectMetadata subjectMetadata = subjectMetadataBean.addUser(subjectID, userID);
        if (subjectMetadata == null){
            ApiError error = createApiError("The user cannot be removed", Response.Status.BAD_REQUEST);
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
        return Response.status(Response.Status.OK).entity(subjectMetadata).build();
    }


    @POST
    @Operation(summary = "Create a new subject", description = "Returns the created subject.")
    @ApiResponses({
            @ApiResponse(description = "Create new subject", responseCode = "200", content = @Content(schema = @Schema(implementation =
                    SubjectMetadata.class))),
            @ApiResponse(description = "The subject cannot be created", responseCode = "400")
    })
    public Response createSubject(SubjectMetadata subjectMetadata) {

        if (subjectMetadata.getName() == null || subjectMetadata.getProgrammingLanguage() == null) {
            ApiError error = createApiError("Some parameters are missing", Response.Status.BAD_REQUEST);
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        } else {
            try {
                subjectMetadata = subjectMetadataBean.createSubjectMetadata(subjectMetadata);
                return Response.status(Response.Status.OK).entity(subjectMetadata).build();
            } catch (Exception e) {
                ApiError error = createApiError("The subject cannot be added", Response.Status.BAD_REQUEST);
                return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
            }

        }
    }

    @PUT
    @Operation(summary = "Update an existing subject", description = "Returns the updated subject.")
    @ApiResponses({
            @ApiResponse(description = "Update subject", responseCode = "200", content = @Content(schema = @Schema(implementation =
                    SubjectMetadata.class))),
            @ApiResponse(description = "Missing parameters", responseCode = "400"),
            @ApiResponse(description = "The subject cannot be found", responseCode = "404")
    })
    @Path("/{subjectID}")
    public Response putSubject(@PathParam("subjectID") Integer subjectID, SubjectMetadata updatedSubject) {
        if (updatedSubject.getName() == null || updatedSubject.getProgrammingLanguage() == null) {
            ApiError error = createApiError("Some parameters are missing", Response.Status.BAD_REQUEST);
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        updatedSubject = subjectMetadataBean.putSubjectMetadata(subjectID, updatedSubject);
        if (updatedSubject == null) {
            ApiError error = createApiError("The subject was not found", Response.Status.NOT_FOUND);
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
        return Response.status(Response.Status.OK).entity(updatedSubject).build();
    }

    @DELETE
    @Operation(summary = "Delete an existing subject", description = "Deletes a specific subject.")
    @ApiResponses({
            @ApiResponse(description = "Delete subject", responseCode = "204"),
            @ApiResponse(description = "The subject cannot be found", responseCode = "404")
    })
    @Path("/{subjectID}")
    public Response deleteSubject(@PathParam("subjectID") Integer subjectID) {
        if (subjectMetadataBean.deleteSubjectMetadata(subjectID)) {
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            ApiError error = createApiError("The subject was not found", Response.Status.NOT_FOUND);
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
    }

    public ApiError createApiError(String message, Response.Status responseStatus){
        ApiError error = new ApiError();
        error.setCode(responseStatus.toString());
        error.setMessage(message);
        error.setStatus(responseStatus.getStatusCode());
        return error;
    }
}
