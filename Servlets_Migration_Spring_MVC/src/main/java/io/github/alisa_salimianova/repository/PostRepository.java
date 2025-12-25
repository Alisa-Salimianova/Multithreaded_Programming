package io.github.alisa_salimianova.repository;

import org.springframework.stereotype.Repository;
import io.github.alisa_salimianova.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepository {
    private final ConcurrentHashMap<Long, Post> posts = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public List<Post> all() {
        return new ArrayList<>(posts.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(posts.get(id));
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            long newId = nextId.getAndIncrement();
            post.setId(newId);
            posts.put(newId, post);
            return post;
        } else {
            posts.put(post.getId(), post);
            return posts.get(post.getId());
        }
    }

    public void removeById(long id) {
        posts.remove(id);
    }
}