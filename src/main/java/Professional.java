import java.util.ArrayList;
import java.util.List;

public class Professional {
    private String user;
    private List<String> skill;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<String>  getSkill() {
        return skill;
    }

    public void setSkill(List<String> skill) {
        this.skill = skill;
    }

    public String toString() {
        if (getUser() != null) {
            return "The user " + this.getUser() + " has the following skills" + skill.toString();
        } else {
            return "The user " + "has the following skills" + skill.toString();
        }
    }

}
