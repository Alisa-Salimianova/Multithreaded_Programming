package io.github.alisa_salimianova.service;

import org.springframework.stereotype.Service;
import io.github.alisa_salimianova.exception.NotFoundException;
import io.github.alisa_salimianova.model.Post;
import io.github.alisa_salimianova.repository.PostRepository;

import java.util.List;

@Service
public class PostService {
    private final PostRepository repository;

    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    public List<Post> all() {
        return repository.all();
    }

    public Post getById(long id) {
        return repository.getById(id).orElseThrow(() ->
                new NotFoundException("Post not found with id: " + id));
    }

    public Post save(Post post) {
        return repository.save(post);
    }

    public void removeById(long id) {
        repository.removeById(id);
    }
}