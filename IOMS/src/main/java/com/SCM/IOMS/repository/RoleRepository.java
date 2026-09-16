package com.SCM.IOMS.repository;

import com.SCM.IOMS.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    
    // Tìm vai trò theo mã chuẩn (VD: truyền vào "ADMIN" hoặc "EMPLOYEE")
    Optional<Role> findByRoleCode(String roleCode);

    // Kiểm tra xem mã role đã tồn tại chưa (Dùng khi Admin muốn tạo thêm Role mới)
    Boolean existsByRoleCode(String roleCode);
}