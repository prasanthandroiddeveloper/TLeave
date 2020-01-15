package com.naestech.f_tleave;

public class DataAdapter {

    private String name,Status,date,reason,permission,rjctrsn,sdate,Asgto,type,dept_id,user_id,Loginasgn;
    private int dayscnt,id;
    private double cnt;
    private boolean isSelected;

    public String getname(){return name;}
    public void setname(String string){this.name = string;}


    public String getstatus(){return Status;}
    public void setstatus(String string){this.Status = string;}

    public String getdate(){return date;}
    public void setdate(String string){this.date = string;}

    public String getreason(){return reason;}
    public void setreason(String string){this.reason = string;}

    public int getNumber() { return this.dayscnt; }
    public void setNumber(int num) {this.dayscnt = num; }

    public int getId() { return this.id; }
    public void setId(int num) {this.id = num; }

    public String getPermission(){return permission;}
    public void setPermission(String string){this.permission = string;}

    public String getRjctReason(){return rjctrsn;}
    public void setRjctReason(String string){this.rjctrsn = string;}

    public String getsdate(){return sdate;}
    public void setsdate(String string){this.sdate = string;}

    public String getAsg(){return Asgto;}
    public void setAsg(String string){this.Asgto = string;}

    public boolean getSelected() {
        return isSelected;
    }
    public void setSelected(boolean selected) { isSelected = selected; }

    public void setType(String string) {this.type = string;}
    public String getType() {return type;}

    public String getDeptid(){return dept_id;}
    public void setDeptid(String string){this.dept_id = string;}

    public String getUserid(){return user_id;}
    public void setUserid(String string){this.user_id = string;}

    public String getLAsgn(){return Loginasgn;}
    public void setLAsgn(String string){this.Loginasgn = string;}

    public double getcnt() { return this.cnt; }
    public void setcnt(Double num) {this.cnt = num; }
}
