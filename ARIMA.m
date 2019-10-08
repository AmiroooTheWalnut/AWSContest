function val=ARIMA(RealDemands,month)
degrees=min(month-1,5);
remainingDegrees=degrees;
p=0;
D=0;
q=0;
for i=1:degrees
    if remainingDegrees>0
        if p<1
            p=p+1;
            remainingDegrees=remainingDegrees-1;
        end
    else
        break;
    end
    if remainingDegrees>0
        if D<0
            D=D+1;
            remainingDegrees=remainingDegrees-1;
        end
    else
        break;
    end
    if remainingDegrees>0
        if q<1
            q=q+1;
            remainingDegrees=remainingDegrees-1;
        end
    else
        break;
    end
end
Mdl = arima(p,D,q);
EstMdl = estimate(Mdl,RealDemands(max(1,month-4):month,3),'Display','off');
val = forecast(EstMdl,1);