package securinets.securiday.securiday2k19_back.repositories;

import org.springframework.data.repository.CrudRepository;
import securinets.securiday.securiday2k19_back.model.Team;

public interface TeamRepository extends CrudRepository<Team,Integer> {
    public Team findByNameAndPassword(String name, String password);
    public Team findByName(String name);
}