// ===============================
// AUTH CHECK
// ===============================
const token = localStorage.getItem("token");

if (!token) {
    alert("Session expired. Please login again.");
    window.location.href = "/login.html";
}

// ===============================
// ADD COURSE
// ===============================
function addCourse() {
    const title = document.getElementById("courseTitle").value;
    const description = document.getElementById("courseDesc").value;

    if (!title || !description) {
        alert("Please fill all fields");
        return;
    }

    fetch("/api/teacher/courses", {
        method: "POST",
        headers: {
            "Authorization": "Bearer " + token,
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ title, description })
    })
    .then(res => {
        if (!res.ok) {
            return res.text().then(t => { throw new Error(t); });
        }
        return res.text();   // ✅ NOT json
    })
    .then(msg => {
        alert(msg || "Course added successfully");
        loadCourses();
        loadCourseList();
    })
    .catch(err => {
        console.error(err);
        alert(err.message);
    });
}

// ===============================
// LOAD COURSE LIST (UL)
// ===============================
function loadCourseList() {
    fetch("/api/teacher/courses", {
        headers: {
            "Authorization": "Bearer " + token
        }
    })
    .then(res => {
        if (res.status === 204) return [];
        if (!res.ok) throw new Error("403 Forbidden – Teacher access required");
        return res.json();
    })
    .then(data => {
        const ul = document.getElementById("courseList");
        if (!ul) return;

        ul.innerHTML = "";

        if (!data || data.length === 0) {
            ul.innerHTML = "<li>No courses found</li>";
            return;
        }

        data.forEach(c => {
            const li = document.createElement("li");
            li.textContent = c.title;
            ul.appendChild(li);
        });
    })
    .catch(err => {
        console.error(err);
        alert(err.message);
    });
}

// ===============================
// LOAD COURSES (DROPDOWN)
// ===============================
function loadCourses() {
    const courseSelect = document.getElementById("courseSelect");
    if (!courseSelect) return;

    fetch("/api/teacher/courses", {
        headers: {
            "Authorization": "Bearer " + token
        }
    })
    .then(res => {
        if (res.status === 204) return [];
        if (!res.ok) throw new Error("Unauthorized");
        return res.json();
    })
    .then(data => {
        courseSelect.innerHTML = `<option value="">-- Select Course --</option>`;
        data.forEach(c => {
            courseSelect.innerHTML += `<option value="${c.id}">${c.title}</option>`;
        });
    })
    .catch(err => console.error(err));
}

// ===============================
// LOAD STUDENTS
// ===============================
function loadStudents() {
    const courseId = document.getElementById("courseSelect").value;
    if (!courseId) return;

    fetch(`/api/teacher/students?courseId=${courseId}`, {
        headers: { "Authorization": "Bearer " + token }
    })
    .then(res => {
        if (!res.ok) throw new Error("Failed to load students");
        return res.json();
    })
    .then(students => {
        const tbody = document.getElementById("studentTable");
        tbody.innerHTML = "";

        if (!students || students.length === 0) {
            tbody.innerHTML = `<tr><td colspan="3">No students enrolled</td></tr>`;
            return;
        }

        students.forEach(s => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${s.name}</td>
                <td>${s.email}</td>
                <td>
                    <button onclick="markAttendance(${s.id}, true)">Present</button>
                    <button onclick="markAttendance(${s.id}, false)">Absent</button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    })
    .catch(err => alert(err.message));
}

// ===============================
// MARK ATTENDANCE
// ===============================
function markAttendance(studentId, status) {
    const courseId = document.getElementById("courseSelect").value;

    if (!courseId) {
        alert("Select course first");
        return;
    }

    fetch(`/api/teacher/attendance?studentId=${studentId}&courseId=${courseId}&status=${status}`, {
        method: "POST",
        headers: {
            "Authorization": "Bearer " + token
        }
    })
    .then(res => {
        if (!res.ok) return res.text().then(t => { throw new Error(t); });
        return res.text();
    })
    .then(msg => alert(msg || "Attendance marked"))
    .catch(err => alert(err.message));
}

// ===============================
// UPLOAD MARKS
// ===============================
function uploadMarks() {
    fetch("/api/teacher/marks", {
        method: "POST",
        headers: {
            "Authorization": "Bearer " + token,
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            studentId: document.getElementById("studentId").value,
            courseId: document.getElementById("course").value,
            internal: document.getElementById("internal").value,
            external: document.getElementById("external").value
        })
    })
    .then(res => {
        if (!res.ok) throw new Error("Access denied or bad request");
        return res.text();
    })
    .then(alert)
    .catch(err => alert(err.message));
}