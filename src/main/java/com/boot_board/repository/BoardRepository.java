package com.boot_board.repository;


import com.boot_board.domain.Board;
import com.boot_board.repository.search.BoardSearch;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch
{
    @EntityGraph(attributePaths = "imageSet")
    @Query("select b from Board b where b.bno = :bno")
    Optional<Board> findByIdWithImages(Long bno);

}
