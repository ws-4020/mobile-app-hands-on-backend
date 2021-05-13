package com.example.todo.api;

import com.example.openapi.OpenApiValidator;
import com.example.system.nablarch.FlywayExecutor;
import nablarch.core.repository.SystemRepository;
import nablarch.fw.web.HttpResponse;
import nablarch.fw.web.RestMockHttpRequest;
import nablarch.test.core.http.SimpleRestTestSupport;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.file.Paths;

public class TodoDeleteRestApiTest extends SimpleRestTestSupport {

    public static OpenApiValidator openApiValidator = new OpenApiValidator(Paths.get("rest-api-specification/openapi.yaml"));

    @BeforeClass
    public static void setUpClass() {
        FlywayExecutor flywayExecutor = SystemRepository.get("dbMigration");
        flywayExecutor.migrate(true);
    }

    @Test
    public void RESTAPIでToDoを削除できる() throws Exception {

        RestMockHttpRequest request = delete("/api/todos/2002");
        HttpResponse response = sendRequest(request);

        assertStatusCode("ToDoの削除", HttpResponse.Status.NO_CONTENT, response);

        openApiValidator.validate("deleteTodo", request, response);

    }

}
