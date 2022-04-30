package com.microservices.example.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.microservices.example.models.CatalogItem;
import com.microservices.example.models.Movie;
import com.microservices.example.models.UserRating;

@RestController
@RequestMapping("/catalog")
public class CatalogController { //Catalog of movies that contains the movie's name, description, and rating
	
	@Autowired
    WebClient.Builder webClientBuilder;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {
//		UserRating userRating = webClientBuilder.build().get()
//				.uri("http://ratings-data-service/ratingsdata/user/" + userId)
//				.retrieve().bodyToMono(UserRating.class).block();

		UserRating userRating = restTemplate.getForObject("http://client-movie-rating/ratingsdata/user/" + userId, UserRating.class);

		//Code for Docker
//		UserRating userRating = restTemplate.getForObject("http://moviesmicroservices-client-rating-1/ratingsdata/user/" + userId, UserRating.class);
		
		return userRating.getRatings().stream()
                .map(rating -> {
                    Movie movie = restTemplate.getForObject("http://client-movie-info/movies/" + rating.getMovieId(), Movie.class);
                    return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
                })
                .collect(Collectors.toList());
		
		//Code for Docker
//		return userRating.getRatings().stream()
//                .map(rating -> {
//                    Movie movie = restTemplate.getForObject("http://moviesmicroservices-client-info-1/movies/" + rating.getMovieId(), Movie.class);
//                    return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
//                })
//                .collect(Collectors.toList());
		
		
//		return userRating.getRatings().stream()
//	                .map(rating -> {
//	                	Movie movie = webClientBuilder.build().get()
//	                			.uri("http://localhost:8082/movies/"+ rating.getMovieId())
//	                			.retrieve().bodyToMono(Movie.class).block();
//	                })
//	                .collect(Collectors.toList());
		
	}
	
//	@Value("${greeting}")
//	private String greeting;
//	
//	@GetMapping("/greeting/print")
//	public String getGreeting() {
//		return greeting;
//	}
	
}
