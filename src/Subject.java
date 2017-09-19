
/**
 * Created by Данил on 29.08.2017.
 */
public class Subject {

    private String classroom;
    private String time;
    private String type;
    private String name;
    private String teacher;

    public void make(String clroom, String tim, String tp, String nam, String teach){
        classroom=clroom;
        time = tim;
        type = tp;
        name = nam;
        teacher = teach;
    }


    public String toHtmlTable(){
        if(name!="Военная кафедра"){
            return "<b>"+name+"</b>,<br>"+type+",<br>"+teacher+",<br>"+classroom;
        }else {
            return "<b>"+name+"</b>";
        }

    }


    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setName(String name) {
        switch (name){
            case "Безопасность жизнедеятельности":{
                this.name="БЖд";
            }break;
            case "Протоколы, сервисы и услуги в IP-сетях":{
                this.name="ПСИУ";
            }break;
            case "Военная подготовка":{
                this.name="Военная кафедра";
            }break;
            default: this.name=trim(name);
        }
    }

    private String trim(String name){
        String[] tmp;
        String str="";

        tmp=name.split(" ");
        for (int i = 0; i < tmp.length; i++) {

            if(tmp[i].length()>2){
                str+=tmp[i].toUpperCase().charAt(0);
            } else {
                str+=tmp[i].charAt(0);
            }
        }
        return str;
    }

    public void setType(String type) {
        switch (type){
            case "Лабораторная работа":{
                this.type = "лаб.";
            }break;
            case "Практические занятия":{
                if(name!="Военная кафедра"){
                    this.type = "практика";
                } else {
                    this.type="";
                }

            }break;
            case "Лекция":{
                this.type = type.toLowerCase();
            }break;

        }

    }

    public void setTeacher(String teacher) {

        if(teacher.length()>1){
            this.teacher=teacher.substring(0,teacher.indexOf("("));
        } else {
            this.teacher="";
        }
        //this.teacher = teacher;
    }

    public String getClassroom() {
        return classroom;
    }

    public String getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getTeacher() {
        return teacher;
    }

    private void normalize(){

    }
}
