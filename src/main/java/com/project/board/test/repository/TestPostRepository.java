package com.project.board.test.repository;

import com.project.board.test.entity.TestPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestPostRepository extends JpaRepository<TestPost, Long> {

}
