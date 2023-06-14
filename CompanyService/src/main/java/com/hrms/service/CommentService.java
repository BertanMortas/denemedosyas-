package com.hrms.service;

import com.hrms.dto.request.ApproveCommentsRequestDto;
import com.hrms.dto.request.CreateCommentRequestDto;
import com.hrms.dto.request.FindByIdCommentRequestDto;
import com.hrms.dto.response.GetNameByIdResponseDto;
import com.hrms.dto.response.ShowCommentsResponseDto;
import com.hrms.exception.CompanyManagerException;
import com.hrms.exception.ErrorType;
import com.hrms.manager.IUserprofileManager;
import com.hrms.mapper.ICommentMapper;
import com.hrms.repository.ICommentRepository;
import com.hrms.repository.entity.Comment;
import com.hrms.repository.view.VWgetComment;
import com.hrms.utility.JwtTokenProvider;
import com.hrms.utility.ServiceManager;
import com.uttesh.exude.ExudeData;
import com.uttesh.exude.exception.InvalidDataException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService extends ServiceManager<Comment,Long> {
    private final ICommentRepository commentRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final IUserprofileManager userprofileManager;
    public CommentService(ICommentRepository commentRepository, JwtTokenProvider jwtTokenProvider, IUserprofileManager userprofileManager) {
        super(commentRepository);
        this.commentRepository = commentRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userprofileManager = userprofileManager;
    }
    public Comment createComment(CreateCommentRequestDto dto){
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(dto.getToken());
        if (authId.isEmpty()) {
            throw new CompanyManagerException(ErrorType.INVALID_TOKEN);
        }
        List<String> roles = jwtTokenProvider.getRoleFromToken(dto.getToken());
        if (!roles.stream().anyMatch(x-> x.equals("EMPLOYEE"))) {
            throw new CompanyManagerException(ErrorType.AUTHORIZATION_ERROR);
        }
        try {
            String newComment = ExudeData.getInstance().getSwearWords(dto.getComment());
            if (!newComment.equals("")) {
                throw new CompanyManagerException(ErrorType.BAD_WORD);
            }
        } catch (InvalidDataException e) {
            throw new RuntimeException(e.getMessage());// todo hata yönetimi yapılacak
        }
        return save(ICommentMapper.NSTANCE.toComment(dto));
    }
    public Boolean approveComments(ApproveCommentsRequestDto dto){
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(dto.getToken());
        if (authId.isEmpty()) {
            throw new CompanyManagerException(ErrorType.INVALID_TOKEN);
        }
        List<String> roles = jwtTokenProvider.getRoleFromToken(dto.getToken());
        if (!roles.stream().anyMatch(x-> x.equals("ADMIN"))) {
            throw new CompanyManagerException(ErrorType.AUTHORIZATION_ERROR);
        }
        Optional<Comment> comment = findById(dto.getCommentId());
        comment.get().setIsApproved(true);
        update(comment.get());
        return true;
    }
    public List<ShowCommentsResponseDto> showCommentsByCompanyId(Long commentId) {
        List<VWgetComment> commentList = commentRepository.findAllCommentsByCompanyId(commentId);
        List<ShowCommentsResponseDto> responseList = new ArrayList<>();
        List<GetNameByIdResponseDto> nameList = new ArrayList<>();
        for (VWgetComment comment : commentList) {
            GetNameByIdResponseDto userProfileNames = userprofileManager.findById(comment.getUserId()).getBody();
            nameList.add(userProfileNames);
        }
        for (int i = 0; i < nameList.size(); i++) {
            GetNameByIdResponseDto userProfile = nameList.get(i);
            VWgetComment comment = commentList.get(i);
            responseList.add(ShowCommentsResponseDto.builder()
                    .name(userProfile.getName())
                    .surname(userProfile.getSurname())
                    .comment(comment.getComment())
                    .build());
        }
        return responseList;
    }
    public List<Comment> showCommentsToBeApproved(String token){
        // todo bu method front end isteğine göre geri dönüş tipi değişecek
        // todo managerda id ile personelin adı ve soyadı getiren method hazır
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        if (authId.isEmpty()) {
            throw new CompanyManagerException(ErrorType.INVALID_TOKEN);
        }
        List<String> roles = jwtTokenProvider.getRoleFromToken(token);
        if (!roles.stream().anyMatch(x-> x.equals("ADMIN"))) {
            throw new CompanyManagerException(ErrorType.AUTHORIZATION_ERROR);
        }
        return commentRepository.findAllByIsApproved(false);
    }

    public Boolean deleteComment(FindByIdCommentRequestDto dto){
        List<String> roles = jwtTokenProvider.getRoleFromToken(dto.getToken());
        if (!roles.contains("ADMIN"))
            throw new CompanyManagerException(ErrorType.AUTHORIZATION_ERROR);
        Optional<Comment> optionalComment = findById(dto.getCommentId());
        if(optionalComment.isEmpty())
            throw new CompanyManagerException(ErrorType.COMMENT_NOT_FOUND);
        deleteById(optionalComment.get().getId());
        return true;
    }
    //
}
