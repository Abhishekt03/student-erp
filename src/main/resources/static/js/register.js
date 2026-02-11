document.addEventListener("DOMContentLoaded", () => {
    const btn = document.getElementById("registerBtn");
    if (btn) {
        btn.addEventListener("click", register);
    }
});

function register() {

    const data = {
        name: document.getElementById("name").value,
        email: document.getElementById("email").value,
        password: document.getElementById("password").value,
        role: document.getElementById("role").value,
        dob: document.getElementById("dob").value,
        phone: document.getElementById("phone").value,
        address: document.getElementById("address").value
    };

    if (!data.email || !data.password || !data.role) {
        alert("Please fill required fields");
        return;
    }

    // ✅ IMPORTANT: NO localhost (works on Render)
    fetch("/api/auth/register", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    })
    .then(res => {
        if (!res.ok) {
            return res.text().then(err => { throw err; });
        }
        return res.text();
    })
    .then(msg => {
        alert(msg);
        window.location.href = "/login.html";
    })
    .catch(err => {
        console.error(err);
        alert("Registration failed");
    });
}
