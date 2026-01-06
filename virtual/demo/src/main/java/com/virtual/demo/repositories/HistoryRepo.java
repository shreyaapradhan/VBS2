package com.virtual.demo.repositories;

import com.virtual.demo.models.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@Repository
public interface HistoryRepo extends JpaRepository<History,Integer> {
}
