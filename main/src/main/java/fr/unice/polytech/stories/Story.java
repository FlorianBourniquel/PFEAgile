package fr.unice.polytech.stories;

import fr.unice.polytech.models.Class;
import fr.unice.polytech.models.RelationShip;
import java.util.ArrayList;
import java.util.List;

public class Story extends Node{

    private String text;
    private int number;
    private int storyPoints;
    private int businessValue;
    private Role role;
    private StoryMean means;
    private List<Class> classes = new ArrayList<>();
    private List<RelationShip> relationShips = new ArrayList<>();


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public StoryMean getMeans() {
        return means;
    }

    public void setMeans(StoryMean means) {
        this.means = means;
    }

    @Override
    public String toString() {
        return "Story{" +
                "text='" + text + '\'' +
                ", number=" + number +
                ", role=" + role +
                ", means=" + means +
                ", classes=" + classes +
                ", relationShips=" + relationShips +
                '}';
    }

    @Override
    public String toNode(String var) {
        return "("+var+":Story {" +
                "name:"+ss("US" + number ) +
                ",text:"+ss(text) +
                ",business_value:" + businessValue +
                ",story_points:"+ storyPoints +
                "}) ";
    }


    @Override
    public String createQuery() {
        return  " MERGE " + role.toNode("r") + " \n" +
                " CREATE " + toNode("s") + " -[:HAS_FOR_MEAN]-> " + means.toNode() + " \n" +
                " MERGE (s) -[:HAS_FOR_ROLE]->(r)" +" \n\n" +
                 mergeClasses() + " \n" +
                 createDomainModel() + " \n" +
                 createRelationShips() +" \n\n" +
                 createRelationBetweenRoleAndDomain() +" \n";
    }

    private String createRelationBetweenRoleAndDomain() {
        Class roleClass = classes.stream().filter(x ->x.getName().equalsIgnoreCase(role.getText())).findFirst().orElse(null);
        if(roleClass != null){
            String classAlias = "c" + classes.indexOf(roleClass);
        return " MERGE ("+classAlias+")-[:IS_LINKED_TO]->(r)";
        }
        return "";
    }

    private String createRelationShips() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < relationShips.size(); i++) {
            RelationShip relationShip = relationShips.get(i);
            String sourceAlias = "c"+ classes.indexOf(relationShip.getSource());
            String destAlias = "c"+ classes.indexOf(relationShip.getDestination());
            sb.append(relationShip.createQuery("r"+i,sourceAlias,destAlias,"s"));
        }
        return sb.toString();
    }

    private String mergeClasses() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < classes.size(); i++) {
            sb.append(" MERGE ").append(classes.get(i).toNode("c"+i)).append(" \n");
        }
        return sb.toString();
    }


    private String createDomainModel(){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < classes.size(); i++) {
            sb.append(" MERGE (s)-[:INVOLVES]-> ").append("(c"+i+")").append(" \n");
        }
        return sb.toString();

    }


    public List<Class> getClasses() {
        return classes;
    }

    public void setClasses(List<Class> classes) {
        this.classes = classes;
    }

    public List<RelationShip> getRelationShips() {
        return relationShips;
    }

    public void setRelationShips(List<RelationShip> relationShips) {
        this.relationShips = relationShips;
    }

    public int getStoryPoints() {
        return storyPoints;
    }

    public void setStoryPoints(int storyPoints) {
        this.storyPoints = storyPoints;
    }

    public int getBusinessValue() {
        return businessValue;
    }

    public void setBusinessValue(int businessValue) {
        this.businessValue = businessValue;
    }

    public String ss(String content){
        return "\""+content+"\"";
    }
}
