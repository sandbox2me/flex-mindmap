package com.serviceops.architecture.servicesarchitecture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
public class ArchApi {

    @Autowired
    ServiceI service;




    @GetMapping("/getApps/{factory}/{group}/{process}")
    public List<Map<String,Object>> getApps(@PathVariable String group, @PathVariable  String process,@PathVariable  String factory){
        Map data = new HashMap();
        data.put("process",process);
        data.put("group",group);
        List list = new ArrayList();
        list.add(data);

        return service.getAppData(process,group,factory);
    }

    @GetMapping("/getLinks/{factory}/{group}/{process}")
    public List<Map<String,Object>> getLinks(@PathVariable String group, @PathVariable  String process,@PathVariable  String factory){
        Map data = new HashMap();
        data.put("process",process);
        data.put("group",group);
        List list = new ArrayList();
        list.add(data);
        return service.getLinks(process,group,factory);
    }

    @GetMapping("/getAppLinks/{factory}/{group}/{process}")
    public Resp getAppLinks(@PathVariable String group, @PathVariable  String process,@PathVariable  String factory){
        Map data = new HashMap();
        data.put("process",process);
        data.put("group",group);
        List list = new ArrayList();
        list.add(data);
        return new Resp(service.getAppData(process,group,factory),service.getLinks(process,group,factory));
    }


    @GetMapping("/getProcesses/{factory}")
    public Response getProcesses(@PathVariable  String factory){
        return service.getProcesses(factory);
    }

    @GetMapping("/getTree")
    public List<Map<String,Object>> getTree(){

        List<Response> childs = new ArrayList<Response>();
        Response resp = new Response();
        resp.name = "Agent Enablement";
        resp.children = service.getTree("Agent Enablement").children;
        childs.add(resp);
        resp = new Response();
        resp.name = "DIP";
        resp.children = service.getTree("DIP").children;
        childs.add(resp);

        List<Map<String,Object>> finalresp = new ArrayList<Map<String,Object>>();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("name", "Service Ops");
        map.put("children", childs);
        finalresp.add(map);


        return finalresp;
    }





    @GetMapping("/load")
    public String load(){
        service.load();
        return "Done";
    }

    @PostMapping("/createApps")
    public Input postData(@RequestBody Input input){
        return input;
    }
}

class Input{
    String input1;

    public String getInput2() {
        return input2;
    }

    public void setInput2(String input2) {
        this.input2 = input2;
    }

    String input2;


    public String getInput1() {

        return input1;
    }

    public void setInput1(String input1) {
        this.input1 = input1;
    }
}

class Resp{
    List<Map<String,Object>> apps;
    List<Map<String,Object>> links;

    public Resp(List<Map<String, Object>> apps, List<Map<String, Object>> links) {
        this.apps = apps;
        this.links = links;
    }

    public List<Map<String, Object>> getApps() {
        return apps;
    }

    public void setApps(List<Map<String, Object>> apps) {
        this.apps = apps;
    }

    public List<Map<String, Object>> getLinks() {
        return links;
    }

    public void setLinks(List<Map<String, Object>> links) {
        this.links = links;
    }
}


