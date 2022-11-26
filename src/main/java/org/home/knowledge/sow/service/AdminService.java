package org.home.knowledge.sow.service;

import java.util.List;

import jakarta.transaction.Transactional;

import org.home.knowledge.sow.model.Author;
import org.home.knowledge.sow.model.Comment;
import org.home.knowledge.sow.model.Post;
import org.home.knowledge.sow.model.Tag;
import org.home.knowledge.sow.model.Topic;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdminService {

        private final PostService postService;
        private final AuthorService authorService;
        private final TopicService topicService;
        private final TagService tagService;
        private final CommentService commentService;

        public AdminService(PostService postService, AuthorService authorService, TopicService topicService,
                        TagService tagService, CommentService commentService) {
                this.postService = postService;
                this.authorService = authorService;
                this.topicService = topicService;
                this.tagService = tagService;
                this.commentService = commentService;
        }

        public void loadSampleData() {
                log.info("Loading sample data");
                // TODO: verify that there is no better method of creating a system user context
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                Authentication systemAuthentication = new PreAuthenticatedAuthenticationToken("system", null);
                systemAuthentication.setAuthenticated(true);
                context.setAuthentication(systemAuthentication);
                SecurityContextHolder.setContext(context);

                var topics = topicService.saveAll(buildSampleTopics());
                var javaTopic = topics.stream().filter(topic -> topic.getName().equals("Java")).findFirst().get();

                var tags = tagService.saveAll((buildSampleTags()));
                var bestPractice = tags.stream().filter(tag -> tag.getName().equals("Best Practice")).findFirst().get();

        // @formatter:off
        // Tag javaTag = Tag.builder()
        //                  .name("Java")
        //                  .description("General Java information")
        //                  .build();

        Author philAuthor = Author.builder()
                            .firstName("Phillip")
                            .lastName("Williams")
                            .build();

        var comment = Comment.builder()
                            .content("What a great post, please write more")
                            .build();

        Post javaPost = Post.builder()
                            .topic(javaTopic)
                            //.tags(List.of(bestPractice))
                            //.comments(List.of(comment))
                            .author(philAuthor)
                            .title("Java 9 through 17, what was enhanced")
                            .shortDescription("A brief intro to what enhancements were made to Java from 9 to 17")
                            .contents("Java 1.8 or less commonly referred to as 8 was the most used version of Java. Since then, 9 through 17 have come up with 17 being the latet long term support (LTS) version of Java")
                            .build();

        //javaTag.setPosts(List.of(javaPost));
        //philAuthor.setPosts(List.of(javaPost));
        //comment.setPost(javaPost);

        // @formatter:on

                // javaTag = tagService.save(javaTag);
                philAuthor = authorService.save(philAuthor);
                javaPost = postService.save(javaPost);

                // bestPractice.setPosts(List.of(javaPost));
                // bestPractice = tagService.save(bestPractice);
                // javaPost.setTags(List.of(bestPractice));
                // javaPost = postService.save(javaPost);
                // tagPost(javaPost, bestPractice);

                comment.setPost(javaPost);
                commentService.save(comment);
                // javaPost.setComments(List.of(comment));
                // javaPost = postService.save(javaPost);

                // retrieve fresh
                // Post testPost = postService.findById(javaPost.getId());

                // log system user back out
                SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
                log.info("Loaded sample data");
        }

        @Transactional
        private Post tagPost(Post post, Tag tag) {
                tag.setPosts(List.of(post));
                // bestPractice = tagService.save(bestPractice);
                post.setTags(List.of(tag));
                return postService.save(post);
        }

        private List<Topic> buildSampleTopics() {
                var java = Topic.builder()
                                .name("Java")
                                .description(
                                                "Java related posts. This is a generic topic and there are more suitable topics for specific Java related things such as Spring, Hibernate/JPA, etc")
                                .build();
                var react = Topic.builder()
                                .name("React")
                                .description(
                                                "React related posts. This is a generic topic and there are more suitable topics for specific React related things such as NextJS, hooks, state management, etc")
                                .build();
                var tooling = Topic.builder()
                                .name("Tools")
                                .description(
                                                "Tool related posts. Suitable posts for this topic include IDE, versions of programming runtimes, middleware, etc but should not include libraries")
                                .build();

                return List.of(java, react, tooling);
        }

        private List<Tag> buildSampleTags() {
                var bestPractice = Tag.builder()
                                .name("Best Practice")
                                .description("Best practice related posts")
                                .build();
                var antiPattern = Tag.builder()
                                .name("Anti-pattern")
                                .description("Bad habits and dead ends... things we should avoid. Try not to be too opinionated though")
                                .build();
                var trend = Tag.builder()
                                .name("Trend")
                                .description(
                                                "Not widely accepted but currently a trend to watch and see if it's just a fad or will eventually become widespread")
                                .build();
                return List.of(bestPractice, antiPattern, trend);
        }

}
