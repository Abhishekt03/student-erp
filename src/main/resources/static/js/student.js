const token = localStorage.getItem("token");

if (!token) {
    alert("Session expired. Please login again.");
    window.location.href = "../login.html";
}


/* ===== AVAILABLE COURSES ===== */
function loadCourses() {
    fetch("http://localhost:8080/api/courses", {
        headers: { "Authorization": "Bearer " + token }
    })
    .then(res => res.json())
    .then(data => {
        let table = document.getElementById("courseTable");
        table.innerHTML = "";
        data.forEach(c => {
            table.innerHTML += `
                <tr>
                    <td>${c.title}</td>
                    <td>
                        <button onclick="enroll(${c.id})">Enroll</button>
                    </td>
                </tr>`;
        });
    });
}

function enroll(courseId) {

    const token = localStorage.getItem("token");
    if (!token) {
        alert("Login required");
        return;
    }

    fetch(`http://localhost:8080/api/student/enroll/${courseId}`, {
        method: "POST",
        headers: {
            "Authorization": "Bearer " + token
        }
    })
    .then(res => {
        if (!res.ok) return res.text().then(t => { throw new Error(t); });
        return res.text();
    })
    .then(alert)
    .catch(err => {
        console.error(err);
        alert("Enrollment failed");
    });
}


/* ===== ATTENDANCE ===== */


/* LOAD STUDENT COURSES */
fetch("http://localhost:8080/api/student/attendance", {
    headers: {
        "Authorization": "Bearer " + localStorage.getItem("token")
    }
})
.then(res => {
    if (!res.ok) throw new Error("Access denied");
    return res.json();
})
.then(data => {
    const tbody = document.getElementById("attendanceBody");
    if (!tbody) return;

    tbody.innerHTML = "";

    if (!data || data.length === 0) {
        tbody.innerHTML = `<tr><td colspan="3">No attendance found</td></tr>`;
        return;
    }

    data.forEach(a => {
        const tr = document.createElement("tr");
        tr.innerHTML = `
    <td>${a.course.title}</td>
    <td>${a.date}</td>
    <td style="
  color: ${a.status === true ? 'green' : 'red'};
  font-weight: bold;
">
  ${a.status === true ? "PRESENT" : "ABSENT"}
</td>

<td>${a.time ? a.time.slice(0,5) : "-"}</td>


`;

        tbody.appendChild(tr);
    });
let total = data.length;
let presentCount = data.filter(a => a.status === true).length;

let percent = total === 0
    ? 0
    : ((presentCount / total) * 100).toFixed(2);

document.getElementById("attendancePercent").innerText =
    `Attendance Percentage: ${percent}%`;


})



/* ===== STUDENT MARKS ===== */


document.addEventListener("DOMContentLoaded", () => {

    fetch("http://localhost:8080/api/student/marks", {
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token")
        }
    })
    .then(res => {
        if (!res.ok) throw new Error("403 Forbidden");
        return res.json();
    })
    .then(data => {

        // ✅ data is VALID here
        const tbody = document.getElementById("marksBody");

        if (!tbody) return;


        tbody.innerHTML = "";

        data.forEach(m => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${m.course}</td>
                <td>${m.internal}</td>
                <td>${m.external}</td>
                <td>${m.total}</td>
                <td>${m.grade}</td>
            `;
            tbody.appendChild(tr);
        });
    })
    .catch(err => console.error(err));

});

document.addEventListener("DOMContentLoaded", () => {

    fetch("http://localhost:8080/api/student/timetable", {
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token")
        }
    })
    
    .then(res => res.json())
    .then(data => {

        console.log("TIMETABLE DATA 👉", data);
    
    
        const tbody = document.getElementById("timetableBody");
        
       

        if (!tbody) return;


        tbody.innerHTML = "";
        

        if (!data || data.length === 0) {
            tbody.innerHTML =
              `<tr><td colspan="4">No timetable found</td></tr>`;
            return;
        }

        data.forEach(t => {
            tbody.innerHTML += `
                <tr>
                    <td>${t.courseTitle}</td>

                    <td>${t.day}</td>
                    <td>${t.startTime} - ${t.endTime}</td>
                    <td>${t.room}</td>
                </tr>
            `;
        });
    })
    .catch(err => console.error(err));
});


function logout() {
    localStorage.clear();
    window.location.href = "../login.html";
}
