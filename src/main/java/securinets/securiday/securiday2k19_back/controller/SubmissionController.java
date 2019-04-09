package securinets.securiday.securiday2k19_back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import securinets.securiday.securiday2k19_back.model.Status;
import securinets.securiday.securiday2k19_back.model.Submission;
import securinets.securiday.securiday2k19_back.model.Team;
import securinets.securiday.securiday2k19_back.repositories.SubmissionRepository;
import securinets.securiday.securiday2k19_back.repositories.TeamRepository;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@CrossOrigin(origins="*")
@RestController
public class SubmissionController {
    @Autowired
    SubmissionRepository submissionRepository;

    @Autowired
    TeamRepository teamRepository;

    private File folder = new File("uploads");

    @RequestMapping(path = "/submission", method = RequestMethod.GET)
    public ArrayList<Submission> findAll(){
        return (ArrayList<Submission>) submissionRepository.findAll();
    }

    @RequestMapping(path = "/submission/{id}", method = RequestMethod.GET)
    public ArrayList<Submission> findAllByTeam(@PathVariable int id){
        Team team = teamRepository.findById(id).get();
        return submissionRepository.findAllByTeam(team);
    }
    @RequestMapping(path = "/submit", method = RequestMethod.POST)
    public Submission submit(@RequestParam String teamName, @RequestParam MultipartFile file){
        //get team
        Team team = teamRepository.findByName(teamName);
        //get date
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String time = dateFormat.format(date);
        //get status
        String status = Status.PENDING;
        //create submission
        Submission submission = new Submission();
        //put args
        submission.setTeam(team);
        submission.setStatus(status);
        submission.setTime(time);
        //save submission
        submissionRepository.save(submission);
        //upload file
        try{
            folder.mkdir();
            Path targetPath = Paths.get(folder.getAbsolutePath()+"/"+submission.getId()+".pdf");
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        //return submission
        return submission;
    }
}
