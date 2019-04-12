package securinets.securiday.securiday2k19_back.controller;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import securinets.securiday.securiday2k19_back.model.Status;
import securinets.securiday.securiday2k19_back.model.Submission;
import securinets.securiday.securiday2k19_back.model.Team;
import securinets.securiday.securiday2k19_back.repositories.SubmissionRepository;
import securinets.securiday.securiday2k19_back.repositories.TeamRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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

    @RequestMapping(path = "/submission", method = RequestMethod.PUT)
    public Submission judge( @RequestBody Submission submission ) {
        submissionRepository.save(submission);

        if(submission.getStatus()==Status.ACCEPTED){
            Team team = submission.getTeam();
            team.setScore(team.getScore()+submission.getScore());
            teamRepository.save(team);
        }

        return submission;
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

    @RequestMapping(value="/report/{id}", method=RequestMethod.GET)
    public void getDownload(HttpServletResponse response, @PathVariable int id) {
        //get submission
        Submission submission = submissionRepository.findById(id).get();
        try {
            // Get your file stream from wherever.
            File file = new File(folder.getAbsolutePath()+"/"+submission.getId()+".pdf");
            InputStream myStream = new FileInputStream(file);
            // Set the content type and attachment header.
            response.addHeader("Content-disposition", "attachment;filename=report"+submission.getId()+".pdf");
            response.setContentType("application/pdf");

            // Copy the stream to the response's output stream.
                IOUtils.copy(myStream, response.getOutputStream());
                response.flushBuffer();
            }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
