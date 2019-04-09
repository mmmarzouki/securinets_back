package securinets.securiday.securiday2k19_back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import securinets.securiday.securiday2k19_back.model.Submission;
import securinets.securiday.securiday2k19_back.model.Team;
import securinets.securiday.securiday2k19_back.repositories.SubmissionRepository;
import securinets.securiday.securiday2k19_back.repositories.TeamRepository;

import java.util.ArrayList;

@CrossOrigin(origins="*")
@RestController
public class SubmissionController {
    @Autowired
    SubmissionRepository submissionRepository;

    @Autowired
    TeamRepository teamRepository;

    @RequestMapping(path = "/submission", method = RequestMethod.GET)
    public ArrayList<Submission> findAll(){
        return (ArrayList<Submission>) submissionRepository.findAll();
    }

    @RequestMapping(path = "/submission/{id}", method = RequestMethod.GET)
    public ArrayList<Submission> findAllByTeam(@PathVariable int id){
        Team team = teamRepository.findById(id).get();
        return submissionRepository.findAllByTeam(team);
    }
}
