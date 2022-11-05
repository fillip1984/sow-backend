package org.home.knowledge.sow.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.home.knowledge.sow.model.spec.AbstractEntity;

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
public class Author extends AbstractEntity {

    @Size(max = 100)
    @NotNull
    @NotBlank
    private String name;

    @Size(max = 500)
    private String bio;

    @ManyToMany
    private List<Post> posts;

    @ManyToMany
    private List<Comment> comments;

    // TODO: back by User or influencer so we can tie authors back to employees or
    // non-employees
}
