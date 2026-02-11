document.addEventListener("DOMContentLoaded", () => {

    const token = localStorage.getItem("token");
    if (!token) {
        alert("Please login again");
        window.location.href = "/login.html";
        return;
    }

    loadProfile();
    loadPhoto();
});

function loadProfile() {
    fetch("/api/student/profile", {
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token")
        }
    })
    .then(r => {
        if (!r.ok) throw new Error("Unauthorized");
        return r.json();
    })
    .then(d => {
        document.getElementById("userId").innerText = d.id ?? "-";
        document.getElementById("name").innerText = d.name ?? "-";
        document.getElementById("email").innerText = d.email ?? "-";
        document.getElementById("role").innerText = d.role ?? "-";
        document.getElementById("dob").innerText = d.dob ?? "-";
        document.getElementById("phone").innerText = d.phone ?? "-";
        document.getElementById("address").innerText = d.address ?? "-";
    })
    .catch(err => {
        console.error(err);
        alert("Session expired. Login again.");
        window.location.href = "/login.html";
    });
}

function openFilePicker() {
    document.getElementById("photoInput").click();
}

function loadPhoto() {
    fetch("/api/student/profile/photo", {
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token")
        }
    })
    .then(r => {
        if (!r.ok) throw new Error("Photo not found");
        return r.blob();
    })
    .then(b => {
        document.getElementById("profilePic").src =
            URL.createObjectURL(b);
    })
    .catch(() => {
        console.warn("No profile photo");
    });
}

function uploadPhoto() {
    const fileInput = document.getElementById("photoInput");
    const file = fileInput.files[0];

    if (!file) {
        alert("Please select a file");
        return;
    }

    const formData = new FormData();
    formData.append("photo", file);

    fetch("/api/student/profile/photo", {   // ✅ FIXED
        method: "POST",
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token")
        },
        body: formData
    })
    .then(res => {
        if (!res.ok) throw new Error("Upload failed");
        alert("Photo uploaded successfully");
        loadPhoto();
    })
    .catch(() => alert("Photo upload failed"));
}
