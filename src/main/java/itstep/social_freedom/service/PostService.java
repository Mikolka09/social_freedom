package itstep.social_freedom.service;

import itstep.social_freedom.entity.Post;
import itstep.social_freedom.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<Post> allPosts() {
        return postRepository.findAll();
    }
}
