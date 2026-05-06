function showMessage(msg, success) {
    var box = document.getElementById("messageBox");

    box.textContent = msg;
    box.className = success ? "success-message" : "error-message";
    box.style.display = "block";

    setTimeout(function () {
        box.style.display = "none";
    }, 3000);
}