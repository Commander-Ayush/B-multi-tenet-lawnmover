package com.growthmul.app.lawnmover_fs.controller;

import com.growthmul.app.lawnmover_fs.dto.ReviewDto;
import com.growthmul.app.lawnmover_fs.dto.ReviewSubmitRequest;
import com.growthmul.app.lawnmover_fs.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired private ReviewService reviewService;

    @GetMapping
    public List<ReviewDto> getApproved(@RequestHeader(value = "Origin", required = false) String origin) {
        return reviewService.getApprovedReviews(origin);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void submit(@RequestHeader(value = "Origin", required = false) String origin,
                       @RequestBody ReviewSubmitRequest req) {
        reviewService.submitReview(origin, req);
    }
}
