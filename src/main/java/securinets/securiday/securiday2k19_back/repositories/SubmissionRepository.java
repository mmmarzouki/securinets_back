package securinets.securiday.securiday2k19_back.repositories;

import org.springframework.data.repository.CrudRepository;
import securinets.securiday.securiday2k19_back.model.Submission;
import securinets.securiday.securiday2k19_back.model.Team;

import java.util.ArrayList;

public interface SubmissionRepository extends CrudRepository<Submission,Integer> {

    ArrayList<Submission> findAllByTeam(Team team);
}