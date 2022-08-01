package Lesson_7;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

import static java.sql.DriverManager.getConnection;

public class MainApp {
    public static void main(String[] args) throws IOException, InterruptedException, SQLException {
        RunApplication RunApplication = new RunApplication();
        RunApplication.getWeather();


        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection connection = getConnection("jdbc:sqlite:Lesson_7db.db");
             Statement statement = connection.createStatement()) {

            // Пример создания БД
            performCreateDB(statement);
            readStudentsFromDB(statement);


        }
    }

    private static void performCreateDB(Statement statement) throws SQLException {
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS weathers (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "City STRING, CurrentDay STRING, CurrentDayTemperature DOUBLE, CurrentDayConditions STRING);");
    }

    String insertWeatherQuery = "INSERT INTO weather (City, CurrentDay, CurrentDayTemperature, CurrentDayConditions) VALUES (?,?,?,?)";

//    @Override
    public boolean saveWeatherData(WeatherData weatherData) throws SQLException {
        try (Connection connection = getConnection("jdbc:sqlite:Lesson_7db.db");
             PreparedStatement saveWeather = connection.prepareStatement(insertWeatherQuery)) {
            saveWeather.setString(1, weatherData.getCity());
            saveWeather.setString(2, weatherData.getCurrentDay());
            saveWeather.setDouble(3, weatherData.getCurrentDayTemperature());
            saveWeather.setString(4, weatherData.getCurrentDayConditions());
            return saveWeather.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        throw new SQLException("Failure on saving weather object");
    }

    private static void readStudentsFromDB(Statement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT * FROM students");
        ArrayList<WeatherData> arrayList = new ArrayList<>();
        while (resultSet.next()) {
            System.out.println(
                    resultSet.getString("City") + " - " +
                            resultSet.getString("CurrentDay") + " - " +
                            resultSet.getDouble("CurrentDayTemperature") + " - " +
                            resultSet.getString("CurrentDayConditions")


            );
            arrayList.add(new WeatherData(resultSet.getString("City"),resultSet.getString("CurrentDay"),resultSet.getDouble("CurrentDayTemperature"),resultSet.getString("CurrentDayConditions")));
        }
        System.out.println("GGG");
    }








    public static class WeatherData {

        private Integer id;
        private String City;
        private String CurrentDay;
        private Double CurrentDayTemperature;
        private String CurrentDayConditions;

        public WeatherData(String city, String currentDay, double currentDayTemperature, String currentDayConditions) {
        }

        public WeatherData(Integer id, String City, String CurrentDay, Double CurrentDayTemperature, String CurrentDayConditions) {
            this.id = id;
            this.City = City;
            this.CurrentDay = CurrentDay;
            this.CurrentDayTemperature = CurrentDayTemperature;
            this.CurrentDayConditions = CurrentDayConditions;
        }
        public Integer getId() {
            return id;
        }

        public void setId(Integer  id) {
            this.id = id;
        }

        public String getCity() {
            return City;
        }

        public void setCity(String City) {
            this.City = City;
        }

        public String getCurrentDay() {
            return CurrentDay;
        }

        public void setCurrentDay(String CurrentDay) {
            this.CurrentDay = CurrentDay;
        }

        public Double getCurrentDayTemperature() {
            return CurrentDayTemperature;
        }

        public void setCurrentDayTemperature(Double CurrentDayTemperature) {
            this.CurrentDayTemperature = CurrentDayTemperature;
        }

        public String getCurrentDayConditions() {
            return CurrentDayConditions;
        }

        public void setCurrentDayConditions(String CurrentDayConditions) {
            this.CurrentDayConditions = CurrentDayConditions;
        }
    }
}