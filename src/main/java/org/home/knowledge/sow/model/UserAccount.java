package org.home.knowledge.sow.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;

import org.home.knowledge.sow.model.spec.AbstractEntity;

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
@ToString(callSuper = true, exclude = "password")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAccount extends AbstractEntity {

    private String username;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    // @JoinTable(name = "User_", joinColumns = @JoinColumn(name = "tag_id"),
    // inverseJoinColumns = @JoinColumn(name = "post_id"))
    private List<UserRole> roles;

}