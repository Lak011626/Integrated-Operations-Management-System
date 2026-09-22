function testConnection() {
  const resultDiv = document.getElementById("api-result");
  resultDiv.innerHTML = "⏳ Đang gọi Backend...";
  resultDiv.style.color = "orange";

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

async function login() {
  const username = document.getElementById("username")?.value?.trim();
  const password = document.getElementById("password")?.value?.trim();
  const messageBox = document.getElementById("login-message");

  if (!username || !password) {
    messageBox.textContent = "Vui lòng nhập username và password.";
    messageBox.className = "alert error";
    return;
  }

  messageBox.textContent = "Đang đăng nhập...";
  messageBox.className = "alert info";

  try {
    const response = await fetch("/api/auth/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ username, password }),
    });

    const data = await response.json();

    if (!response.ok) {
      const msg = data?.message || "Đăng nhập thất bại";
      throw new Error(msg);
    }

    const token = data?.data?.token;
    const user = data?.data;

    if (!token) {
      throw new Error("Server không trả về token");
    }

    localStorage.setItem("ioms_token", token);
    localStorage.setItem("ioms_username", user?.username || username);
    localStorage.setItem("ioms_user", JSON.stringify(user));

    messageBox.textContent = "Đăng nhập thành công! Đang chuyển hướng...";
    messageBox.className = "alert success";

    setTimeout(() => {
      window.location.href = routeAfterLogin();
    }, 700);
  } catch (error) {
    messageBox.textContent = error.message || "Đăng nhập thất bại";
    messageBox.className = "alert error";
  }
}

function logout() {
  localStorage.removeItem("ioms_token");
  localStorage.removeItem("ioms_username");
  localStorage.removeItem("ioms_user");
  window.location.href = "/";
}

function getToken() {
  return localStorage.getItem("ioms_token");
}

function isLoggedIn() {
  return !!getToken();
}

function getCurrentUser() {
  try {
    const raw = localStorage.getItem("ioms_user");
    return raw ? JSON.parse(raw) : null;
  } catch {
    return null;
  }
}

function normalizeRoleValue(role) {
  const raw = String(role || "")
    .trim()
    .toUpperCase();
  const withoutPrefix = raw.replace(/^ROLE_/, "");

  const aliasMap = {
    ADMINISTRATOR: "ADMIN",
    ADMIN: "ADMIN",
    MANAGER: "MANAGER",
    STAFF: "STAFF",
    EMPLOYEE: "STAFF",
    USER: "STAFF",
  };

  return aliasMap[withoutPrefix] || withoutPrefix;
}

function normalizeRoles(roles) {
  if (!Array.isArray(roles)) return [];
  return roles.filter(Boolean).map((role) => normalizeRoleValue(role));
}

function hasAnyRole(...roles) {
  const currentRoles = normalizeRoles(getCurrentUser()?.roles || []);
  const allowedRoles = roles.map((role) => normalizeRoleValue(role));

  return currentRoles.some((role) => allowedRoles.includes(role));
}

function routeAfterLogin() {
  if (hasAnyRole("ADMIN", "MANAGER")) {
    return "/dashboard.html";
  }
  return "/staff-dashboard.html";
}

function checkAuthAndRedirect() {
  const token = getToken();
  const user = getCurrentUser();

  if (!token || !user) {
    window.location.href = "/index.html";
    return false;
  }

  const isManagerOrAdmin = hasAnyRole("ADMIN", "MANAGER");
  const path = window.location.pathname;

  if (path.endsWith("/dashboard.html") && !isManagerOrAdmin) {
    window.location.href = "/staff-dashboard.html";
    return false;
  }

  if (path.endsWith("/staff-dashboard.html") && isManagerOrAdmin) {
    window.location.href = "/dashboard.html";
    return false;
  }

  return true;
}
