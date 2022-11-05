package org.home.knowledge.sow.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
public class Post extends AbstractEntity {
    @NotNull
    @NotBlank
    @Size(max = 100)
    private String title;

    @NotNull
    @NotBlank
    @Size(max = 250)
    private String shortDescription;

    @NotNull
    @NotBlank
    @Size(max = 9000)
    private String contents;

    @NotNull
    @ManyToOne
    private Author author;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    // TODO: see:
    // https://stackoverflow.com/questions/36803306/should-jointable-be-specified-in-both-sides-of-a-manytomany-relationship
    // or maybe See #3: https://www.baeldung.com/jpa-many-to-many
    @ManyToMany
    @JoinTable(name = "Post_Tag", joinColumns = @JoinColumn(name = "tag_id"), inverseJoinColumns = @JoinColumn(name = "post_id"))
    private List<Tag> tags;
}
