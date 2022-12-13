package blog.example.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name="blog")
public class BlogEntity {
	public BlogEntity(String blogTitle, String fileName, String categoryName, String message, Long userId) {
		this.blogTitle = blogTitle;
		this.blogImage = fileName;
		this.categoryName = categoryName;
		this.message = message;
		this.userId = userId;
	}
	@Id
	@Column(name="blog_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long blogId;

	@NonNull
	@Column(name="blog_title")
	private String blogTitle;

	@NonNull
	@Column(name="blog_image")
	private String blogImage;

	@Column(name="category_Name")
	private String categoryName;

	@NonNull
	@Column(name="message")
	private String message;

	@Column(name="user_Id")
	private Long userId;
}
