package com.example.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "userpermission")
public class Userpermission {
    @Id
    @Column(name = "iduserpermission", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idpermission", nullable = false)
    private com.example.sni.Permission idpermission;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "iduser", nullable = false)
    private com.example.sni.User iduser;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idtopic", nullable = false)
    private com.example.sni.Topic idtopic;

}