package com.wimoor.amazon.inboundV2.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.wimoor.amazon.inboundV2.pojo.dto.BoxAnalysisDTO;
import com.wimoor.amazon.inboundV2.pojo.dto.BoxInfo;
import com.wimoor.amazon.inboundV2.pojo.dto.SKUItemDTO;
import com.wimoor.amazon.inboundV2.service.IShipInboundBoxAnalysisService;
import com.wimoor.amazon.inboundV2.service.IShipInboundItemService;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service("shipInboundBoxAnalysis3Service")
public class ShipInboundBoxAnalysis3ServiceImpl implements IShipInboundBoxAnalysisService {
    /**
     * ‌重量限制‌：单个外箱的重量不能超过22.5kg（50磅）。如果超过这个重量，四周和上面需要贴上“Team Lift”（需要多人搬运）标签；如果超过45.4kg（100磅），则需要贴上“Mech Lift”（需要机械搬运）标签。
     * <p>
     * ‌尺寸限制‌：
     * <p>
     * ‌标准尺寸箱子‌：任何一边的长度不得超过63.5cm（25英寸）。这是为了确保箱子能够方便地放置在货架上，并通过自动化设备进行搬运。
     * ‌特殊超大尺寸箱子‌：如果箱子的任何一边长度超过63.5cm（25英寸），则被视为超大尺寸箱子。超大尺寸箱子的重量一般也不应超过22.5kg（50磅），但具体情况需要提前与亚马逊沟通并遵循特定流程。
     * ‌特殊情况‌：
     * <p>
     * ‌包含珠宝和手表的箱子‌：重量不得超过18kg（40磅），以更好地保护商品并确保操作的准确性。
     * ‌欧洲站‌：重量限制为22.7kg，超过15kg的外箱需要贴上超重标签。
     * <p>
     * 每个SKU必须出现在五个以上相同的箱子
     */
    final IShipInboundItemService iShipInboundItemService;


    @Override
    public List<BoxInfo> boxAnalysis(UserInfo user, BoxAnalysisDTO dto) {
           BigDecimal totalWeight = BigDecimal.ZERO;
           BigDecimal totalVolumn = BigDecimal.ZERO ;
           for(SKUItemDTO item:dto.getItemlist()){
               BigDecimal volume = item.getWidth().multiply(item.getLength()).multiply(item.getHeight());
               totalVolumn = totalVolumn.add(volume.multiply(new BigDecimal(item.getQuantity())));
               totalWeight = totalWeight.add(item.getWeight().multiply(new BigDecimal(item.getQuantity())));
           }
           int boxindex=0;
           BoxInfo box = dto.getBoxlist().get(boxindex++);
           BigDecimal volume = box.getWidth().multiply(box.getLength()).multiply(box.getHeight());
           BigDecimal maxDensity=new BigDecimal("22").divide(volume,10,BigDecimal.ROUND_HALF_UP);
           BigDecimal minDensity=totalWeight.divide(totalVolumn,10,BigDecimal.ROUND_HALF_UP);
           while(minDensity.compareTo(maxDensity)>=0){
               box = dto.getBoxlist().get(boxindex++);
               volume = box.getWidth().multiply(box.getLength()).multiply(box.getHeight());
               maxDensity=new BigDecimal("22").divide(volume,10,BigDecimal.ROUND_HALF_UP);
           }
           Collections.sort(dto.getItemlist(), new Comparator<SKUItemDTO>() {
                @Override
                public int compare(SKUItemDTO o1, SKUItemDTO o2) {
                    BigDecimal volume1 = o1.getWidth().multiply(o1.getLength()).multiply(o1.getHeight());
                    BigDecimal density1=o1.getWeight().divide(volume1,10,BigDecimal.ROUND_HALF_UP);
                    BigDecimal volume2 = o2.getWidth().multiply(o2.getLength()).multiply(o2.getHeight());
                    BigDecimal density2=o2.getWeight().divide(volume2,10,BigDecimal.ROUND_HALF_UP);
                    density1=density1.divide(new BigDecimal(o1.getQuantity()),10,BigDecimal.ROUND_HALF_UP);
                    density2=density2.divide(new BigDecimal(o2.getQuantity()),10,BigDecimal.ROUND_HALF_UP);
                    return density1.compareTo(density2);
                }
            });
            System.out.println("Max Density================"+maxDensity);
            System.out.println("Min Density================"+minDensity);
            for(int i=0;i<dto.getItemlist().size();i++){
                SKUItemDTO item= dto.getItemlist().get(i);
                BigDecimal itemvolume = item.getWidth().multiply(item.getLength()).multiply(item.getHeight());
                System.out.println(item.getWeight()+"================"+item.getQuantity());
                System.out.println("Density================"+item.getWeight().divide(itemvolume,10,BigDecimal.ROUND_HALF_UP));
            }
           boolean hassku=true;
           List<BoxInfo> result=new ArrayList<BoxInfo>();
               hassku=false;
               for(int i=0;i<dto.getItemlist().size();i++){
                   SKUItemDTO lightItem=dto.getItemlist().get(i);
                   if(lightItem.isIsused()==true){
                       continue;
                   }
                   BoxInfo mybox=new BoxInfo();
                   BeanUtil.copyProperties(box,mybox);
                   List<SKUItemDTO> inBoxItemlist=new ArrayList<SKUItemDTO>();
                   if(getDensity(inBoxItemlist,lightItem).compareTo(minDensity)<0){
                       mybox.setItemlist(inBoxItemlist);
                       result.add(mybox);
                       lightItem.setIsused(true);
                       inBoxItemlist.add(lightItem);
                       for(int j=dto.getItemlist().size()-1;j>=0;j--){
                           //综合密度=（产品1发货量*产品1重量+产品5发货量*产品5重量）/（产品1发货量*产品1体积+产品5发货量*产品5体积）
                           SKUItemDTO item=dto.getItemlist().get(j);
                           if(item.isIsused()==true){
                               continue;
                           }
                           if(getDensity(inBoxItemlist,item).compareTo(minDensity)>0||inBoxItemlist.size()>=3){
                               inBoxItemlist.add(item);
                               item.setIsused(true);
                               mybox.setIsok(true);
                               break;
                           }else{
                               inBoxItemlist.add(item);
                               item.setIsused(true);
                           }
                       }
                   }
               }
        BoxInfo mybox=new BoxInfo();
        BeanUtil.copyProperties(box,mybox);
        List<SKUItemDTO> inBoxItemlist=new ArrayList<SKUItemDTO>();
        for(int i=0;i<dto.getItemlist().size();i++){
            SKUItemDTO lightItem=dto.getItemlist().get(i);
            if(lightItem.isIsused()==true){
                continue;
            }
            lightItem.setIsused(true);
            inBoxItemlist.add(lightItem);
        }
        if(inBoxItemlist.size()!=0){
            mybox.setItemlist(inBoxItemlist);
            mybox.setIsok(true);
            result.add(mybox);
        }
        for(BoxInfo boxitem:result){
            BigDecimal totalItemVolumn=BigDecimal.ZERO;
            BigDecimal boxWeight=BigDecimal.ZERO;
            BigDecimal boxvolume = boxitem.getWidth().multiply(boxitem.getLength()).multiply(boxitem.getHeight());
            for(SKUItemDTO item:boxitem.getItemlist()){
                BigDecimal itemvolume = item.getWidth().multiply(item.getLength()).multiply(item.getHeight());
                totalItemVolumn=totalItemVolumn.add(itemvolume.multiply(new BigDecimal(item.getQuantity())));
            }
            int boxnum = totalItemVolumn.divide(boxvolume, RoundingMode.CEILING).intValue();
            if(boxnum==0){
                boxnum=1;
            }
            boxvolume=BigDecimal.ZERO;
            for(SKUItemDTO item:boxitem.getItemlist()){
                BigDecimal itemvolume = item.getWidth().multiply(item.getLength()).multiply(item.getHeight());
                item.setBoxnumber(boxnum);
                item.setBoxquantity(item.getQuantity().intValue()/boxnum);
                boxWeight=boxWeight.add(item.getWeight().multiply(new BigDecimal(item.getBoxquantity())));
                boxvolume=boxvolume.add(itemvolume.multiply(new BigDecimal(item.getBoxquantity())));
            }
            boxitem.setSkuTotalVolumn(boxvolume);
            boxitem.setSkuTotalWeight(boxWeight);
            boxitem.setIsok(true);
        }
        if(result.size()==1){
            List<BoxInfo> newresult=new ArrayList<BoxInfo>();
            for(BoxInfo boxitem:result){
                BoxAnalysisDTO subdto=new BoxAnalysisDTO();
                BeanUtil.copyProperties(dto,subdto);
                for(SKUItemDTO item:boxitem.getItemlist()){
                    item.setIsused(false);
                }
                subdto.setItemlist(boxitem.getItemlist());
                newresult.addAll(boxAnalysis2(user,subdto));
            }
            return newresult;
        }else{
            return result;
        }

    }

    private Comparable<BigDecimal> getDensity(List<SKUItemDTO> items,SKUItemDTO item) {
        BigDecimal totalWeight = BigDecimal.ZERO;
        BigDecimal totalVolum = BigDecimal.ZERO;
        for(int i=0;i<items.size();i++){
            BigDecimal volume=items.get(i).getWidth().multiply(items.get(i).getLength()).multiply(items.get(i).getHeight());
            totalWeight=totalWeight.add(items.get(i).getWeight().multiply(new BigDecimal(items.get(i).getQuantity())));
            totalVolum=totalVolum.add(volume.multiply(new BigDecimal(items.get(i).getQuantity())));
        }
        BigDecimal volume=item.getWidth().multiply(item.getLength()).multiply(item.getHeight());
        totalWeight=totalWeight.add(item.getWeight().multiply(new BigDecimal(item.getQuantity())));
        totalVolum=totalVolum.add(volume.multiply(new BigDecimal(item.getQuantity())));
        return totalWeight.divide(totalVolum,10,BigDecimal.ROUND_HALF_UP);
    }


    public List<BoxInfo>  boxAnalysis2(UserInfo user, BoxAnalysisDTO dto) {
        BoxInfo firstBox = dto.getBoxlist().get(0);
        BigDecimal volume = firstBox.getWidth().multiply(firstBox.getLength()).multiply(firstBox.getHeight());
        BigDecimal totalVolume = BigDecimal.ZERO;
        Integer totalQuantity = 0;
        BigDecimal goodVolume = BigDecimal.ZERO;//单个理想体积
        BigDecimal goodBoxNum = BigDecimal.ZERO;//箱子个数预估
        for (SKUItemDTO item : dto.getItemlist()) {
            if (item.getWidth() == null || item.getLength() == null || item.getHeight() == null) {
                throw new BizException("SKU: " + item.getSku() + " 尺寸信息为空");
            }
            BigDecimal itemvolume = item.getWidth().multiply(item.getLength()).multiply(item.getHeight());
            totalVolume = totalVolume.add(itemvolume.multiply(new BigDecimal(item.getQuantity())));
            totalQuantity += item.getQuantity();
            item.setBoxnumber(0);
            item.setBoxquantity(0);
        }
        goodVolume = totalVolume.divide(new BigDecimal(totalQuantity), 2, BigDecimal.ROUND_HALF_UP);
        goodBoxNum = totalVolume.divide(volume, 2, BigDecimal.ROUND_HALF_UP);
        //加入所有的产品都均匀的放在每一个箱子中goodBoxNum中每个产品的占比等于
        for (SKUItemDTO item : dto.getItemlist()) {
            BigDecimal itemvolume = item.getWidth().multiply(item.getLength()).multiply(item.getHeight());
            BigDecimal leastBoxVolume = itemvolume.multiply(new BigDecimal(item.getQuantity())).divide(totalVolume, 2, BigDecimal.ROUND_HALF_UP).multiply(volume);
            splitBoxItem(item, volume, goodBoxNum, leastBoxVolume);
        }
        return groupBox(dto);
    }

    public  List<BoxInfo> groupBox(BoxAnalysisDTO dto){
        BoxInfo firstBox = dto.getBoxlist().get(0);
        BigDecimal volume = firstBox.getWidth().multiply(firstBox.getLength()).multiply(firstBox.getHeight());
        List<BoxInfo> result=null;
        Collections.sort(dto.getItemlist(), new Comparator<SKUItemDTO>() {
            @Override
            public int compare(SKUItemDTO o1, SKUItemDTO o2) { return o1.getWeight().compareTo(o2.getWeight());  }
        });

        List<BoxInfo> list=new ArrayList<BoxInfo>();
        while(hasUsableItem(dto.getItemlist())){
            BoxInfo box=new BoxInfo();
            BeanUtil.copyProperties(firstBox,box);
            box.setSkuTotalWeight(new BigDecimal("0"));
            box.setSkuTotalVolumn(new BigDecimal("0"));
            BigDecimal onevolume = volume;
            Integer boxnum=null;
            while(onevolume.compareTo(new BigDecimal("-800"))>0){
                SKUItemDTO lightitem=null;
                for(int i=0;i<dto.getItemlist().size();i++){
                    SKUItemDTO item = dto.getItemlist().get(i);
                    if(item.isIsused()==false){
                        if(boxnum!=null&&boxnum.compareTo(item.getBoxnumber())!=0){
                            continue;
                        }
                        lightitem=item;
                        break;
                    }
                }
                if(lightitem==null){break;}
                BigDecimal itemlvolume=lightitem.getWidth().multiply(lightitem.getLength()).multiply(lightitem.getHeight());
                onevolume=onevolume.subtract(itemlvolume.multiply(new BigDecimal(lightitem.getBoxquantity())));
                if(onevolume.compareTo(new BigDecimal("-800"))>0){
                    if(box.getItemlist()!=null){
                        lightitem.setIsused(true);
                        box.getItemlist().add(lightitem);
                    }else{
                        box.setItemlist(new ArrayList<SKUItemDTO>());
                        lightitem.setIsused(true);
                        boxnum=lightitem.getBoxnumber();
                        box.getItemlist().add(lightitem);
                    }
                    box.setSkuTotalWeight(box.getSkuTotalWeight().add(lightitem.getWeight().multiply(new BigDecimal(lightitem.getBoxquantity()))));
                    box.setSkuTotalVolumn(box.getSkuTotalVolumn().add(itemlvolume.multiply(new BigDecimal(lightitem.getBoxquantity()))));
                }else{
                    break;
                }
                SKUItemDTO weightitem=null;
                for(int i=dto.getItemlist().size()-1;i>0;i--){
                    SKUItemDTO item = dto.getItemlist().get(i);
                    if(item.isIsused()==false){
                        if(boxnum!=null&&boxnum.compareTo(item.getBoxnumber())!=0){
                            continue;
                        }
                        weightitem=item;
                        break;
                    }
                }
                if(weightitem==null){break;}
                BigDecimal itemwvolume=weightitem.getWidth().multiply(weightitem.getLength()).multiply(weightitem.getHeight());
                onevolume=onevolume.subtract(itemwvolume.multiply(new BigDecimal(weightitem.getBoxquantity())));
                if(onevolume.compareTo(new BigDecimal("-800"))>0){
                    if(box.getItemlist()!=null){
                        weightitem.setIsused(true);
                        box.getItemlist().add(weightitem);
                    }else{
                        box.setItemlist(new ArrayList<SKUItemDTO>());
                        weightitem.setIsused(true);
                        boxnum=weightitem.getBoxnumber();
                        box.getItemlist().add(weightitem);
                    }
                    box.setSkuTotalWeight(box.getSkuTotalWeight().add(weightitem.getWeight().multiply(new BigDecimal(weightitem.getBoxquantity()))));
                    box.setSkuTotalVolumn(box.getSkuTotalVolumn().add(itemwvolume.multiply(new BigDecimal(weightitem.getBoxquantity()))));
                }else{
                    break;
                }
            }
            list.add(box);
        }
        int hasgoogbox=0;
        for(BoxInfo box:list){
            box.setIsok(false);
            for(BoxInfo item:dto.getBoxlist()){
                volume=item.getWidth().multiply(item.getLength()).multiply(item.getHeight());
                if(box.getSkuTotalVolumn().intValue()-volume.intValue()<2000&&box.getSkuTotalVolumn().intValue()-volume.intValue()>-2000){
                    box.setHeight(item.getHeight());
                    box.setLength(item.getLength());
                    box.setWidth(item.getWidth());
                    box.setIsok(true);
                    hasgoogbox++;
                    break;
                }
            }
        }
        return list;
    }
    private boolean hasUsableItem(List<SKUItemDTO> itemlist) {
        for(SKUItemDTO item:itemlist){
            if(item.isIsused()==false){
                return true;
            }
        }
        return false;
    }

    private void splitBoxItem(SKUItemDTO item, BigDecimal volume, BigDecimal goodBoxNum,BigDecimal leastBoxVolume  ) {
        BigDecimal itemvolume=item.getWidth().multiply(item.getLength()).multiply(item.getHeight());
        int[] boxnums={5,10,15,20,25,30,35,40,45,50,55,60,65,70,75,80,85,90,95,100};
        for(int i=0;i<=boxnums.length-1&&boxnums[i]<goodBoxNum.intValue()+5;i++){
            int singlenumber = item.getQuantity() / boxnums[i];
            BigDecimal boxinvolume = itemvolume.multiply(new BigDecimal(singlenumber));
            if(leastBoxVolume.compareTo(boxinvolume)>=0){
                item.setBoxnumber(boxnums[i]);
                item.setBoxquantity(item.getQuantity() / boxnums[i]);
                item.setIsused(false);
                return;
            }
            if(boxinvolume.compareTo(volume)>=0 )   {
                continue;
            }else{
                item.setBoxnumber(boxnums[i]);
                item.setBoxquantity(singlenumber);
                item.setIsused(false);
                return;
            }
        }

    }
}