package securinets.securiday.securiday2k19_back.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import securinets.securiday.securiday2k19_back.model.Team;
import securinets.securiday.securiday2k19_back.repositories.TeamRepository;

import java.util.ArrayList;

@CrossOrigin(origins = "*")
@RestController
public class TeamController {

    @Autowired
    TeamRepository teamRepository;

    @RequestMapping (value = "/login", method = RequestMethod.POST)
    public Team login(@RequestBody Team teamInput){
        Team teamFound = teamRepository.findByNameAndPassword(teamInput.getName(),teamInput.getPassword());
        return teamFound;
    }

    @RequestMapping (value = "/team", method = RequestMethod.GET)
    public ArrayList<Team> findAll(){
        ArrayList<Team> teams = (ArrayList<Team>) teamRepository.findAll();
        teams.removeIf(team -> {boolean x= team.getName().startsWith("judge");return x;});
        return teams;
    }
}