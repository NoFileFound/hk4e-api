package org.httpsrv;

import org.httpsrv.data.Retcode;
import java.util.LinkedHashMap;

public interface ResponseHandler {
    LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data);
}