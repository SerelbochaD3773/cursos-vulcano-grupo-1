package com.grupo1.cursosvulcano.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grupo1.cursosvulcano.model.entity.Module;
import com.grupo1.cursosvulcano.model.entity.Review;
import com.grupo1.cursosvulcano.model.entity.User;
import com.grupo1.cursosvulcano.repository.ModuleRepository;
import com.grupo1.cursosvulcano.repository.ReviewRepository;
import com.grupo1.cursosvulcano.repository.UserRepository;


@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Review getReviewById(Long id) {
        return reviewRepository.findById(id).orElse(null);
    }

    public Review saveReview(Review review) {
        review.setUser(resolveUser(review.getUser()));
        review.setModule(resolveModule(review.getModule()));
        return reviewRepository.save(review);
    }

    public Review updateReview(Long id, Review review) {
        Review existingReview = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review no encontrada"));

        existingReview.setComment(review.getComment());
        existingReview.setRating(review.getRating());
        existingReview.setExperience(review.getExperience());

        if (review.getUser() != null && review.getUser().getId() != null) {
            existingReview.setUser(resolveUser(review.getUser()));
        }

        if (review.getModule() != null && review.getModule().getId() != null) {
            existingReview.setModule(resolveModule(review.getModule()));
        }

        return reviewRepository.save(existingReview);
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    private User resolveUser(User user) {
        if (user == null || user.getId() == null) {
            throw new RuntimeException("El usuario es obligatorio para la review");
        }

        return userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + user.getId()));
    }

    private Module resolveModule(Module module) {
        if (module == null || module.getId() == null) {
            throw new RuntimeException("El módulo es obligatorio para la review");
        }

        return moduleRepository.findById(module.getId())
                .orElseThrow(() -> new RuntimeException("Módulo no encontrado con ID: " + module.getId()));
    }
}
