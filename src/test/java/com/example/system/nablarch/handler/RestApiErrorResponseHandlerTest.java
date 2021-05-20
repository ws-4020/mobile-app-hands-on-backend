package com.example.system.nablarch.handler;

import nablarch.common.dao.NoDataException;
import nablarch.fw.ExecutionContext;
import nablarch.fw.Handler;
import nablarch.fw.web.HttpErrorResponse;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class RestApiErrorResponseHandlerTest {

    @Test
    public void notFoundResponse() throws Exception {
        RestApiErrorResponseHandler sut = new RestApiErrorResponseHandler();
        ExecutionContext context = new ExecutionContext();

        context.addHandler(new Handler<Object, Object>() {
            @Override
            public Object handle(Object o, ExecutionContext executionContext) {
                throw new NoDataException();
            }
        });
        try {
            sut.handle(null, context);
            fail("missing throw HttpErrorResponse.");
        } catch (HttpErrorResponse result) {
            assertThat(result.getResponse().getStatusCode(), CoreMatchers.is(404));
        }
    }

}