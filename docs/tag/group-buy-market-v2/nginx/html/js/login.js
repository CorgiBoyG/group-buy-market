document.addEventListener("DOMContentLoaded", () => {
  const loginForm = document.getElementById("loginForm")
  const errorMessage = document.getElementById("errorMessage")

  loginForm.addEventListener("submit", (e) => {
    e.preventDefault()

    const username = document.getElementById("username").value
    const password = document.getElementById("password").value

    // 登录成功
    errorMessage.style.display = "none"

    // 设置Cookie (有效期为1天)
    const expirationDate = new Date()
    expirationDate.setDate(expirationDate.getDate() + 1)
    document.cookie = `username=${username}; expires=${expirationDate.toUTCString()}; path=/`

    // 跳转到首页
    window.location.href = "index.html"
  })
})

