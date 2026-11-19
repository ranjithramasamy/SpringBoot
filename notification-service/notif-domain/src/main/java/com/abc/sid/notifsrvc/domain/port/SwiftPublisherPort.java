package com.abc.sid.notifsrvc.domain.port;

import java.util.Map;

public interface SwiftPublisherPort {
    void publish(String partitionKey, byte[] xmlPayload, Map<String, String> headers);
}
