package com.hrms.controller;
import com.hrms.dto.request.ApproveCommentsRequestDto;
import com.hrms.dto.request.CreateCommentRequestDto;
import com.hrms.dto.request.FindByIdCommentRequestDto;
import com.hrms.dto.response.ShowCommentsResponseDto;
import com.hrms.repository.entity.Comment;
import com.hrms.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.hrms.constant.ApiUrls.*;

@RestController
@RequestMapping(COMMENT)
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    @PostMapping(CREATE)
    public ResponseEntity<Comment> createComment(@RequestBody CreateCommentRequestDto dto){
        return ResponseEntity.ok(commentService.createComment(dto));
    }
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(APPROVE_COMMENTS)
    public ResponseEntity<Boolean> approveComments(@RequestBody ApproveCommentsRequestDto dto){
        return ResponseEntity.ok(commentService.approveComments(dto));
    }
    @GetMapping("/show-comments-by-company-id"+"/{companyId}")
    public ResponseEntity<List<ShowCommentsResponseDto>> showCommentsByCompanyId(@PathVariable Long companyId){
        return ResponseEntity.ok(commentService.showCommentsByCompanyId(companyId));
    }
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/not-approved-comments"+"/{token}")
    public ResponseEntity<List<Comment>> showCommentsToBeApproved(@PathVariable String token){
        return ResponseEntity.ok(commentService.showCommentsToBeApproved(token));
    }
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @DeleteMapping("/delete-comment")
    public ResponseEntity<Boolean> deleteComment(@RequestBody FindByIdCommentRequestDto dto){
        return ResponseEntity.ok(commentService.deleteComment(dto));
    }
}
