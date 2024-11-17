package telran.forum.test;

import telran.forum.dao.ForumImpl;
import telran.forum.model.Post;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class InterfaceTest {

    private ForumImpl forum;
    private Post[] posts;
    private int size;
    private LocalDateTime now = LocalDateTime.now();

    @org.junit.jupiter.api.BeforeEach
    void setUp() {

        forum = new ForumImpl(new Post[0]);

        posts = new Post[5];
        posts[0] = new Post(11, "Java Basics", "John Doe", "This post covers the basics of Java programming.");
        posts[1] = new Post(22, "Understanding OOP", "Jane Smith", "This article explains the key concepts of Object-Oriented Programming.");
        posts[2] = new Post(33, "Getting Started with Web Development", "Bob Brown", "This post guides you through the basics of web development.");
        posts[3] = new Post(44, "Python for Beginners", "John Doe", "A beginner's guide to starting with Python programming.");
        posts[4] = new Post(55, "Data Structures 101", "John Doe", "An introduction to various data structures in programming.");

        posts[0].setDate(now.minusDays(5));
        posts[1].setDate(now.minusDays(2));
        posts[2].setDate(now.plusDays(3));
        posts[3].setDate(now.plusDays(1));
        posts[4].setDate(now.plusHours(8));

        Arrays.sort(posts, (p1, p2) -> p1.getDate().compareTo(p2.getDate()));

        for (int i = 0; i < posts.length; i++) {
            forum.addPost(posts[i]);
        }
    }

    @org.junit.jupiter.api.Test
    void testAddPost() {

        Post newPost = new Post(66, "Top 10 Algorithms and Data Structures for Competitive Programming", "Alice Johnson", "In this post, we will discuss Important top 10 algorithms and data structures for competitive coding");

        assertFalse(forum.addPost(null));
        assertFalse(forum.addPost(posts[2]));
        assertTrue(forum.addPost(newPost));
        assertEquals(6, forum.size());

    }

    @Test
    void testRemovePost() {
        assertTrue(forum.removePost(posts[4].getPostId()));
        assertEquals(4, forum.size());
    }

    @Test
    void testUpdatePost() {
        String test = "This is post for testing";
        int testId = posts[2].getPostId();
        assertTrue(forum.updatePost(testId, test));
        assertEquals(forum.getPostById(testId).getContent(), test);
    }

    @Test
    void testGetPostById() {
        Post testPost = posts[2];
        assertEquals(testPost, forum.getPostById(testPost.getPostId()));
        assertNull(forum.getPostById(66));
    }

    @Test
    void testGetPostsByAuthor() {
        Post[] expected = {posts[0], posts[2], posts[3]};
        Post[] res = forum.getPostsByAuthor("John Doe");

        assertArrayEquals(expected, res);
        assertArrayEquals(new Post[0], forum.getPostsByAuthor("Vasiliy Pechkin"));
    }

    @Test
    void testGetPostsByAuthorOverride() {
        Post[] expected = {posts[2], posts[3]};
        Post[] res = forum.getPostsByAuthor("John Doe", LocalDate.now().minusDays(1), LocalDate.now().plusDays(2));

        assertArrayEquals(expected, res);
    }

    @Test
    void testSize() {
        assertEquals(5, forum.size());
    }
}