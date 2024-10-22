package com.clinic.service.impl;

import com.clinic.entity.Feedback;
import com.clinic.service.FeedbackService;
import com.clinic.mapper.FeedbackMapper;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author Mafty
 * @description 针对表【feedback(用户意见与反馈)】的数据库操作Service实现
 * @createDate 2024-09-18 17:19:41
 */
@Service
public class FeedbackServiceImpl extends MPJBaseServiceImpl<FeedbackMapper, Feedback>
        implements FeedbackService {
}