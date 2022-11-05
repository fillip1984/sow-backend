package org.home.knowledge.sow.model.dto;

import java.util.List;

import org.home.knowledge.sow.model.Author;
import org.home.knowledge.sow.model.Post;
import org.home.knowledge.sow.model.Tag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataExport {
    private List<Post> posts;
    private List<Author> authors;
    private List<Tag> tags;

}
