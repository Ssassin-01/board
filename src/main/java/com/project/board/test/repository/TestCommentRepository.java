package com.project.board.test.repository;

import com.project.board.entity.Post;
import com.project.board.test.entity.TestComment;
import com.project.board.test.entity.TestPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestCommentRepository extends JpaRepository<TestComment, Long> {
    List<TestComment> findByPost(TestPost post);
}
