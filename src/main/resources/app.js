// Mock Database
let students = JSON.parse(localStorage.getItem('students')) || [];
let courses = JSON.parse(localStorage.getItem('courses')) || [];
let enrollments = JSON.parse(localStorage.getItem('enrollments')) || [];
let grades = JSON.parse(localStorage.getItem('grades')) || [];
let notes = JSON.parse(localStorage.getItem('notes')) || [];
let userWidgets = JSON.parse(localStorage.getItem('userWidgets')) || {};

let currentUser = null;

const WIDGET_TYPES = {
    ENROLLED_COURSES: 'enrolledCourses',
    GPA: 'gpa',
    TOTAL_CREDITS: 'totalCredits',
    AVERAGE_SCORE: 'averageScore'
};

const widgetLabels = {
    [WIDGET_TYPES.ENROLLED_COURSES]: '📚 Enrolled Courses',
    [WIDGET_TYPES.GPA]: '⭐ GPA',
    [WIDGET_TYPES.TOTAL_CREDITS]: '📖 Total Credits',
    [WIDGET_TYPES.AVERAGE_SCORE]: '📊 Average Score'
};

// Initialize sample data
function initSampleData() {
    if (students.length === 0) {
        students = [
            { id: 1, name: "Alex Johnson", email: "alex@harvard.edu", password: "pass123", studentId: "HARVARD001", phone: "+1 234 567 8900" },
            { id: 2, name: "Sarah Williams", email: "sarah@harvard.edu", password: "pass456", studentId: "HARVARD002", phone: "+1 987 654 3210" }
        ];
        localStorage.setItem('students', JSON.stringify(students));
    }
    if (courses.length === 0) {
        courses = [
            { id: "C101", name: "Data Structures", code: "CS201", credits: 4, instructor: "Dr. Smith" },
            { id: "C102", name: "Database Systems", code: "CS301", credits: 3, instructor: "Dr. Johnson" },
            { id: "C103", name: "Web Development", code: "CS401", credits: 3, instructor: "Prof. Brown" }
        ];
        localStorage.setItem('courses', JSON.stringify(courses));
    }
    if (enrollments.length === 0) {
        enrollments = [
            { studentId: "HARVARD001", courseId: "C101", enrolledDate: "2024-01-15" },
            { studentId: "HARVARD001", courseId: "C102", enrolledDate: "2024-01-15" }
        ];
        localStorage.setItem('enrollments', JSON.stringify(enrollments));
    }
    if (grades.length === 0) {
        grades = [
            { studentId: "HARVARD001", courseId: "C101", marks: 88, grade: "B+" },
            { studentId: "HARVARD001", courseId: "C102", marks: 92, grade: "A-" }
        ];
        localStorage.setItem('grades', JSON.stringify(grades));
    }
}
initSampleData();

function showToast(message, isSuccess = true) {
    const toast = document.createElement('div');
    toast.className = 'toast-notification';
    toast.innerHTML = `<i class="fas ${isSuccess ? 'fa-check-circle' : 'fa-exclamation-circle'}"></i> ${message}`;
    document.body.appendChild(toast);
    setTimeout(() => toast.remove(), 3000);
}

// Navigation Functions
function showHome() {
    document.getElementById('homeSection').style.display = 'flex';
    document.getElementById('loginSection').style.display = 'none';
    document.getElementById('registerSection').style.display = 'none';
    document.getElementById('forgotPasswordSection').style.display = 'none';
    document.getElementById('dashboardSection').style.display = 'none';
}

function showLoginFromHome() {
    document.getElementById('homeSection').style.display = 'none';
    document.getElementById('loginSection').style.display = 'flex';
    document.getElementById('registerSection').style.display = 'none';
    document.getElementById('forgotPasswordSection').style.display = 'none';
}

function showRegisterFromHome() {
    document.getElementById('homeSection').style.display = 'none';
    document.getElementById('loginSection').style.display = 'none';
    document.getElementById('registerSection').style.display = 'flex';
    document.getElementById('forgotPasswordSection').style.display = 'none';
}

function showLogin() {
    document.getElementById('homeSection').style.display = 'none';
    document.getElementById('loginSection').style.display = 'flex';
    document.getElementById('registerSection').style.display = 'none';
    document.getElementById('forgotPasswordSection').style.display = 'none';
}

function showRegisterFromLogin() {
    document.getElementById('homeSection').style.display = 'none';
    document.getElementById('loginSection').style.display = 'none';
    document.getElementById('registerSection').style.display = 'flex';
    document.getElementById('forgotPasswordSection').style.display = 'none';
}

function showLoginFromRegister() {
    document.getElementById('homeSection').style.display = 'none';
    document.getElementById('loginSection').style.display = 'flex';
    document.getElementById('registerSection').style.display = 'none';
    document.getElementById('forgotPasswordSection').style.display = 'none';
}

function showForgotPassword() {
    document.getElementById('homeSection').style.display = 'none';
    document.getElementById('loginSection').style.display = 'none';
    document.getElementById('registerSection').style.display = 'none';
    document.getElementById('forgotPasswordSection').style.display = 'flex';
}

function resetPassword() {
    const email = document.getElementById('forgotEmail').value;
    const newPassword = document.getElementById('newPasswordReset').value;
    const confirmPassword = document.getElementById('confirmPasswordReset').value;
    if (!email || !newPassword || !confirmPassword) return showToast('Please fill all fields', false);
    if (newPassword !== confirmPassword) return showToast('Passwords do not match', false);
    if (newPassword.length < 4) return showToast('Password must be at least 4 characters', false);
    const student = students.find(s => s.email === email);
    if (!student) return showToast('Email not found', false);
    student.password = newPassword;
    localStorage.setItem('students', JSON.stringify(students));
    showToast('Password reset successfully! Please login.', true);
    showLogin();
}

async function handleLogin() {

    const email = document.getElementById('loginEmail').value;
    const password = document.getElementById('loginPassword').value;

    if (!email || !password) {
        return showToast('Please enter email and password', false);
    }

    try {

        const response = await fetch(
            'http://localhost:8080/api/auth/login',
            {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    email: email,
                    password: password
                })
            }
        );

        const data = await response.json();

        if (data.success) {

            currentUser = data.data;

            if (!userWidgets[currentUser.studentId]) {

                userWidgets[currentUser.studentId] = [
                    'enrolledCourses',
                    'gpa',
                    'totalCredits',
                    'averageScore'
                ];

                localStorage.setItem(
                    'userWidgets',
                    JSON.stringify(userWidgets)
                );
            }

            showToast(
                `✨ Welcome back ${currentUser.name}!`,
                true
            );

            document.getElementById('homeSection').style.display = 'none';
            document.getElementById('loginSection').style.display = 'none';
            document.getElementById('registerSection').style.display = 'none';
            document.getElementById('forgotPasswordSection').style.display = 'none';
            document.getElementById('dashboardSection').style.display = 'block';

            updateSidebarInfo();
            renderDashboard();

        } else {

            showToast(data.message, false);
        }

    } catch (error) {

        console.log(error);
        showToast('Backend server not running', false);
    }
}

async function handleRegister() {

    const name = document.getElementById('regName').value;
    const email = document.getElementById('regEmail').value;
    const studentId = document.getElementById('regStudentId').value;
    const phone = document.getElementById('regPhone').value;
    const password = document.getElementById('regPassword').value;

    if (!name || !email || !studentId || !password) {
        return showToast('Please fill all required fields', false);
    }

    try {

        const response = await fetch(
            'http://localhost:8080/api/auth/register',
            {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    name,
                    email,
                    studentId,
                    phone,
                    password
                })
            }
        );

        const data = await response.json();

        if (data.success) {

            showToast(
                '✅ Account created successfully!',
                true
            );

            showLogin();

        } else {

            showToast(data.message, false);
        }

    } catch (error) {

        console.log(error);
        showToast('Cannot connect to backend', false);
    }
}

function handleLogout() {
    currentUser = null;
    showToast('You have been logged out', true);
    showHome();
}

function updateSidebarInfo() {
    document.getElementById('sidebarUserName').innerText = currentUser.name;
    document.getElementById('sidebarUserID').innerText = `ID: ${currentUser.studentId}`;
    document.getElementById('sidebarFaculty').innerText = "Harvard Faculty of Computing";
}

function getUserWidgets() {
    return userWidgets[currentUser.studentId] || [];
}

function saveUserWidgets(widgets) {
    userWidgets[currentUser.studentId] = widgets;
    localStorage.setItem('userWidgets', JSON.stringify(userWidgets));
}

function addWidget(widgetType) {
    const currentWidgets = getUserWidgets();
    if (!currentWidgets.includes(widgetType)) {
        currentWidgets.push(widgetType);
        saveUserWidgets(currentWidgets);
        showToast(`${widgetLabels[widgetType]} added to dashboard!`, true);
        renderDashboard();
    } else {
        showToast('This widget is already on your dashboard', false);
    }
}

function removeWidget(widgetType) {
    const currentWidgets = getUserWidgets();
    const newWidgets = currentWidgets.filter(w => w !== widgetType);
    saveUserWidgets(newWidgets);
    showToast(`${widgetLabels[widgetType]} removed from dashboard`, true);
    renderDashboard();
}

function getWidgetData() {
    const studentCourses = enrollments.filter(e => e.studentId === currentUser.studentId);
    const studentGrades = grades.filter(g => g.studentId === currentUser.studentId);
    let totalCredits = 0, totalPoints = 0, totalMarks = 0;
    studentCourses.forEach(e => { const c = courses.find(c => c.id === e.courseId); if(c) totalCredits += c.credits; });
    studentGrades.forEach(g => {
        const c = courses.find(c => c.id === g.courseId);
        let gp = g.marks >= 90 ? 4 : g.marks >= 80 ? 3.5 : g.marks >= 70 ? 3 : g.marks >= 60 ? 2.5 : 2;
        totalPoints += gp * (c ? c.credits : 3);
        totalMarks += g.marks;
    });
    const gpa = totalCredits > 0 ? (totalPoints/totalCredits).toFixed(2) : "0.00";
    const avgScore = studentGrades.length > 0 ? (totalMarks/studentGrades.length).toFixed(1) : "N/A";
    return { enrolledCount: studentCourses.length, gpa, totalCredits, avgScore, courses: studentCourses.map(e => courses.find(c => c.id === e.courseId)).filter(c => c), grades: studentGrades };
}

function renderDashboard() {
    const container = document.getElementById('dynamicContent');
    const widgets = getUserWidgets();
    const data = getWidgetData();

    let selectorHtml = `
        <div class="widget-selector">
            <h3><i class="fas fa-question-circle"></i> Do you want to see?</h3>
            <p>Select which widgets you want to display on your dashboard. Click on any option below to add it.</p>
            <div class="widget-buttons">
                <div class="widget-option ${widgets.includes('enrolledCourses') ? 'selected' : ''}" onclick="addWidget('enrolledCourses')"><i class="fas fa-book"></i> Enrolled Courses</div>
                <div class="widget-option ${widgets.includes('gpa') ? 'selected' : ''}" onclick="addWidget('gpa')"><i class="fas fa-star"></i> GPA</div>
                <div class="widget-option ${widgets.includes('totalCredits') ? 'selected' : ''}" onclick="addWidget('totalCredits')"><i class="fas fa-layer-group"></i> Total Credits</div>
                <div class="widget-option ${widgets.includes('averageScore') ? 'selected' : ''}" onclick="addWidget('averageScore')"><i class="fas fa-chart-simple"></i> Average Score</div>
            </div>
            <small style="color: #94a3b8;"><i class="fas fa-info-circle"></i> Tip: Click the <i class="fas fa-trash"></i> icon on any widget to remove it.</small>
        </div>
    `;

    if (widgets.length === 0) {
        container.innerHTML = selectorHtml + `<div class="empty-widgets"><i class="fas fa-chalkboard-user" style="font-size: 48px; margin-bottom: 15px;"></i><h3>No widgets selected</h3><p>Click on the widgets above to add them to your dashboard</p></div>`;
        return;
    }

    let widgetsHtml = '<div class="widgets-grid">';
    widgets.forEach(widgetType => {
        if (widgetType === 'enrolledCourses') {
            widgetsHtml += `<div class="widget-card"><div class="widget-header"><span class="widget-title"><i class="fas fa-book"></i> Enrolled Courses</span><button class="widget-remove" onclick="removeWidget('enrolledCourses')"><i class="fas fa-trash"></i></button></div><div class="widget-value">${data.enrolledCount}</div><div class="widget-detail">Courses currently enrolled</div>${data.courses.length > 0 ? `<div style="margin-top: 15px;"><small>📋 ${data.courses.map(c => c.name).join(', ')}</small></div>` : ''}</div>`;
        } else if (widgetType === 'gpa') {
            widgetsHtml += `<div class="widget-card"><div class="widget-header"><span class="widget-title"><i class="fas fa-star"></i> GPA</span><button class="widget-remove" onclick="removeWidget('gpa')"><i class="fas fa-trash"></i></button></div><div class="widget-value">${data.gpa}</div><div class="widget-detail">out of 4.0 scale</div></div>`;
        } else if (widgetType === 'totalCredits') {
            widgetsHtml += `<div class="widget-card"><div class="widget-header"><span class="widget-title"><i class="fas fa-layer-group"></i> Total Credits</span><button class="widget-remove" onclick="removeWidget('totalCredits')"><i class="fas fa-trash"></i></button></div><div class="widget-value">${data.totalCredits}</div><div class="widget-detail">credit hours completed</div></div>`;
        } else if (widgetType === 'averageScore') {
            widgetsHtml += `<div class="widget-card"><div class="widget-header"><span class="widget-title"><i class="fas fa-chart-simple"></i> Average Score</span><button class="widget-remove" onclick="removeWidget('averageScore')"><i class="fas fa-trash"></i></button></div><div class="widget-value">${data.avgScore}</div><div class="widget-detail">average marks across all courses</div></div>`;
        }
    });
    widgetsHtml += '</div>';

    let performanceHtml = `<h3 style="margin-top: 20px;">📈 Recent Performance</h3><table class="performance-table"><thead><tr><th>Course Name</th><th>Marks</th><th>Grade</th></tr></thead><tbody>${data.grades.map(g => { const c = courses.find(c => c.id === g.courseId); return `<tr><td><strong>${c?.name}</strong><br><small>${c?.code}</small></td><td>${g.marks}</td><td><span class="badge badge-success">${g.grade}</span></td></tr>`; }).join('') || '<tr><td colspan="3">No grades available yet</td></tr>'}</tbody></table>`;

    container.innerHTML = selectorHtml + widgetsHtml + performanceHtml;
}

function showDashboardSection(section, event) {
    document.querySelectorAll('.nav-item').forEach(item => item.classList.remove('active'));
    if (event && event.target) event.target.closest('.nav-item').classList.add('active');
    const container = document.getElementById('dynamicContent');
    if (section === 'dashboard') renderDashboard();
    else if (section === 'mycourses') {
        const studentCourses = enrollments.filter(e => e.studentId === currentUser.studentId);
        container.innerHTML = `<h2 class="section-title">📖 My Courses</h2><div class="widgets-grid">${studentCourses.map(e => { const c = courses.find(c => c.id === e.courseId); return `<div class="widget-card"><h3>${c?.name}</h3><p><strong>Code:</strong> ${c?.code}</p><p><strong>Credits:</strong> ${c?.credits}</p><p><strong>Instructor:</strong> ${c?.instructor}</p><button class="btn btn-secondary" onclick="unregisterCourse('${c?.id}')" style="margin-top: 10px; width:100%;">Unregister</button></div>`; }).join('') || '<p>No courses enrolled</p>'}</div>`;
    } else if (section === 'mygrades') {
        const studentGrades = grades.filter(g => g.studentId === currentUser.studentId);
        container.innerHTML = `<h2 class="section-title">📈 My Grades</h2><table class="performance-table"><thead><tr><th>Course</th><th>Marks</th><th>Grade</th><th>Status</th></tr></thead><tbody>${studentGrades.map(g => { const c = courses.find(c => c.id === g.courseId); const status = g.marks >= 60 ? 'Passed' : 'Failed'; return `<tr><td><strong>${c?.name}</strong><br><small>${c?.code}</small></td><td>${g.marks}</td><td><span class="badge badge-success">${g.grade}</span></td><td>${status}</td></tr>`; }).join('') || '<tr><td colspan="4">No grades available</td></tr>'}</tbody></table>`;
    } else if (section === 'profile') {
        container.innerHTML = `<h2 class="section-title">👤 My Profile</h2><div class="profile-card"><div class="info-row"><span class="info-label">Full Name:</span><span class="info-value"><strong>${currentUser.name}</strong></span></div><div class="info-row"><span class="info-label">Email Address:</span><span class="info-value">${currentUser.email}</span></div><div class="info-row"><span class="info-label">Student ID:</span><span class="info-value">${currentUser.studentId}</span></div><div class="info-row"><span class="info-label">Faculty:</span><span class="info-value"><span class="badge badge-success">Harvard Faculty of Computing</span></span></div><div class="info-row"><span class="info-label">Phone Number:</span><span class="info-value">${currentUser.phone || 'Not provided'}</span></div></div><div class="phone-section"><h3><i class="fas fa-phone"></i> Change Phone Number</h3><div class="form-group"><input type="tel" id="newPhoneNumber" placeholder="New phone number"></div><button class="btn btn-primary" onclick="changePhone()">Update Phone</button></div><div class="password-section"><h3><i class="fas fa-lock"></i> Change Password</h3><div class="form-group"><input type="password" id="currentPassword" placeholder="Current password"></div><div class="form-group"><input type="password" id="newPassword" placeholder="New password"></div><div class="form-group"><input type="password" id="confirmPassword" placeholder="Confirm password"></div><button class="btn btn-primary" onclick="changePassword()">Change Password</button></div>`;
    } else if (section === 'registration') {

          const registrationCourses = [
              {
                  id: "CS101",
                  name: "Java Programming",
                  code: "CS101",
                  credits: 4,
                  price: 9500
              },
              {
                  id: "CS102",
                  name: "Python for Beginners",
                  code: "CS102",
                  credits: 3,
                  price: 8500
              },
              {
                  id: "CS103",
                  name: "Web Development Fundamentals",
                  code: "CS103",
                  credits: 4,
                  price: 10000
              },
              {
                  id: "CS104",
                  name: "Full Stack Development",
                  code: "CS104",
                  credits: 4,
                  price: 9800
              },
              {
                  id: "CS105",
                  name: "Mobile App Development",
                  code: "CS105",
                  credits: 3,
                  price: 8700
              },
              {
                  id: "CS106",
                  name: "Object-Oriented Programming",
                  code: "CS106",
                  credits: 4,
                  price: 9200
              }
          ];

          let selectedCourses = JSON.parse(
              localStorage.getItem("selectedCourses")
          ) || [];

          function saveSelectedCourses() {

              localStorage.setItem(
                  "selectedCourses",
                  JSON.stringify(selectedCourses)
              );
          }

          function renderRegistrationPage() {

              container.innerHTML = `

              <h2 class="section-title">
                  📌 Course Registration
              </h2>

              <div class="registration-grid">

                  ${registrationCourses.map(course => {

                      const isSelected = selectedCourses.some(
                          c => c.id === course.id
                      );

                      return `

                      <div class="registration-card ${isSelected ? 'selected-course' : ''}">

                          <div class="course-top">
                              <h3>${course.name}</h3>
                          </div>

                          <div class="course-details">
                              <p><strong>Course Code:</strong> ${course.code}</p>
                              <p><strong>Credits:</strong> ${course.credits}</p>
                              <p><strong>Price:</strong> LKR ${course.price}</p>
                          </div>

                          <div class="course-actions">

                              ${
                                  !isSelected
                                  ?
                                  `<button class="btn btn-primary"
                                      onclick="registerSelectedCourse('${course.id}')">
                                      Register
                                  </button>`
                                  :
                                  `<button class="btn cancel-btn"
                                      onclick="cancelSelectedCourse('${course.id}')">
                                      Cancel
                                  </button>`
                              }

                          </div>

                      </div>

                      `;

                  }).join('')}

              </div>

              <div class="registration-next-area">

                  <button class="btn btn-primary next-btn"
                      onclick="showRegisteredCoursesPage()">

                      Next

                  </button>

              </div>

              `;
          }

          window.registerSelectedCourse = function(courseId) {

              const course = registrationCourses.find(
                  c => c.id === courseId
              );

              if (!selectedCourses.some(c => c.id === courseId)) {

                  selectedCourses.push(course);

                  saveSelectedCourses();

                  showToast(
                      `${course.name} registered successfully!`,
                      true
                  );

                  renderRegistrationPage();
              }
          };

          window.cancelSelectedCourse = function(courseId) {

              selectedCourses = selectedCourses.filter(
                  c => c.id !== courseId
              );

              saveSelectedCourses();

              showToast(
                  'Course removed successfully!',
                  true
              );

              renderRegistrationPage();
          };

          window.showRegisteredCoursesPage = function() {

              if (selectedCourses.length === 0) {

                  showToast(
                      'Please register at least one course',
                      false
                  );

                  return;
              }

              let totalCredits = 0;
              let totalAmount = 0;

              selectedCourses.forEach(course => {

                  totalCredits += course.credits;
                  totalAmount += course.price;
              });

              container.innerHTML = `

              <h2 class="section-title">
                  📚 Registered Courses Management
              </h2>

              <table class="performance-table">

                  <thead>
                      <tr>
                          <th>Course</th>
                          <th>Code</th>
                          <th>Credits</th>
                          <th>Price</th>
                          <th>Actions</th>
                      </tr>
                  </thead>

                  <tbody>

                      ${selectedCourses.map(course => `

                          <tr>

                              <td>${course.name}</td>
                              <td>${course.code}</td>
                              <td>${course.credits}</td>
                              <td>LKR ${course.price}</td>

                              <td>

                                  <div class="action-buttons">

                                      <button class="btn-icon btn-edit"
                                          onclick="updateCourse('${course.id}')">

                                          Edit

                                      </button>

                                      <button class="btn-icon btn-delete"
                                          onclick="removeCourse('${course.id}')">

                                          Remove

                                      </button>

                                  </div>

                              </td>

                          </tr>

                      `).join('')}

                  </tbody>

              </table>

              <div class="summary-box">

                  <h3>Total Credits: ${totalCredits}</h3>

                  <h3>Total Payment: LKR ${totalAmount}</h3>

              </div>

              <div class="payment-buttons">

                  <button class="btn cancel-btn"
                      onclick="renderRegistrationPage()">

                      Back

                  </button>

                  <button class="btn btn-primary"
                      onclick="showPaymentPage()">

                      Confirm Payment

                  </button>

              </div>

              `;
          };

          window.updateCourse = function(courseId) {

              const course = selectedCourses.find(
                  c => c.id === courseId
              );

              const updatedCredits = prompt(
                  'Update Credits (Max 4)',
                  course.credits
              );

              if (updatedCredits > 4) {

                  showToast(
                      'Credits cannot exceed 4',
                      false
                  );

                  return;
              }

              course.credits = Number(updatedCredits);

              saveSelectedCourses();

              showToast(
                  'Course updated successfully!',
                  true
              );

              showRegisteredCoursesPage();
          };

          window.removeCourse = function(courseId) {

              selectedCourses = selectedCourses.filter(
                  c => c.id !== courseId
              );

              saveSelectedCourses();

              showToast(
                  'Course removed successfully!',
                  true
              );

              showRegisteredCoursesPage();
          };

          window.showPaymentPage = function() {

              let totalAmount = 0;

              selectedCourses.forEach(course => {

                  totalAmount += course.price;

              });

              container.innerHTML = `

              <h2 class="section-title">
                  💳 Payment Confirmation
              </h2>

              <div class="payment-card">

                  <h3>Registered Courses Summary</h3>

                  <div class="payment-course-list">

                      ${selectedCourses.map(course => `

                          <div class="payment-course-item">

                              <span>${course.name}</span>

                              <strong>
                                  LKR ${course.price}
                              </strong>

                          </div>

                      `).join('')}

                  </div>

                  <div class="payment-total">

                      Total Amount :
                      <strong>LKR ${totalAmount}</strong>

                  </div>

                  <div class="payment-methods">

                      <label>
                          <input type="radio"
                              name="paymentMethod"
                              checked>

                          Cash Payment
                      </label>

                      <label>
                          <input type="radio"
                              name="paymentMethod">

                          Online Payment
                      </label>

                  </div>

                  <div class="payment-buttons">

                      <button class="btn cancel-btn"
                          onclick="showRegisteredCoursesPage()">

                          Cancel Payment

                      </button>

                      <button class="btn btn-primary"
                          onclick="completePayment()">

                          Confirm Payment

                      </button>

                  </div>

              </div>

              `;
          };

          window.completePayment = function() {

              showToast(
                  '✅ Payment completed successfully!',
                  true
              );

              container.innerHTML = `

              <div class="success-payment">

                  <i class="fas fa-check-circle"></i>

                  <h2>
                      Payment Successful
                  </h2>

                  <p>
                      Your courses have been registered successfully.
                  </p>

                  <button class="btn btn-primary"
                      onclick="renderRegistrationPage()">

                      Back to Registration

                  </button>

              </div>

              `;
          };

          renderRegistrationPage();
      }
}

function unregisterCourse(courseId) {
    const idx = enrollments.findIndex(e => e.studentId === currentUser.studentId && e.courseId === courseId);
    if (idx !== -1) enrollments.splice(idx, 1);
    localStorage.setItem('enrollments', JSON.stringify(enrollments));
    showToast('Unregistered from course', true);
    showDashboardSection('mycourses', null);
}

function registerCourse(courseId) {
    enrollments.push({ studentId: currentUser.studentId, courseId, enrolledDate: new Date().toISOString() });
    localStorage.setItem('enrollments', JSON.stringify(enrollments));
    showToast('Registered successfully!', true);
    showDashboardSection('registration', null);
}

function changePhone() {
    const newPhone = document.getElementById('newPhoneNumber')?.value;
    if (!newPhone) return showToast('Enter phone number', false);
    currentUser.phone = newPhone;
    const idx = students.findIndex(s => s.id === currentUser.id);
    if (idx !== -1) students[idx].phone = newPhone;
    localStorage.setItem('students', JSON.stringify(students));
    showToast('Phone updated!', true);
    showDashboardSection('profile', null);
}

async function resetPassword() {

    const email = document.getElementById('forgotEmail').value;
    const newPassword = document.getElementById('newPasswordReset').value;
    const confirmPassword = document.getElementById('confirmPasswordReset').value;

    if (!email || !newPassword || !confirmPassword) {
        return showToast('Please fill all fields', false);
    }

    if (newPassword !== confirmPassword) {
        return showToast('Passwords do not match', false);
    }

    try {

        const response = await fetch(
            'http://localhost:8080/api/auth/reset-password',
            {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    email: email,
                    newPassword: newPassword,
                    confirmPassword: confirmPassword

                })
            }
        );

        const data = await response.json();

        if (data.success) {

            showToast('Password reset successful!', true);
            showLogin();

        } else {
            showToast(data.message, false);
        }

    } catch (error) {

        console.log(error);
        showToast('Cannot connect to backend', false);
    }
}

function addNote() {
    const title = document.getElementById('noteTitle')?.value;
    const content = document.getElementById('noteContent')?.value;
    if (!title || !content) return showToast('Fill both fields', false);
    notes.push({ id: Date.now(), studentId: currentUser.studentId, title, content, createdAt: new Date().toISOString() });
    localStorage.setItem('notes', JSON.stringify(notes));
    showToast('Note added', true);
    document.getElementById('noteTitle').value = '';
    document.getElementById('noteContent').value = '';
    renderNotes();
}

function renderNotes() {
    const userNotes = notes.filter(n => n.studentId === currentUser.studentId);
    const container = document.getElementById('notesList');
    if (!container) return;
    if (userNotes.length === 0) { container.innerHTML = '<p>No notes yet</p>'; return; }
    container.innerHTML = `<table class="performance-table"><thead><tr><th>Title</th><th>Content</th><th>Actions</th></tr></thead><tbody>${userNotes.map(n => `<tr><td><strong>${escapeHtml(n.title)}</strong></td><td>${escapeHtml(n.content)}</td><td><button class="btn-icon btn-edit" onclick="editNote(${n.id})">Edit</button> <button class="btn-icon btn-delete" onclick="deleteNote(${n.id})">Delete</button></td></tr>`).join('')}</tbody></table>`;
}

function editNote(id) {
    const note = notes.find(n => n.id === id);
    if (note) {
        const newTitle = prompt('Edit title:', note.title);
        const newContent = prompt('Edit content:', note.content);
        if (newTitle && newContent) { note.title = newTitle; note.content = newContent; localStorage.setItem('notes', JSON.stringify(notes)); renderNotes(); showToast('Note updated', true); }
    }
}

function deleteNote(id) {
    if (confirm('Delete this note?')) {
        const idx = notes.findIndex(n => n.id === id);
        if (idx !== -1) notes.splice(idx, 1);
        localStorage.setItem('notes', JSON.stringify(notes));
        renderNotes();
        showToast('Note deleted', true);
    }
}

function escapeHtml(text) { if (!text) return ''; return text.replace(/[&<>]/g, m => m === '&' ? '&' : m === '<' ? '<' : '>'); }