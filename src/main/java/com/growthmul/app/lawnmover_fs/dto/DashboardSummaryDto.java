package com.growthmul.app.lawnmover_fs.dto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DashboardSummaryDto {
    private long totalRequests;
    private long monthBookings;
    private long pending;
    private long completed;
    private double totalRevenue;
    private double monthRevenue;
    private long activeServiceCount;
    private double completionRate;
    private double avgJobValue;
    private double yearRevenue;
    private long yearBookings;
    private String currentYearLabel;
    private List<MonthlyChartEntry> monthlyChart;
    private Map<String, Long> serviceBreakdown = new LinkedHashMap<>();
    private List<BookingDto> recentRequests;
    // Revenue for the four time-window filter buttons on the sales panel
    private double rev1m;
    private double rev3m;
    private double rev6m;
    private double rev12m;
    // Badge count for the Reviews panel nav link
    private long pendingReviewCount;

    public long getTotalRequests() { return totalRequests; }
    public void setTotalRequests(long totalRequests) { this.totalRequests = totalRequests; }
    public long getMonthBookings() { return monthBookings; }
    public void setMonthBookings(long monthBookings) { this.monthBookings = monthBookings; }
    public long getPending() { return pending; }
    public void setPending(long pending) { this.pending = pending; }
    public long getCompleted() { return completed; }
    public void setCompleted(long completed) { this.completed = completed; }
    public double getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; }
    public double getMonthRevenue() { return monthRevenue; }
    public void setMonthRevenue(double monthRevenue) { this.monthRevenue = monthRevenue; }
    public long getActiveServiceCount() { return activeServiceCount; }
    public void setActiveServiceCount(long activeServiceCount) { this.activeServiceCount = activeServiceCount; }
    public double getCompletionRate() { return completionRate; }
    public void setCompletionRate(double completionRate) { this.completionRate = completionRate; }
    public double getAvgJobValue() { return avgJobValue; }
    public void setAvgJobValue(double avgJobValue) { this.avgJobValue = avgJobValue; }
    public double getYearRevenue() { return yearRevenue; }
    public void setYearRevenue(double yearRevenue) { this.yearRevenue = yearRevenue; }
    public long getYearBookings() { return yearBookings; }
    public void setYearBookings(long yearBookings) { this.yearBookings = yearBookings; }
    public String getCurrentYearLabel() { return currentYearLabel; }
    public void setCurrentYearLabel(String currentYearLabel) { this.currentYearLabel = currentYearLabel; }
    public List<MonthlyChartEntry> getMonthlyChart() { return monthlyChart; }
    public void setMonthlyChart(List<MonthlyChartEntry> monthlyChart) { this.monthlyChart = monthlyChart; }
    public Map<String, Long> getServiceBreakdown() { return serviceBreakdown; }
    public void setServiceBreakdown(Map<String, Long> serviceBreakdown) { this.serviceBreakdown = serviceBreakdown; }
    public List<BookingDto> getRecentRequests() { return recentRequests; }
    public void setRecentRequests(List<BookingDto> recentRequests) { this.recentRequests = recentRequests; }
    public double getRev1m() { return rev1m; }
    public void setRev1m(double rev1m) { this.rev1m = rev1m; }
    public double getRev3m() { return rev3m; }
    public void setRev3m(double rev3m) { this.rev3m = rev3m; }
    public double getRev6m() { return rev6m; }
    public void setRev6m(double rev6m) { this.rev6m = rev6m; }
    public double getRev12m() { return rev12m; }
    public void setRev12m(double rev12m) { this.rev12m = rev12m; }
    public long getPendingReviewCount() { return pendingReviewCount; }
    public void setPendingReviewCount(long pendingReviewCount) { this.pendingReviewCount = pendingReviewCount; }

    public static class MonthlyChartEntry {
        private String label;
        private double revenue;
        private long count;

        public MonthlyChartEntry() {}
        public MonthlyChartEntry(String label, double revenue, long count) {
            this.label = label; this.revenue = revenue; this.count = count;
        }

        public String getLabel() { return label; }
        public void setLabel(String label) { this.label = label; }
        public double getRevenue() { return revenue; }
        public void setRevenue(double revenue) { this.revenue = revenue; }
        public long getCount() { return count; }
        public void setCount(long count) { this.count = count; }
    }
}
