package com.library.management.library_management.repository;

import com.library.management.library_management.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

//responsible for database access
public interface MemberRepository extends JpaRepository<Member, Integer> {

}
