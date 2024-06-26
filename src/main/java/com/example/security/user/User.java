package com.example.security.user;

import com.example.security.post.Post;
import com.example.security.post.comment.Comment;
import com.example.security.product.Product;
import com.example.security.user.follow.Follow;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;

    private LocalDate birthdate;
    private String imageUrl;
    private Long phone;
    private String address;
    private String bio;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "publisher",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Product> publishedProducts;

    @OneToMany(mappedBy = "owner",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Post> publishedPosts;


    @ManyToMany
    @JoinTable(name = "_user_liked_products",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> likedProducts = new ArrayList<>();

    @ManyToMany(mappedBy = "product_reports")
    private Set<Product> productsToReport = new HashSet<>();

    @ManyToMany(mappedBy = "post_reports")
    private Set<Post> postsToReport = new HashSet<>();

    @ManyToMany(mappedBy = "likes")
    @JsonIgnore
    private Set<Post> likedPosts = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> userComments;


    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL)
    private Set<Follow> follows = new HashSet<>();

    private int numberOfFollowers;
    private int numberOfFollowing;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
