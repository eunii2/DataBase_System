import java.sql.*;
import java.util.Scanner;

public class ClubManagement {
    private static Connection conn = null;
    private static final String URL = "jdbc:mysql://192.168.30.2:4567/club";
    private static final String USER = "choigaeun";
    private static final String PASSWORD = "7562";

    public static void main(String[] args) {
        createConnection();
        if (conn == null) {
            return;
        }

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n========동아리 관리 프로그램========");
            System.out.println("1. User 관리");
            System.out.println("2. Club 관리");
            System.out.println("3. 예약 관리");
            System.out.println("99. 종료");
            System.out.print("선택 : ");
            String choice = sc.nextLine();

            if (choice.equals("1")) {
                userMenu(sc);
            } else if (choice.equals("2")) {
                clubMainMenu(sc);
            } else if (choice.equals("3")) {
                reservationMenu(sc);
            } else if (choice.equals("99")) {
                break;
            } else {
                System.out.println("잘못된 선택입니다.");
            }
        }

        closeConnection();
        sc.close();
    }

    private static void createConnection() {
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("DB 연결 성공!");
        } catch (SQLException e) {
            System.out.println("DB 연결 실패: " + e.getMessage());
        }
    }

    private static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("DB 연결 해제");
            } catch (SQLException e) {
                System.out.println("DB 연결 해제 실패: " + e.getMessage());
            }
        }
    }

    // ================== User 관리 ==================
    private static void userMenu(Scanner sc) {
        while (true) {
            System.out.println("\n===User 관리===");
            System.out.println("1. User 등록");
            System.out.println("2. User 조회");
            System.out.println("3. User 수정(Major)");
            System.out.println("4. User 삭제");
            System.out.println("9. 뒤로가기");
            System.out.print("선택: ");
            String choice = sc.nextLine();

            if (choice.equals("1")) {
                insertUser(sc);
            } else if (choice.equals("2")) {
                findUser(sc);
            } else if (choice.equals("3")) {
                updateUser(sc);
            } else if (choice.equals("4")) {
                deleteUser(sc);
            } else if (choice.equals("9")) {
                break;
            } else {
                System.out.println("잘못된 선택입니다.");
            }
        }
    }

    private static void insertUser(Scanner sc) {
        try {
            System.out.print("Student_ID: ");
            String studentId = sc.nextLine();
            System.out.print("Fname: ");
            String fname = sc.nextLine();
            System.out.print("Lname: ");
            String lname = sc.nextLine();
            System.out.print("Major: ");
            String major = sc.nextLine();
            System.out.print("Contact_info: ");
            String contact = sc.nextLine();
            System.out.print("Role: ");
            String role = sc.nextLine();
            System.out.print("Club_Name: ");
            String clubName = sc.nextLine();

            String sql = "INSERT INTO User(Student_ID, Fname, Lname, Major, Contact_info, Role, Club_Name) VALUES(?,?,?,?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, studentId);
            pstmt.setString(2, fname);
            pstmt.setString(3, lname);
            pstmt.setString(4, major);
            pstmt.setString(5, contact);
            pstmt.setString(6, role);
            pstmt.setString(7, clubName);

            int res = pstmt.executeUpdate();
            if (res > 0) System.out.println("User 등록 성공");
            else System.out.println("User 등록 실패");
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("등록 실패: " + e.getMessage());
        }
    }

    private static void findUser(Scanner sc) {
        try {
            System.out.print("조회할 Student_ID: ");
            String studentId = sc.nextLine();

            String sql = "SELECT * FROM User WHERE Student_ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("Student_ID: " + rs.getString("Student_ID"));
                System.out.println("Fname: " + rs.getString("Fname"));
                System.out.println("Lname: " + rs.getString("Lname"));
                System.out.println("Major: " + rs.getString("Major"));
                System.out.println("Contact_info: " + rs.getString("Contact_info"));
                System.out.println("Role: " + rs.getString("Role"));
                System.out.println("Club_Name: " + rs.getString("Club_Name"));
            } else {
                System.out.println("해당 User 없음");
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("조회 실패: " + e.getMessage());
        }
    }

    private static void updateUser(Scanner sc) {
        try {
            System.out.print("수정할 Student_ID: ");
            String studentId = sc.nextLine();
            System.out.print("수정할 Major: ");
            String newMajor = sc.nextLine();

            String sql = "UPDATE User SET Major = ? WHERE Student_ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newMajor);
            pstmt.setString(2, studentId);
            int res = pstmt.executeUpdate();
            if (res > 0) System.out.println("User 수정 성공");
            else System.out.println("수정 실패(학번 확인)");
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("수정 실패: " + e.getMessage());
        }
    }

    private static void deleteUser(Scanner sc) {
        try {
            System.out.print("삭제할 Student_ID: ");
            String studentId = sc.nextLine();

            String sql = "DELETE FROM User WHERE Student_ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, studentId);
            int res = pstmt.executeUpdate();
            if (res > 0) System.out.println("User 삭제 성공");
            else System.out.println("삭제 실패(학번 확인)");
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("삭제 실패: " + e.getMessage());
        }
    }

    // ================== Club 메인 메뉴 ==================
    private static void clubMainMenu(Scanner sc) {
        while (true) {
            System.out.println("\n===Club 관리===");
            System.out.println("1. Club CRUD (등록/조회/수정/삭제)");
            System.out.println("2. Club 선택 후 하위 엔티티 관리");
            System.out.println("9. 뒤로가기");
            System.out.print("선택: ");
            String choice = sc.nextLine();

            if (choice.equals("1")) {
                clubCrudMenu(sc);
            } else if (choice.equals("2")) {
                String chosenClub = chooseClub(sc);
                if (chosenClub != null) {
                    clubEntityMenu(sc, chosenClub);
                }
            } else if (choice.equals("9")) {
                break;
            } else {
                System.out.println("잘못된 선택입니다.");
            }
        }
    }

    private static void clubCrudMenu(Scanner sc) {
        while (true) {
            System.out.println("\n===Club CRUD===");
            System.out.println("1. Club 전체 조회");
            System.out.println("2. Club 단일 조회");
            System.out.println("3. Club 등록");
            System.out.println("4. Club 수정(Location)");
            System.out.println("5. Club 삭제");
            System.out.println("9. 뒤로가기");
            System.out.print("선택: ");
            String choice = sc.nextLine();

            if (choice.equals("1")) {
                listClubs();
            } else if (choice.equals("2")) {
                findClub(sc);
            } else if (choice.equals("3")) {
                insertClub(sc);
            } else if (choice.equals("4")) {
                updateClub(sc);
            } else if (choice.equals("5")) {
                deleteClub(sc);
            } else if (choice.equals("9")) {
                break;
            } else {
                System.out.println("잘못된 선택입니다.");
            }
        }
    }

    private static String chooseClub(Scanner sc) {
        String sql = "SELECT Club_Name FROM Club";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("\n===Club 목록===");
            rs.last();
            int count = rs.getRow();
            rs.beforeFirst();
            if (count == 0) {
                System.out.println("등록된 Club이 없습니다. Club CRUD 메뉴에서 Club을 먼저 등록해주세요.");
                System.out.println("9. 뒤로가기");
                String back = sc.nextLine();
                return null;
            } else {
                int index = 0;
                while (rs.next()) {
                    index++;
                    System.out.println(index + ". " + rs.getString("Club_Name"));
                }
                System.out.println("몇 번 Club을 선택하시겠습니까? (9. 뒤로가기)");
                String choice = sc.nextLine();
                if (choice.equals("9")) {
                    return null;
                }
                try {
                    int clubIndex = Integer.parseInt(choice);
                    if (clubIndex < 1 || clubIndex > count) {
                        System.out.println("잘못된 선택입니다.");
                        return null;
                    }
                    rs.beforeFirst();
                    int current = 0;
                    String selectedClub = null;
                    while (rs.next()) {
                        current++;
                        if (current == clubIndex) {
                            selectedClub = rs.getString("Club_Name");
                            break;
                        }
                    }
                    return selectedClub;
                } catch (NumberFormatException e) {
                    System.out.println("숫자를 입력해주세요.");
                    return null;
                }
            }
        } catch (SQLException e) {
            System.out.println("Club 조회 실패: " + e.getMessage());
            return null;
        }
    }

    private static void listClubs() {
        String sql = "SELECT * FROM Club";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("\n=== Club 전체 조회 ===");
            boolean exist = false;
            while (rs.next()) {
                exist = true;
                System.out.printf("Club_Name: %s, Location: %s, Club_Info: %s\n",
                        rs.getString("Club_Name"), rs.getString("Location"), rs.getString("Club_Info"));
            }
            if (!exist) {
                System.out.println("등록된 Club이 없습니다.");
            }
        } catch (SQLException e) {
            System.out.println("조회 실패: " + e.getMessage());
        }
    }

    private static void findClub(Scanner sc) {
        try {
            System.out.print("조회할 Club_Name: ");
            String clubName = sc.nextLine();
            String sql = "SELECT * FROM Club WHERE Club_Name = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, clubName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("Club_Name: " + rs.getString("Club_Name"));
                System.out.println("Location: " + rs.getString("Location"));
                System.out.println("Club_Info: " + rs.getString("Club_Info"));
            } else {
                System.out.println("해당 Club 없음");
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Club 조회 실패: " + e.getMessage());
        }
    }

    private static void insertClub(Scanner sc) {
        try {
            System.out.print("Club_Name: ");
            String clubName = sc.nextLine();
            System.out.print("Location: ");
            String location = sc.nextLine();
            System.out.print("Club_Info: ");
            String clubInfo = sc.nextLine();

            String sql = "INSERT INTO Club(Club_Name, Location, Club_Info) VALUES(?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, clubName);
            pstmt.setString(2, location);
            pstmt.setString(3, clubInfo);
            int res = pstmt.executeUpdate();
            if (res > 0) System.out.println("Club 등록 성공");
            else System.out.println("Club 등록 실패");
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Club 등록 실패: " + e.getMessage());
        }
    }

    private static void updateClub(Scanner sc) {
        try {
            System.out.print("수정할 Club_Name: ");
            String clubName = sc.nextLine();
            System.out.print("새로운 Location: ");
            String newLocation = sc.nextLine();

            String sql = "UPDATE Club SET Location = ? WHERE Club_Name = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newLocation);
            pstmt.setString(2, clubName);
            int res = pstmt.executeUpdate();
            if (res > 0) System.out.println("Club 수정 성공");
            else System.out.println("수정 실패(Club_Name 확인)");
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Club 수정 실패: " + e.getMessage());
        }
    }

    private static void deleteClub(Scanner sc) {
        try {
            System.out.print("삭제할 Club_Name: ");
            String clubName = sc.nextLine();

            String sql = "DELETE FROM Club WHERE Club_Name = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, clubName);
            int res = pstmt.executeUpdate();
            if (res > 0) System.out.println("Club 삭제 성공");
            else System.out.println("삭제 실패(Club_Name 확인)");
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Club 삭제 실패: " + e.getMessage());
        }
    }

    private static void clubEntityMenu(Scanner sc, String clubName) {
        while (true) {
            System.out.println("\n===Club 관련 엔티티 관리===");
            System.out.println("선택한 Club: " + clubName);
            System.out.println("1. Board");
            System.out.println("2. Notice/Event");
            System.out.println("3. Report");
            System.out.println("4. Budget");
            System.out.println("5. Study_Group");
            System.out.println("6. Chat");
            System.out.println("9. 뒤로가기");
            System.out.print("선택: ");
            String choice = sc.nextLine();

            if (choice.equals("1")) {
                boardMenu(sc, clubName);
            } else if (choice.equals("2")) {
                noticeEventMenu(sc, clubName);
            } else if (choice.equals("3")) {
                reportMenu(sc, clubName);
            } else if (choice.equals("4")) {
                budgetMenu(sc, clubName);
            } else if (choice.equals("5")) {
                studyGroupMenu(sc, clubName);
            } else if (choice.equals("6")) {
                chatMenu(sc, clubName);
            } else if (choice.equals("9")) {
                break;
            } else {
                System.out.println("잘못된 선택입니다.");
            }
        }
    }

    // Board
    private static void boardMenu(Scanner sc, String clubName) {
        while (true) {
            System.out.println("\n===Board 관리 (" + clubName + ")===");
            System.out.println("1. Board 등록");
            System.out.println("2. Board 조회");
            System.out.println("3. Board 수정(Board_Type)");
            System.out.println("4. Board 삭제");
            System.out.println("9. 뒤로가기");
            System.out.print("선택: ");
            String choice = sc.nextLine();

            if (choice.equals("1")) {
                insertBoard(sc, clubName);
            } else if (choice.equals("2")) {
                findBoard(sc);
            } else if (choice.equals("3")) {
                updateBoard(sc);
            } else if (choice.equals("4")) {
                deleteBoard(sc);
            } else if (choice.equals("9")) {
                break;
            } else {
                System.out.println("잘못된 선택입니다.");
            }
        }
    }

    private static void insertBoard(Scanner sc, String clubName) {
        try {
            System.out.print("Board_Name: ");
            String boardName = sc.nextLine();
            System.out.print("Board_Type: ");
            String boardType = sc.nextLine();

            String sql = "INSERT INTO Board(Board_Name, Board_Type, Club_Name) VALUES(?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, boardName);
            pstmt.setString(2, boardType);
            pstmt.setString(3, clubName);

            int res = pstmt.executeUpdate();
            if (res > 0) System.out.println("Board 등록 성공");
            else System.out.println("Board 등록 실패");
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Board 등록 실패: " + e.getMessage());
        }
    }

    private static void findBoard(Scanner sc) {
        try {
            System.out.print("조회할 Board_Name: ");
            String boardName = sc.nextLine();

            String sql = "SELECT * FROM Board WHERE Board_Name = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, boardName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("Board_Name: " + rs.getString("Board_Name"));
                System.out.println("Board_Type: " + rs.getString("Board_Type"));
                System.out.println("Club_Name: " + rs.getString("Club_Name"));
            } else {
                System.out.println("해당 Board 없음");
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Board 조회 실패: " + e.getMessage());
        }
    }

    private static void updateBoard(Scanner sc) {
        try {
            System.out.print("수정할 Board_Name: ");
            String boardName = sc.nextLine();
            System.out.print("새로운 Board_Type: ");
            String newType = sc.nextLine();

            String sql = "UPDATE Board SET Board_Type = ? WHERE Board_Name = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newType);
            pstmt.setString(2, boardName);
            int res = pstmt.executeUpdate();
            if (res > 0) System.out.println("Board 수정 성공");
            else System.out.println("수정 실패(Board_Name 확인)");
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Board 수정 실패: " + e.getMessage());
        }
    }

    private static void deleteBoard(Scanner sc) {
        try {
            System.out.print("삭제할 Board_Name: ");
            String boardName = sc.nextLine();

            String sql = "DELETE FROM Board WHERE Board_Name = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, boardName);
            int res = pstmt.executeUpdate();
            if (res > 0) System.out.println("Board 삭제 성공");
            else System.out.println("삭제 실패(Board_Name 확인)");
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Board 삭제 실패: " + e.getMessage());
        }
    }

    // Notice/Event
    private static void noticeEventMenu(Scanner sc, String clubName) {
        while (true) {
            System.out.println("\n===Notice/Event 관리 (" + clubName + ")===");
            System.out.println("1. Notice/Event 등록");
            System.out.println("2. Notice/Event 조회");
            System.out.println("3. Notice/Event 수정(Title)");
            System.out.println("4. Notice/Event 삭제");
            System.out.println("9. 뒤로가기");
            System.out.print("선택: ");
            String choice = sc.nextLine();

            if (choice.equals("1")) {
                insertNoticeEvent(sc);
            } else if (choice.equals("2")) {
                findNoticeEvent(sc);
            } else if (choice.equals("3")) {
                updateNoticeEvent(sc);
            } else if (choice.equals("4")) {
                deleteNoticeEvent(sc);
            } else if (choice.equals("9")) {
                break;
            } else {
                System.out.println("잘못된 선택입니다.");
            }
        }
    }

    private static void insertNoticeEvent(Scanner sc) {
        try {
            System.out.print("NoticeEvent_ID: ");
            int id = Integer.parseInt(sc.nextLine());
            System.out.print("Title: ");
            String title = sc.nextLine();
            System.out.print("Content: ");
            String content = sc.nextLine();
            System.out.print("Date(YYYY-MM-DD): ");
            String date = sc.nextLine();
            System.out.print("Board_Name: ");
            String boardName = sc.nextLine();

            String sql = "INSERT INTO Notice_Event(NoticeEvent_ID, Title, Content, Date, Board_Name) VALUES(?,?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setString(2, title);
            pstmt.setString(3, content);
            pstmt.setString(4, date);
            pstmt.setString(5, boardName);

            int res = pstmt.executeUpdate();
            if (res > 0) System.out.println("Notice/Event 등록 성공");
            else System.out.println("등록 실패");
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Notice/Event 등록 실패: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("ID는 숫자여야 합니다.");
        }
    }

    private static void findNoticeEvent(Scanner sc) {
        try {
            System.out.print("조회할 NoticeEvent_ID: ");
            int id = Integer.parseInt(sc.nextLine());

            String sql = "SELECT * FROM Notice_Event WHERE NoticeEvent_ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("NoticeEvent_ID: " + rs.getInt("NoticeEvent_ID"));
                System.out.println("Title: " + rs.getString("Title"));
                System.out.println("Content: " + rs.getString("Content"));
                System.out.println("Date: " + rs.getString("Date"));
                System.out.println("Board_Name: " + rs.getString("Board_Name"));
            } else {
                System.out.println("해당 Notice/Event 없음");
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("조회 실패: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("ID는 숫자여야 합니다.");
        }
    }

    private static void updateNoticeEvent(Scanner sc) {
        try {
            System.out.print("수정할 NoticeEvent_ID: ");
            int id = Integer.parseInt(sc.nextLine());
            System.out.print("새로운 Title: ");
            String newTitle = sc.nextLine();

            String sql = "UPDATE Notice_Event SET Title = ? WHERE NoticeEvent_ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newTitle);
            pstmt.setInt(2, id);
            int res = pstmt.executeUpdate();
            if (res > 0) System.out.println("Notice/Event 수정 성공");
            else System.out.println("수정 실패(ID 확인)");
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("수정 실패: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("ID는 숫자여야 합니다.");
        }
    }

    private static void deleteNoticeEvent(Scanner sc) {
        try {
            System.out.print("삭제할 NoticeEvent_ID: ");
            int id = Integer.parseInt(sc.nextLine());

            String sql = "DELETE FROM Notice_Event WHERE NoticeEvent_ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            int res = pstmt.executeUpdate();
            if (res > 0) System.out.println("삭제 성공");
            else System.out.println("삭제 실패(ID 확인)");
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("삭제 실패: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("ID는 숫자여야 합니다.");
        }
    }

    // Report
    private static void reportMenu(Scanner sc, String clubName) {
        while (true) {
            System.out.println("\n===Report 관리 (" + clubName + ")===");
            System.out.println("1. Report 등록");
            System.out.println("2. Report 조회");
            System.out.println("3. Report 수정(Activity_Statistics)");
            System.out.println("4. Report 삭제");
            System.out.println("9. 뒤로가기");
            System.out.print("선택: ");
            String choice = sc.nextLine();

            if (choice.equals("1")) {
                insertReport(sc, clubName);
            } else if (choice.equals("2")) {
                findReport(sc);
            } else if (choice.equals("3")) {
                updateReport(sc);
            } else if (choice.equals("4")) {
                deleteReport(sc);
            } else if (choice.equals("9")) {
                break;
            } else {
                System.out.println("잘못된 선택입니다.");
            }
        }
    }

    private static void insertReport(Scanner sc, String clubName) {
        try {
            System.out.print("Report_ID: ");
            int reportId = Integer.parseInt(sc.nextLine());
            System.out.print("Report_Date(YYYY-MM-DD): ");
            String reportDate = sc.nextLine();
            System.out.print("Activity_Statistics: ");
            String stats = sc.nextLine();

            String sql = "INSERT INTO Report(Report_ID, Report_Date, Activity_Statistics, Club_Name) VALUES(?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reportId);
            pstmt.setString(2, reportDate);
            pstmt.setString(3, stats);
            pstmt.setString(4, clubName);

            int res = pstmt.executeUpdate();
            if (res > 0) System.out.println("Report 등록 성공");
            else System.out.println("등록 실패");
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("등록 실패: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Report_ID는 숫자여야 합니다.");
        }
    }

    private static void findReport(Scanner sc) {
        try {
            System.out.print("조회할 Report_ID: ");
            int reportId = Integer.parseInt(sc.nextLine());

            String sql = "SELECT * FROM Report WHERE Report_ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reportId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("Report_ID: " + rs.getInt("Report_ID"));
                System.out.println("Report_Date: " + rs.getString("Report_Date"));
                System.out.println("Activity_Statistics: " + rs.getString("Activity_Statistics"));
                System.out.println("Club_Name: " + rs.getString("Club_Name"));
            } else {
                System.out.println("해당 Report 없음");
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("조회 실패: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Report_ID는 숫자여야 합니다.");
        }
    }

    private static void updateReport(Scanner sc) {
        try {
            System.out.print("수정할 Report_ID: ");
            int reportId = Integer.parseInt(sc.nextLine());
            System.out.print("새로운 Activity_Statistics: ");
            String newStats = sc.nextLine();

            String sql = "UPDATE Report SET Activity_Statistics = ? WHERE Report_ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newStats);
            pstmt.setInt(2, reportId);
            int res = pstmt.executeUpdate();
            if (res > 0) System.out.println("Report 수정 성공");
            else System.out.println("수정 실패(ID 확인)");
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("수정 실패: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Report_ID는 숫자여야 합니다.");
        }
    }

    private static void deleteReport(Scanner sc) {
        try {
            System.out.print("삭제할 Report_ID: ");
            int reportId = Integer.parseInt(sc.nextLine());

            String sql = "DELETE FROM Report WHERE Report_ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reportId);
            int res = pstmt.executeUpdate();
            if (res > 0) System.out.println("삭제 성공");
            else System.out.println("삭제 실패(ID 확인)");
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("삭제 실패: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Report_ID는 숫자여야 합니다.");
        }
    }

    // Budget
    private static void budgetMenu(Scanner sc, String clubName) {
        while (true) {
            System.out.println("\n===Budget 관리 (" + clubName + ")===");
            System.out.println("1. Budget 등록");
            System.out.println("2. Budget 조회");
            System.out.println("3. Budget 수정(Amount)");
            System.out.println("4. Budget 삭제");
            System.out.println("9. 뒤로가기");
            System.out.print("선택: ");
            String choice = sc.nextLine();

            if (choice.equals("1")) {
                insertBudget(sc, clubName);
            } else if (choice.equals("2")) {
                findBudget(sc);
            } else if (choice.equals("3")) {
                updateBudget(sc);
            } else if (choice.equals("4")) {
                deleteBudget(sc);
            } else if (choice.equals("9")) {
                break;
            } else {
                System.out.println("잘못된 선택입니다.");
            }
        }
    }

    private static void insertBudget(Scanner sc, String clubName) {
        try {
            System.out.print("Budget_ID: ");
            int budgetId = Integer.parseInt(sc.nextLine());
            System.out.print("Item: ");
            String item = sc.nextLine();
            System.out.print("Usage: ");
            String usage = sc.nextLine();
            System.out.print("Amount: ");
            double amount = Double.parseDouble(sc.nextLine());

            String sql = "INSERT INTO Budget(Budget_ID, Item, `Usage`, Amount, Club_Name) VALUES(?,?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, budgetId);
            pstmt.setString(2, item);
            pstmt.setString(3, usage);
            pstmt.setDouble(4, amount);
            pstmt.setString(5, clubName);

            int res = pstmt.executeUpdate();
            if (res > 0) System.out.println("Budget 등록 성공");
            else System.out.println("등록 실패");
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("등록 실패: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Budget_ID와 Amount는 숫자여야 합니다.");
        }
    }

    private static void findBudget(Scanner sc) {
        try {
            System.out.print("조회할 Budget_ID: ");
            int budgetId = Integer.parseInt(sc.nextLine());

            String sql = "SELECT * FROM Budget WHERE Budget_ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, budgetId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("Budget_ID: " + rs.getInt("Budget_ID"));
                System.out.println("Item: " + rs.getString("Item"));
                System.out.println("Usage: " + rs.getString("Usage"));
                System.out.println("Amount: " + rs.getDouble("Amount"));
                System.out.println("Club_Name: " + rs.getString("Club_Name"));
            } else {
                System.out.println("해당 Budget 없음");
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("조회 실패: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Budget_ID는 숫자여야 합니다.");
        }
    }

    private static void updateBudget(Scanner sc) {
        try {
            System.out.print("수정할 Budget_ID: ");
            int budgetId = Integer.parseInt(sc.nextLine());
            System.out.print("새로운 Amount: ");
            double newAmount = Double.parseDouble(sc.nextLine());

            String sql = "UPDATE Budget SET Amount = ? WHERE Budget_ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, newAmount);
            pstmt.setInt(2, budgetId);
            int res = pstmt.executeUpdate();
            if (res > 0) System.out.println("Budget 수정 성공");
            else System.out.println("수정 실패(ID 확인)");
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("수정 실패: " + e.getMessage());
        } catch (NumberFormatException e2) {
            System.out.println("Budget_ID와 Amount는 숫자여야 합니다.");
        }
    }

    private static void deleteBudget(Scanner sc) {
        try {
            System.out.print("삭제할 Budget_ID: ");
            int budgetId = Integer.parseInt(sc.nextLine());

            String sql = "DELETE FROM Budget WHERE Budget_ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, budgetId);
            int res = pstmt.executeUpdate();
            if (res > 0) System.out.println("Budget 삭제 성공");
            else System.out.println("삭제 실패(ID 확인)");
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("삭제 실패: " + e.getMessage());
        } catch (NumberFormatException e2) {
            System.out.println("Budget_ID는 숫자여야 합니다.");
        }
    }

    // Study_Group
    private static void studyGroupMenu(Scanner sc, String clubName) {
        while (true) {
            System.out.println("\n===Study_Group 관리 (" + clubName + ")===");
            System.out.println("1. Study_Group 등록");
            System.out.println("2. Study_Group 조회");
            System.out.println("3. Study_Group 수정(Activity)");
            System.out.println("4. Study_Group 삭제");
            System.out.println("9. 뒤로가기");
            System.out.print("선택: ");
            String choice = sc.nextLine();

            if (choice.equals("1")) {
                insertStudyGroup(sc);
            } else if (choice.equals("2")) {
                findStudyGroup(sc);
            } else if (choice.equals("3")) {
                updateStudyGroup(sc);
            } else if (choice.equals("4")) {
                deleteStudyGroup(sc);
            } else if (choice.equals("9")) {
                break;
            } else {
                System.out.println("잘못된 선택입니다.");
            }
        }
    }

    private static void insertStudyGroup(Scanner sc) {
        try {
            System.out.print("Group_Name: ");
            String groupName = sc.nextLine();
            System.out.print("Activity: ");
            String activity = sc.nextLine();
            System.out.print("Controller_ID(User.Student_ID): ");
            String controllerId = sc.nextLine();

            String sql = "INSERT INTO Study_Group(Group_Name, Activity, Controller_ID) VALUES(?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, groupName);
            pstmt.setString(2, activity);
            pstmt.setString(3, controllerId);
            int res = pstmt.executeUpdate();
            if (res > 0) System.out.println("Study_Group 등록 성공");
            else System.out.println("등록 실패");
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("등록 실패: " + e.getMessage());
        }
    }

    private static void findStudyGroup(Scanner sc) {
        try {
            System.out.print("조회할 Group_Name: ");
            String groupName = sc.nextLine();

            String sql = "SELECT * FROM Study_Group WHERE Group_Name = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, groupName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("Group_Name: " + rs.getString("Group_Name"));
                System.out.println("Activity: " + rs.getString("Activity"));
                System.out.println("Controller_ID: " + rs.getString("Controller_ID"));
            } else {
                System.out.println("해당 Study_Group 없음");
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("조회 실패: " + e.getMessage());
        }
    }

    private static void updateStudyGroup(Scanner sc) {
        try {
            System.out.print("수정할 Group_Name: ");
            String groupName = sc.nextLine();
            System.out.print("새로운 Activity: ");
            String newActivity = sc.nextLine();

            String sql = "UPDATE Study_Group SET Activity = ? WHERE Group_Name = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newActivity);
            pstmt.setString(2, groupName);
            int res = pstmt.executeUpdate();
            if (res > 0) System.out.println("Study_Group 수정 성공");
            else System.out.println("수정 실패(Group_Name 확인)");
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("수정 실패: " + e.getMessage());
        }
    }

    private static void deleteStudyGroup(Scanner sc) {
        try {
            System.out.print("삭제할 Group_Name: ");
            String groupName = sc.nextLine();

            String sql = "DELETE FROM Study_Group WHERE Group_Name = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, groupName);
            int res = pstmt.executeUpdate();
            if (res > 0) System.out.println("Study_Group 삭제 성공");
            else System.out.println("삭제 실패(Group_Name 확인)");
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("삭제 실패: " + e.getMessage());
        }
    }

    // Chat
    private static void chatMenu(Scanner sc, String clubName) {
        while (true) {
            System.out.println("\n===Chat 관리 (" + clubName + ")===");
            System.out.println("1. Chat 등록");
            System.out.println("2. Chat 조회");
            System.out.println("3. Chat 수정(Timestamp)");
            System.out.println("4. Chat 삭제");
            System.out.println("9. 뒤로가기");
            System.out.print("선택: ");
            String choice = sc.nextLine();

            if (choice.equals("1")) {
                insertChat(sc, clubName);
            } else if (choice.equals("2")) {
                findChat(sc);
            } else if (choice.equals("3")) {
                updateChat(sc);
            } else if (choice.equals("4")) {
                deleteChat(sc);
            } else if (choice.equals("9")) {
                break;
            } else {
                System.out.println("잘못된 선택입니다.");
            }
        }
    }

    private static void insertChat(Scanner sc, String clubName) {
        try {
            System.out.print("Chat_ID: ");
            int chatId = Integer.parseInt(sc.nextLine());
            System.out.print("Sender_ID(User.Student_ID): ");
            String senderId = sc.nextLine();
            System.out.print("Receiver_ID(User.Student_ID): ");
            String receiverId = sc.nextLine();
            System.out.print("Timestamp(YYYY-MM-DD HH:MM:SS): ");
            String timestamp = sc.nextLine();

            String sql = "INSERT INTO Chat(Chat_ID, Sender_ID, Receiver_ID, Timestamp, Club_Name) VALUES(?,?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, chatId);
            pstmt.setString(2, senderId);
            pstmt.setString(3, receiverId);
            pstmt.setString(4, timestamp);
            pstmt.setString(5, clubName);

            int res = pstmt.executeUpdate();
            if (res > 0) System.out.println("Chat 등록 성공");
            else System.out.println("등록 실패");
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("등록 실패: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Chat_ID는 숫자여야 합니다.");
        }
    }

    private static void findChat(Scanner sc) {
        try {
            System.out.print("조회할 Chat_ID: ");
            int chatId = Integer.parseInt(sc.nextLine());

            String sql = "SELECT * FROM Chat WHERE Chat_ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, chatId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("Chat_ID: " + rs.getInt("Chat_ID"));
                System.out.println("Sender_ID: " + rs.getString("Sender_ID"));
                System.out.println("Receiver_ID: " + rs.getString("Receiver_ID"));
                System.out.println("Timestamp: " + rs.getString("Timestamp"));
                System.out.println("Club_Name: " + rs.getString("Club_Name"));
            } else {
                System.out.println("해당 Chat 없음");
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("조회 실패: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Chat_ID는 숫자여야 합니다.");
        }
    }

    private static void updateChat(Scanner sc) {
        try {
            System.out.print("수정할 Chat_ID: ");
            int chatId = Integer.parseInt(sc.nextLine());
            System.out.print("새로운 Timestamp(YYYY-MM-DD HH:MM:SS): ");
            String newTimestamp = sc.nextLine();

            String sql = "UPDATE Chat SET Timestamp = ? WHERE Chat_ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newTimestamp);
            pstmt.setInt(2, chatId);
            int res = pstmt.executeUpdate();
            if (res > 0) System.out.println("Chat 수정 성공");
            else System.out.println("수정 실패(ID 확인)");
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("수정 실패: " + e.getMessage());
        } catch (NumberFormatException ex) {
            System.out.println("Chat_ID는 숫자여야 합니다.");
        }
    }

    private static void deleteChat(Scanner sc) {
        try {
            System.out.print("삭제할 Chat_ID: ");
            int chatId = Integer.parseInt(sc.nextLine());

            String sql = "DELETE FROM Chat WHERE Chat_ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, chatId);
            int res = pstmt.executeUpdate();
            if (res > 0) System.out.println("Chat 삭제 성공");
            else System.out.println("삭제 실패(ID 확인)");
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("삭제 실패: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Chat_ID는 숫자여야 합니다.");
        }
    }

    // Facility
    private static void facilityMenu(Scanner sc) {
        while (true) {
            System.out.println("\n===Facility 관리===");
            System.out.println("1. Facility 등록");
            System.out.println("2. Facility 조회");
            System.out.println("3. Facility 수정(Capacity)");
            System.out.println("4. Facility 삭제");
            System.out.println("9. 뒤로가기");
            System.out.print("선택: ");
            String choice = sc.nextLine();

            if (choice.equals("1")) {
                insertFacility(sc);
            } else if (choice.equals("2")) {
                findFacility(sc);
            } else if (choice.equals("3")) {
                updateFacility(sc);
            } else if (choice.equals("4")) {
                deleteFacility(sc);
            } else if (choice.equals("9")) {
                break;
            } else {
                System.out.println("잘못된 선택입니다.");
            }
        }
    }

    private static void insertFacility(Scanner sc) {
        try {
            System.out.print("Facility_ID: ");
            int facilityId = Integer.parseInt(sc.nextLine());
            System.out.print("Type: ");
            String type = sc.nextLine();
            System.out.print("Capacity: ");
            int capacity = Integer.parseInt(sc.nextLine());
            System.out.print("Availability_Status: ");
            String status = sc.nextLine();
            System.out.print("Location: ");
            String location = sc.nextLine();

            String sql = "INSERT INTO Facility(Facility_ID, Type, Capacity, Availability_Status, Location) VALUES(?,?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, facilityId);
            pstmt.setString(2, type);
            pstmt.setInt(3, capacity);
            pstmt.setString(4, status);
            pstmt.setString(5, location);
            int res = pstmt.executeUpdate();
            if (res > 0) System.out.println("Facility 등록 성공");
            else System.out.println("등록 실패");
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("등록 실패: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Facility_ID와 Capacity는 숫자여야 합니다.");
        }
    }

    private static void findFacility(Scanner sc) {
        try {
            System.out.print("조회할 Facility_ID: ");
            int facilityId = Integer.parseInt(sc.nextLine());

            String sql = "SELECT * FROM Facility WHERE Facility_ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, facilityId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("Facility_ID: " + rs.getInt("Facility_ID"));
                System.out.println("Type: " + rs.getString("Type"));
                System.out.println("Capacity: " + rs.getInt("Capacity"));
                System.out.println("Availability_Status: " + rs.getString("Availability_Status"));
                System.out.println("Location: " + rs.getString("Location"));
            } else {
                System.out.println("해당 Facility 없음");
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("조회 실패: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Facility_ID는 숫자여야 합니다.");
        }
    }

    private static void updateFacility(Scanner sc) {
        try {
            System.out.print("수정할 Facility_ID: ");
            int facilityId = Integer.parseInt(sc.nextLine());
            System.out.print("새로운 Capacity: ");
            int newCapacity = Integer.parseInt(sc.nextLine());

            String sql = "UPDATE Facility SET Capacity = ? WHERE Facility_ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, newCapacity);
            pstmt.setInt(2, facilityId);
            int res = pstmt.executeUpdate();
            if (res > 0) System.out.println("Facility 수정 성공");
            else System.out.println("수정 실패(ID 확인)");
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("수정 실패: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Facility_ID와 Capacity는 숫자여야 합니다.");
        }
    }

    private static void deleteFacility(Scanner sc) {
        try {
            System.out.print("삭제할 Facility_ID: ");
            int facilityId = Integer.parseInt(sc.nextLine());

            String sql = "DELETE FROM Facility WHERE Facility_ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, facilityId);
            int res = pstmt.executeUpdate();
            if (res > 0) System.out.println("Facility 삭제 성공");
            else System.out.println("삭제 실패(ID 확인)");
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("삭제 실패: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Facility_ID는 숫자여야 합니다.");
        }
    }

    // ================== 예약 관리 ==================
    private static void reservationMenu(Scanner sc) {
        while (true) {
            System.out.println("\n===예약 관리===");
            System.out.println("1. 시설 예약");
            System.out.println("2. 예약 취소");
            System.out.println("9. 뒤로가기");
            System.out.print("선택: ");
            String choice = sc.nextLine();

            if (choice.equals("1")) {
                reserveFacility(sc);
            } else if (choice.equals("2")) {
                cancelReservation(sc);
            } else if (choice.equals("9")) {
                break;
            } else {
                System.out.println("잘못된 선택입니다.");
            }
        }
    }

    private static void reserveFacility(Scanner sc) {
        try {
            System.out.print("예약할 Facility_ID: ");
            String facilityId = sc.nextLine();
            System.out.print("Student_ID: ");
            String studentId = sc.nextLine();
            System.out.print("Date(YYYY-MM-DD): ");
            String date = sc.nextLine();
            System.out.print("Start_time(HH:MM): ");
            String startTime = sc.nextLine();
            System.out.print("End_time(HH:MM): ");
            String endTime = sc.nextLine();
            String status = "Reserved";

            String sql = "INSERT INTO Reservation(Date, Start_time, End_time, Status, Student_ID, Facility_ID) VALUES(?,?,?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, date);
            pstmt.setString(2, startTime);
            pstmt.setString(3, endTime);
            pstmt.setString(4, status);
            pstmt.setString(5, studentId);
            pstmt.setString(6, facilityId);

            int res = pstmt.executeUpdate();
            if (res > 0) System.out.println("시설 예약 성공");
            else System.out.println("시설 예약 실패");
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("예약 실패: " + e.getMessage());
        }
    }

    private static void cancelReservation(Scanner sc) {
        try {
            System.out.print("취소할 Reservation_ID: ");
            String reservationId = sc.nextLine();

            String sql = "DELETE FROM Reservation WHERE Reservation_ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, reservationId);
            int res = pstmt.executeUpdate();
            if (res > 0) System.out.println("예약 취소 성공");
            else System.out.println("취소 실패(예약ID 확인)");
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("취소 실패: " + e.getMessage());
        }
    }

}
