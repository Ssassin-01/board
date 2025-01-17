package com.project.board.test.repository;

import com.project.board.test.entity.TestMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TestMemberRepository extends JpaRepository<TestMember, Long> {
    Optional<TestMember> findByUsername(String username);
    Optional<TestMember> findByEmail(String email);
}
