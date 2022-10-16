package itstep.social_freedom.controller;


import itstep.social_freedom.entity.Category;
import itstep.social_freedom.entity.Post;
import itstep.social_freedom.entity.Tag;
import itstep.social_freedom.entity.User;
import itstep.social_freedom.service.CategoryService;
import itstep.social_freedom.service.FileService;
import itstep.social_freedom.service.PostService;
import itstep.social_freedom.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Controller
public class PostController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    @Autowired
    private PostService postService;

    @Autowired
    private FileService fileService;

    @GetMapping("/post/{id}")
    public String index(@PathVariable(name = "id") Long id, Model model) {
        List<Post> posts = postService.allPosts();
        if (!posts.isEmpty()) {
            model.addAttribute("posts", posts);
            return "post/index";
        } else {
            return "redirect:create/" + id;
        }
    }

    @GetMapping("/post/create/{id}")
    public String create(@PathVariable(name = "id") Long id, Model model) {
        model.addAttribute("user_id", id);
        model.addAttribute("post", new Post());
        List<Category> categories = categoryService.allCategory();
        List<Tag> tags = tagService.allTag();
        model.addAttribute("categories", categories);
        model.addAttribute("tags", tags);
        return "post/create-post";
    }

    @GetMapping("/post/store")
    public String store(@ModelAttribute("post") @Valid Post post,
                        BindingResult bindingResult, Model model,
                        @RequestParam(value = "id") Long id,
                        @RequestParam(value = "imgUrl") MultipartFile file,
                        @RequestParam(value = "title") String title,
                        @RequestParam(value = "shortName") String shortName,
                        @RequestParam(value = "category_id") Long category_id,
                        @RequestParam(value = "description") String description,
                        @RequestParam(value = "tag_id") Long[] tag_id) {

        if (bindingResult.hasErrors()) {
            return "post/create-post";
        }

        post.setTitle(title);
        post.setShortName(shortName);
        post.setCategory(categoryService.findCategoryById(category_id));
        post.setBody(description);
        if (tag_id != null) {
            if (tag_id.length != 0) {
                Set<Tag> tagSet = new HashSet<>();
                for (Long t : tag_id) {
                    Tag tag = tagService.findTagById(t);
                    tagSet.add(tag);
                }
                post.setTags(tagSet);
            }
        }

        if (file != null) {
            if (!file.isEmpty())
                post.setImgUrl(fileService.uploadFile(file, "post"));
        }

        return "";
    }

}
