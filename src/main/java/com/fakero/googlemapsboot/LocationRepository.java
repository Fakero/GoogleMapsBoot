/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fakero.googlemapsboot;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author fakero
 */
public interface LocationRepository extends JpaRepository<Location, Long> {
    
}
