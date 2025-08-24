/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

import java.util.Scanner;
import java.util.Random;

/**
 * Console application that simulates generating a simple usage report
 * for a user-specified period.
 *
 * <p>The program:</p>
 * <ol>
 *   <li>Prompts the user to enter a reporting period (free text, e.g. {@code 2024-01-01 to 2024-01-31}).</li>
 *   <li>Simulates data fetch/analysis by producing a random usage count.</li>
 *   <li>Builds a short, formatted report string and prints it to standard output.</li>
 * </ol>
 *
 * <p><strong>Notes:</strong></p>
 * <ul>
 *   <li>No date parsing/validation is performed; the period is used as a label only.</li>
 *   <li>Usage values are randomly generated for demonstration purposes.</li>
 *   <li>All I/O is via {@code System.in} and {@code System.out}.</li>
 * </ul>
 *
 * @author dylanchin
 */
public class GenerateUsageReport {

    /**
     * Entry point for the report generator CLI.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("=== Generate Usage Report ===");
        // Start
        System.out.println("Start");

        // Select Report Period
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter report period (e.g., '2024-01-01 to 2024-01-31'): ");
        String reportPeriod = scanner.nextLine();
        System.out.println("Selected Report Period: " + reportPeriod);

        // Fetch & Analyze Data
        System.out.println("Fetching and analyzing data...");
        // Simulate data fetching and analysis
        int usageCount = fetchAndAnalyzeData(reportPeriod);

        // Generate Report
        String report = generateReport(reportPeriod, usageCount);
        System.out.println("Report generated.");

        // Show Report
        showReport(report);

        // End
        System.out.println("End");
    }

    /**
     * Simulates fetching and analyzing usage data for the given period.
     * <p>This implementation returns a pseudo-random total usage count.</p>
     *
     * @param period human-readable period string provided by the user
     * @return an integer in the range {@code 50..199} (inclusive lower bound, exclusive upper bound of 200)
     */
    private static int fetchAndAnalyzeData(String period) {
        // Simulate data fetching and analysis (random usage count)
        Random rand = new Random();
        return 50 + rand.nextInt(150); // Random usage between 50 and 200
    }

    /**
     * Creates a simple, multi-line usage report string for the given period and usage total.
     *
     * @param period      human-readable period label to display in the report
     * @param usageCount  simulated total usage for the period
     * @return a formatted report string ready to be printed
     */
    private static String generateReport(String period, int usageCount) {
        // Generate a simple usage report as a string
        StringBuilder sb = new StringBuilder();
        sb.append("=== Usage Report ===\n");
        sb.append("Period: ").append(period).append("\n");
        sb.append("Total Usage: ").append(usageCount).append("\n");
        sb.append("====================\n");
        return sb.toString();
    }

    /**
     * Outputs the given report to standard output.
     *
     * @param report the report text to display
     */
    private static void showReport(String report) {
        System.out.println("\n" + report);
    }
}
