import controller.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Servlet extends HttpServlet {

    private Map<String, Command> commandsGet = new HashMap<>();
    private Map<String, Command> commandsPost = new HashMap<>();

    public void init() {

        commandsGet.put("", new MainPage());
        commandsGet.put("admin/orders", new AdminOrders());
        commandsGet.put("admin/rooms", new AdminRooms());
        commandsGet.put("login", new Login());
        commandsGet.put("logout", new LogOut());
        commandsGet.put("reservation", new Reservation());
        commandsGet.put("user/orders", new UserOrders());
        commandsGet.put("admin/remove", new AdminRemove());
        commandsGet.put("user/remove", new UserRemove());

        commandsPost.put("admin/orders", new AdminRoomsPost());
        commandsPost.put("login", new LoginPost());
        commandsPost.put("registration", new RegistrationPost());
        commandsPost.put("reservation", new ReservationPost());
        commandsPost.put("admin/setRoom", new AdminSetRoomPost());
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws IOException, ServletException {
        processRequest(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String, Command> commands = new HashMap<>();

        if (request.getMethod().equals("GET")) {
            commands = commandsGet;
        }
        if (request.getMethod().equals("POST")) {
            commands = commandsPost;
        }
        String path = request.getRequestURI();
        path = path.replaceAll(".*/app/", "");
        Command command = commands.getOrDefault(path,
                (r) -> "/WEB-INF/error.jsp");
        String page = command.execute(request);
        if (page.contains("redirect:")) {
            response.sendRedirect(page.replace("redirect:", "/app/"));
        } else {
            request.getRequestDispatcher(page).forward(request, response);
        }
    }

}
