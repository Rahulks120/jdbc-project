import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class FeedbackApp {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/feedbackDB";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "rahul";
    public static void main(String[] args) {

        Connection connection = null;
        Scanner scanner = null;
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            FeedbackDAO feedbackDAO = new FeedbackDAO(connection);
            scanner = new Scanner(System.in);


            while (true) {
                System.out.println("1. Add Feedback");
                System.out.println("2. View All Feedback");
                System.out.println("3. Export Feedback to CSV");
                System.out.println("4. View Feedback by Topic");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        System.out.print("Enter your name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter presentation topic: ");
                        String topic = scanner.nextLine();
                        System.out.print("Enter your feedback: ");
                        String feedback = scanner.nextLine();

                        EmployeeFeedback employeeFeedback = new EmployeeFeedback();
                        employeeFeedback.setEmployeeName(name);
                        employeeFeedback.setTopic(topic);
                        employeeFeedback.setFeedback(feedback);

                        feedbackDAO.addFeedback(employeeFeedback);
                        System.out.println("Feedback submitted successfully!");
                        break;
                    case 2:
                        List<EmployeeFeedback> feedbackList = feedbackDAO.getAllFeedback();
                        for (EmployeeFeedback fb : feedbackList) {
                            System.out.println("Name: " + fb.getEmployeeName());
                            System.out.println("Topic: " + fb.getTopic());
                            System.out.println("Feedback: " + fb.getFeedback());
                            System.out.println("--------------------------");
                        }
                        break;
                    case 3:
                        List<EmployeeFeedback> allFeedbackList = feedbackDAO.getAllFeedback();
                        exportFeedbackToCSV(allFeedbackList);
                        System.out.println("Feedback exported successfully to Downloads folder!");
                        break;
                    case 4:
                        System.out.print("Enter topic to search: ");
                        String searchTopic = scanner.nextLine();
                        List<EmployeeFeedback> topicFeedbackList = feedbackDAO.getFeedbackByTopic(searchTopic);
                        System.out.println("Number of reviews for topic \"" + searchTopic + "\": " + topicFeedbackList.size());
                        for (EmployeeFeedback fb : topicFeedbackList) {
                            System.out.println("Name: " + fb.getEmployeeName());
                            System.out.println("Feedback: " + fb.getFeedback());
                            System.out.println("--------------------------");
                        }
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (SQLException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
        finally {
            if (scanner != null) {
                scanner.close();
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("Failed to close the connection: " + e.getMessage());
                }
            }
        }
    }


    private static void exportFeedbackToCSV(List<EmployeeFeedback> feedbackList) {
        String userHome = System.getProperty("user.home");
        String downloadsPath = userHome + File.separator + "Downloads" + File.separator;
        String filename = "feedback.csv";
        File file = new File(downloadsPath + filename);

        try (FileWriter writer = new FileWriter(file)) {
            writer.append("NAME,TOPIC,FEEDBACK\n");
            for (EmployeeFeedback fb : feedbackList) {
                writer.append(fb.getEmployeeName()).append(",");
                writer.append(fb.getTopic()).append(",");
                writer.append(fb.getFeedback()).append("\n");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the CSV file: " + e.getMessage());
        }
    }
}
