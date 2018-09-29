package org.lab409.util;

import org.lab409.entity.ResponseMessage;

public interface MakeResponse {
    ResponseMessage success();
    ResponseMessage error(int code, String message);
}
