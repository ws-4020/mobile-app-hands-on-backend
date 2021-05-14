package com.example.todo.api;

import com.example.todo.application.TodoService;
import com.example.todo.domain.*;

import nablarch.core.repository.di.config.externalize.annotation.SystemRepositoryComponent;
import nablarch.core.validation.ee.ValidatorUtil;
import nablarch.fw.ExecutionContext;
import nablarch.fw.jaxrs.EntityResponse;
import nablarch.fw.web.HttpRequest;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import nablarch.fw.web.HttpResponse;

@SystemRepositoryComponent
@Path("/todos/{todoId}")
public class TodoAction {

    private final TodoService todoService;

    public TodoAction(TodoService todoService) {
        this.todoService = todoService;
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public EntityResponse put(HttpRequest request, ExecutionContext context, PutRequest requestBody) {

        UserId userId = new UserId("1001");

        ValidatorUtil.validate(requestBody);

        EntityResponse response = new EntityResponse();
        TodoId todoId = new TodoId(Long.valueOf(request.getParam("todoId")[0]));
        TodoStatus status = requestBody.completed ? TodoStatus.COMPLETED : TodoStatus.INCOMPLETE;

        Todo todo = todoService.updateStatus(todoId, status, userId);
        response.setEntity(new TodoResponse(todo.id(), todo.text(), todo.status()));
        response.setStatusCode(HttpResponse.Status.CREATED.getStatusCode());

        return response;

    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public void delete(HttpRequest request, ExecutionContext context) {

        UserId userId = new UserId("1001");

        TodoId todoId = new TodoId(Long.valueOf(request.getParam("todoId")[0]));
        todoService.deleteTodo(todoId, userId);
    }

    public static class PutRequest {
        @NotNull
        public Boolean completed;
    }

}
