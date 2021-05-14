package com.example.system.nablarch.handler;

import nablarch.common.dao.NoDataException;
import nablarch.fw.ExecutionContext;
import nablarch.fw.Handler;
import nablarch.fw.web.HttpErrorResponse;
import nablarch.fw.web.HttpResponse;
import nablarch.fw.web.HttpResponse.Status;

/**
 * 例外をステータスコードに変換するクラス。
 *
 * <a href="https://github.com/Fintan-contents/example-chat/blob/master/backend/src/main/java/com/example/system/nablarch/handler/RestApiErrorResponseHandler.java">SPA + REST API構成のチャットサービス コード例</a>を参考にミニマムな実装を提供する。
 *
 */
public class RestApiErrorResponseHandler implements Handler<Object, Object> {

    @Override
    public Object handle(Object data, ExecutionContext context) {
        try {
            return context.handleNext(data);
        } catch (NoDataException e) {
            throw new HttpErrorResponse(Status.NOT_FOUND.getStatusCode());
        }
    }
}
