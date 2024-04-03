package org.elnar.crudapp.model;

import jakarta.persistence.*;
import lombok.*;
import org.elnar.crudapp.enums.WriterStatus;

import java.util.List;

@Entity
@Table(name = "writers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "posts")
public class Writer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "firstname")
	private String firstname;
	
	@Column(name = "lastname")
	private String lastname;
	
	@Column(name = "writer_status")
	@Enumerated(EnumType.STRING)
	private WriterStatus writerStatus;
	
	@OneToMany(mappedBy = "writer", cascade = CascadeType.PERSIST)
	private List<Post> posts;
}
