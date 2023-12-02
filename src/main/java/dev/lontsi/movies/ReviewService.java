package dev.lontsi.movies;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Review> allReviews() {
        return reviewRepository.findAll();
    }

    public Optional<Review> getReviewById(ObjectId id) {
        return reviewRepository.findById(id);
    }

    public Review createReview(String reviewBody, String imdbId) {

        Review review = reviewRepository.insert(new Review(reviewBody));
        System.out.println(reviewBody + "---" +imdbId);
        System.out.println(review.toString());
        mongoTemplate.update(Movie.class)
                .matching(Criteria.where("imdbId").is(imdbId))
                .apply(new Update().push("reviewIds").value(review))
                .first();

        return review;
    }

}
