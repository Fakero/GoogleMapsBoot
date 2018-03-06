/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fakero.googlemapsboot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author fakero
 */
@Controller
public class MapController {
    
    @Autowired
    private GroupRepository groups;
    
    @Autowired
    private LocationRepository locations;
    
    @Autowired
    private LocationService service;
    
    @GetMapping("/")
    public String home(Model model){
        List<Listloc> list = new ArrayList<>();
        for (Location loc : locations.findAll()){
            Listloc l = new Listloc(loc.getId(), loc.getName(), loc.getLat(), loc.getLng(), loc.getLocagroup().getId());
            list.add(l);
        }
        model.addAttribute("groupId", 0);
        model.addAttribute("groups", groups.findAll());
        model.addAttribute("list", list);
        return "index";
    }
    
    @GetMapping("/{id}")
    public String grp(Model model, @PathVariable String id){
        if(!id.trim().isEmpty()){
            Long groupId = Long.parseLong(id);
            if(groupId > 0){
                List<Listloc> list = new ArrayList<>();
                LocGroup g = groups.findOne(groupId);
                for (Location loc : g.getLocations()){
                    Listloc l = new Listloc(loc.getId(), loc.getName(), loc.getLat(), loc.getLng(), loc.getLocagroup().getId());
                    list.add(l);
                }
                model.addAttribute("groupName", g.getName());
                model.addAttribute("groupId", groupId);
                model.addAttribute("groups", groups.findAll());
                model.addAttribute("list", list);
                return "index";
            }
        }
        return "redirect:/";
    }
    /*
    @GetMapping("/map")
    public String map(Model model){
        if(groups.findAll().isEmpty() || locations.findAll().isEmpty()){
            return "redirect:/";
        }
        List<Listloc> list = new ArrayList<>();
        for (Location loc : locations.findAll()){
            Listloc l = new Listloc(loc.getId(), loc.getName(), loc.getLat(), loc.getLng(), loc.getLocagroup().getId());
            list.add(l);
        }
        model.addAttribute("groupName", groups.findOne(1L));
        model.addAttribute("groupId", 1L);
        model.addAttribute("groups", groups.findAll());
        model.addAttribute("list", list);
        return "map";
    }
    */
    @GetMapping("/map/{id}")
    public String showGroup(Model model, @PathVariable String id){
        if(groups.findAll().isEmpty() || locations.findAll().isEmpty()){
            return "redirect:/";
        }
        if(!id.trim().isEmpty()){
            if(isNumeric(id)){
                try {
                    List<Listloc> list = new ArrayList<>();
                    Long groupId = Long.parseLong(id);
                    LocGroup g = groups.findOne(groupId);
                    for(Location loc : g.getLocations()){
                        Listloc l = new Listloc(loc.getId(), loc.getName(), loc.getLat(), loc.getLng(), loc.getLocagroup().getId());
                        list.add(l);
                    }
                    model.addAttribute("groupName", g.getName());
                    model.addAttribute("groupId", groupId);
                    model.addAttribute("groups", groups.findAll());
                    model.addAttribute("list", list);
                    return "map";
                }
                catch (Exception e){

                }
            }
            else {
                try {
                    List<Listloc> list = new ArrayList<>();
                    LocGroup g = groups.findOneByName(id);
                    Long groupId = g.getId();
                    for(Location loc : groups.findOneByName(id).getLocations()){
                        Listloc l = new Listloc(loc.getId(), loc.getName(), loc.getLat(), loc.getLng(), loc.getLocagroup().getId());
                        list.add(l);
                    }
                    model.addAttribute("groupName", g.getName());
                    model.addAttribute("groupId", groupId);
                    model.addAttribute("groups", groups.findAll());
                    model.addAttribute("list", list);
                    return "map";
                }
                catch (Exception e){

                }
            }
        }
        return "redirect:/";
    }
    
    @PostMapping("/file")
    public String file(@RequestParam("file") MultipartFile file) throws IOException{
        Long groupId = null;
        if(!file.isEmpty()){
            byte[] b = file.getBytes();
            String data = new String(b);
            String[] splitted = data.split("\r\n");
            String groupName = splitted[0];
            LocGroup g = new LocGroup(groupName);
            for(int i = 1; i < splitted.length; i++){
                Location l = service.getLocationByTownName(splitted[i]);
                g.addLocation(l);
            }
            groups.saveAndFlush(g);
            locations.save(g.getLocations());
            groupId = g.getId();
            locations.flush();
        }
        if(groupId != null)
            return "redirect:/map/" + groupId.toString();
        else
            return "redirect:/";
    }
    
    @GetMapping("/dummy")
    public String dummy(){
        addDummy();
        return "redirect:/";
    }
    
    @PostMapping("/group")
    public String addGroup(@RequestParam String name){
        Long groupId = null;
        if(!name.trim().isEmpty()){
            LocGroup g = new LocGroup(name);
            groups.saveAndFlush(g);
            groupId = g.getId();
        }
        if (groupId != null)
            return "redirect:/map/" + groupId.toString();
        else
            return "redirect:/";
    }
    
    @PostMapping("/location")
    public String addLocation(@RequestParam String name, @RequestParam Long groupId){
        if(!name.trim().isEmpty()){
            LocGroup g = groups.findOne(groupId);
            Location l = service.getLocationByTownName(name);
            g.addLocation(l);
            groups.save(g);
            locations.save(l);
        }
        return "redirect:/map/" + groupId.toString();
    }
    
    @DeleteMapping("/location/{id}")
    public String deleteLocation(@PathVariable String id){
        Long locationId = Long.parseLong(id);
        Long groupId = null;
        
        if(locationId > 0){
            Location l = locations.findOne(locationId);
            groupId = l.getLocagroup().getId();
            LocGroup g = groups.findOne(groupId);
            g.removeLocation(l);
            locations.delete(l);
            groups.saveAndFlush(g);
        }
        if(groupId != null)
            return "redirect:/map/" + groupId.toString();
        else
            return "redirect:/";
    }
    
    @DeleteMapping("/group/{id}")
    @Transactional
    public String deleteGroup(@PathVariable Long id){
        LocGroup g = groups.findOne(id);
        locations.delete(g.getLocations());
        groups.delete(id);
        return "redirect:/";
    }
    
    private void addDummy(){
        LocGroup g = new LocGroup("Test");
        Location l = service.getLocationByTownName("Seinäjoki");
        g.addLocation(l);
        groups.saveAndFlush(g);
        locations.saveAndFlush(l);
    }
    
    private static boolean isNumeric(String text){
        try {
            Long.parseLong(text);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
}
