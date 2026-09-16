package com.SCM.IOMS.repository;

import com.SCM.IOMS.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 1. Dành cho Đăng nhập / Xác thực (Security)
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);

    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);

    // 2. Bám sát Database: Quản lý số điện thoại
    Boolean existsByPhoneNumber(String phoneNumber);

    // 3. Bám sát Database: Tìm kiếm nhân viên trên thanh công cụ của Admin
    // (ContainingIgnoreCase: Tìm từ khóa có chứa trong tên, không phân biệt hoa thường)
    List<User> findByFullNameContainingIgnoreCase(String fullName);

    // 4. Bám sát Database: Lọc trạng thái nhân viên
    // (VD: Truyền true để lấy nhân viên đang làm, false để lấy người đã nghỉ việc)
    List<User> findByIsActive(Boolean isActive);
}