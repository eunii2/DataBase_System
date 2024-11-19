import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class hw5 {
    private static final String URL = "jdbc:mysql://192.168.0.18:4567/madang";
    private static final String USER = "choigaeun";
    private static final String PASSWORD = "7562";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("MySQL 데이터베이스에 성공적으로 연결되었습니다!");

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\n옵션을 선택하세요:");
                System.out.println("1: 데이터 삽입");
                System.out.println("2: 데이터 검색");
                System.out.println("3: 데이터 삭제");
                System.out.println("0: 종료");

                System.out.print("선택: ");
                int option = scanner.nextInt();

                switch (option) {
                    case 1:
                        insertData(conn, scanner);
                        break;
                    case 2:
                        searchData(conn);
                        break;
                    case 3:
                        deleteData(conn, scanner);
                        break;
                    case 0:
                        System.out.println("프로그램을 종료합니다.");
                        return;
                    default:
                        System.out.println("잘못된 입력입니다. 다시 시도하세요.");
                }
            }

        } catch (Exception e) {
            System.err.println("데이터베이스 작업 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void insertData(Connection conn, Scanner scanner) {
        System.out.println("\n데이터 삽입을 선택했습니다.");
        System.out.print("Book ID를 입력하세요: ");
        int bookid = scanner.nextInt();
        scanner.nextLine();

        System.out.print("책 이름을 입력하세요: ");
        String bookname = scanner.nextLine();

        System.out.print("출판사를 입력하세요: ");
        String publisher = scanner.nextLine();

        System.out.print("가격을 입력하세요: ");
        int price = scanner.nextInt();

        String insertQuery = "INSERT INTO Book (bookid, bookname, publisher, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            pstmt.setInt(1, bookid);
            pstmt.setString(2, bookname);
            pstmt.setString(3, publisher);
            pstmt.setInt(4, price);

            int rows = pstmt.executeUpdate();
            System.out.println(rows + "개의 데이터가 삽입되었습니다.");
        } catch (Exception e) {
            System.err.println("데이터 삽입 중 오류 발생: " + e.getMessage());
        }
    }

    private static void searchData(Connection conn) {
        System.out.println("\n데이터 검색을 선택했습니다.");
        String searchQuery = "SELECT * FROM Book";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(searchQuery)) {
            System.out.println("검색된 데이터:");
            while (rs.next()) {
                int bookid = rs.getInt("bookid");
                String bookname = rs.getString("bookname");
                String publisher = rs.getString("publisher");
                int price = rs.getInt("price");

                System.out.printf("ID: %d, 이름: %s, 출판사: %s, 가격: %d원\n", bookid, bookname, publisher, price);
            }
        } catch (Exception e) {
            System.err.println("데이터 검색 중 오류 발생: " + e.getMessage());
        }
    }

    private static void deleteData(Connection conn, Scanner scanner) {
        System.out.println("\n데이터 삭제를 선택했습니다.");
        System.out.print("삭제할 Book ID를 입력하세요: ");
        int bookid = scanner.nextInt();

        String deleteQuery = "DELETE FROM Book WHERE bookid = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
            pstmt.setInt(1, bookid);

            int rows = pstmt.executeUpdate();
            System.out.println(rows + "개의 데이터가 삭제되었습니다.");
        } catch (Exception e) {
            System.err.println("데이터 삭제 중 오류 발생: " + e.getMessage());
        }
    }
}