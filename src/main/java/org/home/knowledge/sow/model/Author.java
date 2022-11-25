package org.home.knowledge.sow.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
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
    @JsonIgnoreProperties("author")
    private List<Post> posts;

    // @ManyToMany
    // private List<Comment> comments;

    // TODO: back by User or influencer so we can tie authors back to employees or
    // non-employees
}
