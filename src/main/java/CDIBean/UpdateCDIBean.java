package CDIBean;

import EJB.AdminBeanLocal;
import EJB.UserBeanLocal;
import Entity.Attendance;
import Entity.AttendanceSummary;
import Entity.Course;
import Entity.Division;
import Entity.Semester;
import Entity.Student;
import Entity.Subject;
import Entity.Users;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.Map;
import java.io.Serializable;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ExternalContext;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;

@Named("updateCDIBean")
@ViewScoped
public class UpdateCDIBean implements Serializable {

    @EJB
    AdminBeanLocal abl;

    @EJB
    UserBeanLocal ubl;
    private String searchKeyword; // search input value
    private Collection<Student> filteredStudentList; // filtered list

    private Integer courseId;
    private String courseName;

    private Integer selectedCourseId;
    private Collection<Course> courseList;
    private Collection<Semester> semesterList;

    private boolean showUpdateForm = false;
    private Integer updateSemId;
    private String updateSemName;

    private Integer selectedSemId;
    private Collection<Subject> subjectList;

    private Integer updateSubId;
    private String updateSubName;

    private Integer selectedDivisionSemId;
    private Collection<Division> divisionList;

    private Integer updateDivId;
    private String updateDivName;

    private Collection<Student> studentList;

    private Integer selectedStudentSemId;
    private Integer selectedStudentDivId;

    private Integer updateStudId;
    private String updateStudName;
    private Integer updateStudRoll;

    private String name;
    private String rollNo;
    private String attendance; // P/A
    private Date attendanceDate;
    private String todayDateString;
    private Map<Integer, String> attendanceStatusMap = new HashMap<>();

    private Integer selectedSubId;
    private Collection<Attendance> dateWiseAttendanceList;
    private Date selectedDate; // java.util.Date

    public void loadDateWiseAttendance() {
        try {
            if (selectedDate == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "Select date!", null));
                return;
            }

            if (selectedCourseId == null || selectedCourseId == -1
                    || selectedSemId == null || selectedSemId == -1
                    || selectedStudentDivId == null || selectedStudentDivId == -1
                    || updateSubId == null || updateSubId == -1) {

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "Please select Course, Semester, Division, and Subject.", null));
                return;
            }

            // ✅ Clear previous data
            dateWiseAttendanceList = new ArrayList<>();

            // ✅ Convert java.util.Date to java.sql.Date
            java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());

            // ✅ Debug logging
            System.out.println("Loading attendance for date: " + sqlDate);
            System.out.println("Course: " + selectedCourseId
                    + ", Sem: " + selectedSemId
                    + ", Div: " + selectedStudentDivId
                    + ", Sub: " + updateSubId);

            // ✅ Get attendance for selected date
            dateWiseAttendanceList = abl.getAttendanceByDate(
                    selectedCourseId, selectedSemId, selectedStudentDivId, updateSubId, sqlDate
            );

            if (dateWiseAttendanceList.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage("No attendance found for selected date."));
            } else {
                System.out.println("Found " + dateWiseAttendanceList.size() + " attendance records");
            }

        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error loading attendance: " + e.getMessage(), null));
        }
    }

    public void markAllPresent() {
        try {
            if (studentList != null) {
                for (Student st : studentList) {
                    attendanceStatusMap.put(st.getStudId(), "present");
                }
            }
            message = "All students marked Present!";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void markAllAbsent() {
        try {
            if (studentList != null) {
                for (Student st : studentList) {
                    attendanceStatusMap.put(st.getStudId(), "absent");
                }
            }
            message = "All students marked Absent!";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearAttendanceData() {
        dateWiseAttendanceList = new ArrayList<>();
        System.out.println("Cleared attendance data on date change");
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }

    public Collection<Attendance> getDateWiseAttendanceList() {
        return dateWiseAttendanceList;
    }

    public Collection<Student> getFilteredStudentList() {
        return filteredStudentList;
    }

    public Integer getSelectedSubId() {
        return selectedSubId;
    }

    public void setSelectedSubId(Integer selectedSubId) {
        this.selectedSubId = selectedSubId;
    }

    public void filterStudents() {
        if (studentList == null) {
            studentList = new ArrayList<>();
        }
        if (searchKeyword == null || searchKeyword.trim().isEmpty()) {
            filteredStudentList = new ArrayList<>(studentList);
        } else {
            String key = searchKeyword.toLowerCase();
            filteredStudentList = studentList.stream()
                    .filter(s -> (s.getStudentName() != null && s.getStudentName().toLowerCase().contains(key))
                    || (s.getRollNo() != null && s.getRollNo().toString().toLowerCase().contains(key)))
                    .collect(Collectors.toList());
        }
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
        filterStudents(); // filter list every time searchKeyword changes
    }

    public Map<Integer, String> getAttendanceStatusMap() {
        return attendanceStatusMap;
    }
    private Map<Integer, AttendanceSummary> studentSummaryMap = new HashMap<>();

    public Map<Integer, AttendanceSummary> getStudentSummaryMap() {
        return studentSummaryMap;
    }

    @PostConstruct
    public void init() {
        courseList = (Collection<Course>) abl.getAllCourse();
        semesterList = new ArrayList<>();
        subjectList = new ArrayList<>();
        divisionList = new ArrayList<>();
        studentList = new ArrayList<>();
        attendanceDate = new Date();
        selectedCourseId = -1;
        selectedSemId = -1;
        selectedStudentDivId = -1;
        updateSubId = -1;
        // Format today's date for display
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        todayDateString = sdf.format(attendanceDate);
        if (attendanceDate == null) {
            attendanceDate = new Date(); // today
        }
        selectedDate = attendanceDate;
    }

    public void loadStudents() {
        studentList = new ArrayList<>();
        filteredStudentList = new ArrayList<>();
        studentSummaryMap.clear();

        if (selectedCourseId != null && selectedSemId != null
                && selectedStudentDivId != null && updateSubId != null) {

            studentList = abl.getStudentsByCourseSemDiv(selectedCourseId, selectedSemId, selectedStudentDivId);

            for (Student st : studentList) {
                AttendanceSummary summary = abl.findSummaryByStudentAndSubject(st.getStudId(), updateSubId);
                if (summary != null) {
                    studentSummaryMap.put(st.getStudId(), summary);
                } else {
                    studentSummaryMap.put(st.getStudId(), null); // Ya ek empty placeholder
                }
            }

        }

        filteredStudentList = new ArrayList<>(studentList);
    }

    public void showStudent() {
        System.out.println("selectedCourseId = " + selectedCourseId);
        System.out.println("selectedSemId = " + selectedSemId);
        System.out.println("selectedStudentDivId = " + selectedStudentDivId);

        if (selectedCourseId != null && selectedCourseId > 0
                && selectedSemId != null && selectedSemId > 0
                && selectedStudentDivId != null && selectedStudentDivId > 0) {

            studentList = abl.getStudentsByCourseSemDiv(selectedCourseId, selectedSemId, selectedStudentDivId);

            System.out.println("Loaded students: " + studentList.size());
        } else {
            studentList = new ArrayList<>();
            System.out.println("Waiting for valid selection...");
        }
        for (Student st : studentList) {
            System.out.println("Stud: " + st.getStudId() + " | "
                    + "Course: " + st.getCourse().getCourseName() + " | "
                    + "Sem: " + st.getSemester().getSemName() + " | "
                    + "Div: " + st.getDivision().getDivName());
        }

    }

    public void loadDivisions() {
        studentList = new ArrayList<>();
        filteredStudentList = new ArrayList<>();
        studentSummaryMap.clear();

        // reset division selection
        selectedStudentDivId = -1;

        if (selectedCourseId != null && selectedSemId != null) {
            divisionList = abl.getDivisionsByCourseAndSemester(selectedSemId, selectedCourseId);
        } else {
            divisionList = new ArrayList<>();
        }
    }

    public void loadSubjects() {
        studentList = new ArrayList<>();
        filteredStudentList = new ArrayList<>();
        studentSummaryMap.clear();

        // reset subject selection
        updateSubId = -1;

        if (selectedCourseId != null && selectedSemId != null) {
            subjectList = abl.getSubjectsByCourseandSemester(selectedCourseId, selectedSemId);
        } else {
            subjectList = new ArrayList<>();
        }
    }

    public void loadSemesters() {
        selectedSemId = null;
        selectedStudentDivId = null;
        updateSubId = null;

        divisionList = new ArrayList<>();
        subjectList = new ArrayList<>();

        if (selectedCourseId != null) {
            semesterList = abl.getSemestersByCourse(selectedCourseId);
        } else {
            semesterList = new ArrayList<>();
        }
    }

    public void loadDivisionAndSubject() {
//        selectedStudentDivId = null;
//        updateSubId = null;
        loadDivisions();
        loadSubjects();
    }

    // --- Edit/Update/Delete Methods ---
    public void editStudent(Integer studId, String studName, Integer rollNo) {
        updateStudId = studId;
        updateStudName = studName;
        updateStudRoll = rollNo;
        showUpdateForm = true;
    }

    public void updateStudent() {
        abl.updateStudent(updateStudId, updateStudName, updateStudRoll, selectedCourseId, selectedSemId, selectedStudentDivId);
        showStudent();
        showUpdateForm = false;
    }

    public void deleteStudent(Integer studId) {
        abl.deleteStudent(studId, selectedCourseId, selectedSemId, selectedStudentDivId);
        showStudent();
    }

    public void editDivision(Integer divId, String divName) {
        updateDivId = divId;
        updateDivName = divName;
        updateSemId = null;
        updateSubId = null;
        updateStudId = null;
        showUpdateForm = true;

    }

    public void updateDivision() {
        abl.updateDivision(updateDivId, updateDivName, selectedSemId, selectedCourseId);
        loadDivisions();
        showUpdateForm = false;
    }

    public void deleteDivision(Integer divId) {
        abl.deleteDivision(divId, selectedSemId, selectedCourseId);
        loadDivisions();
    }

    public void editSubject(Integer subId, String subName) {
        updateSubId = subId;
        updateSubName = subName;
        showUpdateForm = true;
    }

    public void updateSubject() {
        abl.updateSubject(updateSubId, updateSubName, selectedCourseId, selectedSemId);
        loadSubjects();
        showUpdateForm = false;
    }

    public void deleteSubject(Integer subId) {
        abl.deleteSubject(subId, selectedCourseId, selectedSemId);
        loadSubjects();
    }

    public void editSemester(Integer semId, String semName) {
        updateSemId = semId;
        updateSemName = semName;
        showUpdateForm = true;
    }

    public void updateSemester() {
        abl.updateSemester(updateSemId, updateSemName, selectedCourseId);
        loadSemesters();
        showUpdateForm = false;
    }

    public void deleteSemester(Integer semId) {
        if (selectedCourseId != null) {
            abl.deleteSemester(semId, selectedCourseId);
            loadSemesters(); // reload only selected course semesters
        }
    }

    public void saveUpdate() {
        if (updateSemId != null) {
            updateSemester();
        } else if (updateSubId != null) {
            updateSubject();
        } else if (updateDivId != null) {
            updateDivision();
        } else if (updateStudId != null) {
            updateStudent();
        }
    }

    public void cancelPopup() {
        showUpdateForm = false;
        updateSemId = null;
        updateSemName = null;

        updateSubId = null;
        updateSubName = null;

        updateDivId = null;
        updateDivName = null;

        updateStudId = null;
        updateStudName = null;
        updateStudRoll = null;
    }

    // --- Constructor & Course Update ---
    public UpdateCDIBean() {
        Object id = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("cid");
        Object cname = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("cname");

        if (id != null) {
            this.courseId = (Integer) id;
        }
        if (cname != null) {
            this.courseName = (String) cname;
        }
    }

    public void updateCourse() {
        if (courseId != null) {
            abl.updateCourse(courseName, courseId);
        }
    }

    // --- Attendance Page Navigation ---
    public String goToAttendancePage() {
        // Flash me selected IDs store karo
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.getExternalContext().getFlash().put("cid", selectedCourseId);
        fc.getExternalContext().getFlash().put("cname", courseName);
        fc.getExternalContext().getFlash().put("semId", selectedSemId);
        fc.getExternalContext().getFlash().put("divId", selectedStudentDivId);
        fc.getExternalContext().getFlash().put("subId", updateSubId);

        return "studentAttendance?faces-redirect=true";
    }

    public void exportAttendanceToExcel() {
        try {
            // Workbook & Sheet
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Attendance Report");

            // Header Row
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Roll No");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Total Present");
            header.createCell(3).setCellValue("Total Absent");
            header.createCell(4).setCellValue("Average (%)");

            // Data Rows
            int rowCount = 1;
            for (Student stud : filteredStudentList) {
                Row row = sheet.createRow(rowCount++);
                row.createCell(0).setCellValue(stud.getRollNo());
                row.createCell(1).setCellValue(stud.getStudentName());

                AttendanceSummary summary = studentSummaryMap.get(stud.getStudId());
                if (summary != null) {
                    row.createCell(2).setCellValue(summary.getTotalPresent());
                    row.createCell(3).setCellValue(summary.getTotalAbsent());
                    row.createCell(4).setCellValue(summary.getAvgAttendance());
                } else {
                    row.createCell(2).setCellValue(0);
                    row.createCell(3).setCellValue(0);
                    row.createCell(4).setCellValue(0);
                }
            }

            // Auto-size columns
            for (int i = 0; i < 5; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write to response
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            ec.responseReset();
            ec.setResponseContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            ec.setResponseHeader("Content-Disposition", "attachment; filename=Attendance_Report.xlsx");

            workbook.write(ec.getResponseOutputStream());
            workbook.close();
            fc.responseComplete();

        } catch (Exception e) {
            e.printStackTrace();
            message = "Error exporting Excel: " + e.getMessage();
        }
    }

    // --- Getters & Setters ---
    public Date getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(Date attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getTodayDateString() {
        return todayDateString;
    }

    public java.sql.Date getTodaySqlDate() {
        return new java.sql.Date(attendanceDate.getTime());
    }

    public Integer getSelectedCourseId() {
        return selectedCourseId;
    }

    public void setSelectedCourseId(Integer selectedCourseId) {
        this.selectedCourseId = selectedCourseId;
    }

    public Collection<Course> getCourseList() {
        return courseList;
    }

    public Collection<Semester> getSemesterList() {
        return semesterList;
    }

    public boolean isShowUpdateForm() {
        return showUpdateForm;
    }

    public Integer getUpdateSemId() {
        return updateSemId;
    }

    public String getUpdateSemName() {
        return updateSemName;
    }

    public void setUpdateSemName(String updateSemName) {
        this.updateSemName = updateSemName;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Collection<Student> getStudentList() {
        return studentList;
    }

    public Integer getUpdateStudId() {
        return updateStudId;
    }

    public String getUpdateStudName() {
        return updateStudName;
    }

    public void setUpdateStudName(String updateStudName) {
        this.updateStudName = updateStudName;
    }

    public Integer getUpdateStudRoll() {
        return updateStudRoll;
    }

    public void setUpdateStudRoll(Integer updateStudRoll) {
        this.updateStudRoll = updateStudRoll;
    }

    public Integer getSelectedStudentSemId() {
        return selectedStudentSemId;
    }

    public void setSelectedStudentSemId(Integer selectedStudentSemId) {
        this.selectedStudentSemId = selectedStudentSemId;
    }

    public Integer getSelectedStudentDivId() {
        return selectedStudentDivId;
    }

    public void setSelectedStudentDivId(Integer selectedStudentDivId) {
        this.selectedStudentDivId = selectedStudentDivId;
    }

    public Collection<Subject> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(Collection<Subject> subjectList) {
        this.subjectList = subjectList;
    }

    public Integer getSelectedSemId() {
        return selectedSemId;
    }

    public void setSelectedSemId(Integer selectedSemId) {
        this.selectedSemId = selectedSemId;
    }

    public Integer getUpdateSubId() {
        return updateSubId;
    }

    public void setUpdateSubId(Integer updateSubId) {
        this.updateSubId = updateSubId;
    }

    public Collection<Division> getDivisionList() {
        return divisionList;
    }

    public Integer getUpdateDivId() {
        return updateDivId;
    }

    public String getUpdateDivName() {
        return updateDivName;
    }

    public void setUpdateDivName(String updateDivName) {
        this.updateDivName = updateDivName;
    }

    // --- NEW GETTER FOR SELECTED SUBJECT NAME ---
    public String getSelectedSubName() {
        if (subjectList != null && updateSubId != null) {
            for (Subject sub : subjectList) {
                if (sub.getSubId().equals(updateSubId)) {
                    return sub.getSubName();
                }
            }
        }
        return "";
    }

    public String getUpdateSubName() {
        return updateSubName;
    }

    public void setUpdateSubName(String updateSubName) {
        this.updateSubName = updateSubName;
    }

    public void setAttendanceStatusMap(Map<Integer, String> attendanceStatusMap) {
        this.attendanceStatusMap = attendanceStatusMap;
    }
    private String message; // Add this in your CDI bean

    public String getMessage() {
        return message;
    }

    public void toggleAttendance(Integer studId) {
        String current = attendanceStatusMap.getOrDefault(studId, "absent");
        if ("absent".equals(current)) {
            attendanceStatusMap.put(studId, "present");
        } else {
            attendanceStatusMap.put(studId, "absent");
        }
    }

    public void saveAttendance() {
        Users loggedUser = (Users) FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("loggedUser");

        if (selectedCourseId == null || updateSubId == null || selectedSemId == null || selectedStudentDivId == null) {
            message = "Please select Course, Semester, Division, and Subject.";
            return;
        }

        boolean allSuccess = true;
        StringBuilder alreadyDoneStudents = new StringBuilder();

        for (Student st : studentList) {
            Integer studentId = st.getStudId();
            String status = attendanceStatusMap.getOrDefault(studentId, "absent");
            try {
                ubl.markAttendance(
                        studentId,
                        19, // loggedUser hardcode for now
                        selectedCourseId,
                        updateSubId,
                        selectedSemId,
                        selectedStudentDivId,
                        getTodaySqlDate(),
                        status
                );
                abl.updateSummary(studentId, updateSubId);

            } catch (IllegalArgumentException e) {
                if (e.getMessage().toLowerCase().contains("already marked")) {
                    allSuccess = false;
                    alreadyDoneStudents.append(st.getStudentName()).append(", ");
                } else {
                    allSuccess = false;
                    alreadyDoneStudents.append(st.getStudentName())
                            .append(" (Error: ").append(e.getMessage()).append("), ");
                }
            } catch (Exception e) {
                allSuccess = false;
                alreadyDoneStudents.append(st.getStudentName())
                        .append(" (Error: ").append(e.getMessage()).append("), ");
            }
        }

        String students = alreadyDoneStudents.toString();
        if (!students.isEmpty()) {
            students = students.substring(0, students.length() - 2);
        }

        if (allSuccess) {
            message = "Attendance saved successfully!";
        } else {
            message = "Attendance already marked for Today";
        }
    }

}
