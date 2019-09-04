package xyz.wongs.shumer.client;

import xyz.wongs.shumer.base.utils.DateUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class BroadCastController {
    public static String broadCastXml(){
        StringBuffer sb = new StringBuffer();
        String content = "";
        String [] dyl ={"1890","1390","1860"};
        String [] cj ={
                "|00200220|00100220|00100220","|00305910|00105910|00105910","|00105910|00205910|00105910",
                "|00305910|00205910|00105910","|00105910|00305910|00105910","|00205910|00305910|00105910",
                "|00205910|00105910|00205910","|00305910|00105910|00205910","|00105910|00205910|00205910",
                "|00305910|00205910|00205910","|00105910|00305910|00205910","|00205910|00305910|00205910",
                "|00205910|00105910|00305910","|00305910|00105910|00305910","|00105910|00205910|00305910",
                "|00305910|00205910|00305910","|00105910|00305910|00305910","|00205910|00305910|00305910"
        };
        for (int i = 0; i < 50; i++) {
            Random rand = new Random();
            int num = rand.nextInt(3);

            String key=dyl[0];
            String chj=null;
            if(key.equals("1390")){
                int num2 = rand.nextInt(5);
                chj=cj[num2];
            }else if(key.equals("1890")){
                int num2 = 6+rand.nextInt(5);;
                chj=cj[0];
            }else{
                int num2 = 12+rand.nextInt(5);;
                chj=cj[num2];
            }

            String str="";
            for(int z=0;z<7;z++){
                String a="";
                if(z==0){
                    a = String.valueOf((int) (Math.random() * 10));
                    while (a.equals("0")){
                        a = String.valueOf((int) (Math.random() * 10));
                    }
                }else{
                    a = String.valueOf((int) (Math.random() * 10));
                }
                str = str+a;
            }
            int x = Integer.parseInt(str);
            if(i==49){
                content += key + x + chj ;
            }else{
                content += key + x + chj+ "," ;
            }

        }
        String brcId = getBrcId("022", "1");
        System.out.println(brcId);
        sb.append("<MessageID>"+getMessageIdFromGXB("022")+"</MessageID>");
        sb.append("<CommandCode>ACT_BROADCAST_IND</CommandCode>");
        sb.append("<ServiceType>MOBILE</ServiceType>");
        sb.append("<BrcID>"+brcId+"</BrcID>");
        sb.append("<NPDataList1>"+content+"</NPDataList1>");
        sb.append("<TimeStamp>"+ DateUtils.formatDateTime(new Date())+"</TimeStamp>");
        sb.append("<Remark>"+"模拟CSMS生效广播测试"+"</Remark>");

        FileOutputStream fileOutputStream =null;
        File file=new File("d://actBroadCast/"+brcId+".txt");
        File parent=new File(file.getParent());
        if(!parent.exists()){
            parent.mkdirs();
        }
        try {
            fileOutputStream =new FileOutputStream(file);
            fileOutputStream.write(sb.toString().getBytes("utf-8"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fileOutputStream!=null){
                try{
                    fileOutputStream.close();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

        }
        return sb.toString();
    }
    //注销
    public static String dactBroadCastXml(String npCode,String portOutNetID,String portInNetID,String homeNetID){
        StringBuffer sb = new StringBuffer();
        String brcId=getBrcId("591","2");
        sb.append("<MessageID>"+getMessageIdFromGXB("591")+"</MessageID>");
        sb.append("<CommandCode>DACT_BROADCAST_IND</CommandCode>");
        sb.append("<ServiceType>MOBILE</ServiceType>");
        sb.append("<BrcID>"+brcId+"</BrcID>");
        sb.append("<NpCode>"+npCode+"</NpCode>");
        sb.append("<PortOutNetID>"+portOutNetID+"</PortOutNetID>");
        sb.append("<PortInNetID>"+portInNetID+"</PortInNetID>");
        sb.append("<HomeNetID>"+homeNetID+"</HomeNetID>");
        sb.append("<Remark>"+""+"</Remark>");
        System.out.println(brcId);
        FileOutputStream fileOutputStream =null;
        File file=new File("d://DactBroadCast/"+brcId+".txt");
        File parent=new File(file.getParent());
        if(!parent.exists()){
            parent.mkdirs();
        }
        try {
            fileOutputStream =new FileOutputStream(file);
            fileOutputStream.write(sb.toString().getBytes("utf-8"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fileOutputStream!=null){
                try{
                    fileOutputStream.close();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

        }
        return sb.toString();
    }
    public static String getMessageIdFromGXB(String area){
        String messageId="000"+area+String.valueOf(Math.random()).substring
                (2, 12);
        return messageId;
    }

    public static String getBrcId(String area,String broadcasttype){
        String brcid =area+broadcasttype+new SimpleDateFormat("yyMMdd").format(new Date())+String.valueOf(Math.random()).substring(2, 8);
        return brcid;
    }

    public static void main(String[] args) {
        try {
            Date date1 = new Date();
            for(int i=0;i<1;i++){

                cn.ffcs.np.intf.client.HttpUtil.postXml("http://10.128.44.47:10061/csms/csmsservice",broadCastXml(),90);
                //HttpUtil.postXml("http://10.128.44.236:10061/csms/csmsservice",dactBroadCastXml("18907669008","00105910","00205910","00105910"),60);
            }
            Date date2 = new Date();
            System.out.println(date2.getTime() - date1.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println(getBrcId("591","2"));

    }
}
