package org.httpsrv;

import org.httpsrv.data.Retcode;
import java.util.LinkedHashMap;

public interface ResponseHandler {
    default Boolean isDebug() {
        return true;
    }

    LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data);
}