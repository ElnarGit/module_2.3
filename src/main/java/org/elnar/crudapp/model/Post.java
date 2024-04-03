package org.elnar.crudapp.model;

import jakarta.persistence.*;
import lombok.*;
import org.elnar.crudapp.enums.PostStatus;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "posts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "labels")
public class Post {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "content")
	private String content;
	
	@Column(name = "created", updatable = false)
	private Date created;
	
	@Column(name = "updated")
	private Date updated;
	
	@Column(name = "post_status")
	@Enumerated(EnumType.STRING)
	private PostStatus postStatus;
	
	@ManyToOne
	@JoinColumn(name = "writer_id", referencedColumnName = "id")
	private Writer writer;
	
	@ManyToMany
	@JoinTable(
			name = "post_label",
			joinColumns = @JoinColumn(name = "post_id"),
			inverseJoinColumns = @JoinColumn(name = "label_id")
	)
	private List<Label> labels;
}