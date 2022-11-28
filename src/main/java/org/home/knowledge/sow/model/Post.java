package org.home.knowledge.sow.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

import org.home.knowledge.sow.json.Views;
import org.home.knowledge.sow.model.spec.AbstractEntity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonView(Views.Public.class)
public class Post extends AbstractEntity {
    @NotNull
    @NotBlank
    @Size(min = 5, max = 100)
    private String title;

    @NotNull
    @NotBlank
    @Size(min = 10, max = 250)
    private String shortDescription;

    @NotNull
    @NotBlank
    @Size(min = 10, max = 9000)
    private String contents;

    @NotNull
    @ManyToOne
    @JsonIgnoreProperties("posts")
    @ToString.Exclude
    private Author author;

    // SEE this! FINALLY my json makes sense
    // https://hellokoding.com/handling-circular-reference-of-jpa-hibernate-bidirectional-entity-relationships-with-jackson-jsonignoreproperties/
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    // TODO: after upgrading to Springboot3 and hibernate 6 supplying fetch mode is
    // no longer necessary?
    // @Fetch(FetchMode.SUBSELECT)
    @JsonIgnoreProperties("post")
    @Builder.Default
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<Comment>();

    // TODO: see:
    // https://stackoverflow.com/questions/36803306/should-jointable-be-specified-in-both-sides-of-a-manytomany-relationship
    // or maybe See #3: https://www.baeldung.com/jpa-many-to-many
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "Post_Tag", joinColumns = @JoinColumn(name = "tag_id"), inverseJoinColumns = @JoinColumn(name = "post_id"))
    // TODO: after upgrading to Springboot3 and hibernate 6 supplying fetch mode is
    // no longer necessary?
    // @Fetch(FetchMode.SUBSELECT)
    @JsonIgnoreProperties("posts")
    @ToString.Exclude
    private List<Tag> tags;

    @ManyToOne
    @JsonIgnoreProperties("posts")
    @ToString.Exclude
    private Topic topic;
}
