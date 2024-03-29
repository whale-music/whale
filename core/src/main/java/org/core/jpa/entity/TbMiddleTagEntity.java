package org.core.jpa.entity;

import jakarta.persistence.*;
import org.core.jpa.config.ManualInsertGenerator;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tb_middle_tag")
public class TbMiddleTagEntity implements Serializable {
    public static final long serialVersionUID = 7501414934794658903L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "middle_id", nullable = false)
    private Long middleId;
    @Basic
    @Column(name = "tag_id", nullable = false)
    private Long tagId;
    @Basic
    @Column(name = "type", nullable = false)
    private Byte type;
    @ManyToOne
    @JoinColumn(name = "tag_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private TbTagEntity tbTagByTagId;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getMiddleId() {
        return middleId;
    }
    
    public void setMiddleId(Long middleId) {
        this.middleId = middleId;
    }
    
    public Long getTagId() {
        return tagId;
    }
    
    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }
    
    public Byte getType() {
        return type;
    }
    
    public void setType(Byte type) {
        this.type = type;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TbMiddleTagEntity that = (TbMiddleTagEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(middleId, that.middleId) && Objects.equals(tagId,
                that.tagId) && Objects.equals(type, that.type);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, middleId, tagId, type);
    }
    
    public TbTagEntity getTbTagByTagId() {
        return tbTagByTagId;
    }
    
    public void setTbTagByTagId(TbTagEntity tbTagByTagId) {
        this.tbTagByTagId = tbTagByTagId;
    }
}
