package domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "brands")
public class Brands extends BaseEntity {
    @Id @GeneratedValue
    private Long id;

    private String name;

}
