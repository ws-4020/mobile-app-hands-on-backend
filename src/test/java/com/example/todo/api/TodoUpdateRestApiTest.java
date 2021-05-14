package com.example.todo.api;

import com.example.openapi.OpenApiValidator;
import com.example.system.nablarch.FlywayExecutor;
import nablarch.core.repository.SystemRepository;
import nablarch.fw.web.HttpResponse;
import nablarch.fw.web.RestMockHttpRequest;
import nablarch.test.core.http.SimpleRestTestSupport;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.nio.file.Paths;
import java.util.Map;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TodoUpdateRestApiTest extends SimpleRestTestSupport {

    public static OpenApiValidator openApiValidator = new OpenApiValidator(Paths.get("rest-api-specification/openapi.yaml"));

    @BeforeClass
    public static void setUpClass() {
        FlywayExecutor flywayExecutor = SystemRepository.get("dbMigration");
        flywayExecutor.migrate(true);
    }

    @Test
    public void RESTAPIでToDoの状態を更新できる() throws Exception {
        RestMockHttpRequest request = put("/api/todos/2002")
                .setHeader("Content-Type", MediaType.APPLICATION_JSON)
                .setBody(Map.of("completed", true));
        HttpResponse response = sendRequest(request);

        assertStatusCode("ToDoのステータス更新", HttpResponse.Status.CREATED, response);

        assertThat(response.getBodyString(), hasJsonPath("$.id", equalTo(2002)));
        assertThat(response.getBodyString(), hasJsonPath("$.text", equalTo("やること２")));
        assertThat(response.getBodyString(), hasJsonPath("$.completed", equalTo(true)));

        openApiValidator.validate("putTodo", request, response);
        
    }

    @Test
    public void RESTAPIでToDoの状態を更新できない() throws Exception {
        RestMockHttpRequest request = put("/api/todos/9002")
                .setHeader("Content-Type", MediaType.APPLICATION_JSON)
                .setBody(Map.of("completed", true));
        HttpResponse response = sendRequest(request);

        assertStatusCode("ToDoのステータス更新出来ない(該当無)", HttpResponse.Status.NOT_FOUND, response);

        openApiValidator.validate("putTodo", request, response);
    }

}
