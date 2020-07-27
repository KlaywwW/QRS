package com.starvincci.qrcodeadmin.controller;

import com.alibaba.fastjson.JSON;
import com.starvincci.qrcodeadmin.pojo.*;
import com.starvincci.qrcodeadmin.service.ScandataServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.PublicKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

//运行时在pom文件中加入新的依赖需要重启项目，不重启重新编译会出现java.lang.NoClassDefFoundError

/**
 * @author jdp
 */
//@Controller
@RestController
public class QRController {

    @Resource
    private ScandataServiceImpl scandataService;

    @PostMapping(value = "/home",produces = "application/json;charset=UTF-8")
    public int show(@RequestBody QRParam qrParam) {

//        System.out.println("str---" + strs);
//        QRParam qrParam = JSON.parseObject(strs, QRParam.class);
        System.out.println(qrParam.toString());
        //截取二维码信息
        String str = qrParam.getStrs();
        String[] splitstr = str.split("@");
        int splistlength = splitstr.length;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        SimpleDateFormat dfTime= new SimpleDateFormat("HH:mm:ss");


        String time = df.format(new Date());// new Date()为获取当前系统时间
        String time2= dfTime.format(new Date());

        Scandata scandata = null;
        if (splistlength == 5) {
            //截取工序信息
            String strfacno = qrParam.getFacnostr();
            String[] splitno = strfacno.split("\\+");
            int splitnolength = splitno.length;

            for (int i = 0; i < splitnolength; i++) {


                scandata = new Scandata();
                //二维码数据
                scandata.setPrdno(splitstr[0]);
                scandata.setFacno(splitstr[1]);
                scandata.setBedno(Integer.parseInt(splitstr[2]));
                scandata.setSeq(Integer.parseInt(splitstr[3]));
                scandata.setQty(Integer.parseInt(splitstr[4]));
                //循环添加工序
                scandata.setItem(splitno[i]);
                //工号
                scandata.setWorkno(qrParam.getJobNum());
                scandata.setName(qrParam.getName());
                //日期
                scandata.setRecdate(time);
                scandata.setRecdate1(time);
                //hh:mm:ss
                scandata.setTime(time2);
                Scandata scan = scandataService.checkOnly(scandata);
                if (scan == null) {
                    //添加
                    int res = scandataService.addScandata(scandata);
                    if (res > 0) {
                        System.out.println("添加成功");
                    }
                    System.out.println(scandata.toString());
                } else {
                    System.out.println("该数据已存在");
                    return 0;
                }
            }


        } else if (splistlength == 6) {
            scandata = new Scandata();
            //二维码数据
            scandata.setPrdno(splitstr[0]);
            scandata.setFacno(splitstr[1]);
            scandata.setBedno(Integer.parseInt(splitstr[2]));
            scandata.setSeq(Integer.parseInt(splitstr[3]));
            scandata.setItem(splitstr[4]);
            scandata.setQty(Integer.parseInt(splitstr[5]));
            //工号
            scandata.setWorkno(qrParam.getJobNum());
            scandata.setName(qrParam.getName());
            //日期
            scandata.setRecdate(time);
            scandata.setRecdate1(time);
            //hh:mm:ss
            scandata.setTime(time2);
            Scandata scan = scandataService.checkOnly(scandata);
            if (scan == null) {
                //添加
                int res = scandataService.addScandata(scandata);
                if (res > 0) {
                    System.out.println("添加成功");
                }
            } else {
                System.out.println("该数据已存在");
                return 0;
            }

        }
        return 1;
    }

    @PostMapping(value = "/get",produces = "application/json;charset=UTF-8")
    public String getinfo(@RequestBody String workno) {
        System.out.println(workno);
        Scandata scandata = new Scandata();
        scandata.setWorkno(workno);
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String time = df2.format(new Date());
        scandata.setRecdate(time);

        //前一天
        //Calendar calendar =new GregorianCalendar();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date zero = calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String zerotime = df.format(zero);
        scandata.setZerodate(zerotime);

        List<Scandata> list = scandataService.selectScandataByworkNo(scandata);
        String jsonstr = JSON.toJSONString(list);
        System.out.println(jsonstr);
        return jsonstr;
    }

    @PostMapping(value = "/getBydate",produces = "application/json;charset=UTF-8")
//    @ResponseBody
    public List<Scandata> getinfoBydate(@RequestBody String jsons) {
        QRParam qrParam = JSON.parseObject(jsons, QRParam.class);
        System.out.println(qrParam.toString());
        System.out.println(jsons);
        Scandata scandata = new Scandata();
        scandata.setZerodate(qrParam.getStartDate());
        String d=qrParam.getEndDate();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String nextDay=null;
        try {
            Date date=sdf.parse(d);
            Calendar cld = Calendar.getInstance();
            cld.setTime(date);
            cld.add(Calendar.DATE, 1);
            date = cld.getTime();
            //获得下一天日期字符串
            nextDay = sdf.format(date);
            System.out.println("-----"+nextDay);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(nextDay);

        scandata.setRecdate(qrParam.getEndDate());
        scandata.setWorkno(qrParam.getJobNum());
        List<Scandata> list = scandataService.selectScandaaByDate(scandata);
        for (Scandata sca:list) {
            System.out.println(sca.getRecdate1());
        }
        System.out.println(list.size());
        String jsonstr = JSON.toJSONString(list);
        System.out.println(jsonstr);
        return list;
    }

    @PostMapping(value = "/getmingxi",produces = "application/json;charset=UTF-8")
    public String getmingxi(@RequestBody Scandata jsons) {
        System.out.println(jsons);
        List<Scandata> list = scandataService.selectScandInfo(jsons);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String jsonstr = JSON.toJSONString(list);
        System.out.println(jsonstr);
        return jsonstr;
    }

    @GetMapping(value = "/getfacno",produces = "application/json;charset=UTF-8")
    public List<Process> getfacno(@RequestParam String facno) {
        System.out.println(facno);
        List<Process> list = scandataService.getPrdmoedl(facno);
        String jsonstr = JSON.toJSONString(list);
        System.out.println(jsonstr);
        return list;
    }

    @GetMapping(value = "/getname",produces = "application/json;charset=UTF-8")
//    @ResponseBody
    public String getname(@RequestParam String workno){
//        System.out.println(name);
        Employee employee=scandataService.selectname(workno);
        if (employee==null){
            return "查无此人";
        }
        return employee.getName();
    }

    @PostMapping(value = "/getfac",produces = "application/json;charset=UTF-8")
    public List<Scandata> getfac(@RequestBody String prdno){
//        查出制单表的所有信息
        Prdno prd=scandataService.selectPrdByprdno(prdno);

        List<Scandata> list=scandataService.getdataByfacno(prd);
        System.out.println("before");
        for (Scandata scan:list) {
            System.out.println(scan.getFacno()+"-"+scan.getPrdmoedl().getItem()+"-"+scan.getPrdmoedl().getDescn()+"-");
        }

//        ListUtil<Scandata> sortlist=new ListUtil<>();
//        sortlist.sort(list,"prdmoedl.getItem","asc");
//        System.out.println("after");
//        List<Scandata> list=scandataService.getdataByfacno(prd.getFacno());
        for (Scandata scan:list) {
            System.out.println(scan.getFacno()+"-"+scan.getPrdmoedl().getItem()+"-"+scan.getPrdmoedl().getDescn()+"-");
        }
//        String jsonstr = JSON.toJSONString(list);
//        System.out.println(jsonstr);
        return list;
    }

    @RequestMapping(value = "/getfacother",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getother(@RequestBody String prdno){
        Prdno prd=scandataService.selectPrdByprdno(prdno);
        String jsonstr=JSON.toJSONString(prd);
        return jsonstr;
    }


    @GetMapping(value = "/getprd",produces = "application/json;charset=UTF-8")
    public  List<Prdno> getprd(){
        List<Prdno> prdlist=scandataService.getAllprd();
//        String jsonstr = JSON.toJSONString(prdlist);
        return prdlist;
    }

    @PostMapping(value = "/queryseq",produces = "application/json;charset=UTF-8")
    public List<Scandata> queryseq(@RequestBody String str){
        System.out.println(str);
        String[] splitstr = str.split("@");
//        System.out.println(splitstr[0]);
        String prdno=splitstr[0];
        System.out.println(splitstr[3]);
        System.out.println(splitstr[2]);
        List<Scandata> sclist=scandataService.querySeq(prdno,splitstr[3],splitstr[2]);
//        System.out.println(scandataService.querySeq(prdno,splitstr[3],splitstr[2]).size());
//        String jsonstr = JSON.toJSONString(sclist);
        return sclist;
    }

    @RequestMapping(value = "/getworkno",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getworkno(@RequestBody String str){

        System.out.println(str);
        Employee employee=scandataService.selectworkNo(str);
        if (employee==null){
            Employee emp2=new Employee();
            int maxworkno=scandataService.selectMaxWorkNo();
            int max=maxworkno+1;
            System.out.println(String.valueOf(max));
            emp2.setWorkno(String.valueOf(max));
            emp2.setName(str);
            int res=scandataService.insertEmp(emp2);
            if (res>0){
                return emp2.getWorkno();
            }
        }

        return employee.getWorkno();
    }

    @PostMapping(value = "/updateQty",produces = "application/json;charset=UTF-8")
    public String updateQty(@RequestBody Scandata scandata){
        int res=scandataService.updateQty(scandata);
        if (res>0){
            return "success";
        }
        return "error";
    }


}
