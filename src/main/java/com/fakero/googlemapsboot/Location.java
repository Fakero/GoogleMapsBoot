/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fakero.googlemapsboot;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 *
 * @author fakero
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location extends AbstractPersistable<Long> {
    
    private String name;
    private double lat;
    private double lng;
    
    @ManyToOne(targetEntity = LocGroup.class)
    @JoinColumn(name = "locagroup_id")
    private LocGroup locagroup;
    
    public Location(String name, double latitude, double longitude){
        this.name = name;
        this.lat = latitude;
        this.lng = longitude;
    }
}
