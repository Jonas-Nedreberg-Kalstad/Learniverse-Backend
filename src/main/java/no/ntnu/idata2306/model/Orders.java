package no.ntnu.idata2306.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Order details", name = "Orders")
@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    @Schema(description = "Order ID")
    private int id;

    @Column(name = "price", nullable = false)
    @Schema(description = "Order price")
    private BigDecimal price;

    @Column(name = "created", nullable = false)
    @Schema(description = "Creation date of the order")
    private LocalDateTime created;

    @Column(name = "updated", nullable = true)
    @Schema(description = "Update date of the order")
    private LocalDateTime updated;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    @JsonBackReference
    @Schema(description = "Course associated with the order")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonBackReference
    @Schema(description = "User who placed the order")
    private User user;

    @ManyToOne
    @JoinColumn(name = "order_status_id", referencedColumnName = "id")
    @JsonBackReference
    @Schema(description = "Order status")
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "payment_id", referencedColumnName = "id")
    @JsonBackReference
    @Schema(description = "Payment associated with the order")
    private Payment payment;

    @OneToMany(mappedBy = "orders")
    @Schema(description = "Enrollments associated with the order")
    private Set<CourseEnrollments> enrollments = new LinkedHashSet<>();
}