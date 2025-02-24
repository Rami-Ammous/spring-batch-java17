package com.ammous.springbatch.student;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Rami AMMOUS
 */

public interface StudentRepository extends JpaRepository<Student, Integer> {
}
