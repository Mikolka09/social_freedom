package itstep.social_freedom.service;

import itstep.social_freedom.entity.Post;
import itstep.social_freedom.entity.User;
import itstep.social_freedom.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Service
public class PostService {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private PostRepository postRepository;

    public List<Post> allPosts(Long id) {
        List<Post> posts = em.createQuery("SELECT p FROM Post p WHERE p.user.id = :paramId", Post.class)
                .setParameter("paramId", id).getResultList();
        return posts;
    }

    public boolean savePost(Post post) {
        if (post != null) {
            postRepository.save(post);
            return true;
        } else {
            return false;
        }
    }
}
