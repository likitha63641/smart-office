(function () {
    const ADMIN_LINKS = [
        { label: 'Dashboard',     href: '/admin-dashboard.html', match: ['/admin-dashboard.html'] },
        { label: 'Issues',        href: '/issues.html',          match: ['/issues.html', '/issue-details.html'] },
        { label: 'Employees',     href: '/employees.html',       match: ['/employees.html'] },
        { label: 'Technicians',   href: '/technicians.html',     match: ['/technicians.html'] },
        { label: 'Categories',    href: '/categories.html',      match: ['/categories.html'] },
        { label: 'Notifications', href: '/notifications.html',   match: ['/notifications.html'] }
    ];

    const EMPLOYEE_LINKS = [
        { label: 'Dashboard',     href: '/employee-dashboard.html', match: ['/employee-dashboard.html'] },
        { label: 'My Issues',     href: '/issues.html',             match: ['/issues.html', '/issue-details.html'] },
        { label: 'Report Issue',  href: '/create-issue.html',       match: ['/create-issue.html'] },
        { label: 'Notifications', href: '/notifications.html',      match: ['/notifications.html'] }
    ];

    const CSS = `
    .so-nav{background:linear-gradient(135deg,#2563eb,#1d4ed8);color:#fff;padding:0 5%;
        display:flex;justify-content:space-between;align-items:center;gap:16px;
        min-height:64px;box-shadow:0 2px 10px rgba(15,23,42,.15);
        font-family:Segoe UI,Arial,sans-serif;position:sticky;top:0;z-index:1000}
    .so-nav *{box-sizing:border-box}
    .so-nav-brand{font-size:20px;font-weight:700;white-space:nowrap;display:flex;align-items:center;gap:10px}
    .so-nav-tag{font-size:11px;font-weight:700;letter-spacing:.5px;text-transform:uppercase;
        background:rgba(255,255,255,.2);padding:3px 8px;border-radius:20px}
    .so-nav-links{display:flex;align-items:center;gap:4px;flex-wrap:wrap;justify-content:flex-end}
    .so-nav-links a,.so-nav-links button{color:#fff;text-decoration:none;background:none;border:0;
        cursor:pointer;font:inherit;font-size:15px;padding:8px 12px;border-radius:8px;
        border-bottom:2px solid transparent;transition:background .15s}
    .so-nav-links a:hover,.so-nav-links button:hover{background:rgba(255,255,255,.14)}
    .so-nav-links a.active{font-weight:700;background:rgba(255,255,255,.18);border-bottom-color:#fff;border-radius:8px 8px 0 0}
    .so-nav-links .so-logout{margin-left:8px;border:1px solid rgba(255,255,255,.55);border-radius:8px}
    @media(max-width:820px){
        .so-nav{flex-direction:column;align-items:flex-start;padding:12px 5%}
        .so-nav-links{justify-content:flex-start}
        .so-nav-links a,.so-nav-links button{padding:7px 10px;font-size:14px}
    }`;

    function esc(s) {
        return String(s).replace(/[&<>"']/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
    }

    function render(user) {
        const isAdmin = user && user.role === 'ADMIN';
        const links = isAdmin ? ADMIN_LINKS : EMPLOYEE_LINKS;
        const path = window.location.pathname;

        let host = document.getElementById('main-navigation');
        if (!host) {
            host = document.createElement('div');
            host.id = 'main-navigation';
            document.body.insertBefore(host, document.body.firstChild);
        }

        if (!document.getElementById('so-nav-style')) {
            const style = document.createElement('style');
            style.id = 'so-nav-style';
            style.textContent = CSS;
            document.head.appendChild(style);
        }

        host.innerHTML =
            '<nav class="so-nav" aria-label="Main navigation">' +
              '<div class="so-nav-brand">Smart Office' +
                (isAdmin ? '<span class="so-nav-tag">Admin</span>' : '') +
              '</div>' +
              '<div class="so-nav-links">' +
                links.map(l =>
                    '<a href="' + esc(l.href) + '"' +
                    (l.match.indexOf(path) !== -1 ? ' class="active" aria-current="page"' : '') +
                    '>' + esc(l.label) + '</a>'
                ).join('') +
                '<button type="button" class="so-logout" id="so-logout-btn">Logout</button>' +
              '</div>' +
            '</nav>';

        document.getElementById('so-logout-btn').addEventListener('click', function () {
            if (typeof window.smartLogout === 'function') {
                window.smartLogout();
            } else {
                fetch('/auth/logout', { method: 'POST', credentials: 'same-origin' })
                    .catch(function () {})
                    .finally(function () { window.location.href = '/index.html'; });
            }
        });
    }

    document.addEventListener('smartoffice-auth-ready', function (e) {
        render(e.detail || window.currentUser);
    });
    if (window.currentUser) render(window.currentUser);
})();