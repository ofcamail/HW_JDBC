package api;
import model.City;
import model.Employee;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class EmployeeDAOImpl implements EmployeeDAO {
    private Connection connection;

    public EmployeeDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Employee employee) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO employee (first_name, last_name, gender, age, city_id)" +
                "VALUES ((?), (?), (?), (?), (?))");) {
            statement.setString(1, employee.getFirstName());
            statement.setString(2, employee.getLastName());
            statement.setString(3, employee.getGender());
            statement.setInt(4, employee.getAge());
            statement.setInt(5, employee.getCity().getCityId());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Employee getById(int numId) {
        Employee employee = null;
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM employee INNER JOIN city ON employee.city_id = city.city_id AND id = (?)")) {
            statement.setInt(1, numId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                employee = new Employee(resultSet.getInt("id"),
                        resultSet.getString("first_name"), resultSet.getString("last_name"), resultSet.getString("gender")
                        , resultSet.getInt("age"), new City(resultSet.getInt("city_id"), resultSet.getString("city_name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employee;
    }

    @Override
    public List<Employee> getAll() {
        List<Employee> employees = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM employee INNER JOIN city ON city.city_id = employee.city_id ORDER BY id;")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                City city = new City(resultSet.getInt("city_id"), resultSet.getString("city_name"));
                employees.add(new Employee(id, firstName, lastName, gender, age, city));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    @Override
    public void changeCityById(int employeeId, int cityId) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE employee SET city_id = (?) WHERE id = (?)")) {
            statement.setInt(1, cityId);
            statement.setInt(2, employeeId);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeById(int employeeId) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE  FROM employee WHERE id = (?);")) {
            statement.setInt(1, employeeId);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}