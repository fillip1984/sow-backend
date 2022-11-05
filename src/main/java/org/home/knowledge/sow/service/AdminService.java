package org.home.knowledge.sow.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.home.knowledge.sow.model.Author;
import org.home.knowledge.sow.model.Post;
import org.home.knowledge.sow.model.Tag;
import org.home.knowledge.sow.model.dto.DataExport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdminService {

    @Autowired
    private TagService tagService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private PostService postService;

    @Transactional
    public void loadSampleData() {
        log.info("Loading sample data");
        // TODO: verify that there is no better method of creating a system user context
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication systemAuthentication = new PreAuthenticatedAuthenticationToken("system", null);
        systemAuthentication.setAuthenticated(true);
        context.setAuthentication(systemAuthentication);
        SecurityContextHolder.setContext(context);

        // @formatter:off
        Tag javaTag = Tag.builder()
                         .name("Java")
                         .description("General Java information")
                         .build();

        Author philAuthor = Author.builder()
                            .name("Phillip Williams")
                            .build();

        Post javaPost = Post.builder()
                            .tags(List.of(javaTag))
                            .authors(List.of(philAuthor))
                            .title("Java 9 through 17, what was enhanced")
                            .shortDescription("A brief intro to what enhancements were made to Java from 9 to 17")
                            .contents("Java 1.8 or less commonly referred to as 8 was the most used version of Java. Since then, 9 through 17 have come up with 17 being the latet long term support (LTS) version of Java")
                            .build();
        // @formatter:on

        javaTag = tagService.save(javaTag);
        philAuthor = authorService.save(philAuthor);
        javaPost = postService.save(javaPost);

        // retrieve fresh
        Post testPost = postService.findById(javaPost.getId());

        // log system user back out
        SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
        log.info("Retrieved java post as test: {}", testPost);
    }

    /**
     * Imports data via json input
     * 
     * @param inputStream InputStream containing json
     * @return int reporting back the number of records imported
     */
    @Transactional
    public int importData(DataExport data) {
        try {
            log.info("Processing data import");
            var authorIdsBeforeAndAfter = new HashMap<Long, Long>();
            var tagIdsBeforeAndAfter = new HashMap<Long, Long>();
            var postIdsBeforeAndAfter = new HashMap<Long, Long>();

            // @formatter:off
            // note: these stream.map function look odd since we aren't just converting the author/tag to something else. The reason for this 
            // oddity is I tried replacing the object reference in a forEach loop (i.e. author = authorService.save(author)) 
            // but the side effect didn't work and author was the previous version outside of the forEach loop so I switched to map.
            // Give it a try if you'd like, I think a forEach was more suited to what we're doing (recording before and after 
            // database id and updating the author in the collection) but it didn't work once we got down to binding the posts' authors 
            // to their new versions. The ids on the authors/tags were still the original exported values and not their new imported values. 
            // Maybe I'm missing a trick for updating the obejct reference, maybe Java just sucks!
            var importedAuthors = data.getAuthors().stream().map(author -> {
                var beforeId = author.getId();
                author = authorService.save(author);
                authorIdsBeforeAndAfter.put(beforeId, author.getId());
                return author;
            }).collect(Collectors.toList());
            data.setAuthors(importedAuthors);
            
            var importedTags = data.getTags().stream().map(tag -> {
                var beforeId = tag.getId();
                tag = tagService.save(tag);
                tagIdsBeforeAndAfter.put(beforeId, tag.getId());
                return tag;
            }).collect(Collectors.toList());
            data.setTags(importedTags);

            var importedPosts = data.getPosts().stream().map(post -> {
                var beforeId = post.getId();
                // update ids of author and tags prior to save
                var updatedAuthors = post.getAuthors().stream().map(author -> {
                    var afterId = authorIdsBeforeAndAfter.get(author.getId());
                    Optional<Author> potentialAuthor = data.getAuthors().stream().filter(anAuthor -> anAuthor.getId().equals(afterId)).findFirst();
                    if (potentialAuthor.isPresent()) {
                        return potentialAuthor.get();
                    } else {
                        throw new RuntimeException("Unable tod find author. Previous id: " + author.getId());
                    }
                }).collect(Collectors.toList());
                post.setAuthors(updatedAuthors);

                var updatedTags = post.getTags().stream().map(tag -> {
                    var afterId = tagIdsBeforeAndAfter.get(tag.getId());
                    Optional<Tag> potentialTag = data.getTags().stream().filter(aTag -> aTag.getId().equals(afterId)).findFirst();
                    if (potentialTag.isPresent()) {
                        return potentialTag.get();
                    } else {
                        throw new RuntimeException("Unable to find tag. Previous id: " + tag.getId());
                    }
                }).collect(Collectors.toList());
                post.setTags(updatedTags);
                // @formatter:on

                post = postService.save(post);
                postIdsBeforeAndAfter.put(beforeId, post.getId());
                return post;
            }).collect(Collectors.toList());
            data.setPosts(importedPosts);
            log.info("Processed data import");
            return 1;
        } catch (Exception e) {
            String msg = "Exception occurred while attempting to import data";
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    /**
     * Produces DataExport object which contains all exportable data
     * <p>
     * As we add more classes/entities there is a high chance that they won't be
     * added here so if you're missing something that we want to export to
     * backup/import to another region it isn't here intentionally but due to
     * neglect. Add anything and everything you need
     * 
     * @return DataExport object containing all exportable data
     */
    public DataExport exportData() {
        try {
            log.info("Producing data export");
            // @formatter:off
            var data = DataExport.builder()
                                 .posts(postService.findAll())
                                 .tags(tagService.findAll())
                                 .authors(authorService.findAll())
                                 .build();
            // @formatter:on
            log.trace("Produced data export: {}", data);
            return data;
        } catch (Exception e) {
            var msg = "Exception occurred while producing data export";
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }

    }

}