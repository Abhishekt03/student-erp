document.addEventListener("DOMContentLoaded", () => {
    const btn = document.getElementById("registerBtn");
    const agree = document.getElementById("agree");

    if (btn && agree) {
        btn.disabled = true;

        agree.addEventListener("change", () => {
            btn.disabled = !agree.checked;
        });

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

    fetch("/api/auth/register", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    })
    .then(res => {
        if (!res.ok) throw new Error("Registration failed");
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
