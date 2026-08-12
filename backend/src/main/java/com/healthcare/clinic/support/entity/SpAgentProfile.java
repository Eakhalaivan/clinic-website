package com.healthcare.clinic.support.entity;

import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.branch.entity.Branch;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "sp_agent_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpAgentProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Column(name = "max_concurrent_tickets")
    private Integer maxConcurrentTickets;

    @Column(name = "current_active_tickets")
    private Integer currentActiveTickets;

    @Column(name = "primary_skills")
    private String primarySkills;

    @Column(name = "is_available")
    private Boolean isAvailable;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
}
