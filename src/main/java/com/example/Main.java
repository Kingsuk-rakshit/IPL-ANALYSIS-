package com.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class Main extends ApplicationFrame {

    // Path to the CSV file
    private static final String CSV_FILE = "data/Ipl-clean-data2008-2023.csv";

    // Constructor to initialize the application
    public Main(String title) throws IOException {
        super(title);

        // Create the charts
        JFreeChart chart1 = createTopTeamsChart();
        JFreeChart chart2 = createMatchesPerSeasonChart();
        JFreeChart chart3 = createMatchDecisionMethodsPieChart();
        JFreeChart chart4 = createMatchesByLocationChart();
        JFreeChart chart5 = createTossDecisionOutcomeScatterPlot();

        // Panel to hold the charts
        JPanel chartPanel = new JPanel(new GridLayout(3, 2));
        chartPanel.add(new ChartPanel(chart1));
        chartPanel.add(new ChartPanel(chart2));
        chartPanel.add(new ChartPanel(chart3));
        chartPanel.add(new ChartPanel(chart4));
        chartPanel.add(new ChartPanel(chart5));

        // Set the panel size and add it to the frame
        chartPanel.setPreferredSize(new Dimension(1000, 800));
        setContentPane(chartPanel);
    }

    // Method to create dataset for top performing teams
    private DefaultCategoryDataset createTopTeamsDataset() throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Integer> teamWins = new HashMap<>();

        // Read the CSV file
        try (Reader reader = new FileReader(CSV_FILE);
             @SuppressWarnings("deprecation")
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            // Count the wins for each team
            for (CSVRecord record : csvParser) {
                String winner = record.get("winner");
                teamWins.put(winner, teamWins.getOrDefault(winner, 0) + 1);
            }
        }

        // Add the data to the dataset
        for (Map.Entry<String, Integer> entry : teamWins.entrySet()) {
            dataset.addValue(entry.getValue(), "Wins", entry.getKey());
        }

        return dataset;
    }

    // Method to create the bar chart for top performing teams
    private JFreeChart createTopTeamsChart() throws IOException {
        DefaultCategoryDataset dataset = createTopTeamsDataset();
        JFreeChart chart = ChartFactory.createBarChart(
                "Top Performing Teams",
                "Team",
                "Wins",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        // Customize the chart appearance
        BarRenderer renderer = new BarRenderer();
        renderer.setSeriesPaint(0, new Color(79, 129, 189));
        chart.getCategoryPlot().setRenderer(renderer);

        return chart;
    }

    // Method to create dataset for matches per season
    private DefaultCategoryDataset createMatchesPerSeasonDataset() throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Integer> matchesPerSeason = new HashMap<>();

        // Read the CSV file
        try (Reader reader = new FileReader(CSV_FILE);
             @SuppressWarnings("deprecation")
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            // Count the matches for each season
            for (CSVRecord record : csvParser) {
                String season = record.get("season");
                matchesPerSeason.put(season, matchesPerSeason.getOrDefault(season, 0) + 1);
            }
        }

        // Add the data to the dataset
        for (Map.Entry<String, Integer> entry : matchesPerSeason.entrySet()) {
            dataset.addValue(entry.getValue(), "Matches", entry.getKey());
        }

        return dataset;
    }

    // Method to create the bar chart for matches per season
    private JFreeChart createMatchesPerSeasonChart() throws IOException {
        DefaultCategoryDataset dataset = createMatchesPerSeasonDataset();
        JFreeChart chart = ChartFactory.createBarChart(
                "Matches per Season",
                "Season",
                "Matches",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        // Customize the chart appearance
        BarRenderer renderer = new BarRenderer();
        renderer.setSeriesPaint(0, new Color(155, 187, 89));
        chart.getCategoryPlot().setRenderer(renderer);

        return chart;
    }

    // Method to create dataset for match decision methods
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private DefaultPieDataset createMatchDecisionMethodsDataset() throws IOException {
        DefaultPieDataset dataset = new DefaultPieDataset();
        Map<String, Integer> decisionMethods = new HashMap<>();

        // Read the CSV file
        try (Reader reader = new FileReader(CSV_FILE);
             @SuppressWarnings("deprecation")
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            // Count the match decision methods
            for (CSVRecord record : csvParser) {
                String result = record.get("result");
                decisionMethods.put(result, decisionMethods.getOrDefault(result, 0) + 1);
            }
        }

        // Add the data to the dataset
        for (Map.Entry<String, Integer> entry : decisionMethods.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        return dataset;
    }

    // Method to create the pie chart for match decision methods
    private JFreeChart createMatchDecisionMethodsPieChart() throws IOException {
        @SuppressWarnings("rawtypes")
        DefaultPieDataset dataset = createMatchDecisionMethodsDataset();
        JFreeChart chart = ChartFactory.createPieChart(
                "Match Decision Methods",
                dataset,
                true, true, false
        );

        return chart;
    }

    // Method to create dataset for matches by location
    private DefaultCategoryDataset createMatchesByLocationDataset() throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Integer> matchesByLocation = new HashMap<>();

        // Read the CSV file
        try (Reader reader = new FileReader(CSV_FILE);
             @SuppressWarnings("deprecation")
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            // Count the matches for each location
            for (CSVRecord record : csvParser) {
                String venue = record.get("venue_name");
                matchesByLocation.put(venue, matchesByLocation.getOrDefault(venue, 0) + 1);
            }
        }

        // Add the data to the dataset
        for (Map.Entry<String, Integer> entry : matchesByLocation.entrySet()) {
            dataset.addValue(entry.getValue(), "Matches", entry.getKey());
        }

        return dataset;
    }

    // Method to create the bar chart for matches by location
    private JFreeChart createMatchesByLocationChart() throws IOException {
        DefaultCategoryDataset dataset = createMatchesByLocationDataset();
        JFreeChart chart = ChartFactory.createBarChart(
                "Matches by Location",
                "Location",
                "Matches",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        // Customize the chart appearance
        BarRenderer renderer = new BarRenderer();
        renderer.setSeriesPaint(0, new Color(192, 80, 77));
        CategoryAxis domainAxis = chart.getCategoryPlot().getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        return chart;
    }

    // Method to create dataset for toss decision vs match outcome
    private XYDataset createTossDecisionOutcomeDataset() throws IOException {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries homeSeries = new XYSeries("Home Team Won");
        XYSeries awaySeries = new XYSeries("Away Team Won");

        // Read the CSV file
        try (Reader reader = new FileReader(CSV_FILE);
             @SuppressWarnings("deprecation")
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            // Analyze the relationship between toss decision and match outcome
            for (CSVRecord record : csvParser) {
                String tossWinner = record.get("toss_won");
                String matchWinner = record.get("winner");
                if (tossWinner.equals(matchWinner)) {
                    homeSeries.add(1, 1);
                } else {
                    awaySeries.add(1, 0);
                }
            }
        }

        dataset.addSeries(homeSeries);
        dataset.addSeries(awaySeries);

        return dataset;
    }

    // Method to create the scatter plot for toss decision vs match outcome
    private JFreeChart createTossDecisionOutcomeScatterPlot() throws IOException {
        XYDataset dataset = createTossDecisionOutcomeDataset();
        JFreeChart chart = ChartFactory.createScatterPlot(
                "Toss Decision vs Match Outcome",
                "Toss Decision",
                "Match Outcome",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        // Customize the chart appearance
        XYPlot plot = chart.getXYPlot();
        XYItemRenderer renderer = plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(44, 102, 230, 255));
        renderer.setSeriesPaint(1, new Color(255, 161, 206, 255));
        ValueAxis domain = plot.getDomainAxis();
        domain.setRange(0.5, 1.5);
        ValueAxis range = plot.getRangeAxis();
        range.setRange(-0.5, 1.5);

        return chart;
    }

    // Main method to start the application
    public static void main(String[] args) {
        try {
            Main mainApp = new Main("IPL Data Analysis");
            mainApp.pack();
            RefineryUtilities.centerFrameOnScreen(mainApp);
            mainApp.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
