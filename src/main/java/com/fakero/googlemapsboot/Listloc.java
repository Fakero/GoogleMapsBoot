/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fakero.googlemapsboot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author fakero
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Listloc {
    private Long id;
    private String name;
    private double lat;
    private double lng;
    private Long grpId;
}
