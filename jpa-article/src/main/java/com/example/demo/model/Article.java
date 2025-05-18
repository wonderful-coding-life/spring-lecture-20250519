package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Table(name = "article")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name="title", length = 256)
    private String title;
    @Column(name="description", length = 4096)
    private String description;
    @CreatedDate
    @Column(name="created")
    private Date created;
    @LastModifiedDate
    @Column(name="updated")
    private Date updated;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
