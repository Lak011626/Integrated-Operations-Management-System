function testConnection() {
  const resultDiv = document.getElementById("api-result");
  resultDiv.innerHTML = "⏳ Đang gọi Backend...";
  resultDiv.style.color = "orange";

  // Gọi vào endpoint /api/ping của TestController (mà ta đã thống nhất đổi tên thành HealthCheck API)
  fetch("/api/ping")
    .then((response) => {
      if (!response.ok) throw new Error("Lỗi mạng hoặc server");
      return response.json();
    })
    .then((data) => {
      resultDiv.innerHTML = "✅ Thành công: " + data.message;
      resultDiv.style.color = "green";
    })
    .catch((error) => {
      resultDiv.innerHTML = "❌ Lỗi: " + error.message;
      resultDiv.style.color = "red";
    });
}
