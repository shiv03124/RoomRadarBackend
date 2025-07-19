package com.example.RoomRadar.Repository;

import com.example.RoomRadar.Enum.ApplicationStatus;
import com.example.RoomRadar.Model.Application;
import com.example.RoomRadar.Model.Room;
import com.example.RoomRadar.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    @Query("SELECT a.status FROM Application a WHERE a.applicant.id = :userId AND a.room.id = :roomId")
    Optional<ApplicationStatus> findStatusByUserIdAndRoomId(@Param("userId") Long userId, @Param("roomId") Long roomId);

    List<Application> findByApplicantId(Long userId);
    List<Application> findByRoomId(Long id);
    boolean existsByApplicantAndRoom(User applicant, Room room);
    @Query("SELECT a.room.id FROM Application a WHERE a.applicant.id = :userId")
    List<Long> findAppliedRoomIdsByUserId(@Param("userId") Long userId);
}
