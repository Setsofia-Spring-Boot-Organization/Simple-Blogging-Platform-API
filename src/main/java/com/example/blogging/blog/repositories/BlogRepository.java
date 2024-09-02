package com.example.blogging.blog.repositories;

import com.example.blogging.blog.entities.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {

    @Query("SELECT p FROM Blog p WHERE p.category LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR p.content LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR p.tittle LIKE LOWER(CONCAT('%', :term, '%'))")
    List<Blog> findBlogs(@Param("term") String term);
}