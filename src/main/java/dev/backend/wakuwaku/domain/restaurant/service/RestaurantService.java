package dev.backend.wakuwaku.domain.restaurant.service;

import dev.backend.wakuwaku.domain.restaurant.entity.Restaurant;
import dev.backend.wakuwaku.domain.restaurant.repository.RestaurantRepository;
import dev.backend.wakuwaku.global.infra.google.places.old.Result;
import dev.backend.wakuwaku.global.infra.google.places.old.details.GooglePlacesDetailsService;
import dev.backend.wakuwaku.global.infra.google.places.old.textsearch.GooglePlacesTextSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static dev.backend.wakuwaku.domain.restaurant.service.constant.SearchWordConstant.JAPAN;
import static dev.backend.wakuwaku.global.exception.WakuWakuException.INVALID_PARAMETER;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final GooglePlacesDetailsService googlePlacesDetailsService;
    private final GooglePlacesTextSearchService googlePlacesTextSearchService;

    public Restaurant save(Restaurant restaurant) {
        return restaurantRepository.findByPlaceId(restaurant.getPlaceId())
                .orElseGet(
                        () -> restaurantRepository.save(restaurant)
                );
    }

    @Transactional(readOnly = true)
    public Restaurant findById(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(
                        () -> INVALID_PARAMETER
                );
    }

    public List<Restaurant> getSimpleRestaurants(String searchWord) {
        String newWord = duplicateWord(searchWord);

        List<Result> results = googlePlacesTextSearchService.textSearch(JAPAN + newWord);

        return results.stream()
                .map(Restaurant::new)
                .toList();
    }

    public Result getDetailsRestaurant(String placeId) {
        return googlePlacesDetailsService.detailsSearch(placeId);
    }

    private String duplicateWord(String word) {
        if (word.contains("일본")) {
            return word.replace("일본", "");
        }

        return word;
    }
}
