package com.game.service;

import com.game.entity.Player;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface PlayerService {
    List<Player> findAll(Pageable of);
    List<Player> findByParams(Map<String, String> params);
    Integer countByParams(Map<String, String> params);
    Integer count();
    Player createPlayer(Map<String, String> player);
    boolean isAllParamsFound(Map<String, String> params);
    boolean isParamsValidForCreate(Map<String, String> params);
    boolean isIdValid(Long id);
    boolean existsById(Long id);
    Player findById(Long id);
    boolean isParamsValidForUpdate(Map<String, String> params);
    Player updatePlayer(Long id, Map<String, String> params);
    void deleteById(Long id);
}
