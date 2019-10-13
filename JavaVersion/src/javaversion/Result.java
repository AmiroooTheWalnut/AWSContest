/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaversion;

/**
 *
 * @author user
 */
public class Result implements Comparable{
    String algorithmName;
    public double totalCost=1000000000;
    public double averageInventoryCost;
    double TotalInventoryCost;
    public double averageBackOrderCost;
    double TotalBackOrderCost;
    public double EstimatedDemands[];
    double InventoryLevel[];
    double EndingInventoryLevel[];
    double BeginingInventoryLevel[];
    double BackOrderCost[];
    double InventoryCost[];
    double OrderAmount[];
    double BackOrder[];
    
    Result()
    {
        
    }
    
    Result(String p_algorithmName,double p_totalCost,double p_averageInventoryCost,double p_averageBackOrderCost,double p_EstimatedDemands[],double p_InventoryLevel[],double p_InventoryCost[],double p_OrderAmount[],double p_BackOrder[])
    {
        algorithmName=p_algorithmName;
        totalCost=p_totalCost;
        averageInventoryCost=p_averageInventoryCost;
        averageBackOrderCost=p_averageBackOrderCost;
        EstimatedDemands=p_EstimatedDemands;
        InventoryLevel=p_InventoryLevel;
        InventoryCost=p_InventoryCost;
        OrderAmount=p_OrderAmount;
        BackOrder=p_BackOrder;
    }

    @Override
    public int compareTo(Object o) {
        if(totalCost>((Result)o).totalCost)
        {
            return 1;
        }else if(totalCost<((Result)o).totalCost)
        {
            return -1;
        }else{
            return 0;
        }
    }
}
