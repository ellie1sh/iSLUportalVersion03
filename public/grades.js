(function() {
    const API_BASE = '';
    const tableBody = document.getElementById('gradesBody');
    const userNameEl = document.getElementById('userName');

    document.addEventListener('DOMContentLoaded', init);

    async function init() {
        // Try to load account and grades; show placeholders if not logged in
        const token = localStorage.getItem('authToken');

        if (!token) {
            renderPlaceholderRows();
            return;
        }

        try {
            await loadAccount(token);
            await loadGrades(token);
            // Start simple polling stub for real-time updates (can be upgraded to SSE/WebSocket later)
            startPolling(token);
        } catch (e) {
            console.error(e);
            renderPlaceholderRows();
        }
    }

    async function loadAccount(token) {
        const res = await fetch(`${API_BASE}/api/student/account`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        if (!res.ok) throw new Error('Failed to load account');
        const acc = await res.json();
        userNameEl.textContent = `${acc.first_name} ${acc.middle_name || ''} ${acc.last_name}`.trim();
    }

    async function loadGrades(token) {
        // We call the same endpoint as main app; if prelim unpaid, backend returns 403.
        const res = await fetch(`${API_BASE}/api/student/grades`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (res.status === 403) {
            renderPlaceholderRows(true);
            return;
        }

        if (!res.ok) {
            renderPlaceholderRows();
            return;
        }

        const grades = await res.json();
        renderGrades(grades);
    }

    function renderGrades(grades) {
        if (!grades || grades.length === 0) {
            renderPlaceholderRows();
            return;
        }

        tableBody.innerHTML = grades.map(g => {
            const prelim = formatNullable(g.prelim_grade);
            const midterm = formatNullable(g.midterm_grade);
            const tentativeFinal = formatNullable(g.semester_grade);
            const finalGrade = formatNullable(g.final_grade);
            const weights = '';
            return `
                <tr>
                    <td>${escapeHtml(g.subject_code)}</td>
                    <td>${escapeHtml(g.subject_name)}</td>
                    <td>${g.units}</td>
                    <td>${prelim}</td>
                    <td>${midterm}</td>
                    <td>${tentativeFinal}</td>
                    <td>${finalGrade}</td>
                    <td>${weights}</td>
                </tr>
            `;
        }).join('');
    }

    function renderPlaceholderRows(unpaid = false) {
        const subjects = [
            { classCode: '7024', course: 'NSTP-CWTS 1', units: 3 },
            { classCode: '9545', course: 'GSTS', units: 3 },
            { classCode: '9455', course: 'GENM', units: 3 },
            { classCode: '9456', course: 'CFE 103', units: 3 },
            { classCode: '9457', course: 'IT 211', units: 3 },
            { classCode: '9458', course: 'IT 212', units: 2 },
            { classCode: '9458-1', course: 'IT 212L', units: 1 },
            { classCode: '9459', course: 'IT 213', units: 2 },
            { classCode: '9459-1', course: 'IT 213L', units: 1 },
            { classCode: '9547', course: 'FIT 0A', units: 2 }
        ];

        tableBody.innerHTML = subjects.map(s => `
            <tr>
                <td>${escapeHtml(s.classCode)}</td>
                <td>${escapeHtml(s.course)}</td>
                <td>${s.units}</td>
                <td class="placeholder">${unpaid ? 'Locked' : 'Not Yet Submitted'}</td>
                <td class="placeholder">Not Yet Submitted</td>
                <td class="placeholder">Not Yet Submitted</td>
                <td class="placeholder">Not Yet Submitted</td>
                <td class="placeholder">Not Yet Submitted</td>
            </tr>
        `).join('');
    }

    function startPolling(token) {
        setInterval(() => {
            loadGrades(token).catch(() => {});
        }, 15000); // 15s simple refresh
    }

    function formatNullable(v) {
        return (v === null || v === undefined || v === '') ? '<span class="placeholder">Not Yet Submitted</span>' : v;
    }

    function escapeHtml(str) {
        return String(str || '').replace(/[&<>"']/g, m => ({
            '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;'
        })[m]);
    }
})();

