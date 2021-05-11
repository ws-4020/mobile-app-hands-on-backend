package com.example.todo.api;

import com.example.openapi.OpenApiValidator;
import com.example.system.nablarch.FlywayExecutor;
import nablarch.core.repository.SystemRepository;
import nablarch.fw.web.HttpResponse;
import nablarch.fw.web.RestMockHttpRequest;
import nablarch.test.core.http.SimpleRestTestSupport;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.nio.file.Paths;
import java.util.Map;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import java.util.Collections;

public class TodoRegisterRestApiTest extends SimpleRestTestSupport {

    public static OpenApiValidator openApiValidator = new OpenApiValidator(Paths.get("rest-api-specification/openapi.yaml"));

    @BeforeClass
    public static void setUpClass() {
        FlywayExecutor flywayExecutor = SystemRepository.get("dbMigration");
        flywayExecutor.migrate(true);
    }

    @Test
    public void RESTAPIでToDoを登録できる() throws Exception {
        RestMockHttpRequest request = post("/api/todos")
                .setHeader("Content-Type", MediaType.APPLICATION_JSON)
                .setBody(Map.of("text", "テストする"));
        HttpResponse response = sendRequest(request);

        assertStatusCode("ToDoの登録", HttpResponse.Status.OK, response);

        assertThat(response.getBodyString(), hasJsonPath("$.id", Matchers.notNullValue()));
        assertThat(response.getBodyString(), hasJsonPath("$.text", equalTo("テストする")));
        assertThat(response.getBodyString(), hasJsonPath("$.completed", equalTo(false)));

        openApiValidator.validate("postTodo", request, response);
    }

    @Test
    public void ToDo登録時にtext項目が無い場合_登録に失敗して400になる() {
        RestMockHttpRequest request = post("/api/todos")
                .setHeader("Content-Type", MediaType.APPLICATION_JSON)
                .setBody(Collections.emptyMap());
        HttpResponse response = sendRequest(request);

        assertStatusCode("ToDoの登録", HttpResponse.Status.BAD_REQUEST, response);
    }

}