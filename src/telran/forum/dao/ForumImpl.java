package telran.forum.dao;

import telran.forum.model.Post;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;

public class ForumImpl implements Interface{
    private Post[] posts;
    private int size;
    private final Comparator<Post> comparator = (p1, p2) -> {
        int res = p1.getAuthor().compareTo(p2.getAuthor());
        return res == 0? p1.getDate().compareTo(p2.getDate()) : res;
    };

    public ForumImpl(Post[] posts) {
        this.posts = posts;
        size = posts.length;
    }

    public ForumImpl(){
        this.posts = new Post[0];
        size = 0;
    }

    public Post[] getPosts() {
        return posts;
    }

    @Override
    public boolean addPost(Post post) {
        if (post == null
            || getPostById(post.getPostId()) != null){
            return false;
        }

        Post[] newPosts = Arrays.copyOf(posts, ++size);
        int index = Arrays.binarySearch(newPosts, 0, posts.length, post, comparator);
        index = index < 0? -index - 1: index;
        System.arraycopy(newPosts, index, newPosts, index + 1, size - index - 1);
        newPosts[index] = post;
        posts = newPosts;

        return true;
    }

    @Override
    public boolean removePost(int postId) {

        Post res = getPostById(postId);
        if (res == null) {
            return true;
        }

        for (int i = 0; i < size; i++) {
            if (res.equals(posts[i])) {
                System.arraycopy(posts, i + 1, posts, i, size - i - 1);
                posts[--size] = null;
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean updatePost(int postId, String content) {
        Post post = getPostById(postId);
        if (post == null){
            return false;
        }

        if (post.getContent().equals(content)){
            return true;
        }

        post.setContent(content);
        return true;
    }

    @Override
    public Post getPostById(int postId) {

        for (int i = 0; i < posts.length; i++) {
            if (posts[i].getPostId() == postId) {
                return posts[i];
            }
        }
        return null;
    }

    @Override
    public Post[] getPostsByAuthor(String author) {
        Post[] res = new Post[size];
        int j = 0;
        for (int i = 0; i < size; i++) {
            if (posts[i].getAuthor().equals(author)){
                res[j++] = posts[i];
            }
        }

        return Arrays.copyOfRange(res, 0, j);
    }

    @Override
    public Post[] getPostsByAuthor(String author, LocalDate dateFrom, LocalDate dateTo) {

        int indexFrom = getIndexOfElement(Integer.MIN_VALUE, author, dateFrom);
        int indexto = getIndexOfElement(Integer.MAX_VALUE - 1, author, dateTo);

        return Arrays.copyOfRange(posts, indexFrom, indexto);
    }

    private int getIndexOfElement(int id, String author, LocalDate date) {

        Post pattern = new Post(id, "", author, "");
        pattern.setDate(date.atStartOfDay());

        int index = Arrays.binarySearch(posts, 0, size, pattern, comparator);
        return index > 0? index: -index - 1;

    }

    @Override
    public int size() {
        return size;
    }
}
