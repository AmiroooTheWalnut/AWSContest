clc
clear

Demands = csvread('Ten-Year-Demand.csv');
InventoryLevel(size(Demands,1))=0;
InventoryLevel(1)=60;
InventoryCost(size(Demands,1))=0;
OrderAmount(size(Demands,1))=0;
BackOrder(size(Demands,1)+1)=0;
TotalCost=0;
for month=1:119
    %\/\/\/ BEGINGIN OF MONTH
    if InventoryLevel(month)<=Demands(month,3)+BackOrder(month)
        BackOrder(month+1)=(Demands(month,3)+BackOrder(month))-InventoryLevel(month);
        InventoryLevel(month)=0;
        OrderAmount(month)=Demands(month+1,3)+BackOrder(month+1)-InventoryLevel(month);
    elseif InventoryLevel(month)>Demands(month,3)+BackOrder(month)
        InventoryLevel(month)=InventoryLevel(month)-(Demands(month,3)+BackOrder(month)+73);
        BackOrder(month+1)=0;
        OrderAmount(month)=Demands(month+1,3)+BackOrder(month+1)-InventoryLevel(month);
    end
    %^^^ BEGINGIN OF MONTH
    
    %\/\/\/ END OF MONTH
    if InventoryLevel(month)<90
        TotalCost=TotalCost+InventoryLevel(month)*1;
    else
        TotalCost=TotalCost+InventoryLevel(month)*2;
    end
    TotalCost=TotalCost+BackOrder(month)*3;
    InventoryLevel(month+1)=OrderAmount(month);
    %^^^ END OF MONTH
end
%\/\/\/ LAST MONTH
month=120;
%\/\/\/ BEGINGIN OF MONTH
if InventoryLevel(month)<=Demands(month,3)+BackOrder(month)+73
    BackOrder(month+1)=(Demands(month,3)+BackOrder(month))-InventoryLevel(month);
    InventoryLevel(month)=0;
    OrderAmount(month)=BackOrder(month+1)+73-InventoryLevel(month);
elseif InventoryLevel(month)>Demands(month,3)+BackOrder(month)+73
    InventoryLevel(month)=InventoryLevel(month)-(Demands(month,3)+BackOrder(month)+73);
    BackOrder(month+1)=0;
    OrderAmount(month)=BackOrder(month+1)+73-InventoryLevel(month);
end
%^^^ BEGINGIN OF MONTH

%\/\/\/ END OF MONTH
if InventoryLevel(month)<90
    TotalCost=TotalCost+InventoryLevel(month)*1;
else
    TotalCost=TotalCost+InventoryLevel(month)*2;
end
TotalCost=TotalCost+BackOrder(month)*3;
InventoryLevel(month+1)=OrderAmount(month);
%^^^ END OF MONTH
%^^^ LAST MONTH