document.addEventListener("DOMContentLoaded", function () {
  var menuToggle = document.querySelector("[data-menu-toggle]");
  var sidebar = document.querySelector("[data-sidebar]");
  var overlay = document.querySelector("[data-overlay]");

  if (menuToggle && sidebar && overlay) {
    function closeMenu() {
      sidebar.classList.remove("is-open");
      overlay.classList.remove("is-visible");
      menuToggle.setAttribute("aria-expanded", "false");
    }

    menuToggle.addEventListener("click", function () {
      var isOpen = sidebar.classList.toggle("is-open");
      overlay.classList.toggle("is-visible", isOpen);
      menuToggle.setAttribute("aria-expanded", String(isOpen));
    });

    overlay.addEventListener("click", closeMenu);

    document.addEventListener("keydown", function (event) {
      if (event.key === "Escape") {
        closeMenu();
      }
    });

    sidebar.querySelectorAll("a").forEach(function (link) {
      link.addEventListener("click", closeMenu);
    });
  }

  var demoForm = document.querySelector("[data-demo-form]");

  if (demoForm) {
    demoForm.addEventListener("submit", function (event) {
      event.preventDefault();

      if (!demoForm.checkValidity()) {
        demoForm.reportValidity();
        return;
      }

      var message = demoForm.getAttribute("data-success-message");
      window.sessionStorage.setItem("demoSuccessMessage", message);
      window.location.href = demoForm.action;
    });
  }

  var successMessage = window.sessionStorage.getItem("demoSuccessMessage");

  if (successMessage) {
    var pageHeader = document.querySelector(".page-header");

    if (pageHeader) {
      var notice = document.createElement("p");
      notice.className = "notice notice-info";
      notice.setAttribute("role", "status");
      notice.textContent = successMessage;
      pageHeader.insertAdjacentElement("afterend", notice);
    }

    window.sessionStorage.removeItem("demoSuccessMessage");
  }
});
