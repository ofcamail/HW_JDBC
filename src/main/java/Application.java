import api.EmployeeDAOImpl;
import model.City;
import model.Employee;
import java.sql.*;
public class Application {

        public static void main(String[] args) throws SQLException {
final String user = "postgres";
final String password = "Oguhug28";
        String url = "jdbc:postgresql://localhost:5432/skypro";
        City city1 = null;
        EmployeeDAOImpl employeeDAO = new EmployeeDAOImpl(DriverManager.getConnection(url, user, password));
        try(final Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM city WHERE city_id = (?);")){
                statement.setInt(1, 7);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()){
                        city1 = new City(resultSet.getInt("city_id"), resultSet.getString("city_name"));
                }
        }catch (SQLException e) {
                e.printStackTrace();
        }
        Employee employee1 = new Employee("Mama", "Mamina", "female", 30, city1);
        try (final Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM employee WHERE id = (?)")) {
                statement.setInt(1, 4);
                final ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                        String firstName = "Имя: " + resultSet.getString("first_name");
                        String lastName = "Фамилия: " + resultSet.getString("last_name");
                        String age = "Возраст: " + resultSet.getInt("age");
                        System.out.printf(firstName + " " + lastName + " " + age);
                }
        }catch (SQLException e) {
                e.printStackTrace();
        }
        employeeDAO.create(employee1);
        employeeDAO.getAll().forEach(System.out::println);
        employeeDAO.changeCityById(1, 2);
        System.out.println(employeeDAO.getById(4));
        employeeDAO.removeById(3);
}
}