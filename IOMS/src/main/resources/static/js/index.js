/**
 * Xử lý logic phía client cho trang index.html
 */
async function testConnection() {
  const resultDiv = document.getElementById("result");
  resultDiv.style.color = "#007bff";
  resultDiv.textContent = "Đang kết nối tới server...";

  try {
    // Gọi API test kết nối tới Backend Spring Boot
    const response = await fetch("/api/test"); // Hoặc endpoint tương ứng
    if (response.ok) {
      const data = await response.text();
      resultDiv.style.color = "#28a745";
      resultDiv.textContent = "Kết nối thành công: " + data;
    } else {
      resultDiv.style.color = "#dc3545";
      resultDiv.textContent =
        "Phản hồi từ server không thành công (Status: " + response.status + ")";
    }
  } catch (error) {
    resultDiv.style.color = "#dc3545";
    // Thử gọi qua hàm chung từ api.js nếu có sẵn hoặc hiển thị lỗi
    resultDiv.textContent = "Lỗi kết nối tới Backend: " + error.message;
  }
}
