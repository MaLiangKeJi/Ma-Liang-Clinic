package com.clinic.converter;

import com.clinic.controller.FeedbackController;
import com.clinic.entity.Feedback;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FeedbackConverter {
    Feedback toEntity(FeedbackController.AddFeedbackParam param);
}