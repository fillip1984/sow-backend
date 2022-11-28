package org.home.knowledge.sow.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
public class Tag extends AbstractEntity {

    // was going to add min = 2 but what about the single letter programming
    // languages: R, C, etc...
    @NotNull
    @NotBlank
    @Size(max = 50)
    private String name;

    @NotNull
    @NotBlank
    @Size(min = 5, max = 200)
    private String description;

    // TODO: see:
    // https://stackoverflow.com/questions/36803306/should-jointable-be-specified-in-both-sides-of-a-manytomany-relationship
    // or maybe See #3: https://www.baeldung.com/jpa-many-to-many
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "Post_Tag", joinColumns = @JoinColumn(name = "tag_id"), inverseJoinColumns = @JoinColumn(name = "post_id"))
    @JsonIgnoreProperties("tags")
    private List<Post> posts;

}
