package com.library.librarymanagement.repository;

import com.library.librarymanagement.entity.Borrowing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {

    boolean existsByBookIdAndMemberIdAndStatus(
            Long bookId,
            Long memberId,
            String status
    );
}