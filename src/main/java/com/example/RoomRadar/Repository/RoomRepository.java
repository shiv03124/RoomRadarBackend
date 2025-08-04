package com.example.RoomRadar.Repository;

import com.example.RoomRadar.Model.Room;
import com.example.RoomRadar.Model.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {
    @Query("SELECT r FROM Room r WHERE r.status = :status AND r.accommodationType = :accommodationType " +
            "AND r.id NOT IN (SELECT a.room.id FROM Application a WHERE a.applicant.id = :userId)")
    List<Room> findApprovedAndNotAppliedByUser(@Param("status") RoomStatus status,
                                               @Param("accommodationType") String accommodationType,
                                               @Param("userId") Long userId);
    @Query("SELECT r FROM Room r WHERE r.status = :status AND r.accommodationType = :accommodationType")
    List<Room> findByStatusAndAccommodationType(@Param("status") RoomStatus status,
                                                @Param("accommodationType") String accommodationType);

    @Query("SELECT r FROM Room r WHERE r.accommodationType = :type AND r.user.id <> :userId AND r.status = com.example.RoomRadar.Model.RoomStatus.APPROVED")
    List<Room> findByAccommodationTypeAndNotUserId(@Param("type") String accommodationType, @Param("userId") Long userId);
    // All rooms by a specific user with a given status
    List<Room> findByStatusAndUserId(RoomStatus status, Long userId);

    // All rooms not by the user, with a given status
    List<Room> findByStatusAndUserIdNot(RoomStatus status, Long userId);

    // All rooms not by the user, with a given status, and not in a list of IDs (e.g., not applied)
    List<Room> findByStatusAndUserIdNotAndIdNotIn(RoomStatus status, Long userId, List<Long> ids);

    // All rooms with a given status
    List<Room> findByStatus(RoomStatus status);

    // Custom query: All approved rooms not applied by the user
    @Query("SELECT r FROM Room r WHERE r.status = 'APPROVED' AND r.id NOT IN (" +
            "SELECT a.room.id FROM Application a WHERE a.applicant.id = :userId)")
    List<Room> findApprovedRoomsNotAppliedByUser(Long userId);

}
