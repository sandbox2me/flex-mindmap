package com.serviceops.architecture.servicesarchitecture;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ServiceImpl implements ServiceI {




    @Autowired
     JdbcTemplate jdbcTemplate;

    @Override
    public  List<Map<String,Object>> getAppData(String process, String group,String factory) {

        String whc = "";

        if(!StringUtils.isEmpty(group)){
            whc = " and pgroup = '"+ group +"'";

            if(!process.equals("0")){
                whc = whc + " and process = '"+process + "'";
            }


        }





        String sql = "select distinct SOURCE AS \"id\", SOURCE as \"label\" from TEST where Factory='"+factory+"' " + whc;

        List<Map<String,Object>> source = jdbcTemplate.queryForList(sql);


        sql = "select distinct TARGET AS \"id\", TARGET as \"label\" from TEST where Factory='"+factory+"' " + whc;

        List<Map<String,Object>> targets = jdbcTemplate.queryForList(sql);

        for(Map<String,Object> target : targets){
            if(!source.contains(target)){
                source.add(target);
            }
        }


       // source.addAll(jdbcTemplate.queryForList(sql));


        return source;
    }

    @Override
    public List<Map<String,Object>> getLinks(String process, String group,String factory){

        String whc = "";

        if(!StringUtils.isEmpty(group)){
            whc = " and pgroup = '"+ group +"'";

            if(!process.equals("0")){
                whc = whc + " and process = '"+ process + "'";
            }


        }


        String sql = "select SOURCE as \"from\", TARGET as \"to\", 'to' as \"arrows\", InterfaceName as \"label\",CONCAT(Factory,'#',pgroup,'#',process,'#',SOURCE,'#',TARGET,'#',InterfaceName,'#',IntegrationPattern) as \"id\"  from TEST where Factory='"+factory+"' " + whc;
        List<Map<String,Object>> links = jdbcTemplate.queryForList(sql);
        return links;

    }

    @Override
    public void load(){
        //jdbcTemplate.execute("drop table TEST");
        String sql = "CREATE TABLE TEST AS SELECT * FROM CSVREAD('classpath:test.csv')";
        jdbcTemplate.execute(sql);
    }


    @Override
    public Response getProcesses(String factory){
        String sql = "select distinct pgroup from TEST where Factory='"+ factory +"'";

        List<Map<String,Object>> pgroups = jdbcTemplate.queryForList(sql);
        List<Map<String,Object>> resp = new ArrayList<Map<String,Object>>();
        for(Map<String,Object> pgroup : pgroups){
            Map<String,Object> parent = new HashMap<String,Object>();
            String pgroupname = (String)pgroup.get("pgroup");
            parent.put("name",pgroupname);

            sql = "select distinct process from TEST where Factory='"+ factory +"' and  pgroup='"+pgroupname+"'";
            List<Map<String,Object>> processes = jdbcTemplate.queryForList(sql);
            List<Map<String,Object>> childs = new ArrayList<Map<String,Object>>();

            for(Map<String,Object> process : processes){

                Map<String,Object> child = new HashMap<String,Object>();
                String processname = (String)process.get("process");
                child.put("name", process.get("process"));

                sql = "select count(InterfaceName) from TEST where Factory='"+ factory +"' and  pgroup = '"+pgroupname+"' and process = '"+processname+"'";
                Integer intcount = jdbcTemplate.queryForObject(sql,Integer.class);
                child.put("size",intcount*800);

//                sql = "select InterfaceName, Source, Target from TEST where pgroup = '"+pgroupname+"' and process = '"+processname+"'";
//
//                List<Map<String,Object>> interfacenames = jdbcTemplate.queryForList(sql);
//
//                List<Map<String,Object>> ilist = new ArrayList<Map<String,Object>>();
//
//                for(Map<String,Object> interfacename : interfacenames){
//                    Map<String,Object> idata = new HashMap<String,Object>();
//                    String iname = (String)interfacename.get("InterfaceName");
//                    String source = (String)interfacename.get("Source");
//                    String target = (String)interfacename.get("Target");
//                    idata.put("name",iname);
//
//                    List<Map<String,Object>> applist = new ArrayList<Map<String,Object>>();
//                    Map<String,Object> sourcenode = new HashMap<String,Object>();
//                    sourcenode.put("name",source + "(S)");
//                    Map<String,Object> targetnode = new HashMap<String,Object>();
//                    targetnode.put("name",target + "(T)");
//
//                    applist.add(sourcenode);
//                    applist.add(targetnode);
//                    idata.put("children",applist);
//                    ilist.add(idata);
//
//                }
//                child.put("children",ilist);




                childs.add(child);


            }
            parent.put("children",childs);
            resp.add(parent);



        }


        Response response = new Response();
        response.name = "Service Ops";
        response.children = resp;


        return response;

    }



    @Override
    public Response getTree(String factory){
        String sql = "select distinct pgroup from TEST where Factory='"+factory+"'";

        List<Map<String,Object>> pgroups = jdbcTemplate.queryForList(sql);
        List<Map<String,Object>> resp = new ArrayList<Map<String,Object>>();
        for(Map<String,Object> pgroup : pgroups){
            Map<String,Object> parent = new HashMap<String,Object>();
            String pgroupname = (String)pgroup.get("pgroup");
            parent.put("name",pgroupname);

            sql = "select distinct process from TEST where Factory='"+ factory +"' and pgroup='"+pgroupname+"'";
            List<Map<String,Object>> processes = jdbcTemplate.queryForList(sql);
            List<Map<String,Object>> childs = new ArrayList<Map<String,Object>>();

            for(Map<String,Object> process : processes){

                Map<String,Object> child = new HashMap<String,Object>();
                String processname = (String)process.get("process");
                child.put("name", process.get("process"));

                sql = "select count(InterfaceName) from TEST where Factory='"+ factory +"' and  pgroup = '"+pgroupname+"' and process = '"+processname+"'";
                Integer intcount = jdbcTemplate.queryForObject(sql,Integer.class);
                child.put("size",intcount*800);

                sql = "select InterfaceName, Source, Target from TEST where Factory='"+ factory +"' and  pgroup = '"+pgroupname+"' and process = '"+processname+"'";

                List<Map<String,Object>> interfacenames = jdbcTemplate.queryForList(sql);

                List<Map<String,Object>> ilist = new ArrayList<Map<String,Object>>();

                for(Map<String,Object> interfacename : interfacenames){
                    Map<String,Object> idata = new HashMap<String,Object>();
                    String iname = (String)interfacename.get("InterfaceName");
                    String source = (String)interfacename.get("Source");
                    String target = (String)interfacename.get("Target");
                    idata.put("name",iname);

                    List<Map<String,Object>> applist = new ArrayList<Map<String,Object>>();
                    Map<String,Object> sourcenode = new HashMap<String,Object>();
                    sourcenode.put("name",source + "(S)");
                    Map<String,Object> targetnode = new HashMap<String,Object>();
                    targetnode.put("name",target + "(T)");

                    applist.add(sourcenode);
                    applist.add(targetnode);
                    idata.put("children",applist);
                    ilist.add(idata);

                }
                child.put("children",ilist);




                childs.add(child);


            }
            parent.put("children",childs);
            resp.add(parent);



        }


        Response response = new Response();
        response.name = factory;
        response.children = resp;


        return response;

    }



}

class Response{
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Map<String,Object>> getChildren() {
        return children;
    }

    public void setChildren(List<Map<String,Object>> children) {
        this.children = children;
    }

    List<Map<String,Object>> children;

}


