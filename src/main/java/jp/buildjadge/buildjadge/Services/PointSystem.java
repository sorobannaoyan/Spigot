package jp.buildjadge.buildjadge.Services;

import java.util.*;

public class PointSystem {
    public static HashMap<Integer, Point> pointList;
    public PointSystem(){
        pointList = new HashMap<>();
    }

    public static HashMap<Integer, Point> getPointList(){
        return pointList;
    }

    public static void setPointList(HashMap<Integer, Point>pointList){
        PointSystem.pointList = pointList;
    }
    public static String getTopPlayer(){
        String topPlayer = null;
        Integer maxScore = Integer.MIN_VALUE;
        for (Map.Entry<Integer, Point> entry:pointList.entrySet()){
            if(entry.getValue().getPoint() > maxScore){
                maxScore = entry.getValue().getPoint();
                topPlayer = entry.getValue().getName();
            }
        }
        return topPlayer;
    }
    public static List<String> getTopPlayers(){
        List<String> topPlayers = new ArrayList<>();
        List<Map.Entry<Integer,Point>> playersAndScores = new ArrayList<>(pointList.entrySet());
        playersAndScores.sort((a, b) -> b.getValue().getPoint().compareTo(a.getValue().getPoint()));
        for(int i = 0;i<Math.min(3,playersAndScores.size());i++){
            topPlayers.add(playersAndScores.get(i).getValue().getName());
        }
        return topPlayers;
    }
}
