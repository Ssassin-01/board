package com.project.board.test.repository;

import com.project.board.test.entity.TestMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberTestRepository extends JpaRepository<TestMember, Long> {
    Optional<TestMember> findByUsername(String username);
    Optional<TestMember> findByEmail(String email);
}
