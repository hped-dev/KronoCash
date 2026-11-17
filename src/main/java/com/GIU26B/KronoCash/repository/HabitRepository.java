package com.GIU26B.KronoCash.repository;

import com.GIU26B.KronoCash.model.Habit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HabitRepository extends JpaRepository<Habit, Long> {

    List<Habit> findByUserIdOrderByTitleAsc(Long userId);
}
