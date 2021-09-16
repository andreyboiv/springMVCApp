package com.game.controller;

import com.game.entity.Player;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/players")
public class PlayerController {

    private PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping()
    public @ResponseBody
    ResponseEntity<List<Player>> getPlayers(@RequestParam Map<String, String> params) {
        if (params.isEmpty()) {
            return new ResponseEntity<List<Player>>(playerService.findAll(PageRequest.of(0, 3)), HttpStatus.OK);
        } else {
            return new ResponseEntity<List<Player>>(playerService.findByParams(params), HttpStatus.OK);
        }
    }

    @GetMapping("/count")
    public @ResponseBody
    ResponseEntity<Integer> getPlayersCount(@RequestParam Map<String, String> params) {
        if (params.isEmpty()) {
            return new ResponseEntity<Integer>(playerService.count(), HttpStatus.OK);
        } else {
            return new ResponseEntity<Integer>(playerService.countByParams(params), HttpStatus.OK);
        }
    }

    @PostMapping("")
    public @ResponseBody
    ResponseEntity<Player> createPlayer(@RequestBody Map<String, String> params) {
        if (!playerService.isAllParamsFound(params) || !playerService.isParamsValidForCreate(params)) {
            return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
        } else {
            Player player = playerService.createPlayer(params);
            if (player == null) {
                return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<Player>(player, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public @ResponseBody
    ResponseEntity<Player> getPlayerByID(
            @PathVariable Long id
    ) {
        try {
            if (!playerService.isIdValid(id)) {
                return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
            }
            if (!playerService.existsById(id)) {
                return new ResponseEntity<Player>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<Player>(playerService.findById(id), HttpStatus.OK);
            }
        } catch (NullPointerException | IllegalArgumentException e) {
            return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}")
    public @ResponseBody
    ResponseEntity<Player> updatePlayerByID(@PathVariable Long id, @RequestBody Map<String, String> params) {
        try {
            if (!playerService.isIdValid(id) || !playerService.isParamsValidForUpdate(params)) {
                return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
            }
            if (!playerService.existsById(id)) {
                return new ResponseEntity<Player>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<Player>(playerService.updatePlayer(id, params), HttpStatus.OK);
            }
        } catch (NullPointerException | IllegalArgumentException e) {
            return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public @ResponseBody
    ResponseEntity<Player> deletePlayerByID(@PathVariable Long id) {
        try {
            if (!playerService.isIdValid(id)) {
                return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
            }
            if (!playerService.existsById(id)) {
                return new ResponseEntity<Player>(HttpStatus.NOT_FOUND);
            } else {
                playerService.deleteById(id);
                return new ResponseEntity<Player>(HttpStatus.OK);
            }
        } catch (NullPointerException | IllegalArgumentException e) {
            return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
        }
    }

}
