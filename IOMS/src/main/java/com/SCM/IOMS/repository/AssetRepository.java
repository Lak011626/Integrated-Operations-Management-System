package com.SCM.IOMS.repository;

import com.SCM.IOMS.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

    // 1. Dùng khi thêm/sửa tài sản để đảm bảo Mã tài sản (VD: LT-001) không bị trùng
    Optional<Asset> findByAssetCode(String assetCode);
    Boolean existsByAssetCode(String assetCode);

    // 2. Lọc tài sản theo Trạng thái 
    // (RẤT QUAN TRỌNG: VD Admin muốn xem các tài sản đang 'AVAILABLE' để cấp cho nhân viên mới)
    List<Asset> findByStatus(Asset.AssetStatus status);

    // 3. Lọc tài sản theo Danh mục (VD: Chọn dropdown xem toàn bộ "Máy tính" hoặc "Bàn ghế")
    List<Asset> findByCategory(String category);

    // 4. Tìm kiếm tài sản theo Tên tài sản (Tìm gần đúng, không phân biệt hoa/thường)
    List<Asset> findByAssetNameContainingIgnoreCase(String assetName);

    // 5. Xem danh sách tài sản đang được một nhân viên cụ thể giữ (VD: Khi nhân viên nghỉ việc cần thu hồi)
    List<Asset> findByAssigneeId(Long assigneeId);
}