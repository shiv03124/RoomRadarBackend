package com.example.RoomRadar.utils;

import com.example.RoomRadar.Model.Room;
import com.example.RoomRadar.Model.RoomStatus;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;

public class RoomSpecifications {

    public static Specification<Room> hasCity(String city) {
        return (root, query, cb) -> (city == null || city.trim().isEmpty()) ? null : cb.equal(root.get("city"), city.trim());
    }

    public static Specification<Room> hasArea(String area) {
        return (root, query, cb) -> (area == null || area.trim().isEmpty()) ? null : cb.equal(root.get("area"), area.trim());
    }

    public static Specification<Room> hasGender(String gender) {
        return (root, query, cb) -> (gender == null || gender.trim().isEmpty()) ? null : cb.equal(root.get("preferredGender"), gender.trim());
    }
    public static Specification<Room> vacanciesBetween(Integer min, Integer max) {
        return (root, query, cb) -> {
            if (min != null && max != null) return cb.between(root.get("noofvacancies"), min, max);
            else if (min != null) return cb.greaterThanOrEqualTo(root.get("noofvacancies"), min);
            else if (max != null) return cb.lessThanOrEqualTo(root.get("noofvacancies"), max);
            else return null;
        };
    }

    public static Specification<Room> rentBetween(Double min, Double max) {
        return (root, query, cb) -> {
            if (min != null && max != null) return cb.between(root.get("rent"), min, max);
            else if (min != null) return cb.greaterThanOrEqualTo(root.get("rent"), min);
            else if (max != null) return cb.lessThanOrEqualTo(root.get("rent"), max);
            else return null;
        };
    }


    public static Specification<Room> hasAmenity(String amenity) {
        return (root, query, cb) -> {
            if (amenity == null) return null;
            Join<Object, Object> amenitiesJoin = root.join("amenities");
            return cb.equal(amenitiesJoin, amenity);
        };
    }

    public static Specification<Room> hasStatus(RoomStatus status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }


    public static Specification<Room> hasAvailability(Boolean isAvailable) {
        return (root, query, criteriaBuilder) -> {
            if (isAvailable == null) {
                return criteriaBuilder.conjunction(); // no filtering if null
            }
            return criteriaBuilder.equal(root.get("isAvailable"), isAvailable);
        };
    }

    public static Specification<Room> isAvailable(Boolean available) {
        return (root, query, cb) -> cb.equal(root.get("isAvailable"), available);
    }


}