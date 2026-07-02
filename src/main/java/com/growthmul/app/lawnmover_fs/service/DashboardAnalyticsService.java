package com.growthmul.app.lawnmover_fs.service;

import com.growthmul.app.lawnmover_fs.dto.BookingDto;
import com.growthmul.app.lawnmover_fs.dto.DashboardSummaryDto;
import com.growthmul.app.lawnmover_fs.entity.BookingRequest;
import com.growthmul.app.lawnmover_fs.repository.BookingRepository;
import com.growthmul.app.lawnmover_fs.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.growthmul.app.lawnmover_fs.repository.ReviewRepository;

@Service
public class DashboardAnalyticsService {

    @Autowired private BookingRepository bookingRepo;
    @Autowired private ServiceRepository serviceRepo;
    @Autowired private ReviewRepository reviewRepo;

    // Prices are free-text ("Starting at $45 / visit", "$139") since admins
    // type them in as a display string, not a clean number. This pulls the
    // first dollar figure out of that string for revenue math. A price with
    // no parseable "$NN" in it just contributes 0 — better than crashing.
    private static final Pattern PRICE_PATTERN = Pattern.compile("\\$([0-9]+(?:\\.[0-9]{1,2})?)");

    private double priceFor(BookingRequest r) {
        if (r.getServiceOffering() == null) return 0.0;
        String text = r.getServiceOffering().getPrice();
        if (text == null) return 0.0;
        Matcher m = PRICE_PATTERN.matcher(text);
        if (!m.find()) return 0.0;
        try {
            return Double.parseDouble(m.group(1));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private String labelFor(BookingRequest r) {
        return r.getServiceOffering() != null ? r.getServiceOffering().getName() : "Unspecified";
    }

    public DashboardSummaryDto getSummary(Long companyId) {
        List<BookingRequest> requests = bookingRepo.findByCompanyIdOrderBySubmittedAtDesc(companyId);
        long totalRequests = bookingRepo.countByCompanyId(companyId);
        long pending = bookingRepo.findByCompanyIdAndCompleted(companyId, false).size();
        long completedCount = bookingRepo.findByCompanyIdAndCompleted(companyId, true).size();

        YearMonth currentYm = YearMonth.now();
        List<BookingRequest> completedList = requests.stream()
                .filter(BookingRequest::isCompleted)
                .collect(Collectors.toList());

        double totalRevenue = completedList.stream().mapToDouble(this::priceFor).sum();
        double monthRevenue = completedList.stream()
                .filter(r -> YearMonth.from(r.getSubmittedAt()).equals(currentYm))
                .mapToDouble(this::priceFor).sum();
        long monthBookings = requests.stream()
                .filter(r -> YearMonth.from(r.getSubmittedAt()).equals(currentYm))
                .count();

        List<DashboardSummaryDto.MonthlyChartEntry> monthlyChart = new ArrayList<>();
        for (int i = 5; i >= 0; i--) {
            YearMonth ym = currentYm.minusMonths(i);
            double rev = completedList.stream()
                    .filter(r -> YearMonth.from(r.getSubmittedAt()).equals(ym))
                    .mapToDouble(this::priceFor).sum();
            long count = requests.stream()
                    .filter(r -> YearMonth.from(r.getSubmittedAt()).equals(ym))
                    .count();
            monthlyChart.add(new DashboardSummaryDto.MonthlyChartEntry(
                    ym.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH), rev, count));
        }

        int thisYear = currentYm.getYear();
        double yearRevenue = completedList.stream()
                .filter(r -> r.getSubmittedAt().getYear() == thisYear)
                .mapToDouble(this::priceFor).sum();
        long yearBookings = requests.stream()
                .filter(r -> r.getSubmittedAt().getYear() == thisYear)
                .count();

        Map<String, Long> serviceBreakdown = requests.stream()
                .collect(Collectors.groupingBy(this::labelFor, LinkedHashMap::new, Collectors.counting()));

        long activeServiceCount = serviceRepo.findByCompanyIdAndTypeOrderBySortOrder(companyId, "service").size()
                + serviceRepo.findByCompanyIdAndTypeOrderBySortOrder(companyId, "plan").size()
                + serviceRepo.findByCompanyIdAndTypeOrderBySortOrder(companyId, "addon").size();
        double completionRate = totalRequests > 0 ? (completedCount * 100.0 / totalRequests) : 0;
        double avgJobValue = !completedList.isEmpty() ? (totalRevenue / completedList.size()) : 0;

        List<BookingDto> recentRequests = requests.subList(0, Math.min(6, requests.size()))
                .stream().map(BookingDto::from).toList();

        // ── Revenue by time window (for the client-side filter toggles) ──
        // All four windows computed here so the frontend can switch instantly
        // without another fetch call — the data is already in-hand.
        double rev1m  = revenueInMonths(completedList, 1);
        double rev3m  = revenueInMonths(completedList, 3);
        double rev6m  = revenueInMonths(completedList, 6);
        double rev12m = revenueInMonths(completedList, 12);

        long pendingReviewCount = reviewRepo.countByCompanyIdAndApprovedFalse(companyId);

        DashboardSummaryDto dto = new DashboardSummaryDto();
        dto.setTotalRequests(totalRequests);
        dto.setMonthBookings(monthBookings);
        dto.setPending(pending);
        dto.setCompleted(completedCount);
        dto.setTotalRevenue(totalRevenue);
        dto.setMonthRevenue(monthRevenue);
        dto.setActiveServiceCount(activeServiceCount);
        dto.setCompletionRate(completionRate);
        dto.setAvgJobValue(avgJobValue);
        dto.setYearRevenue(yearRevenue);
        dto.setYearBookings(yearBookings);
        dto.setCurrentYearLabel(String.valueOf(thisYear));
        dto.setMonthlyChart(monthlyChart);
        dto.setServiceBreakdown(serviceBreakdown);
        dto.setRecentRequests(recentRequests);
        dto.setRev1m(rev1m);
        dto.setRev3m(rev3m);
        dto.setRev6m(rev6m);
        dto.setRev12m(rev12m);
        dto.setPendingReviewCount(pendingReviewCount);
        return dto;
    }

    private double revenueInMonths(List<BookingRequest> completed, int months) {
        YearMonth cutoff = YearMonth.now().minusMonths(months - 1);
        return completed.stream()
                .filter(r -> !YearMonth.from(r.getSubmittedAt()).isBefore(cutoff))
                .mapToDouble(this::priceFor)
                .sum();
    }
}
