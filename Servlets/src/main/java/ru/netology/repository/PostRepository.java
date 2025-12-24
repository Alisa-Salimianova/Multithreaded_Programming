package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class PostRepository {
    private final ConcurrentHashMap<Long, Post> posts = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public List<Post> all() {
        // Возвращаем копию списка для безопасности
        return new ArrayList<>(posts.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(posts.get(id));
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            // Создание нового поста
            long newId = nextId.getAndIncrement();
            Post newPost = new Post(newId, post.getContent());
            posts.put(newId, newPost);
            return newPost;
        } else {
            // Обновление существующего поста
            return posts.compute(post.getId(), (id, existingPost) -> {
                if (existingPost == null) {
                    // Если пост не найден, создаем новый с указанным ID
                    // Если нужно другое поведение (например, бросать исключение),
                    // можно раскомментировать строку ниже
                    // throw new NotFoundException("Post not found with id: " + post.getId());
                    return new Post(post.getId(), post.getContent());
                } else {
                    existingPost.setContent(post.getContent());
                    return existingPost;
                }
            });
        }
    }

    public void removeById(long id) {
        Post removed = posts.remove(id);
        if (removed == null) {
            throw new NotFoundException("Post not found with id: " + id);
        }
    }
}