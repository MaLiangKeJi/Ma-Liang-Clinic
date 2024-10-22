package com.clinic.cache.on.line;

public interface OnLineStateCache {
    void setOnLine(Long id, Boolean onLine);

    Boolean isOnLine(Long id);
}
