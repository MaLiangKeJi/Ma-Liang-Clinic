package com.clinic.cache.usage;

import com.clinic.entity.Usage;

import java.util.List;

public interface UsageCache {

    List<Usage> search(String name);
}
