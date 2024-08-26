import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeedbackDAO {
    private Connection connection;

    public FeedbackDAO(Connection connection) {
        this.connection = connection;
    }
    public void addFeedback(EmployeeFeedback feedback) throws SQLException {
        String query = "INSERT INTO feedback (employee_name, topic, feedback) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, feedback.getEmployeeName());
            statement.setString(2, feedback.getTopic());
            statement.setString(3, feedback.getFeedback());
            statement.executeUpdate();
        }
    }
    public List<EmployeeFeedback> getFeedbackByTopic(String topic) throws SQLException {
        List<EmployeeFeedback> feedbackList = new ArrayList<>();
        String query = "SELECT * FROM feedback WHERE LOWER(topic) = LOWER(?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, topic);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    EmployeeFeedback feedback = new EmployeeFeedback();
                    feedback.setId(resultSet.getInt("id"));
                    feedback.setEmployeeName(resultSet.getString("employee_name"));
                    feedback.setTopic(resultSet.getString("topic"));
                    feedback.setFeedback(resultSet.getString("feedback"));
                    feedbackList.add(feedback);
                }
            }
        }
        return feedbackList;
    }
    public List<EmployeeFeedback> getAllFeedback() throws SQLException {
        List<EmployeeFeedback> feedbackList = new ArrayList<>();
        String query = "SELECT * FROM feedback";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                EmployeeFeedback feedback = new EmployeeFeedback();
                feedback.setId(resultSet.getInt("id"));
                feedback.setEmployeeName(resultSet.getString("employee_name"));
                feedback.setTopic(resultSet.getString("topic"));
                feedback.setFeedback(resultSet.getString("feedback"));
                feedbackList.add(feedback);
            }
        }
        return feedbackList;
    }

}
