import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lotrik on 07.01.2017.
 */
public class HelloWorldServer {

    static String clientSentence;
    static String capitalizedSentence;
    static String myDriver = "com.mysql.jdbc.Driver";
    static String myUrl = "jdbc:mysql://10.8.0.1:3306/kulinar_pp_ua_dr";
    static String query = "select `nid`,`title`,`body_value` from `node` inner join `field_data_body` on " +
            "(`node`.`nid` = `field_data_body`.`entity_id`) where `node`.`uid`=17;";
    static List<ResponseModel> responseModelList = new ArrayList<>();
    static ResponseModel responseModel = new ResponseModel();

    public static void main(String[] args) throws Exception{

        Class.forName(myDriver);
        Connection conn = DriverManager.getConnection(myUrl, "kulinar_dr", "yander");
        Statement st = conn.createStatement();

        ResultSet rs = st.executeQuery(query);

        List<Integer> nids = new ArrayList<>();
        List<String> gsonList = new ArrayList<>();
        Gson gson = new Gson();

        while (rs.next())
        {
            responseModel.setNid(rs.getInt("nid"));
            responseModel.setTitle(rs.getString("title"));
            responseModel.setBody_value(rs.getString("body_value"));
            gsonList.add(gson.toJson(responseModel));
            responseModelList.add(responseModel);
        }

        Collections.shuffle(responseModelList);

        System.out.println(gsonList);

        conn.close();
        st.close();
        rs.close();

        ServerSocket welcomeSocket = new ServerSocket(10700);

        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            if (connectionSocket.isConnected()) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        connection(connectionSocket, gsonList);
                    }
                });
                thread.start();
            }
        }
    }

//

    private static void connection (Socket socket, List<String> gsonList) {
        try {
//            BufferedReader inFromClient =
//                    new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
//            clientSentence = inFromClient.readLine();
//            System.out.println("Received: " + clientSentence);
//            capitalizedSentence = clientSentence.toUpperCase();
            outToClient.writeBytes(gsonList.toString());
            outToClient.flush();
            socket.close();
        } catch (Exception ex){

        }
    }

}
