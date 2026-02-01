document.addEventListener("DOMContentLoaded", () => {
    loadProfile();
    loadProfilePhoto();
});

function loadProfile() {
    fetch("http://localhost:8080/api/profile", {
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token")
        }
    })
    .then(res => {
        if (!res.ok) throw new Error("Unauthorized");
        return res.json();
    })
    .then(data => {
        document.getElementById("userId").innerText = data.id;
        document.getElementById("name").innerText = data.name;
        document.getElementById("email").innerText = data.email;
        document.getElementById("role").innerText = data.role;
        document.getElementById("dob").innerText = data.dob ?? "-";
        document.getElementById("phone").innerText = data.phone ?? "-";
        document.getElementById("address").innerText = data.address ?? "-";
    })
    .catch(err => console.error(err));
}

function loadProfilePhoto() {
    fetch("http://localhost:8080/api/profile/photo", {
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token")
        }
    })
    .then(res => res.blob())
    .then(blob => {
        document.getElementById("profilePic").src =
            URL.createObjectURL(blob);
    });
}

function openFilePicker() {
    document.getElementById("photoInput").click();
}

function uploadPhoto() {
    const file = document.getElementById("photoInput").files[0];
    if (!file) return;

    const formData = new FormData();
    formData.append("photo", file);

    fetch("http://localhost:8080/api/profile/photo", {
        method: "POST",
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token")
        },
        body: formData
    })
    .then(res => {
        if (!res.ok) throw new Error();
        loadProfilePhoto();
    })
    .catch(() => alert("Photo upload failed"));
}

function logout() {
    localStorage.removeItem("token");
    window.location.href = "../login.html";
}
