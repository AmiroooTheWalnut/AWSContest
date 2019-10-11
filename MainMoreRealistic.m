clc
clear

RealDemands = csvread('Ten-Year-Demand.csv',1);
EstimatedDemands(size(RealDemands,1))=0;
InventoryLevel(size(RealDemands,1))=0;
InventoryLevel(1)=60;
InventoryCost(size(RealDemands,1))=0;
OrderAmount(size(RealDemands,1))=0;
BackOrder(size(RealDemands,1)+1)=0;
BackOrderCost(size(RealDemands,1)+1)=0;
TotalCost=0;

for month=1:119
    %\/\/\/ BEGINGIN OF MONTH
    %\/\/\/ ESTIMATE DEMAND
    EstimatedDemands(month+1)=ARIMA(RealDemands,month);
    %^^^ ESTIMATE DEMAND
    if InventoryLevel(month)<=RealDemands(month,3)+BackOrder(month)
        BackOrder(month+1)=(RealDemands(month,3)+BackOrder(month))-InventoryLevel(month);
        InventoryLevel(month)=0;
        OrderAmount(month)=EstimatedDemands(month+1)+BackOrder(month+1)-InventoryLevel(month);
    elseif InventoryLevel(month)>RealDemands(month,3)+BackOrder(month)
        InventoryLevel(month)=InventoryLevel(month)-(RealDemands(month,3)+BackOrder(month));
        BackOrder(month+1)=0;
        OrderAmount(month)=EstimatedDemands(month+1)+BackOrder(month+1)-InventoryLevel(month);
    end
    %^^^ BEGINGIN OF MONTH
    
    %\/\/\/ END OF MONTH
    if InventoryLevel(month)<90
        TotalCost=TotalCost+InventoryLevel(month)*1;
        InventoryCost(month)=InventoryLevel(month)*1;
    else
        TotalCost=TotalCost+InventoryLevel(month)*2;
        InventoryCost(month)=InventoryLevel(month)*2;
    end
    TotalCost=TotalCost+BackOrder(month)*3;
    BackOrderCost(month)=BackOrder(month)*3;
    InventoryLevel(month+1)=OrderAmount(month);
    InventoryLevel(month+1)=InventoryLevel(month)+OrderAmount(month);
    %^^^ END OF MONTH
end

plot(2:size(EstimatedDemands,2),EstimatedDemands(2:size(EstimatedDemands,2)),'r',2:size(EstimatedDemands,2),RealDemands(2:size(EstimatedDemands,2),3),'b')