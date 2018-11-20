package fr.unice.polytech.repository.dto;

import org.neo4j.driver.v1.Value;

import java.util.Objects;

public class SprintDTO {

    private String name;

    public SprintDTO(Value value) {
        name = value.get("name").asString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SprintDTO)) return false;
        SprintDTO sprintDTO = (SprintDTO) o;
        return Objects.equals(name, sprintDTO.name);
    }
    
}
