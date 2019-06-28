package com.serviceops.architecture.servicesarchitecture;


import org.springframework.stereotype.Component;


import java.util.List;
import java.util.Map;

@Component
public interface ServiceI {
    public List<Map<String,Object>> getAppData(String process, String group,String factory);
    public List<Map<String,Object>> getLinks(String process, String group, String factory);
    public Response getProcesses(String factory);
    public Response getTree(String factory);
    public void load();
}
