package com.hrms.repository;


import com.hrms.repository.entity.Comment;
import com.hrms.repository.view.VWgetComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICommentRepository extends JpaRepository<Comment,Long> {
    @Query("select new com.hrms.repository.view.VWgetComment(c.comment, c.userprofileId) from Comment as c where c.isApproved=true and c.companyId=?1")
    List<VWgetComment> findAllCommentsByCompanyId(Long id);
    List<Comment> findAllByIsApproved(Boolean approveStatus);
}
