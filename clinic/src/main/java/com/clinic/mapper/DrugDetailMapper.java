package com.clinic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clinic.entity.DrugDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Entity com.clinic.entity.DrugBatchDetail
 */
@Mapper
@Repository
public interface DrugDetailMapper extends BaseMapper<DrugDetail> {


}




