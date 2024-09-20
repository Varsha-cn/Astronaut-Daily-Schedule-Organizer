import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

// Task Class
class Task {
    private String description;
    private String startTime;
    private String endTime;
    private String priority;
    private boolean isCompleted;

    public Task(String description, String startTime, String endTime, String priority) {
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.priority = priority;
        this.isCompleted = false;
    }

    public String getDescription() {
        return description;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getPriority() {
        return priority;
    }

    public void markCompleted() {
        this.isCompleted = true;
    }

    @Override
    public String toString() {
        String status = isCompleted ? "Completed" : "Pending";
        return startTime + " - " + endTime + ": " + description + " [" + priority + "] (" + status + ")";
    }
}

// TaskFactory Class (Factory Pattern)
class TaskFactory {
    public static Task createTask(String description, String startTime, String endTime, String priority) {
        return new Task(description, startTime, endTime, priority);
    }
}

// ScheduleManager Class (Singleton Pattern)
class ScheduleManager {
    private static ScheduleManager instance;
    private List<Task> tasks;

    private ScheduleManager() {
        tasks = new ArrayList<>();
    }

    public static ScheduleManager getInstance() {
        if (instance == null) {
            instance = new ScheduleManager();
        }
        return instance;
    }

    // Add Task
    public void addTask(Task task) {
        if (checkConflict(task)) {
            System.out.println("Error: Task conflicts with an existing task.");
        } else {
            tasks.add(task);
            System.out.println("Task added successfully.");
        }
    }

    // Remove Task
    public void removeTask(String description) {
        for (Task task : tasks) {
            if (task.getDescription().equalsIgnoreCase(description)) {
                tasks.remove(task);
                System.out.println("Task removed successfully.");
                return;
            }
        }
        System.out.println("Error: Task not found.");
    }

    // View All Tasks
    public void viewTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks scheduled for the day.");
        } else {
            Collections.sort(tasks, Comparator.comparing(Task::getStartTime));
            for (Task task : tasks) {
                System.out.println(task);
            }
        }
    }

    // Check if the new task conflicts with existing tasks
    private boolean checkConflict(Task newTask) {
        for (Task task : tasks) {
            if (task.getStartTime().compareTo(newTask.getEndTime()) < 0 &&
                newTask.getStartTime().compareTo(task.getEndTime()) < 0) {
                System.out.println("Conflict with task: " + task.getDescription());
                return true;
            }
        }
        return false;
    }
}

// Main Application Class
public class TaskSchedulerApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ScheduleManager scheduleManager = ScheduleManager.getInstance();

        while (true) {
            System.out.println("\n1. Add Task");
            System.out.println("2. Remove Task");
            System.out.println("3. View Tasks");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Task Description: ");
                    String description = scanner.nextLine();
                    System.out.print("Start Time (HH:MM): ");
                    String startTime = scanner.nextLine();
                    System.out.print("End Time (HH:MM): ");
                    String endTime = scanner.nextLine();
                    System.out.print("Priority (High/Medium/Low): ");
                    String priority = scanner.nextLine();
                    Task task = TaskFactory.createTask(description, startTime, endTime, priority);
                    scheduleManager.addTask(task);
                    break;

                case "2":
                    System.out.print("Enter task description to remove: ");
                    String taskToRemove = scanner.nextLine();
                    scheduleManager.removeTask(taskToRemove);
                    break;

                case "3":
                    scheduleManager.viewTasks();
                    break;

                case "4":
                    System.out.println("Exiting...");
                    scanner.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid option. Try again.");
                    break;
            }
        }
    }
}
