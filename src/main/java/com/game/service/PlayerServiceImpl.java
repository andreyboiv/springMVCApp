package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {

    public static final int MAX_NAME_LENGTH = 12;
    public static final int MAX_TITLE_LENGTH = 30;

    public static final int MIN_EXPERIENCE = 0;
    public static final int MAX_EXPERIENCE = 10000000;

    public static final int MIN_REGISTR_YAHR = 2000;
    public static final int MAX_REGISTR_YAHR = 3000;

    @Autowired
    PlayerRepository playerRepository;


    @Override
    public List<Player> findAll(Pageable pageable) {
        return playerRepository.findAll(pageable).stream().collect(Collectors.toList());
    }

    @Override
    public List<Player> findByParams(Map<String, String> params) {
        String name = (String) params.getOrDefault("name", null);
        String title = (String) params.getOrDefault("title", null);
        String profession = "";
        try {
            profession = params.containsKey("profession") ? String.valueOf(Profession.valueOf(params.get("profession"))) : null;
        } catch (IllegalArgumentException iae) {
        }

        String race = "";
        try {
            race = params.containsKey("race") ? String.valueOf(Race.valueOf(params.get("race"))) : null;
        } catch (IllegalArgumentException iae) {
        }

        Date after = null;
        if (params.containsKey("after")) {
            after = new Date(Long.parseLong(params.get("after")));
        }
        Date before = null;
        if (params.containsKey("before")) {
            before = new Date(Long.parseLong(params.get("before")));
        }
        Boolean banned = params.containsKey("banned") ? Boolean.parseBoolean(params.get("banned")) : null;
        Integer minExperience = params.containsKey("minExperience") ? Integer.parseInt(params.get("minExperience")) : null;
        Integer maxExperience = params.containsKey("maxExperience") ? Integer.parseInt(params.get("maxExperience")) : null;
        Integer minLevel = params.containsKey("minLevel") ? Integer.parseInt(params.get("minLevel")) : null;
        Integer maxLevel = params.containsKey("maxLevel") ? Integer.parseInt(params.get("maxLevel")) : null;

        Pageable pageable;
        int pageNumber = Integer.parseInt(params.getOrDefault("pageNumber", "0"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "3"));
        if (params.containsKey("order")) {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, (PlayerOrder.valueOf(params.get("order"))).getFieldName());
        } else {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, PlayerOrder.ID.getFieldName());
        }
        return playerRepository.findAllByParams(name, title, profession,
                race, after, before, banned, minExperience, maxExperience, minLevel, maxLevel, pageable).stream().collect(Collectors.toList());
    }

    @Override
    public Integer countByParams(Map<String, String> params) {
        String name = (String) params.getOrDefault("name", null);
        String title = (String) params.getOrDefault("title", null);
        String profession = "";
        try {
            profession = params.containsKey("profession") ? String.valueOf(Profession.valueOf(params.get("profession"))) : null;
        } catch (IllegalArgumentException iae) {
        }

        String race = "";
        try {
            race = params.containsKey("race") ? String.valueOf(Race.valueOf(params.get("race"))) : null;
        } catch (IllegalArgumentException iae) {
        }

        Date after = null;
        if (params.containsKey("after")) {
            after = new Date(Long.parseLong(params.get("after")));
        }
        Date before = null;
        if (params.containsKey("before")) {
            before = new Date(Long.parseLong(params.get("before")));
        }
        Boolean banned = params.containsKey("banned") ? Boolean.parseBoolean(params.get("banned")) : null;
        Integer minExperience = params.containsKey("minExperience") ? Integer.parseInt(params.get("minExperience")) : null;
        Integer maxExperience = params.containsKey("maxExperience") ? Integer.parseInt(params.get("maxExperience")) : null;
        Integer minLevel = params.containsKey("minLevel") ? Integer.parseInt(params.get("minLevel")) : null;
        Integer maxLevel = params.containsKey("maxLevel") ? Integer.parseInt(params.get("maxLevel")) : null;

        return playerRepository.countByParams(name, title, profession,
                race, after, before, banned, minExperience, maxExperience, minLevel, maxLevel);
    }

    @Override
    public Integer count() {
        try {
            return Math.toIntExact(playerRepository.count());
        } catch (ArithmeticException e) {
            return Integer.MAX_VALUE;
        }
    }

    @Override
    public Player createPlayer(Map<String, String> params) {
        try {
            String name = (String) params.getOrDefault("name", null);

            String title = (String) params.getOrDefault("title", null);
            String profession = params.containsKey("profession") ? String.valueOf(Profession.valueOf(params.get("profession"))) : null;
            String race = params.containsKey("race") ? String.valueOf(Race.valueOf(params.get("race"))) : null;
            Date birthday = params.containsKey("birthday") ? new Date(Long.parseLong(params.get("birthday"))) : null;
            Boolean banned = params.containsKey("banned") ? Boolean.parseBoolean(params.get("banned")) : false;
            Integer experience = params.containsKey("experience") ? Integer.parseInt(params.get("experience")) : null;

            Player player = new Player(name, title, race, profession, birthday, banned, experience);
            player.updateRating();
            return playerRepository.save(player);
        } catch (NullPointerException | IllegalArgumentException | ClassCastException e) {
            return null;
        }
    }

    @Override
    public boolean isParamsValidForCreate(Map<String, String> params) {
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!entry.getKey().equals("banned")) {
                if (entry.getValue() == null || entry.getValue().isEmpty()) {
                    return false;
                }
            }
        }

        String name = params.getOrDefault("name", null);
        String title = params.getOrDefault("title", null);

        if ((name != null && (name.isEmpty() || name.length() > MAX_NAME_LENGTH)) || (title != null && (title.isEmpty() || title.length() > MAX_TITLE_LENGTH))) {
            return false;
        }

        Date birthday = params.containsKey("birthday") ? new Date(Long.parseLong(params.get("birthday"))) : null;
        if (birthday != null) {
            long birthdayL = Long.parseLong(params.get("birthday"));
            if (birthdayL < 0) {
                return false;
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(birthday);
            int year = calendar.get(Calendar.YEAR);

            if (year < MIN_REGISTR_YAHR || year > MAX_REGISTR_YAHR) {
                return false;
            }
        }

        Integer experience = params.containsKey("experience") ? Integer.parseInt(params.get("experience")) : null;
        if (experience != null && (experience < MIN_EXPERIENCE || experience > MAX_EXPERIENCE)) {
            return false;
        }

        try {
            if (params.containsKey("race")) {
                Race race = Race.valueOf(params.get("race"));
            }
            if (params.containsKey("profession")) {
                Profession profession = Profession.valueOf(params.get("profession"));
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    @Override
    public boolean isIdValid(Long id) {
        return id > 0;
    }

    @Override
    public boolean existsById(Long id) {
        return playerRepository.existsById(id);
    }

    @Override
    public Player findById(Long id) {
        return playerRepository.findById(id).get();
    }

    @Override
    public boolean isParamsValidForUpdate(Map<String, String> params) {

        String name = params.getOrDefault("name", null);
        String title = params.getOrDefault("title", null);

        if ((name != null && (name.isEmpty() || name.length() > MAX_NAME_LENGTH)) || (title != null && (title.isEmpty() || title.length() > MAX_TITLE_LENGTH))) {
            return false;
        }

        Date birthday = params.containsKey("birthday") ? new Date(Long.parseLong(params.get("birthday"))) : null;
        if (birthday != null) {
            long birthdayL = Long.parseLong(params.get("birthday"));
            if (birthdayL < 0) {
                return false;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(birthday);
            int year = calendar.get(Calendar.YEAR);
            if (year < MIN_REGISTR_YAHR || year > MAX_REGISTR_YAHR) {
                return false;
            }
        }

        Integer experience = params.containsKey("experience") ? Integer.parseInt(params.get("experience")) : null;
        if (experience != null && (experience < MIN_EXPERIENCE || experience > MAX_EXPERIENCE)) {
            return false;
        }

        return true;
    }

    @Override
    public Player updatePlayer(Long id, Map<String, String> params) {
        if (!playerRepository.findById(id).isPresent() || params == null) {
            return null;
        }

        Player result = playerRepository.findById(id).get();

        String name = params.getOrDefault("name", null);
        String title = params.getOrDefault("title", null);
        String profession = params.containsKey("profession") ? String.valueOf(Profession.valueOf(params.get("profession"))) : null;
        String race = params.containsKey("race") ? String.valueOf(Race.valueOf(params.get("race"))) : null;
        Date birthday = params.containsKey("birthday") ? new Date(Long.parseLong(params.get("birthday"))) : null;
        Boolean banned = params.containsKey("banned") ? Boolean.parseBoolean(params.get("banned")) : false;
        Integer experience = params.containsKey("experience") ? Integer.parseInt(params.get("experience")) : null;

        if (name != null) result.setName(name);
        if (title != null) result.setTitle(title);
        if (profession != null) result.setProfession(profession);
        if (race != null) result.setRace(race);
        if (birthday != null) result.setBirthday(birthday);
        if (banned != null) result.setBanned(banned);
        if (experience != null) result.setExperience(experience);

        result.updateRating();
        playerRepository.saveAndFlush(result);

        return result;
    }

    @Override
    public void deleteById(Long id) {
        playerRepository.deleteById(id);
    }

    @Override
    public boolean isAllParamsFound(Map<String, String> params) {
        return params.containsKey("name")
                && params.containsKey("title")
                && params.containsKey("race")
                && params.containsKey("profession")
                && params.containsKey("birthday")
                && params.containsKey("experience");
    }

}
