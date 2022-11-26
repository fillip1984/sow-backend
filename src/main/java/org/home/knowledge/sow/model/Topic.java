package org.home.knowledge.sow.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.home.knowledge.sow.json.Views;
import org.home.knowledge.sow.model.spec.AbstractEntity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonView(Views.Public.class)
public class Topic extends AbstractEntity {

    @Size(max = 100)
    @NotNull
    @NotBlank
    private String name;

    @Size(max = 250)
    private String description;

    @OneToMany(mappedBy = "topic")
    @JsonIgnoreProperties("topic")
    private List<Post> posts;
}
