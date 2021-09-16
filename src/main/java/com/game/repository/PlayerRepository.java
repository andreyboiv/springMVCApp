package com.game.repository;

import com.game.entity.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface PlayerRepository extends JpaRepository<Player,Long> {

    @Query("select u from Player u where "
            + "(:name is not null and u.name like concat('%', :name, '%') or :name is null) "
            + "and (:title is not null and u.title like concat('%', :title, '%') or :title is null) "
            + "and (:race is not null and u.race like :race or :race is null) "
            + "and (:profession is not null and u.profession like :profession or :profession is null) "
            + "and (:after is not null and u.birthday >= :after or :after is null) "
            + "and (:before is not null and u.birthday <= :before or :before is null) "
            + "and (:banned is not null and u.banned = :banned or :banned is null) "
            + "and (:minExperience is not null and u.experience >= :minExperience or :minExperience is null) "
            + "and (:maxExperience is not null and u.experience <= :maxExperience or :maxExperience is null) "
            + "and (:minLevel is not null and u.level >= :minLevel or :minLevel is null) "
            + "and (:maxLevel is not null and u.level <= :maxLevel or :maxLevel is null) ")
    Page<Player> findAllByParams(
            @Param("name") String name,
            @Param("title") String title,
            @Param("profession") String profession,
            @Param("race") String race,
            @Param("after") Date after,
            @Param("before") Date before,
            @Param("banned") Boolean banned,
            @Param("minExperience") Integer minExperience,
            @Param("maxExperience") Integer maxExperience,
            @Param("minLevel") Integer minLevel,
            @Param("maxLevel") Integer maxLevel,
            Pageable pageable);

    @Override
    Page<Player> findAll(Pageable pageable);

    @Query("select count(u) from Player u where "
            + "(:name is not null and u.name like concat('%', :name, '%') or :name is null) "
            + "and (:title is not null and u.title like concat('%', :title, '%') or :title is null) "
            + "and (:race is not null and u.race like :race or :race is null) "
            + "and (:profession is not null and u.profession like :profession or :profession is null) "
            + "and (:after is not null and u.birthday >= :after or :after is null) "
            + "and (:before is not null and u.birthday <= :before or :before is null) "
            + "and (:banned is not null and u.banned = :banned or :banned is null) "
            + "and (:minExperience is not null and u.experience >= :minExperience or :minExperience is null) "
            + "and (:maxExperience is not null and u.experience <= :maxExperience or :maxExperience is null) "
            + "and (:minLevel is not null and u.level >= :minLevel or :minLevel is null) "
            + "and (:maxLevel is not null and u.level <= :maxLevel or :maxLevel is null) ")
    Integer countByParams(
            @Param("name") String name,
            @Param("title") String title,
            @Param("profession") String profession,
            @Param("race") String race,
            @Param("after") Date after,
            @Param("before")  Date before,
            @Param("banned")  Boolean banned,
            @Param("minExperience") Integer minExperience,
            @Param("maxExperience")  Integer maxExperience,
            @Param("minLevel") Integer minLevel,
            @Param("maxLevel") Integer maxLevel);
}
