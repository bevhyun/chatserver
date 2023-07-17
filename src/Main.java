import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {
    public static ArrayList<PrintWriter> OutputList;
    public static Connection connection;
    public static void main(String[] args) {
        OutputList = new ArrayList<>();
        // DB 연결 설정

        String server = "localhost"; // MySQL 서버 주소
        String database = "datalk"; // MySQL DATABASE 이름
        String user = "root"; // MySQL 서버 아이디
        String password = "0000"; // MySQL 서버 비밀번호



        // MySQL 데이터베이스에 연결하는 자바 애플리케이션 코드
        try {
            // JDBC 드라이버 로딩
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 데이터베이스 연결 - 데이터베이스 서버, 데이터베이스 이름, 사용자 이름 및 암호를 제공
            connection = DriverManager.getConnection("jdbc:mysql://" + server + "/" + database, user, password);
            System.out.println("데이터베이스가 정상적으로 연결되었습니다.");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("데이터베이스 연결 오류: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        System.out.println("서버가 시작되었습니다. 클라이언트의 연결 요청을 기다리고 있습니다...");
        try {
            ServerSocket serversocket = new ServerSocket(8000);
            while (true) {
                Socket clientsocket = serversocket.accept();
                System.out.println("[클라이언트 연결]");

                PrintWriter writer = new PrintWriter(clientsocket.getOutputStream(), true);
                OutputList.add(writer);
                System.out.println(OutputList.size());

                ClientManagerThread thread = new ClientManagerThread(clientsocket);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    } public static void addNewClientName(String clientName) {   // 클라이언트 이름을 삽입하는 메서드
        try {
            String insertQuery = "INSERT INTO client(name) VALUES (?)";
            // 클라이언트의 이름을 삽입하는 쿼리
            PreparedStatement statement = connection.prepareStatement(insertQuery);
            // PreparedStatement 생성
            statement.setString(1, clientName);
            // 첫번째 매개 변수에 클라이언트 이름을 설정

            statement.executeUpdate();
            // 데이터 삽입

            statement.close();
            // 자원 해제
        } catch (SQLException e) {
            System.err.println("데이터베이스 쿼리 실행 오류: " + e.getMessage());
            // 데이터 삽입 오류 시 에러 메세지 출력
            e.printStackTrace();
        }
    } public static void removeClientName(String clientName) {   // 클라이언트 이름을 삭제하는 메서드
        try {
            String deleteQuery = "DELETE FROM client WHERE name = ?";
            // 이름을 삭제하는 쿼리
            PreparedStatement statement = connection.prepareStatement(deleteQuery);
            // PreparedStatement 생성
            statement.setString(1, clientName);
            // // 첫 번째 매개 변수에 클라이언트 이름 설정

            statement.executeUpdate();
            // 데이터 삭제

            statement.close();
            // 자원 해제
        } catch (SQLException e) {
            System.err.println("데이터베이스 쿼리 실행 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void addChatRecord(String message) {
        try {
            String insertQuery = "INSERT INTO record(talking) VALUES (?)";
            // 연결 종료 시 데이터베이스에서 클라이언트 이름을 삭제
            PreparedStatement statement = connection.prepareStatement(insertQuery);
            // 연결 종료 시 데이터베이스에서 클라이언트 이름을 삭제
            statement.setString(1, message);
            // 연결 종료 시 데이터베이스에서 클라이언트 이름을 삭제

            statement.executeUpdate();

            statement.close();
        } catch (SQLException e) {
            System.err.println("데이터베이스 쿼리 실행 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }

}