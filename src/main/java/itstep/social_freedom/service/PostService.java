package itstep.social_freedom.service;

import itstep.social_freedom.entity.Post;
import itstep.social_freedom.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class PostService {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private PostRepository postRepository;

    public List<Post> allPosts(Long id) {
        return postRepository.findAll().stream().filter(x -> Objects.equals(x.getUser().getId(), id))
                .collect(Collectors.toList());
    }

    public List<Post> posts() {
        return postRepository.findAll();
    }

    public boolean savePost(Post post) {
        if (post != null) {
            postRepository.save(post);
            return true;
        } else {
            return false;
        }
    }

    public Post findPostById(Long post_id) {
        return postRepository.findById(post_id).orElse(new Post());
    }

    public void deletePost(Long postId) {
        if (postRepository.findById(postId).isPresent()) {
            Post post = findPostById(postId);
            postRepository.delete(post);
        }
    }

    public String[] arrayBody(String body) {
        String[] arr = body.split("\n");
        return arr;
    }
}
