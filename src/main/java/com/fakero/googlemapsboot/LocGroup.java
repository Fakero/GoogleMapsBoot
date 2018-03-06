/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fakero.googlemapsboot;

import java.util.ArrayList;
import java.util.List;
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
@Table(name = "locgroups")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocGroup extends AbstractPersistable<Long> {
    
    private String name;
    
    @OneToMany(mappedBy = "locagroup")
    private List<Location> locations;
    
    public LocGroup(String name){
        this.name = name;
        locations = new ArrayList<>();
    }
    
    public void addLocation(Location location){
        this.locations.add(location);
        location.setLocagroup(this);
    }
    
    public void removeLocation(Location location){
        this.locations.remove(location);
        location.setLocagroup(null);
    }
}
