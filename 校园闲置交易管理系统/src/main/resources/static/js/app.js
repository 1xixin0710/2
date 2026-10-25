document.addEventListener("DOMContentLoaded", () => {
    const body = document.body;

    document.querySelectorAll("[data-sidebar-toggle]").forEach((button) => {
        button.addEventListener("click", () => body.classList.toggle("sidebar-open"));
    });

    document.querySelectorAll("[data-sidebar-overlay]").forEach((overlay) => {
        overlay.addEventListener("click", () => body.classList.remove("sidebar-open"));
    });

    document.querySelectorAll("[data-password-toggle]").forEach((button) => {
        button.addEventListener("click", () => {
            const input = document.getElementById(button.dataset.passwordToggle);
            if (!input) return;
            input.type = input.type === "password" ? "text" : "password";
            const icon = button.querySelector("svg");
            if (icon) {
                icon.outerHTML = `<i data-lucide="${input.type === "password" ? "eye" : "eye-off"}"></i>`;
                if (window.lucide) window.lucide.createIcons();
            }
        });
    });

    document.querySelectorAll(".flash").forEach((flash) => {
        window.setTimeout(() => flash.classList.add("is-hiding"), 4200);
        window.setTimeout(() => flash.remove(), 4550);
    });

    const dialog = document.getElementById("deleteDialog");
    const deleteForm = document.getElementById("deleteForm");
    const deleteTitle = document.getElementById("deleteItemTitle");

    document.querySelectorAll("[data-delete-item]").forEach((button) => {
        button.addEventListener("click", () => {
            if (!dialog || !deleteForm) return;
            deleteForm.action = `/items/${button.dataset.id}/delete`;
            deleteTitle.textContent = button.dataset.title;
            dialog.showModal();
        });
    });

    document.querySelectorAll("[data-dialog-close]").forEach((button) => {
        button.addEventListener("click", () => dialog?.close());
    });

    dialog?.addEventListener("click", (event) => {
        if (event.target === dialog) dialog.close();
    });

    const category = document.querySelector('select[name="category"]');
    const preview = document.querySelector(".image-preview img");
    const previewImages = {
        "电子数码": "/images/laptop.jpg",
        "书籍资料": "/images/books.jpg",
        "运动户外": "/images/bike.jpg",
        "宿舍生活": "/images/chair.jpg",
        "其他闲置": "/images/lamp.jpg",
    };
    category?.addEventListener("change", () => {
        if (preview && previewImages[category.value]) {
            preview.src = previewImages[category.value];
        }
    });
});
