const token = localStorage.getItem("token");

if (!token) {
    alert("Session expired. Please login again.");
    window.location.href = "../login.html";
}


function addCourse() {
    fetch("http://localhost:8080/api/admin/course", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        },
        body: JSON.stringify({
            title: document.getElementById("title").value,
            description: document.getElementById("desc").value
        })
    })
    .then(res => res.text())
    .then(alert);
}
function addTeacher() {
    fetch("http://localhost:8080/api/admin/teacher", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        },
        body: JSON.stringify({
            email: document.getElementById("email").value,
            password: document.getElementById("password").value
        })
    })
    .then(res => res.text())
    .then(alert);
}
function assign() {
    fetch("http://localhost:8080/api/admin/assign", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        },
        body: JSON.stringify({
            teacherEmail: document.getElementById("teacherEmail").value,
            courseId: document.getElementById("courseId").value
        })
    })
    .then(res => res.text())
    .then(alert);
}
fetch("http://localhost:8080/api/admin/users", {
    headers: { "Authorization": "Bearer " + token }
})
.then(res => res.json())
.then(data => {
    let ul = document.getElementById("users");
    data.forEach(u => {
        let li = document.createElement("li");
        li.textContent = u.email + " (" + u.role + ")";
        ul.appendChild(li);
    });
});
