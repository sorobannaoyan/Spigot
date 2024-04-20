package jp.buildjadge.buildjadge.Services;

public class Point {
    private String name;
    private Integer point;
    public Point(String  name, Integer point) {
        this.name = name;
        this.point = point;
    }

    public String getName(){
        return name;
    }
    public Integer getPoint(){
        return point;
    }
    public void addPoint(Integer point){
        this.point += point;
    }
}
