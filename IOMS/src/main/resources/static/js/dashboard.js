/* ==========================================================
   dashboard.js
   Logic cho trang Tổng quan (Dashboard) của IOMS.
   Yêu cầu: /js/api.js đã được nạp trước file này để có
   getCurrentUser(), checkAuthAndRedirect(), logout().
   ========================================================== */
document.addEventListener("DOMContentLoaded", () => {
  const sidebar = document.getElementById("sidebar");
  const overlay = document.getElementById("sidebarOverlay");
  const toggleBtn = document.getElementById("toggleSidebarBtn");
  const isMobile = () => window.innerWidth <= 900;

  function toggleSidebar() {
    if (isMobile()) {
      sidebar.classList.toggle("mobile-open");
      overlay.classList.toggle("visible");
    } else {
      sidebar.classList.toggle("collapsed");
      document.querySelector(".main-wrapper").classList.toggle("expanded");
    }
    const expanded =
      sidebar.classList.contains("mobile-open") ||
      !sidebar.classList.contains("collapsed");
    toggleBtn.setAttribute("aria-expanded", String(expanded));
  }
  toggleBtn.addEventListener("click", toggleSidebar);
  overlay.addEventListener("click", () => {
    sidebar.classList.remove("mobile-open");
    overlay.classList.remove("visible");
  });

  function setupDropdown(toggleEl, panelEl) {
    function close() {
      panelEl.classList.remove("open");
      toggleEl.setAttribute("aria-expanded", "false");
    }
    function open() {
      document
        .querySelectorAll(".dropdown-panel.open")
        .forEach((p) => p.classList.remove("open"));
      panelEl.classList.add("open");
      toggleEl.setAttribute("aria-expanded", "true");
    }
    toggleEl.addEventListener("click", (e) => {
      e.stopPropagation();
      panelEl.classList.contains("open") ? close() : open();
    });
    toggleEl.addEventListener("keydown", (e) => {
      if (e.key === "Enter" || e.key === " ") {
        e.preventDefault();
        toggleEl.click();
      }
      if (e.key === "Escape") close();
    });
    return { close };
  }
  const notifDropdown = setupDropdown(
    document.getElementById("notifToggle"),
    document.getElementById("notifPanel"),
  );
  const userDropdown = setupDropdown(
    document.getElementById("userMenuToggle"),
    document.getElementById("userPanel"),
  );
  document.addEventListener("click", () => {
    notifDropdown.close();
    userDropdown.close();
  });

  document.querySelectorAll("[data-stat]").forEach((el) => {
    const value = parseInt(el.textContent.replace(/\D/g, ""), 10);
    if (!isNaN(value)) el.textContent = value.toLocaleString("vi-VN");
  });

  const searchInput = document.getElementById("activitySearch");
  const statusFilter = document.getElementById("statusFilter");
  const tableBody = document.getElementById("activityTableBody");
  const allRows = Array.from(tableBody.querySelectorAll("tr"));

  function applyFilters() {
    const term = searchInput.value.trim().toLowerCase();
    const status = statusFilter.value;
    let visibleCount = 0;

    allRows.forEach((row) => {
      const text = row.textContent.toLowerCase();
      const matchesText = text.includes(term);
      const matchesStatus = !status || row.dataset.status === status;
      const show = matchesText && matchesStatus;
      row.style.display = show ? "" : "none";
      if (show) visibleCount++;
    });

    let emptyRow = tableBody.querySelector(".table-empty-row");
    if (visibleCount === 0) {
      if (!emptyRow) {
        emptyRow = document.createElement("tr");
        emptyRow.className = "table-empty-row";
        emptyRow.innerHTML = `<td colspan="5">Không tìm thấy hoạt động phù hợp.</td>`;
        tableBody.appendChild(emptyRow);
      }
    } else if (emptyRow) {
      emptyRow.remove();
    }
  }
  searchInput.addEventListener("input", applyFilters);
  statusFilter.addEventListener("change", applyFilters);

  const chartCanvas = document.getElementById("revenueChart");
  if (chartCanvas && window.Chart) {
    new Chart(chartCanvas, {
      type: "line",
      data: {
        labels: ["T2", "T3", "T4", "T5", "T6", "T7", "CN"],
        datasets: [
          {
            label: "Doanh thu (triệu đ)",
            data: [120, 145, 132, 160, 158, 190, 176],
            borderColor: "#2563eb",
            backgroundColor: "rgba(37,99,235,0.1)",
            tension: 0.35,
            fill: true,
          },
          {
            label: "Đơn hàng",
            data: [40, 52, 45, 60, 58, 70, 62],
            borderColor: "#f97316",
            backgroundColor: "rgba(249,115,22,0.1)",
            tension: 0.35,
            fill: true,
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: { legend: { position: "bottom" } },
        scales: { y: { beginAtZero: true } },
      },
    });
    chartCanvas.parentElement.style.height = "300px";
  }

  document.getElementById("footerYear").textContent = new Date().getFullYear();

  try {
    const user = typeof getCurrentUser === "function" ? getCurrentUser() : null;
    if (!user) {
      if (typeof checkAuthAndRedirect === "function") {
        checkAuthAndRedirect();
      } else {
        window.location.href = "/login.html";
      }
      return;
    }

    const roleList = Array.isArray(user.roles) ? user.roles : [];
    const normalizedRoles = roleList.map((role) =>
      String(role).trim().toUpperCase(),
    );
    const isManagerOrAdmin = normalizedRoles.some(
      (role) =>
        role === "ADMIN" ||
        role === "MANAGER" ||
        role.includes("ADMIN") ||
        role.includes("MANAGER"),
    );

    if (!isManagerOrAdmin) {
      window.location.href = "/staff-dashboard.html";
      return;
    }

    const joinDate = user.createdAt ? new Date(user.createdAt) : null;
    document.getElementById("user-badge").textContent = "Manager / Admin";
    document.getElementById("user-name").textContent = user.username || "-";
    document.getElementById("header-username").textContent =
      user.username || "User";
    document.getElementById("user-email").textContent = user.email || "-";
    document.getElementById("user-fullname").textContent = user.fullName || "-";
    document.getElementById("user-roles").textContent =
      roleList.join(", ") || "-";
    document.getElementById("manager-name").textContent =
      user.fullName || user.username || "-";
    document.getElementById("manager-job-title").textContent =
      roleList.join(", ") || "-";
    document.getElementById("manager-join-date").textContent = joinDate
      ? joinDate.toLocaleDateString("vi-VN")
      : "Chưa cập nhật";
    document.getElementById("manager-leave-days").textContent = "2 ngày";
    document.getElementById("manager-pending-tasks").textContent =
      "6 công việc";
    document.getElementById("manager-todo-tasks").textContent = "11 công việc";
    document.getElementById("manager-latest-report").textContent =
      "Báo cáo doanh thu tuần này đã được tổng hợp";
  } catch (err) {
    console.error("Lỗi khi tải thông tin người dùng:", err);
    window.location.href = "/login.html";
  }

  document.getElementById("logoutLink").addEventListener("click", (e) => {
    e.preventDefault();
    if (confirm("Bạn có chắc muốn đăng xuất?")) {
      if (typeof logout === "function") logout();
      else window.location.href = "/login.html";
    }
  });
});
