import java.io.*;
import java.net.Socket;

class ClientManagerThread extends Thread {
    private Socket clientSocket;
    private String clientName;
    private BufferedReader input;
    private PrintWriter output;
    public ClientManagerThread(Socket socket) {
        this.clientSocket = socket;
        try {
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream(), true);
            // 클라이언트로부터 닉네임 입력 받기
            this.clientName = input.readLine();
            // 데이터베이스에 닉네임 저장
            Main.addNewClientName(clientName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    } @Override
    public void run() {
        try {
            String msg;
            while ((msg = input.readLine()) != null) {
            // 클라이언트와의 통신 처리 코드

            Main.addChatRecord(msg);
                // 데이터베이스에 채팅 기록을 추가
            }
        } catch (IOException e) {
            // 연결이 종료된 것으로 간주하고 클라이언트를 제거
            System.out.println("클라이언트 연결이 종료되었습니다.");
        } finally {
            if (clientSocket != null) {
                try {
                    Main.removeClientName(clientName);
                    // 연결 종료 시 데이터베이스에서 클라이언트 이름을 삭제
                    input.close();
                    // 종료된 클라이언트의 소켓 및 입출력 스트림을 닫음
                    output.close();
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}