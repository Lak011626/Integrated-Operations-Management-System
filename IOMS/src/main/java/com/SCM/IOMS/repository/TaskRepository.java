package com.SCM.IOMS.repository;

import com.SCM.IOMS.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    // 1. Phục vụ Dashboard Nhân viên: Lấy tất cả việc mình được giao
    List<Task> findByAssigneeId(Long assigneeId);

    // Lấy việc của mình theo trạng thái (VD: Lấy việc đang làm IN_PROGRESS của user 1)
    List<Task> findByAssigneeIdAndStatus(Long assigneeId, Task.TaskStatus status);

    // 2. Phục vụ Dashboard Quản lý: Lấy tất cả việc quản lý đã giao cho người khác
    List<Task> findByCreatorId(Long creatorId);

    // 3. Chức năng Lọc (Filter) trên Web Quản trị
    List<Task> findByStatus(Task.TaskStatus status);
    List<Task> findByPriority(Task.TaskPriority priority);

    // 4. Tìm kiếm công việc theo tên (Gõ từ khóa vào thanh search)
    List<Task> findByTitleContainingIgnoreCase(String title);
}