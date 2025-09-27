package edu.slu.accounts.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Minimal reader for legacy text files under LabProjectPrelimsV3.
 * This keeps the Java-only requirement while preserving data shown in prior UI.
 */
public final class LegacyDataUtil {
    private LegacyDataUtil() {}

    private static File resolveFile(String filename) {
        // 1) direct (working dir)
        File direct = new File(filename);
        if (direct.exists()) return direct.getAbsoluteFile();

        // 2) common project-relative paths
        String[] prefixes = new String[]{
            "LabProjectPrelimsV3/",
            "LabProjectPrelimsV3/LabProjectPrelimsV2/",
            "./LabProjectPrelimsV3/",
            "."
        };
        for (String p : prefixes) {
            File f = new File(p + filename);
            if (f.exists()) return f.getAbsoluteFile();
        }

        // 3) walk up from code source
        try {
            URL codeSourceUrl = LegacyDataUtil.class.getProtectionDomain().getCodeSource().getLocation();
            File location = new File(codeSourceUrl.toURI());
            File dir = location.isFile() ? location.getParentFile() : location;
            for (int i = 0; i < 8 && dir != null; i++) {
                File candidate = new File(dir, filename);
                if (candidate.exists()) return candidate.getAbsoluteFile();

                // also try nested LabProjectPrelimsV3
                File nested = new File(dir, "LabProjectPrelimsV3/" + filename);
                if (nested.exists()) return nested.getAbsoluteFile();

                dir = dir.getParentFile();
            }
        } catch (URISyntaxException ignored) {}

        return direct.getAbsoluteFile();
    }

    public static Object[][] loadSchedule(String studentId) {
        List<Object[]> rows = new ArrayList<>();
        File file = resolveFile("courseSchedules.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("===") || line.startsWith("Format:")) continue;
                String[] parts = line.split(",");
                if (parts.length < 11) continue;
                if (!parts[0].trim().equals(studentId)) continue;
                rows.add(new Object[]{
                    parts[1].trim(), // ClassCode
                    parts[2].trim(), // CourseNumber
                    parts[3].trim(), // CourseDescription
                    parts[4].trim(), // Units
                    parts[5].trim(), // StartTime
                    parts[6].trim(), // EndTime
                    parts[7].trim(), // Days
                    parts[8].trim(), // Room
                    parts[9].trim(), // Instructor
                    parts[10].trim() // Semester
                });
            }
        } catch (Exception ignored) {}
        if (rows.isEmpty()) {
            return new Object[][]{{"No schedule found", "", "", "", "", "", "", "", "", ""}};
        }
        return rows.toArray(new Object[0][]);
    }

    public static String[] scheduleColumns() {
        return new String[]{"Class Code", "Course No.", "Description", "Units", "Start", "End", "Days", "Room", "Instructor", "Semester"};
    }

    public static Object[][] loadAttendance(String studentId) {
        List<Object[]> rows = new ArrayList<>();
        File file = resolveFile("attendanceRecords.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("===") || line.startsWith("Format:")) continue;
                String[] parts = line.split(",");
                if (parts.length < 6) continue;
                if (!parts[0].trim().equals(studentId)) continue;
                rows.add(new Object[]{
                    parts[1].trim(), // SubjectCode
                    parts[2].trim(), // SubjectName
                    parts[3].trim(), // Date
                    parts[4].trim(), // Status
                    parts.length > 5 ? parts[5].trim() : "" // Remarks
                });
            }
        } catch (Exception ignored) {}
        if (rows.isEmpty()) {
            return new Object[][]{{"No attendance records", "", "", "", ""}};
        }
        return rows.toArray(new Object[0][]);
    }

    public static String[] attendanceColumns() {
        return new String[]{"Subject", "Subject Name", "Date", "Status", "Remarks"};
    }

    public static Object[][] loadTranscript(String studentId) {
        List<Object[]> rows = new ArrayList<>();
        File file = resolveFile("gradeRecords.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("===") || line.startsWith("Format:")) continue;
                String[] parts = line.split(",");
                if (parts.length < 5) continue;
                if (!parts[0].trim().equals(studentId)) continue;

                // Identify status (usually last token)
                String status = parts[parts.length - 1].trim();
                if (!"Completed".equalsIgnoreCase(status)) continue;

                // Identify semester (one token before last)
                String semester = parts[parts.length - 2].trim();

                String subjectCode = parts[1].trim();
                String subjectName = parts[2].trim();

                // Find the numeric final grade: walk from (len-3) backward until numeric
                String finalGrade = "";
                for (int i = parts.length - 3; i >= 3; i--) {
                    String tok = parts[i].trim();
                    if (tok.isEmpty()) continue;
                    if (isNumeric(tok)) { finalGrade = tok; break; }
                }

                rows.add(new Object[]{semester, subjectCode, subjectName, finalGrade});
            }
        } catch (Exception ignored) {}
        if (rows.isEmpty()) {
            return new Object[][]{{"No completed courses", "", "", ""}};
        }
        return rows.toArray(new Object[0][]);
    }

    public static String[] transcriptColumns() {
        return new String[]{"Semester", "Subject", "Subject Name", "Final Grade"};
    }

    private static boolean isNumeric(String s) {
        try { Double.parseDouble(s); return true; } catch (Exception e) { return false; }
    }
}

