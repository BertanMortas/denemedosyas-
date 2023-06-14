package com.hrms.mapper;

import com.hrms.dto.request.CreateCommentRequestDto;
import com.hrms.repository.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ICommentMapper {
    ICommentMapper NSTANCE = Mappers.getMapper(ICommentMapper.class);
    Comment toComment(final CreateCommentRequestDto dto);
}
