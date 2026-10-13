(function () {
    const path = window.location.pathname;

    window.smartLogout = async function () {
        try { await fetch('/auth/logout', { method: 'POST' }); } catch (_) {}
        window.location.href = '/index.html';
    };

    window.apiFetch = async function (url, options = {}) {
        const response = await fetch(url, { credentials: 'same-origin', ...options });
        if (response.status === 401) {
            window.location.href = '/index.html';
            throw new Error('Your session has expired. Please login again.');
        }
        return response;
    };

    async function guard() {
        const publicPage = path === '/' || path === '/index.html' || path === '/auth.js';
        if (publicPage) return;
        try {
            const response = await fetch('/auth/me', { credentials: 'same-origin' });
            if (!response.ok) {
                window.location.href = '/index.html';
                return;
            }
            const me = await response.json();
            window.currentUser = me;

            // NEW: admins do not report issues; they review auto-generated incident summaries.
            if (me.role === 'ADMIN') {
                document.querySelectorAll('a[href="/create-issue.html"]').forEach(el => el.remove());
                if (path === '/create-issue.html') {
                    window.location.href = '/issues.html';
                    return;
                }
            }

            document.dispatchEvent(new CustomEvent('smartoffice-auth-ready', { detail: me }));
            const adminOnly = ['/employees.html', '/technicians.html', '/admin-dashboard.html', '/categories.html'];
            if (me.role === 'USER' && adminOnly.includes(path)) {
                window.location.href = '/dashboard.html';
            }
            document.querySelectorAll('[data-admin-only]').forEach(el => {
                if (me.role !== 'ADMIN') el.remove();
            });
            document.querySelectorAll('[data-user-name]').forEach(el => el.textContent = me.name || me.email);
        } catch (_) {
            window.location.href = '/index.html';
        }
    }

    window.addEventListener('DOMContentLoaded', guard);
})();