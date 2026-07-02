package com.growthmul.app.lawnmover_fs.dto;

import com.growthmul.app.lawnmover_fs.entity.Review;
import java.time.LocalDateTime;

public class ReviewDto {
    private Long id;
    private String reviewerName;
    private String reviewerCity;
    private int stars;
    private String text;
    private boolean approved;
    private LocalDateTime submittedAt;

    public static ReviewDto from(Review r) {
        ReviewDto dto = new ReviewDto();
        dto.id = r.getId();
        dto.reviewerName = r.getReviewerName();
        dto.reviewerCity = r.getReviewerCity();
        dto.stars = r.getStars();
        dto.text = r.getText();
        dto.approved = r.isApproved();
        dto.submittedAt = r.getSubmittedAt();
        return dto;
    }

    public Long getId() { return id; }
    public String getReviewerName() { return reviewerName; }
    public String getReviewerCity() { return reviewerCity; }
    public int getStars() { return stars; }
    public String getText() { return text; }
    public boolean isApproved() { return approved; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
}
