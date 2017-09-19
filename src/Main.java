import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;


public class Main {

    public static void main(String[] args) throws Throwable {
        ServerSocket ss = new ServerSocket(8080);

        while (true) {
            Socket s = ss.accept();

            System.err.println("Client accepted");
            new Thread(new SocketProcessor(s)).start();
        }
    }

    private static class SocketProcessor implements Runnable {

        private Socket s;
        private InputStream is;
        private OutputStream os;
        Document doc;
        DayWeek dw = new DayWeek();
        String[] dayDate = new String[6];
        Parser parser = new Parser();
        Subject[][] subjects = new Subject[6][6];
        String[] days = {"Понедельник","Вторник","Среда","Четверг","Пятница","Суббота"};
        String[] times = {"9:00-10:35","10:45-12:20","13:00-14:35","14:45-16:20","16:30-18:05"};


        private SocketProcessor(Socket s) throws Throwable {
            this.s = s;
            this.is = s.getInputStream();
            this.os = s.getOutputStream();
        }

        public void run() {
            try {
                readInputHeaders();

                compose();
                fillSubject();
                normolizeSubjects(parser.parse(doc));
                getDayDate();
                String str = makehtml();
                writeResponse(str);


            } catch (Throwable t) {
                /*do nothing*/
                t.printStackTrace();
            } finally {
                try {
                    s.close();
                } catch (Throwable t) {
                    /*do nothing*/
                }
            }
            System.err.println("Client processing finished");
        }

        private void compose(){

            try{
                doc = getHtml("daniilhacker@mail.ru", "199617");

            }catch (IOException e){

            }

        }

        private void normolizeSubjects(Subject[][] tmp){
            for (int i = 0; i < subjects.length; i++) {
                for (int j = 0; j < tmp[i].length; j++) {
                    if(tmp[i][j] == null) continue;
                    subjects[i][Integer.parseInt(tmp[i][j].getTime().charAt(0)+"")-1]= tmp[i][j];
                    /*
                    if(subjects[i][j].getTime().charAt(0)!=j){

                    }*/
                }
            }
        }

        private void fillSubject(){
            for (int i = 0; i < subjects.length; i++) {
                for (int j = 0; j < subjects[i].length; j++) {
                    subjects[i][j] = new Subject();
                }
            }
        }

        private  String makehtml(){
            String result = Constants.style;
            result+=" \t\t<td class=\"white\">"+ parser.parseWeek(doc)+" неделя</td>\n";

            for (int i = 0; i < dayDate.length; i++) {
                result+="<td ><div class=\"date\">"+dayDate[i]+"</div><div class=\"white\">"+days[i]+"</div></td>\n";
            }

            for (int d = 0; d < subjects.length-1 ; d++) {
                result+="<tr>";

                for (int i = 0; i < subjects[d].length; i++) {
                    if(i==0){
                        result+="<td class=\"white\">"+times[d]+"</td>";
                    }
                    if(subjects[i][d] == null || subjects[i][d].getName() == null) {
                        result+="<td></td>";
                        continue;
                    }

                    result+="<td>";
                    result += subjects[i][d].toHtmlTable();
                    result+="</td>";

                }
                result+="</tr>";
            }

            result+="</table>\n"+" </body>\n" + "</html>";

            return result;
        }
        private void getDayDate(){
            for (int i = 0; i < dayDate.length; i++) {
                dayDate[i]=dw.getDate(false,i);
            }
        }
        private Map<String,String> getMidenCookies() throws IOException {

            //get cookies
            Connection.Response res = Jsoup.connect("https://cabs.itut.ru/cabinet/")
                    .referrer("http://www.google.com")
                    .method(Connection.Method.GET)
                    .timeout(0)
                    .execute();

            Map<String,String> midenCookies = res.cookies();

            return midenCookies;
        }

        private Document getHtml(String login,String pass) throws IOException{

            Map<String,String> cookies = getMidenCookies();

            //push login
            Jsoup.connect("https://cabs.itut.ru/cabinet/lib/updatesession.php?key=users&value="+login)
                    .cookies(cookies)
                    .userAgent("Mozilla")
                    .method(Connection.Method.POST)
                    .execute();

            //push password
            Jsoup.connect("https://cabs.itut.ru/cabinet/lib/updatesession.php?key=parole&value="+pass)
                    .cookies(cookies)
                    .userAgent("Mozilla")
                    .method(Connection.Method.POST)
                    .execute();

            //check for correct login and password
            Jsoup.connect("https://cabs.itut.ru/cabinet/lib/autentification.php")
                    .cookies(cookies)
                    .userAgent("Mozilla")
                    .method(Connection.Method.POST)
                    .execute();

            //log in to cabinet
            Jsoup.connect("https://cabs.itut.ru/cabinet/?login=yes")
                    .cookies(cookies)
                    .userAgent("Mozilla")
                    .method(Connection.Method.POST)
                    .get();

            //get schedule html
            return Jsoup.connect("https://cabs.itut.ru/cabinet/project/cabinet/forms/raspisanie.php")
                    .cookies(cookies)
                    .userAgent("Mozilla")
                    .method(Connection.Method.GET)
                    .get();
        }


        private void writeResponse(String s) throws Throwable {
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Server: Server/2017-09-18\r\n" +
                    "Content-Type: text/html\r\n" +
                    //"Content-Length: " + s.length() + "\r\n" +
                    "Connection: close\r\n\r\n";
            String result = response + s;
            os.write(result.getBytes());
            os.flush();

        }

        private void readInputHeaders() throws Throwable {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while(true) {
                String s = br.readLine();
                if(s == null || s.trim().length() == 0) {
                    break;
                }
            }
        }
    }
}
