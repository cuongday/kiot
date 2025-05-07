package com.qad.posbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.qad.posbe.domain.Shift;
import com.qad.posbe.domain.User;
import com.qad.posbe.util.constant.ShiftStatus;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long>, JpaSpecificationExecutor<Shift> {
    // Tìm ca làm việc đang mở của user
    Optional<Shift> findByUserAndStatus(User user, ShiftStatus status);
    
    // Tìm ca làm việc theo user
    List<Shift> findByUser(User user);
    
    // Tìm ca làm việc trong khoảng thời gian
    List<Shift> findByStartTimeBetween(Instant startTime, Instant endTime);
    
    // Kiểm tra có ca đang mở của user không
    boolean existsByUserAndStatus(User user, ShiftStatus status);
} 