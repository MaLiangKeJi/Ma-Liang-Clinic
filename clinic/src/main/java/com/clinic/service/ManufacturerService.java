package com.clinic.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clinic.dto.Manufacturer;

public interface ManufacturerService {

    Page<Manufacturer> search(Manufacturer param);
}
