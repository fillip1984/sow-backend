package org.home.knowledge.sow.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
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

    @NotNull
    @NotBlank
    @Size(min = 2, max = 100)
    private String firstName;

    @NotNull
    @NotBlank
    @Size(min = 2, max = 100)
    private String lastName;

    @Size(max = 100)
    private String preferredName;

    @Transient
    public String getFullNameFirstThenLast() {
        return firstName + " " + lastName;
    }

    @Transient
    public String getFullNameLastCommaFirst() {
        return lastName + ", " + firstName;
    }

    @Size(max = 500)
    private String bio;

    @OneToMany(mappedBy = "author")
    private List<Post> posts;

    // @ManyToMany
    // private List<Comment> comments;

    // TODO: back by User or influencer so we can tie authors back to employees or
    // non-employees
}
